/*
 * JBoss, Home of Professional Open Source Copyright 2011 Red Hat Inc. and/or
 * its affiliates and other contributors as indicated by the @authors tag. All
 * rights reserved. See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, v. 2.1.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */
package org.jboss.ircbot.plugins.jira;

import static org.jboss.ircbot.Command.PRIVMSG;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.ircbot.AbstractBotService;
import org.jboss.ircbot.BotException;
import org.jboss.ircbot.BotRuntime;
import org.jboss.ircbot.ClientMessage;
import org.jboss.ircbot.Message;
import org.jboss.ircbot.MessageBuilder;
import org.jboss.ircbot.ServerMessage;
import org.jboss.ircbot.User;
import org.jboss.ircbot.plugins.jira.JIRAServiceConfig.JIRATracker;
import org.jboss.logging.Logger;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class JIRABotService extends AbstractBotService< JIRAServiceConfig > {

    private static final Pattern JIRA_ID_PATTERN = Pattern.compile( "[A-Z]+[1-9]?-[0-9]+" );
    private static final Logger LOGGER = Logger.getLogger( JIRABotService.class );
    private ExecutorService scrapeTasks;
    private Set< String > blackList;

    public JIRABotService() {
        super();
    }

    @Override
    public void init( final BotRuntime< JIRAServiceConfig > runtime ) throws BotException {
        super.init( runtime );
        scrapeTasks = Executors.newFixedThreadPool( 5, JIRAThreadFactory.INSTANCE );
        blackList = new ExpirableCache< String >( 60 );
    }

    @Override
    public void destroy() throws BotException {
        try {
            blackList.clear();
            blackList = null;
            scrapeTasks.shutdownNow();
            scrapeTasks = null;
        } finally {
            super.destroy();
        }
    }

    @Override
    public void onMessage( final ServerMessage msg ) throws BotException {
        if ( PRIVMSG == msg.getCommand() ) {
            processMessage( msg );
        }
    }

    @Override
    public void onMessage( final ClientMessage msg ) throws BotException {
        if ( !msg.isOurMessage( this ) && ( PRIVMSG == msg.getCommand() ) ) {
            processMessage( msg );
        }
    }

    private void processMessage( final Message msg ) {
        final String msgTarget = getMessageTarget( msg );
        final Set< String > jiraCandidates = getJIRACandidates( msg );
        for ( final String jiraCandidate : jiraCandidates ) {
            if ( blackList.contains( msgTarget + jiraCandidate ) ) {
                LOGGER.info( jiraCandidate + " black listed for " + msgTarget );
                continue;
            }
            for ( final JIRATracker tracker : getServiceConfig().getTrackers() ) {
                final String htmlURL = tracker.getHtmlURL();
                final String jsonURL = tracker.getJsonURL();
                scrapeTasks.submit( new Runnable() {

                    public void run() {
                        final JIRAIssue jiraIssue = JIRAIssuePageScraper.getInstance().scrape( jiraCandidate, htmlURL, jsonURL );
                        if ( jiraIssue != null ) {
                            if ( blackList.add( msgTarget + jiraCandidate ) ) {
                                final MessageBuilder msgBuilder = getMessageFactory().newMessage( PRIVMSG );
                                msgBuilder.addParam( msgTarget );
                                msgBuilder.addParam( jiraIssue );
                                getConnection().send( msgBuilder.build() );
                            }
                        }
                    }
                } );
            }
        }
    }

    @Override
    public Class< JIRAServiceConfig > getConfigClass() {
        return JIRAServiceConfig.class;
    }

    private String getMessageTarget( final Message msg ) {
        String retVal = msg.getParams().get( 0 );
        if ( getBotConfig().getBotNick().equals( retVal ) ) {
            // private message to our bot, respond privately
            retVal = ( ( User ) msg.getSender() ).getNickName();
        }
        return retVal;
    }

    private Set< String > getJIRACandidates( final Message msg ) {
        final Set< String > retVal = new HashSet< String >();
        final Matcher m = JIRA_ID_PATTERN.matcher( msg.getParamsString() );
        while ( m.find() ) {
            retVal.add( m.group() );
        }
        return retVal;
    }
}

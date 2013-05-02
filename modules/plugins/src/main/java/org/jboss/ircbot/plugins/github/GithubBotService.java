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
package org.jboss.ircbot.plugins.github;

import static org.jboss.ircbot.Command.PRIVMSG;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
import org.jboss.ircbot.MessageFactory;
import org.jboss.ircbot.ServerConnection;
import org.jboss.ircbot.ServerMessage;
import org.jboss.ircbot.User;
import org.jboss.logging.Logger;

import com.sun.net.httpserver.HttpServer;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class GithubBotService extends AbstractBotService< GithubServiceConfig > {

    private static final Logger LOGGER = Logger.getLogger( GithubBotService.class );
    private static final Pattern GITHUB_COMMIT_PATTERN = Pattern.compile( "https://github.com/\\S+/commit/[0-9a-f]+" );
    private static final Pattern GITHUB_PULL_REQ_PATTERN = Pattern.compile( "https://github.com/\\S+/pull/[0-9]+" );
    private ExecutorService scrapeTasks;
    private HttpServer server;

    public GithubBotService() {
        super();
    }

    @Override
    public void init( final BotRuntime< GithubServiceConfig > runtime ) throws BotException {
        super.init( runtime );
        scrapeTasks = Executors.newSingleThreadExecutor( GithubServiceThreadFactory.INSTANCE );
        final GithubServiceConfig ourConfig = runtime.getServiceConfig();
        if ( ourConfig.getNotifications().length != 0 ) {
            // there are registered notifications
            final ServerConnection conn = runtime.getConnection();
            final MessageFactory msgFactory = runtime.getMessageFactory();
            final String host = ourConfig.getHttpServerHost();
            final int port = ourConfig.getHttpServerPort();
            try {
                server = HttpServer.create( new InetSocketAddress( InetAddress.getByName( host ), port ), 0 );
                server.setExecutor( Executors.newCachedThreadPool() );
                server.start();
                server.createContext( "/", new GithubNotificationHandler( conn, msgFactory, ourConfig.getNotifications() ) );
                LOGGER.info( "Started HTTP server for push notifications: " + host + ":" + port );
            } catch ( final IOException ioe ) {
                LOGGER.error( ioe.getMessage(), ioe );
            }
        }
    }

    @Override
    public void destroy() throws BotException {
        try {
            if ( server != null ) {
                server.stop( 0 );
                LOGGER.info( "Stopped HTTP server for push notifications" );
            }
            scrapeTasks.shutdownNow();
            scrapeTasks = null;
        } finally {
            super.destroy();
        }
    }

    @Override
    public Class< GithubServiceConfig > getConfigClass() {
        return GithubServiceConfig.class;
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

    private void processMessage( final Message msg ) throws BotException {
        final String msgTarget = getMessageTarget( msg );
        // detect & process github commit url(s)
        final Set< String > commitURLs = getGithubCommits( msg );
        for ( final String commitURL : commitURLs ) {
            LOGGER.info( "Processing github commit URL: " + commitURL );
            scrapeTasks.submit( new Runnable() {
                public void run() {
                    final GithubCommit commit = GithubCommitPageScraper.getInstance().scrape( commitURL );
                    if ( commit != null ) {
                        final MessageBuilder msgBuilder = getMessageFactory().newMessage( PRIVMSG );
                        msgBuilder.addParam( msgTarget );
                        msgBuilder.addParam( commit );
                        getConnection().send( msgBuilder.build() );
                    }
                }
            } );
        }
        // detect & process github pull request url(s)
        final Set< String > pullRequestURLs = getGithubPullRequests( msg );
        for ( final String pullRequestURL : pullRequestURLs ) {
            LOGGER.info( "Processing github pull request URL: " + pullRequestURL );
            scrapeTasks.submit( new Runnable() {
                public void run() {
                    final GithubPullRequest pullRequest = GithubPullRequestPageScraper.getInstance().scrape( pullRequestURL );
                    if ( pullRequest != null ) {
                        final MessageBuilder msgBuilder = getMessageFactory().newMessage( PRIVMSG );
                        msgBuilder.addParam( msgTarget );
                        msgBuilder.addParam( pullRequest );
                        getConnection().send( msgBuilder.build() );
                    }
                }
            } );
        }
    }

    private String getMessageTarget( final Message msg ) {
        String retVal = msg.getParams().get( 0 );
        if ( getBotConfig().getBotNick().equals( retVal ) ) {
            // private message to our bot, respond privately
            retVal = ( ( User ) msg.getSender() ).getNickName();
        }
        return retVal;
    }

    private static Set< String > getGithubCommits( final Message msg ) {
        final Set< String > retVal = new HashSet< String >();
        final Matcher m = GITHUB_COMMIT_PATTERN.matcher( msg.getParamsString() );
        while ( m.find() ) {
            retVal.add( m.group() );
        }
        return retVal;
    }

    private static Set< String > getGithubPullRequests( final Message msg ) {
        final Set< String > retVal = new HashSet< String >();
        final Matcher m = GITHUB_PULL_REQ_PATTERN.matcher( msg.getParamsString() );
        while ( m.find() ) {
            retVal.add( m.group() );
        }
        return retVal;
    }
}

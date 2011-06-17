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
package org.jboss.ircbot.plugins.login;

import static org.jboss.ircbot.Character.COLON;
import static org.jboss.ircbot.Character.COMMA;
import static org.jboss.ircbot.Command.JOIN;
import static org.jboss.ircbot.Command.NICK;
import static org.jboss.ircbot.Command.NS;
import static org.jboss.ircbot.Command.QUIT;
import static org.jboss.ircbot.Command.USER;

import java.util.Iterator;
import java.util.Set;

import org.jboss.ircbot.AbstractBotService;
import org.jboss.ircbot.BotConfig;
import org.jboss.ircbot.BotException;
import org.jboss.ircbot.BotRuntime;
import org.jboss.ircbot.ClientMessage;
import org.jboss.ircbot.MessageBuilder;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class LoginBotService extends AbstractBotService< Void > {

    private static final String IDENTIFY = "IDENTIFY";
    private static final long FIFTEEN_SECONDS = 15000;

    public LoginBotService() {
        super();
    }

    @Override
    public void init( final BotRuntime< Void > runtime ) throws BotException {
        super.init( runtime );
        // NICK message
        final ClientMessage nickMessage = newNickMessage();
        getConnection().send( nickMessage );
        // USER message
        final ClientMessage userMessage = newUserMessage();
        getConnection().send( userMessage );
        // NS IDENTIFY message
        final ClientMessage nsIdentifyMessage = newNickServIdentifyMessage();
        if ( nsIdentifyMessage != null ) {
            getConnection().send( nsIdentifyMessage );
            try {
                // wait for NickServ to identify our bot
                Thread.sleep( FIFTEEN_SECONDS );
            } catch ( final InterruptedException ignore ) {
                // ignored
            }
        }
        // JOIN message
        final ClientMessage joinMessage = newJoinMessage();
        getConnection().send( joinMessage );
    }

    @Override
    public void destroy() throws BotException {
        try {
            // QUIT message
            final ClientMessage quitMessage = newQuitMessage();
            getConnection().send( quitMessage );
        } finally {
            super.destroy();
        }
    }

    private ClientMessage newNickMessage() {
        final MessageBuilder msgBuilder = getMessageFactory().newMessage( NICK );
        msgBuilder.addParam( getBotConfig().getBotNick() );
        return msgBuilder.build();
    }

    private ClientMessage newUserMessage() {
        final MessageBuilder msgBuilder = getMessageFactory().newMessage( USER );
        final BotConfig botConfig = getBotConfig();
        msgBuilder.addParam( botConfig.getBotNick() );
        msgBuilder.addParam( botConfig.getBotNick() );
        msgBuilder.addParam( botConfig.getServerAddress() );
        msgBuilder.addParam( COLON + botConfig.getBotFullName() );
        return msgBuilder.build();
    }

    private ClientMessage newNickServIdentifyMessage() {
        final BotConfig botConfig = getBotConfig();
        if ( botConfig.getBotPassword() == null ) {
            return null;
        }
        final MessageBuilder msgBuilder = getMessageFactory().newMessage( NS );
        msgBuilder.addParam( IDENTIFY );
        msgBuilder.addParam( botConfig.getBotPassword() );
        return msgBuilder.build();
    }

    private ClientMessage newJoinMessage() {
        final Set< String > channels = getBotConfig().getServerChannels();
        final MessageBuilder msgBuilder = getMessageFactory().newMessage( JOIN );
        final Iterator< String > channelsIterator = channels.iterator();
        final StringBuilder channelsList = new StringBuilder();
        while ( channelsIterator.hasNext() ) {
            channelsList.append( channelsIterator.next() );
            if ( channelsIterator.hasNext() ) {
                channelsList.append( COMMA );
            }
        }
        msgBuilder.addParam( channelsList );
        return msgBuilder.build();
    }

    private ClientMessage newQuitMessage() {
        final MessageBuilder msgBuilder = getMessageFactory().newMessage( QUIT );
        msgBuilder.addParam( "Leaving" );
        return msgBuilder.build();
    }
}

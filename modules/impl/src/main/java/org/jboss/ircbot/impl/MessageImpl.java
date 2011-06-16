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
package org.jboss.ircbot.impl;

import static org.jboss.ircbot.Character.COLON;
import static org.jboss.ircbot.Character.SPACE;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.jboss.ircbot.BotService;
import org.jboss.ircbot.ClientMessage;
import org.jboss.ircbot.Command;
import org.jboss.ircbot.ReplyCode;
import org.jboss.ircbot.Sender;
import org.jboss.ircbot.ServerMessage;
import org.jboss.logging.Logger;

/**
 * IRC message implementation.
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class MessageImpl implements ServerMessage, ClientMessage {

    private static final Logger LOGGER = Logger.getLogger( MessageImpl.class );
    private final Sender prefix;
    private final Command command;
    private final ReplyCode replyCode;
    private final List< String > params;
    private final BotService< ? > producer;

    public MessageImpl( final Command command ) {
        this( null, command, null, new LinkedList< String >(), null );
    }

    public MessageImpl( final Sender prefix, final Command command, final ReplyCode replyCode, final List< String > params, final BotService< ? > producer ) {
        this.prefix = prefix;
        this.command = command;
        this.replyCode = replyCode;
        this.params = params;
        this.producer = producer;
    }

    public static MessageImpl valueOf( final String rawMessage ) {
        try {
            final StringTokenizer st = new StringTokenizer( rawMessage, SPACE );
            Sender sender = null;
            String currentToken = st.nextToken();
            if ( currentToken.startsWith( COLON ) ) {
                sender = SenderFactory.newInstance( currentToken );
                currentToken = st.nextToken();
            }
            final Command command = Command.of( currentToken );
            final ReplyCode replyCode = ReplyCode.of( currentToken );
            final List< String > params = new LinkedList< String >();
            while ( st.hasMoreTokens() ) {
                params.add( st.nextToken() );
            }
            if ( ( command == Command.UNKNOWN ) && ( replyCode == ReplyCode.UNKNOWN ) ) {
                LOGGER.warn( "Received message with unknown command or reply code: " + rawMessage );
            }
            return new MessageImpl( sender, command, replyCode, params, null );
        }
        catch ( final Exception e ) {
            LOGGER.fatal( "Wrong IRC message: " + rawMessage );
            return null;
        }
    }

    public BotService< ? > getProducer() {
        return producer;
    }

    public Sender getSender() {
        return prefix;
    }

    public Command getCommand() {
        return command;
    }

    public List< String > getParams() {
        return Collections.unmodifiableList( params );
    }

    public String getParamsString() {
        final StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < params.size(); i++ ) {
            if ( i != 0 ) {
                sb.append( SPACE );
            }
            sb.append( params.get( i ) );
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if ( prefix != null ) {
            sb.append( COLON );
            sb.append( prefix );
            sb.append( SPACE );
        }
        if ( command != null ) {
            sb.append( command );
        }
        if ( replyCode != null ) {
            sb.append( replyCode );
        }
        sb.append( SPACE );
        if ( params != null ) {
            for ( int i = 0; i < params.size(); i++ ) {
                if ( i != 0 ) {
                    sb.append( SPACE );
                }
                sb.append( params.get( i ) );
            }
        }
        return sb.toString();
    }

    public void addParam( final String s ) {
        if ( s == null ) {
            throw new IllegalArgumentException();
        }
        params.add( s );
    }

    public void addParam( final Object o ) {
        if ( o == null ) {
            throw new IllegalArgumentException();
        }
        params.add( o.toString() );
    }

    public ReplyCode getReply() {
        return replyCode;
    }

    public boolean isOurMessage( final BotService< ? > service ) {
        return producer == service;
    }
}

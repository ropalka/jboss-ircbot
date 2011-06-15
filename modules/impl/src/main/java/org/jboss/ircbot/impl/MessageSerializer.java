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

import static org.jboss.ircbot.impl.AbstractMessageQueue.TIMEOUT;
import static org.jboss.ircbot.impl.AbstractMessageQueue.TIME_UNIT;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.jboss.ircbot.ClientMessage;
import org.jboss.logging.Logger;

/**
 * Takes IRC messages from outbound message queue and sends them to the server.
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class MessageSerializer implements Runnable {

    private static final String CRLF = "\r\n";
    private static final Logger LOGGER = Logger.getLogger( MessageSerializer.class );
    private final BufferedWriter out;
    private ClientMessage msg;

    public MessageSerializer( final OutputStream out ) throws IOException {
        this.out = new BufferedWriter( new OutputStreamWriter( out ) );
    }

    public void run() {
        while ( true ) {
            try {
                msg = OutboundMessageQueue.getInstance().poll( TIMEOUT, TIME_UNIT );
                if ( msg != null ) {
                    final String msgText = msg.toString();
                    LOGGER.info( "Sending to IRC server: " + msgText );
                    out.write( msgText );
                    out.write( CRLF );
                    out.flush();
                }
            }
            catch ( final Exception e ) {
                LOGGER.error( e.getMessage(), e );
            }
        }
    }
}

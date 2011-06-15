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

import java.util.List;

import org.jboss.ircbot.BotService;
import org.jboss.ircbot.ClientMessage;
import org.jboss.ircbot.ServerMessage;
import org.jboss.logging.Logger;

/**
 * Notifies all Bot services about incomming IRC messages.
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class Notifier implements Runnable {

    private static final Logger LOG = Logger.getLogger( Notifier.class );
    private final List< BotService< ? >> listeners;

    public Notifier( final List< BotService< ? >> listeners ) {
        this.listeners = listeners;
    }

    public void run() {
        final InboundMessageQueue queue = InboundMessageQueue.getInstance();
        MessageImpl msg;
        while ( true ) {
            try {
                msg = queue.poll( TIMEOUT, TIME_UNIT );
                if ( msg != null ) {
                    for ( final BotService< ? > listener : listeners ) {
                        try {
                            if ( msg.getProducer() == null ) {
                                listener.onMessage( ( ServerMessage ) msg );
                            }
                            else {
                                listener.onMessage( ( ClientMessage ) msg );
                            }
                        }
                        catch ( final Exception e ) {
                            LOG.error( e.getMessage(), e );
                        }
                    }
                }
            }
            catch ( final InterruptedException e ) {
                LOG.error( e.getMessage(), e );
            }
        }
    }
}

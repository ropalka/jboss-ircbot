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

import org.jboss.ircbot.ClientMessage;
import org.jboss.ircbot.ServerConnection;
import org.jboss.logging.Logger;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class TeeMessageQueue extends AbstractMessageQueue implements ServerConnection {

    private static final Logger LOGGER = Logger.getLogger( TeeMessageQueue.class );
    private static final TeeMessageQueue INSTANCE = new TeeMessageQueue();
    private static final OutboundMessageQueue OUT_QUEUE = OutboundMessageQueue.getInstance();
    private static final InboundMessageQueue IN_QUEUE = InboundMessageQueue.getInstance();

    private TeeMessageQueue() {
        // forbidden inheritance
    }

    public static TeeMessageQueue getInstance() {
        return INSTANCE;
    }

    public void send( final ClientMessage msg ) {
        final MessageImpl msgImpl = ( MessageImpl ) msg;
        try {
            IN_QUEUE.add( msgImpl );
            OUT_QUEUE.add( msgImpl );
        }
        catch ( final InterruptedException e ) {
            LOGGER.error( "Not able to add message to the queue", e );
        }
    }
}

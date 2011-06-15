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
package org.jboss.ircbot;

/**
 * <p>
 * Defines methods that all IRC bot services have to implement.
 * </p>
 * <p>
 * A bot service is a small Java program that runs within an IRC bot runtime.
 * There can be two types of bot services implemented.
 * <ul>
 * <li>synchronous bot services just listening or responding to
 * <code>{@link #onMessage(ServerMessage)}</code> event</li>
 * <li>asynchronous bot services not listening to
 * <code>{@link #onMessage(ServerMessage)}</code> but generating notification
 * messages (usually start their own threads)</li>
 * </ul>
 * </p>
 * <p>
 * Note that synchronous services can behave as one way services (just listening
 * on the channel) or message services (server message may result in multiple
 * messages being sent back to the IRC server).
 * </p>
 * <p>
 * To implement this interface, you can extend
 * <code>{@link org.jboss.ircbot.AbstractBotService}</code> template class.
 * </p>
 * <p>
 * This interface defines life-cycle methods and are called in the following
 * sequence:
 * <ol>
 * <li>When bot service starts its <code>{@link #init(BotRuntime)}</code> method
 * is called.</li>
 * <li>Each message from IRC server will result in
 * <code>{@link #onMessage(ServerMessage)}</code> method call.</li>
 * <li>When bot service stops its <code>{@link #destroy()}</code> method is
 * called.</li>
 * </ol>
 * </p>
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 * @see AbstractBotService
 * @param <T>
 *            either X2JB compatible interface or <code>Void</code> if custom
 *            service configuration is not supported.
 */
public interface BotService< T > {

    /**
     * Initializes bot service.
     * 
     * @param runtime
     *            bot runtime
     * @throws BotException
     *             if some problem occurs
     */
    void init( BotRuntime< T > runtime ) throws BotException;

    /**
     * Destroys bot service.
     * 
     * @throws BotException
     *             if some problem occurs
     */
    void destroy() throws BotException;

    /**
     * Each message from IRC server will result in this method being called. Bot
     * services can implement this method to process such events.
     * 
     * @param msg
     *            IRC server message
     * @throws BotException
     *             if ome problem occurs
     */
    void onMessage( ServerMessage msg ) throws BotException;

    /**
     * Each message from IRC bot will result in this method being called. Bot
     * services can implement this method to process such events.
     * 
     * @param msg
     *            IRC bot message
     * @throws BotException
     *             if ome problem occurs
     */
    void onMessage( ClientMessage msg ) throws BotException;

    /**
     * Each bot service can provide its own config. This method have to return
     * either X2JB compatible interface or null if no configuration is available
     * for this service.
     * 
     * @return X2JB compatible interface or null
     */
    Class< T > getConfigClass();
}

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
 * Template class providing default bot service implementation.
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 * @see BotService
 * @param <T>
 *            either X2JB compatible interface or <code>Void</code> if custom
 *            service configuration is not supported.
 */
public abstract class AbstractBotService< T > implements BotService< T > {

    /**
     * Bot service runtime.
     */
    private BotRuntime< T > runtime;

    /**
     * Constructor.
     */
    protected AbstractBotService() {
        // intended for inheritance
    }

    /**
     * Template method that stores runtime in local variable.
     * 
     * @see BotService#init(BotRuntime)
     * 
     * @param runtime
     *            bot runtime
     * @throws BotException
     *             if some problem occurs
     */
    public void init( final BotRuntime< T > runtime ) throws BotException {
        this.runtime = runtime;
    }

    /**
     * Template method that cleanups local runtime variable.
     * 
     * @see BotService#destroy()
     * 
     * @throws BotException
     *             if some problem occurs
     */
    public void destroy() throws BotException {
        runtime = null;
    }

    /**
     * Template method that does nothing.
     * 
     * @see BotService#onMessage(ServerMessage)
     * 
     * @param msg
     *            IRC server message
     * @throws BotException
     *             if some problem occurs
     */
    public void onMessage( final ServerMessage msg ) throws BotException {
        // does nothing
    }

    /**
     * Template method that does nothing.
     * 
     * @see BotService#onMessage(ClientMessage)
     * 
     * @param msg
     *            IRC bot message
     * @throws BotException
     *             if some problem occurs
     */
    public void onMessage( final ClientMessage msg ) throws BotException {
        // does nothing
    }

    /**
     * Template method that returns null.
     * 
     * @see BotService#getConfigClass()
     * 
     * @return <code>null</code>
     */
    public Class< T > getConfigClass() {
        return null;
    }

    /**
     * Returns service config.
     * 
     * @return service config
     */
    protected final T getServiceConfig() {
        assertRuntimeAvailable();
        return runtime.getServiceConfig();
    }

    /**
     * Returns bot config.
     * 
     * @return bot config
     */
    protected final BotConfig getBotConfig() {
        assertRuntimeAvailable();
        return runtime.getBotConfig();
    }

    /**
     * Returns message factory.
     * 
     * @return message factory
     */
    protected final MessageFactory getMessageFactory() {
        assertRuntimeAvailable();
        return runtime.getMessageFactory();
    }

    /**
     * Returns server connection for sending generated messages.
     * 
     * @return server connection
     */
    protected final ServerConnection getConnection() {
        assertRuntimeAvailable();
        return runtime.getConnection();
    }

    /**
     * Assert method that ensures proper subclassing.
     */
    private void assertRuntimeAvailable() {
        assert runtime != null : "Runtime not available. Did U call super.init() in your init() method?";
    }
}

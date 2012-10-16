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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.ircbot.AbstractBotService;
import org.jboss.ircbot.BotException;
import org.jboss.ircbot.BotRuntime;
import org.jboss.ircbot.MessageFactory;
import org.jboss.ircbot.ServerConnection;
import org.jboss.logging.Logger;

import com.sun.net.httpserver.HttpServer;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class GithubPushBotService extends AbstractBotService< GithubPushConfig > {

    private static final Logger LOGGER = Logger.getLogger( GithubPushBotService.class );
    private HttpServer server;

    public GithubPushBotService() {
        super();
    }

    @Override
    public void init( final BotRuntime< GithubPushConfig > runtime ) throws BotException {
        super.init( runtime );
        final GithubPushConfig ourConfig = runtime.getServiceConfig();
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
                server.createContext( "/", new GithubPushHttpHandler( conn, msgFactory, ourConfig.getNotifications() ) );
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
        } finally {
            super.destroy();
        }
    }

    @Override
    public Class< GithubPushConfig > getConfigClass() {
        return GithubPushConfig.class;
    }
}

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
package org.jboss.ircbot.services;

import static org.jboss.ircbot.Character.COLON;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.jboss.ircbot.config.Server;
import org.jboss.logging.Logger;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class ConnectionService implements Service< Socket > {

    private static final Logger LOG = Logger.getLogger( ConnectionService.class );
    private static final ConnectionService INSTANCE = new ConnectionService();
    private final InjectedValue< Server > injectedConfig = new InjectedValue< Server >();
    private Socket socket;

    private ConnectionService() {
        // forbidden inheritance
    }

    public Socket getValue() {
        return socket;
    }

    public void start( final StartContext context ) throws StartException {
        try {
            final String hostname = injectedConfig.getValue().getAddress();
            final int port = injectedConfig.getValue().getPort();
            final InetAddress targetAddress = InetAddress.getByName( hostname );
            LOG.trace( "Binding socket to " + hostname + COLON + port );
            socket = new Socket( targetAddress, port );
        }
        catch ( final Exception e ) {
            LOG.fatal( e.getMessage(), e );
            context.failed( new StartException( e ) );
        }
    }

    public void stop( final StopContext context ) {
        if ( socket != null ) {
            final String hostname = injectedConfig.getValue().getAddress();
            final int port = injectedConfig.getValue().getPort();
            LOG.trace( "Unbinding socket from " + hostname + COLON + port );
            try {
                socket.close();
            }
            catch ( final IOException e ) {
                LOG.warn( e.getMessage(), e );
            }
        }
    }

    private Injector< Server > getInjector() {
        return injectedConfig;
    }

    public static void install( final ServiceTarget serviceTarget ) {
        final ServiceBuilder< Socket > builder = serviceTarget.addService( Services.CONNECTION, INSTANCE );
        builder.addDependency( Services.CONFIGURATION, Server.class, INSTANCE.getInjector() );
        builder.install();
    }
}

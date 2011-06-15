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

import java.util.LinkedList;

import org.jboss.ircbot.BotService;
import org.jboss.ircbot.config.Processor;
import org.jboss.ircbot.config.Server;
import org.jboss.ircbot.impl.Notifier;
import org.jboss.logging.Logger;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.AbstractService;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class ProcessorsService extends AbstractService< Void > {

    private static final Logger LOG = Logger.getLogger( ProcessorsService.class );
    private static final ProcessorsService INSTANCE = new ProcessorsService();
    private final LinkedList< BotService< ? >> listeners = new LinkedList< BotService< ? >>();
    private final InjectedValue< Server > injectedConfig = new InjectedValue< Server >();

    private ProcessorsService() {
        // forbidden inheritance
    }

    public void start( final StartContext context ) throws StartException {
        try {
            final ServiceContainer container = context.getController().getServiceContainer();
            for ( final Processor processor : injectedConfig.getValue().getProcessors().getArray() ) {
                final BotService< ? > object = instantiate( processor );
                // register bot service
                final ProcessorService service = new ProcessorService( object, processor.getConfigElement() );
                final ServiceName name = Services.PROCESSORS.append( processor.getName() );
                final Injector< Server > configInjector = service.getInjector();
                container.addService( name, service ).addDependency( Services.PROCESSORS ).addDependency( Services.CONFIGURATION, Server.class, configInjector ).install();
                // register bot service as listener?
                listeners.add( object );
            }
            final Thread workerThread = new Thread( new Notifier( listeners ), "Inbound Queue Worker Thread" );
            workerThread.setDaemon( true );
            workerThread.start();
        }
        catch ( final Exception e ) {
            LOG.fatal( e.getMessage(), e );
            context.failed( new StartException( e ) );
        }
    }

    public void stop( final StopContext context ) {
        listeners.clear();
    }

    private BotService< ? > instantiate( final Processor processor ) throws Exception {
        return ( BotService< ? > ) Class.forName( processor.getClassName() ).newInstance();
    }

    private Injector< Server > getInjector() {
        return injectedConfig;
    }

    public static void install( final ServiceTarget serviceTarget ) {
        final ServiceBuilder< Void > builder = serviceTarget.addService( Services.PROCESSORS, INSTANCE );
        builder.addDependency( Services.CONFIGURATION, Server.class, INSTANCE.getInjector() );
        builder.install();
    }
}

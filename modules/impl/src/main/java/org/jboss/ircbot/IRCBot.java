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

import org.jboss.ircbot.services.ConfigurationService;
import org.jboss.ircbot.services.ConnectionService;
import org.jboss.ircbot.services.PathService;
import org.jboss.ircbot.services.ProcessorsService;
import org.jboss.ircbot.services.SerializationService;
import org.jboss.ircbot.services.Services;
import org.jboss.logging.Logger;
import org.jboss.msc.service.BatchServiceTarget;
import org.jboss.msc.service.ServiceContainer;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class IRCBot implements Runnable {

    private static final Logger LOG = Logger.getLogger( IRCBot.class );
    private final String configFile;

    IRCBot( final String configFile ) {
        this.configFile = configFile;
    }

    public void run() {
        try {
            LOG.info( "Starting IRC Bot" );
            final ServiceContainer container = ServiceContainer.Factory.create( "ircbot" );
            Runtime.getRuntime().addShutdownHook( new Thread( "IRC Bot Shutdown Hook" ) {

                @Override
                public void run() {
                    LOG.info( "Stopping IRC Bot" );
                    container.shutdown();
                }
            } );
            final BatchServiceTarget serviceTarget = container.batchTarget();
            PathService.addService( Services.CONFIG_FILE, configFile, serviceTarget );
            ConfigurationService.install( serviceTarget );
            ConnectionService.install( serviceTarget );
            SerializationService.install( serviceTarget );
            ProcessorsService.install( serviceTarget );
            container.awaitTermination();
        } catch ( final Exception e ) {
            throw new RuntimeException( e );
        }
    }
}

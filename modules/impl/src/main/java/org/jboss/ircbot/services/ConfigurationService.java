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

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
import org.w3c.dom.Document;
import org.x2jb.bind.XML2Java;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class ConfigurationService implements Service< Server > {

    private static final Logger LOG = Logger.getLogger( ConfigurationService.class );
    private static final ConfigurationService INSTANCE = new ConfigurationService();
    private final InjectedValue< String > injectedConfigFile = new InjectedValue< String >();
    private Server config;

    private ConfigurationService() {
        // forbidden inheritance
    }

    public void start( final StartContext context ) throws StartException {
        try {
            config = getConfig( injectedConfigFile.getValue() );
        }
        catch ( final Exception e ) {
            LOG.fatal( e.getMessage(), e );
            context.failed( new StartException( e ) );
        }
    }

    public void stop( final StopContext context ) {
        config = null;
    }

    public Server getValue() {
        return config;
    }

    private Injector< String > getInjector() {
        return injectedConfigFile;
    }

    public static void install( final ServiceTarget serviceTarget ) {
        final ServiceBuilder< Server > builder = serviceTarget.addService( Services.CONFIGURATION, INSTANCE );
        builder.addDependency( Services.CONFIG_FILE, String.class, INSTANCE.getInjector() );
        builder.install();
    }

    private static Server getConfig( final String file ) throws Exception {
        final Document configDoc = getDocument( file );
        return XML2Java.bind( configDoc, Server.class );
    }

    private static Document getDocument( final String fileName ) throws Exception {
        final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setIgnoringComments( true );
        final DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse( new FileInputStream( new File( fileName ) ) );
    }
}

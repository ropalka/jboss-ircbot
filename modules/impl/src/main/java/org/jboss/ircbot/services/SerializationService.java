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

import java.io.IOException;
import java.net.Socket;

import org.jboss.ircbot.impl.MessageDeserializer;
import org.jboss.ircbot.impl.MessageSerializer;
import org.jboss.logging.Logger;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.AbstractService;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.value.InjectedValue;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class SerializationService extends AbstractService< Void > {

    private static final Logger LOG = Logger.getLogger( SerializationService.class );
    private static final SerializationService INSTANCE = new SerializationService();
    private final InjectedValue< Socket > injectedSocket = new InjectedValue< Socket >();

    private SerializationService() {
        // forbidden inheritance
    }

    public void start( final StartContext context ) throws StartException {
        try {
            // deserialization thread
            final Runnable deserializer = new MessageDeserializer( injectedSocket.getValue().getInputStream() );
            final Thread deserializerThread = new Thread( deserializer, "IRC Bot Message deserializer" );
            deserializerThread.setDaemon( true );
            deserializerThread.start();
            // serialization thread
            final Runnable serializer = new MessageSerializer( injectedSocket.getValue().getOutputStream() );
            final Thread serializerThread = new Thread( serializer, "IRC Bot Message serializer" );
            serializerThread.setDaemon( true );
            serializerThread.start();
        }
        catch ( final IOException e ) {
            LOG.fatal( e.getMessage(), e );
            context.failed( new StartException( e ) );
        }
    }

    private Injector< Socket > getInjector() {
        return injectedSocket;
    }

    public static void install( final ServiceTarget serviceTarget ) {
        final ServiceBuilder< Void > builder = serviceTarget.addService( Services.SERIALIZATION, INSTANCE );
        builder.addDependency( Services.CONNECTION, Socket.class, INSTANCE.getInjector() );
        builder.install();
    }
}

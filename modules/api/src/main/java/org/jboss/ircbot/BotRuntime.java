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
 * IRC bot runtime.
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 * @param <T>
 *            either X2JB compatible interface or <code>Void</code>
 */
public interface BotRuntime< T > {

    /**
     * Gets bot config.
     * 
     * @return bot config
     */
    BotConfig getBotConfig();

    /**
     * Gets config associated with service.
     * 
     * @return service config
     */
    T getServiceConfig();

    /**
     * Returns message factory.
     * 
     * @return message factory
     */
    MessageFactory getMessageFactory();

    /**
     * Returns connection to IRC server.
     * 
     * @return connection to IRC server
     */
    ServerConnection getConnection();
}

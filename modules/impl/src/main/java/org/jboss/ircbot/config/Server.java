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
package org.jboss.ircbot.config;

import org.x2jb.bind.Binding;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public interface Server {

    @Binding( nodeName = "address", isElementNode = false )
    String getAddress();

    @Binding( nodeName = "port", isElementNode = false )
    int getPort();

    @Binding( nodeName = "nick", isElementNode = false )
    String getBotName();

    @Binding( nodeName = "password", isElementNode = false, isNodeMandatory = false )
    String getBotPassword();

    @Binding( nodeName = "fullname", isElementNode = false )
    String getBotFullName();

    @Binding( nodeName = "channels" )
    Channels getChannels();

    @Binding( nodeName = "processors" )
    Processors getProcessors();
}

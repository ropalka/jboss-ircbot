/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.jboss.ircbot.impl;

import java.util.Set;

import org.jboss.ircbot.BotConfig;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class BotConfigImpl implements BotConfig
{

    private final String serverAddress;
    private final int serverPort;
    private final Set<String> serverChannels;
    private final String botNick;
    private final String botPassword;
    private final String botFullName;

    public BotConfigImpl(final String serverAddress, final int serverPort, final Set<String> serverChannels,
        final String botNick, final String botFullName, final String botPassword)
    {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.serverChannels = serverChannels;
        this.botNick = botNick;
        this.botFullName = botFullName;
        this.botPassword = botPassword;
    }

    public String getServerAddress()
    {
        return serverAddress;
    }

    public int getServerPort()
    {
        return serverPort;
    }

    public Set<String> getServerChannels()
    {
        return serverChannels;
    }

    public String getBotNick()
    {
        return botNick;
    }

    public String getBotFullName()
    {
        return botFullName;
    }

    public String getBotPassword()
    {
        return botPassword;
    }

}

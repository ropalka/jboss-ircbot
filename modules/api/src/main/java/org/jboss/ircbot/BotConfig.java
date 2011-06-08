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
package org.jboss.ircbot;

import java.util.Set;

/**
 * IRC Bot config.
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public interface BotConfig
{

    /**
     * Returns server address this bot is connected to.
     * 
     * @return server address
     */
    String getServerAddress();

    /**
     * Returns server port this bot is connected to.
     * 
     * @return server port
     */
    int getServerPort();

    /**
     * Returns channels this bot is listening/speaking to on target IRC server.
     * 
     * @return server channels
     */
    Set<String> getServerChannels();

    /**
     * Returns bot nick.
     * 
     * @return bot nick
     */
    String getBotNick();

    /**
     * Returns full bot name.
     * 
     * @return full bot name
     */
    String getBotFullName();

    /**
     * Returns bot password.
     *
     * @return bot password or <b>null</b> if not specified.
     */
    String getBotPassword();

}

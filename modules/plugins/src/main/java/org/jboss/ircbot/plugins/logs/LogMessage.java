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
package org.jboss.ircbot.plugins.logs;

import static org.jboss.ircbot.Character.AMPERSAND;
import static org.jboss.ircbot.Character.EXCLAMATION_MARK;
import static org.jboss.ircbot.Character.NUMBER_SIGN;
import static org.jboss.ircbot.Character.PLUS_SIGN;
import static org.jboss.ircbot.Character.SPACE;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
final class LogMessage
{

    private String date;
    private String time;
    private String channel;
    private String message;

    LogMessage()
    {
        super();
    }

    void setDate(final String date)
    {
        this.date = date;
    }

    String getDate()
    {
        return date;
    }

    void setTime(final String time)
    {
        this.time = time;
    }

    String getTime()
    {
        return time;
    }

    void setChannel(final String channel)
    {
        assertChannelName(channel);
        this.channel = channel;
    }

    private static void assertChannelName(final String channel)
    {
        if (channel.startsWith(AMPERSAND))
        {
            return;
        }
        if (channel.startsWith(EXCLAMATION_MARK))
        {
            return;
        }
        if (channel.startsWith(PLUS_SIGN))
        {
            return;
        }
        if (channel.startsWith(NUMBER_SIGN))
        {
            return;
        }

        throw new IllegalArgumentException("Incorrect channel name: " + channel);
    }

    String getChannel()
    {
        return channel;
    }

    void setMessage(final String message)
    {
        this.message = message;
    }

    String getMessage()
    {
        return message;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append(time).append(SPACE).append(message);
        return sb.toString();
    }

}

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

import static org.jboss.ircbot.Color.BLACK;
import static org.jboss.ircbot.Color.BLUE;
import static org.jboss.ircbot.Color.BROWN;
import static org.jboss.ircbot.Color.CYAN;
import static org.jboss.ircbot.Color.DARK_BLUE;
import static org.jboss.ircbot.Color.DARK_GREEN;
import static org.jboss.ircbot.Color.DARK_GRAY;
import static org.jboss.ircbot.Color.GREEN;
import static org.jboss.ircbot.Color.LIGHT_GRAY;
import static org.jboss.ircbot.Color.MAGENTA;
import static org.jboss.ircbot.Color.OLIVE;
import static org.jboss.ircbot.Color.PURPLE;
import static org.jboss.ircbot.Color.RED;
import static org.jboss.ircbot.Color.TEAL;
import static org.jboss.ircbot.Color.WHITE;
import static org.jboss.ircbot.Color.YELLOW;
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

    public static final String BLACK_COLOR = "BLACK";
    public static final String BLUE_COLOR = "BLUE";
    public static final String BROWN_COLOR = "BROWN";
    public static final String CYAN_COLOR = "CYAN";
    public static final String DARK_BLUE_COLOR = "DARK_BLUE";
    public static final String DARK_GREEN_COLOR = "DARK_GREEN";
    public static final String DARK_GRAY_COLOR = "DARK_GRAY";
    public static final String GREEN_COLOR = "GREEN";
    public static final String LIGHT_GRAY_COLOR = "LIGHT_GRAY";
    public static final String MAGENTA_COLOR = "MAGENTA";
    public static final String OLIVE_COLOR = "OLIVE";
    public static final String PURPLE_COLOR = "PURPLE";
    public static final String RED_COLOR = "RED";
    public static final String TEAL_COLOR = "TEAL";
    public static final String WHITE_COLOR = "WHITE";
    public static final String YELLOW_COLOR = "YELLOW";

    private String date;
    private String time;
    private String channel;
    private String color;
    private String detail;
    
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
    
    void setColor(final String color)
    {
        this.color = color;
    }

    String getColor()
    {
        return color;
    }

    void setDetail(final String detail)
    {
        this.detail = detail;
    }

    String getDetail()
    {
        return detail;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append(time).append(SPACE).append(getColorString(color)).append(SPACE).append(detail);
        return sb.toString();
    }
    
    private static String getColorString(final String color)
    {
        String colorString = null;
        if (BLACK == color)
        {
            colorString = BLACK_COLOR;
        }
        else if (BLUE == color)
        {
            colorString = BLUE_COLOR;
        }
        else if (BROWN == color)
        {
            colorString = BROWN_COLOR;
        }
        else if (CYAN == color)
        {
            colorString = CYAN_COLOR;
        }
        else if (DARK_BLUE == color)
        {
            colorString = DARK_BLUE_COLOR;
        }
        else if (DARK_GREEN == color)
        {
            colorString = DARK_GREEN_COLOR;
        }
        else if (DARK_GRAY == color)
        {
            colorString = DARK_GRAY_COLOR;
        }
        else if (GREEN == color)
        {
            colorString = GREEN_COLOR;
        }
        else if (LIGHT_GRAY == color)
        {
            colorString = LIGHT_GRAY_COLOR;
        }
        else if (MAGENTA == color)
        {
            colorString = MAGENTA_COLOR;
        }
        else if (OLIVE == color)
        {
            colorString = OLIVE_COLOR;
        }
        else if (PURPLE == color)
        {
            colorString = PURPLE_COLOR;
        }
        else if (RED == color)
        {
            colorString = RED_COLOR;
        }
        else if (TEAL == color)
        {
            colorString = TEAL_COLOR;
        }
        else if (WHITE == color)
        {
            colorString = WHITE_COLOR;
        }
        else if (YELLOW == color)
        {
            colorString = YELLOW_COLOR;
        }

        if (colorString == null)
        {
            throw new IllegalArgumentException("Unrecognized color " + color);
        }

        return colorString;
    }

}

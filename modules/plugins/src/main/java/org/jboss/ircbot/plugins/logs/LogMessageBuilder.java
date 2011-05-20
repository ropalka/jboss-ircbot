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

import static org.jboss.ircbot.Character.COLON;
import static org.jboss.ircbot.Character.DIGIT_0;
import static org.jboss.ircbot.Character.HYPHEN;
import static org.jboss.ircbot.Character.LEFT_SQUARE_BRACKET;
import static org.jboss.ircbot.Character.RIGHT_SQUARE_BRACKET;

import java.util.Calendar;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
final class LogMessageBuilder
{
    
    private static final int TEN = 10;
    private static final int SIXTY = 60;
    private static final String[] TIME_VALUES = new String[SIXTY];
    
    static
    {
        // initialize time values from 0 to 9
        for (int i = 0; i < TEN; i++)
        {
            TIME_VALUES[i] = DIGIT_0 + i;
        }
        // initialize time values from 10 to 59
        for (int i = TEN; i < SIXTY; i++)
        {
            TIME_VALUES[i] = String.valueOf(i);
        }
    }
    
    final LogMessage delegate;

    private LogMessageBuilder()
    {
        delegate = new LogMessage();
        delegate.setDate(getDate());
        delegate.setTime(getTime());
    }
    
    static LogMessageBuilder newInstance()
    {
        return new LogMessageBuilder();
    }

    LogMessageBuilder setChannel(final String channel)
    {
        delegate.setChannel(channel);
        return this;
    }

    LogMessageBuilder setColor(final String color)
    {
        delegate.setColor(color);
        return this;
    }

    LogMessageBuilder setDetail(final String detail)
    {
        delegate.setDetail(detail);
        return this;
    }

    LogMessage build()
    {
        if (delegate.getColor() == null)
        {
            throw new IllegalStateException("Message color not specified");
        }
        if (delegate.getDetail() == null)
        {
            throw new IllegalStateException("Message detail not specified");
        }
        return delegate;
    }

    private static String getTime()
    {
        final StringBuilder sb = new StringBuilder();
        final Calendar calendar = Calendar.getInstance();
        // hour
        final String hour = format(calendar.get(Calendar.HOUR_OF_DAY));
        sb.append(LEFT_SQUARE_BRACKET).append(hour).append(COLON);
        // minute
        final String minute = format(calendar.get(Calendar.MINUTE));
        sb.append(minute).append(COLON);
        // second
        final String second = format(calendar.get(Calendar.SECOND));
        sb.append(second).append(RIGHT_SQUARE_BRACKET);
        
        return sb.toString();
    }
    
    private static String getDate()
    {
        final StringBuilder sb = new StringBuilder();
        final Calendar calendar = Calendar.getInstance();
        // year
        final String year = String.valueOf(calendar.get(Calendar.YEAR));
        sb.append(year).append(HYPHEN);
        // month
        final String month = format(calendar.get(Calendar.MONTH) + 1);
        sb.append(month).append(HYPHEN);
        // day
        final String day = format(calendar.get(Calendar.DAY_OF_MONTH));
        sb.append(day);
        
        return sb.toString();
    }

    private static String format(final int value)
    {
        return TIME_VALUES[value];
    }

}

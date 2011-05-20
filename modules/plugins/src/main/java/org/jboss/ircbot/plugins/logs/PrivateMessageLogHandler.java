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
import static org.jboss.ircbot.Character.GREATER_THAN;
import static org.jboss.ircbot.Character.LESS_THAN;
import static org.jboss.ircbot.Character.SPACE;
import static org.jboss.ircbot.Character.TILDE;
import static org.jboss.ircbot.Color.BLACK;
import static org.jboss.ircbot.Color.PURPLE;
import static org.jboss.ircbot.plugins.logs.MessageUtils.getChannel;
import static org.jboss.ircbot.plugins.logs.MessageUtils.getUser;

import java.util.regex.Pattern;

import org.jboss.ircbot.Message;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
final class PrivateMessageLogHandler implements LogHandler
{

    private static final String ACTION = "ACTION";
    private static final Pattern ME_MESSAGE_PATTERN = Pattern.compile("\\x01" + ACTION + ".*\\x01");
    private static final LogHandler SINGLETON = new PrivateMessageLogHandler();

    private PrivateMessageLogHandler()
    {
        // forbidden inheritance
    }
    
    static LogHandler getInstance()
    {
        return SINGLETON;
    }

    public LogMessage getLogMessage(final Message msg)
    {
        final LogMessageBuilder logBuilder = LogMessageBuilder.newInstance();
        logBuilder.setChannel(getChannel(msg, false));
        logBuilder.setColor(getColor(msg));
        logBuilder.setDetail(getDetail(msg));
        return logBuilder.build();
    }
    
    private static String getDetail(final Message msg)
    {
        final String user = getUser(msg);
        final StringBuilder sb = new StringBuilder();
        sb.append(LESS_THAN).append(user).append(GREATER_THAN).append(SPACE);
        sb.append(getMessageBodyASCII(msg));
        return sb.toString();
    }
    
    private static String getMessageBody(final Message msg)
    {
        final String msgBody = msg.getParamsString();
        final int colonIndex = msgBody.indexOf(COLON) + 1;

        return msgBody.substring(colonIndex);
    }
    
    private static String getMessageBodyASCII(final Message msg)
    {
        if (!isMonologue(msg))
        {
            return toASCII(getMessageBody(msg));
        }
        else
        {
            return toASCII(getMessageBody(msg).trim().substring(ACTION.length() + 1));
        }
    }
    
    private static String toASCII(final String s)
    {
        final StringBuilder sb = new StringBuilder();
        char c = 0;
        for (int i = 0; i < s.length(); i++)
        {
            c = s.charAt(i);
            if (c >= SPACE.charAt(0) && c <= TILDE.charAt(0))
            {
                sb.append(c);
            }
        }
        
        return sb.toString();
    }
    
    private static String getColor(final Message msg)
    {
        return isMonologue(msg) ? PURPLE : BLACK;
    }
    
    private static boolean isMonologue(final Message msg)
    {
        final String msgBody = getMessageBody(msg);
        
        return ME_MESSAGE_PATTERN.matcher(msgBody).find();
    }
    
}

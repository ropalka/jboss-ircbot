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

import static org.jboss.ircbot.Character.ASTERISK;
import static org.jboss.ircbot.Character.SPACE;
import static org.jboss.ircbot.Color.MAGENTA;
import static org.jboss.ircbot.plugins.logs.MessageUtils.getUser;

import org.jboss.ircbot.Message;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
final class QuitMessageLogHandler implements LogHandler
{

    private static final LogHandler SINGLETON = new QuitMessageLogHandler();

    private QuitMessageLogHandler()
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
        logBuilder.setColor(MAGENTA);
        logBuilder.setDetail(getDetail(msg));
        return logBuilder.build();
    }

    private static String getDetail(final Message msg)
    {
        final String user = getUser(msg);
        final StringBuilder sb = new StringBuilder();
        sb.append(ASTERISK).append(ASTERISK).append(ASTERISK).append(SPACE);
        sb.append(user).append(SPACE).append("has").append(SPACE).append("quit");
        sb.append(SPACE).append("IRC");
        return sb.toString();
    }

}

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

import static org.jboss.ircbot.Command.JOIN;
import static org.jboss.ircbot.Command.PART;
import static org.jboss.ircbot.Command.PRIVMSG;
import static org.jboss.ircbot.Command.QUIT;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.IdentityHashMap;
import java.util.Map;

import org.jboss.ircbot.AbstractBotService;
import org.jboss.ircbot.BotException;
import org.jboss.ircbot.ClientMessage;
import org.jboss.ircbot.Command;
import org.jboss.ircbot.Message;
import org.jboss.ircbot.ServerMessage;
import org.jboss.logging.Logger;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class LoggingBotService extends AbstractBotService<Void>
{

    private static final Logger LOG = Logger.getLogger(LoggingBotService.class);
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final Map<Command, LogHandler> LOG_HANDLERS = new IdentityHashMap<Command, LogHandler>();

    static
    {
        LOG_HANDLERS.put(JOIN, JoinMessageLogHandler.getInstance());
        LOG_HANDLERS.put(PART, PartMessageLogHandler.getInstance());
        LOG_HANDLERS.put(PRIVMSG, PrivateMessageLogHandler.getInstance());
        LOG_HANDLERS.put(QUIT, QuitMessageLogHandler.getInstance());
    }

    public LoggingBotService()
    {
        super();
    }

    @Override
    public void onMessage(final ServerMessage msg) throws BotException
    {
        if (!isNullSender(msg))
        {
            logMessage(msg);
        }
    }

    @Override
    public void onMessage(final ClientMessage msg) throws BotException
    {
        if (!isNullSender(msg))
        {
            logMessage(msg);
        }
    }

    private boolean isNullSender(final Message msg)
    {
        return msg.getSender() == null;
    }

    private void logMessage(final Message msg)
    {
        final LogHandler handler = LOG_HANDLERS.get(msg.getCommand());
        if (handler != null)
        {
            final LogMessage logMessage = handler.getLogMessage(msg);
            writeToFileSystem(logMessage);
        }
    }

    private void writeToFileSystem(final LogMessage logMessage)
    {
        final String channel = logMessage.getChannel();
        final String date = logMessage.getDate();
        if (channel != null)
        {
            writeToFile(channel, date, logMessage);
        }
        else
        {
            for (final String configChannel : getBotConfig().getServerChannels())
            {
                writeToFile(configChannel, date, logMessage);
            }
        }
    }

    private static void writeToFile(final String channel, final String date, final LogMessage logMessage)
    {
        Writer out = null;
        try
        {
            out = getOutputStream(channel, date);
            out.write(logMessage.toString());
            out.write(LINE_SEPARATOR);
        }
        catch (final IOException e)
        {
            LOG.warn("Unable to write to file", e);
        }
        finally
        {
            safeClose(out);
        }
    }

    private static void safeClose(final Closeable closeable)
    {
        if (closeable == null)
        {
            return;
        }

        try
        {
            closeable.close();
        }
        catch (final IOException e)
        {
            LOG.warn("Unable to close file", e);
        }
    }

    private static Writer getOutputStream(final String channel, final String date) throws IOException
    {
        return new FileWriter(getFile(channel, date), true);
    }

    private static File getFile(final String channel, final String date) throws IOException
    {
        final File channelDir = new File(channel);
        final File dateFile = new File(channel, date);
        if (!channelDir.exists())
        {
            if (!channelDir.mkdirs())
            {
                throw new RuntimeException("Insufficient rights to create directory " + channelDir.getAbsolutePath());
            }
        }
        if (!dateFile.exists())
        {
            if (!dateFile.createNewFile())
            {
                throw new RuntimeException("Insufficient rights to create file " + dateFile.getAbsolutePath());
            }
        }

        return dateFile;
    }

}

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

import static org.jboss.ircbot.Character.AT_SIGN;

import java.util.LinkedList;
import java.util.List;

import org.jboss.ircbot.BotConfig;
import org.jboss.ircbot.BotRuntime;
import org.jboss.ircbot.BotService;
import org.jboss.ircbot.ClientMessage;
import org.jboss.ircbot.Command;
import org.jboss.ircbot.MessageBuilder;
import org.jboss.ircbot.MessageFactory;
import org.jboss.ircbot.Sender;
import org.jboss.ircbot.ServerConnection;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class BotRuntimeImpl<E> implements BotRuntime<E>
{

    private final BotConfig botConfig;
    private final Object serviceConfig;
    private final ServerConnection conn = TeeMessageQueue.getInstance();
    private final MessageFactory msgFactory;

    public BotRuntimeImpl(final BotConfig botConfig, final Object serviceConfig, final BotService<?> producer)
    {
        this.botConfig = botConfig;
        this.serviceConfig = serviceConfig;
        this.msgFactory = new MessageFactoryImpl(producer, botConfig.getBotNick());
    }

    public BotConfig getBotConfig()
    {
        return botConfig;
    }

    @SuppressWarnings("unchecked")
    public E getServiceConfig()
    {
        return (E) serviceConfig;
    }

    public MessageFactory getMessageFactory()
    {
        return msgFactory;
    }

    public ServerConnection getConnection()
    {
        return conn;
    }

    /**
     * Default message factory implementation.
     * 
     * @author <a href="ropalka@redhat.com">Richard Opalka</a>
     */
    private static final class MessageFactoryImpl implements MessageFactory
    {

        private final BotService<?> producer;
        private final String botName;

        private MessageFactoryImpl(final BotService<?> producer, final String botName)
        {
            this.producer = producer;
            this.botName = botName;
        }

        public MessageBuilder newMessage(final Command command)
        {
            if (command == Command.UNKNOWN)
            {
                throw new IllegalArgumentException("UNKNOWN not acceptable");
            }
            return new MessageBuilderImpl(command, producer, botName);
        }

    }

    private static final class MessageBuilderImpl implements MessageBuilder
    {

        private static final String LOCALHOST = "localhost";
        private final BotService<?> producer;
        private final Command command;
        private final Sender botName;
        private final List<String> params = new LinkedList<String>();
        private boolean finished;

        private MessageBuilderImpl(final Command command, final BotService<?> producer, final String botName)
        {
            this.command = command;
            this.producer = producer;
            this.botName = newSender(botName);
        }

        public MessageBuilder addParam(final String s)
        {
            if (s == null)
            {
                throw new IllegalArgumentException();
            }
            if (finished)
            {
                throw new IllegalStateException();
            }
            params.add(s);
            return this;
        }

        public MessageBuilder addParam(final Object o)
        {
            if (o == null)
            {
                throw new IllegalArgumentException();
            }
            addParam(o.toString());
            return this;
        }

        public ClientMessage build()
        {
            if (finished)
            {
                throw new IllegalStateException();
            }
            finished = true;
            return new MessageImpl(botName, command, null, params, producer);
        }

        private Sender newSender(final String botName)
        {
            final StringBuilder botNameBuilder = new StringBuilder();
            botNameBuilder.append(botName).append(AT_SIGN).append(LOCALHOST);
            return SenderFactory.newInstance(botNameBuilder.toString());
        }

    }

}

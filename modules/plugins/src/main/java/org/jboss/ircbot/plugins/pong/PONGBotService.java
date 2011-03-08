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
package org.jboss.ircbot.plugins.pong;

import static org.jboss.ircbot.Command.PING;
import static org.jboss.ircbot.Command.PONG;

import org.jboss.ircbot.AbstractBotService;
import org.jboss.ircbot.BotException;
import org.jboss.ircbot.ClientMessage;
import org.jboss.ircbot.MessageBuilder;
import org.jboss.ircbot.ServerMessage;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class PONGBotService extends AbstractBotService<Void>
{

    public PONGBotService()
    {
        super();
    }

    public void onMessage(final ServerMessage msg) throws BotException
    {
        if (PING == msg.getCommand())
        {
            final ClientMessage pongMessage = newPongMessage(msg);
            getConnection().send(pongMessage);
        }
    }

    private ClientMessage newPongMessage(final ServerMessage pingMsg)
    {
        final MessageBuilder msgBuilder = getMessageFactory().newMessage(PONG);
        msgBuilder.addParam(getBotConfig().getBotNick());
        return msgBuilder.build();
    }

}

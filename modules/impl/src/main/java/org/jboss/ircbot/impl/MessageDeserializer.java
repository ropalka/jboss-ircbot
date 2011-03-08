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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.jboss.logging.Logger;

/**
 * Reads incoming IRC messages and adds them to the inbound message queue.
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class MessageDeserializer implements Runnable
{

    private static final Logger LOGGER = Logger.getLogger(MessageDeserializer.class);
    private final BufferedReader in;
    private String line;

    public MessageDeserializer(final InputStream in) throws IOException
    {
        this.in = new BufferedReader(new InputStreamReader(in));
    }

    public void run()
    {
        while (true)
        {
            try
            {
                if (in.ready())
                {
                    line = in.readLine();
                }
                if (line != null)
                {
                    LOGGER.info("Received from IRC server: " + line);
                    final MessageImpl msg = MessageImpl.valueOf(line);
                    if (msg != null)
                    {
                        InboundMessageQueue.getInstance().add(msg);
                    }
                    line = null;
                }
                else
                {
                    Thread.sleep(InboundMessageQueue.TIMEOUT);
                }
            }
            catch (final Exception e)
            {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

}

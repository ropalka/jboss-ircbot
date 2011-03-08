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

import java.util.List;

/**
 * Generic IRC message.
 * <p>
 * Params are space separated. See <a
 * href="http://tools.ietf.org/html/rfc2812">IRC client protocol
 * specification</a> for details to see what params are needed for particular
 * IRC command.
 * </p>
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 * @see Command
 * @see Sender
 */
public interface Message
{

    /**
     * Returns sender of this message or null if not available.
     * 
     * @return sender of this message
     */
    Sender getSender();

    /**
     * Returns IRC command of this message or
     * <code>{@link org.jboss.ircbot.Command#UNKNOWN}</code> if this message
     * doesn't contain IRC command.
     * 
     * @return IRC command
     */
    Command getCommand();

    /**
     * Returns message parameters.
     * 
     * @return message parameters
     */
    List<String> getParams();

    /**
     * Returns message parameters string.
     * 
     * @return message parameters string
     */
    String getParamsString();

}

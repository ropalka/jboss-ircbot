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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * IRC message fonts.
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class Font
{

    /**
     * Bold font.
     */
    public static final String BOLD = "\u0002";

    /**
     * Normal font (default).
     */
    public static final String NORMAL = "\u000f";

    /**
     * Underlined font.
     */
    public static final String UNDERLINE = "\u001f";

    /**
     * All color values.
     */
    private static final Collection<String> ALL_FONTS;

    static
    {
        final List<String> values = new LinkedList<String>();
        values.add(BOLD);
        values.add(NORMAL);
        values.add(UNDERLINE);
        ALL_FONTS = Collections.unmodifiableList(values);
    }

    /**
     * Constructor.
     */
    private Font()
    {
        // forbidden inheritance
    }

    /**
     * Returns all known colors.
     * 
     * @return all known colors.
     */
    public static Collection<String> values()
    {
        return ALL_FONTS;
    }

}

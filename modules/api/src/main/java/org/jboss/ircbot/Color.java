/*
 * JBoss, Home of Professional Open Source Copyright 2011 Red Hat Inc. and/or
 * its affiliates and other contributors as indicated by the @authors tag. All
 * rights reserved. See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, v. 2.1.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */
package org.jboss.ircbot;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * IRC text message colors.
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class Color {

    /**
     * Black color (default).
     */
    public static final String BLACK = "\u000301";
    /**
     * Blue color.
     */
    public static final String BLUE = "\u000312";
    /**
     * Brown color.
     */
    public static final String BROWN = "\u000305";
    /**
     * Cyan color.
     */
    public static final String CYAN = "\u000311";
    /**
     * Dark blue color.
     */
    public static final String DARK_BLUE = "\u000302";
    /**
     * Dark green color.
     */
    public static final String DARK_GREEN = "\u000303";
    /**
     * Dark gray color.
     */
    public static final String DARK_GRAY = "\u000314";
    /**
     * Green color.
     */
    public static final String GREEN = "\u000309";
    /**
     * Light gray color.
     */
    public static final String LIGHT_GRAY = "\u000315";
    /**
     * Magenta color.
     */
    public static final String MAGENTA = "\u000313";
    /**
     * Olive color.
     */
    public static final String OLIVE = "\u000307";
    /**
     * Purple color.
     */
    public static final String PURPLE = "\u000306";
    /**
     * Red color.
     */
    public static final String RED = "\u000304";
    /**
     * Teal color.
     */
    public static final String TEAL = "\u000310";
    /**
     * White color.
     */
    public static final String WHITE = "\u000300";
    /**
     * Yellow color.
     */
    public static final String YELLOW = "\u000308";
    /**
     * All color values.
     */
    private static final Collection< String > ALL_COLORS;
    static {
        final List< String > values = new LinkedList< String >();
        values.add( BLACK );
        values.add( BLUE );
        values.add( BROWN );
        values.add( CYAN );
        values.add( DARK_BLUE );
        values.add( DARK_GREEN );
        values.add( DARK_GRAY );
        values.add( GREEN );
        values.add( LIGHT_GRAY );
        values.add( MAGENTA );
        values.add( OLIVE );
        values.add( PURPLE );
        values.add( RED );
        values.add( TEAL );
        values.add( WHITE );
        values.add( YELLOW );
        ALL_COLORS = Collections.unmodifiableList( values );
    }

    /**
     * Constructor.
     */
    private Color() {
        // forbidden inheritance
    }

    /**
     * Returns all known colors.
     * 
     * @return all known colors.
     */
    public static Collection< String > values() {
        return ALL_COLORS;
    }
}

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
package org.jboss.ircbot.plugins.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class HTMLHelper {

    private static final Map< String, String > RESERVED_HTML_CHARACTERS = new HashMap< String, String >();
    private static final Map< String, String > ENTITY_NUMBERS = new HashMap< String, String >();
    private static final int MIN = 32;
    private static final int MAX = 127;
    static {
        RESERVED_HTML_CHARACTERS.put( "&nbsp;", " " );
        RESERVED_HTML_CHARACTERS.put( "&quot;", "\"" );
        RESERVED_HTML_CHARACTERS.put( "&apos;", "'" );
        RESERVED_HTML_CHARACTERS.put( "&amp;", "&" );
        RESERVED_HTML_CHARACTERS.put( "&lt;", "<" );
        RESERVED_HTML_CHARACTERS.put( "&gt;", ">" );
        for ( int i = MIN; i < MAX; i++ ) {
            ENTITY_NUMBERS.put( "&#" + i + ";", Character.toString( ( char ) i ) );
        }
    }

    private HTMLHelper() {
        // forbidden instantiation
    }

    public static String escape( final String s ) {
        String retVal = s;
        retVal = decode( retVal, RESERVED_HTML_CHARACTERS );
        retVal = decode( retVal, ENTITY_NUMBERS );
        return retVal;
    }

    private static String decode( final String s, final Map< String, String > substitution ) {
        String retVal = s;
        final Iterator< String > i = substitution.keySet().iterator();
        while ( i.hasNext() ) {
            final String target = i.next();
            final String replacement = substitution.get( target );
            retVal = retVal.replace( target, replacement );
        }
        return retVal;
    }
}

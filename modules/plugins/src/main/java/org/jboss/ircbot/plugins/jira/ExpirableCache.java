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
package org.jboss.ircbot.plugins.jira;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
final class ExpirableCache< E > implements Set< E > {

    private final Map< E, Long > map;
    private final int timeout;

    ExpirableCache( final int timeout ) {
        if ( timeout <= 0 ) {
            throw new IllegalArgumentException( "timeout cannot be negative" );
        }
        this.timeout = timeout;
        map = new HashMap< E, Long >();
    }

    public synchronized boolean contains( final Object key ) {
        // invalidate timed out cache entries first
        final Iterator< Entry< E, Long > > i = map.entrySet().iterator();
        final Long currentTime = System.currentTimeMillis();
        long duration;
        while ( i.hasNext() ) {
            final Entry< E, Long > entry = i.next();
            duration = ( currentTime - entry.getValue().longValue() ) / 1000;
            if ( duration > timeout ) {
                i.remove();
            }
        }
        // return valid cached data
        return map.containsKey( key );
    }

    public synchronized boolean add( final E key ) {
        if ( map.containsKey( key ) ) {
            return false;
        }
        map.put( key, System.currentTimeMillis() );
        return true;
    }

    public synchronized void clear() {
        map.clear();
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public Iterator< E > iterator() {
        throw new UnsupportedOperationException();
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    public < T > T[] toArray( final T[] a ) {
        throw new UnsupportedOperationException();
    }

    public boolean remove( final Object o ) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll( final Collection< ? > c ) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll( final Collection< ? extends E > c ) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll( final Collection< ? > c ) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll( final Collection< ? > c ) {
        throw new UnsupportedOperationException();
    }
}

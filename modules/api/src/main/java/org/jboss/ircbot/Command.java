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

import java.util.HashMap;
import java.util.Map;

/**
 * <a href="http://tools.ietf.org/html/rfc2812#section-3">IRC commands</a>.
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public enum Command {
    /**
     * Unknown command.
     */
    UNKNOWN( null ),
    /**
     * PASS command.
     */
    PASS( "PASS" ),
    /**
     * NICK command.
     */
    NICK( "NICK" ),
    /**
     * USER command.
     */
    USER( "USER" ),
    /**
     * OPER command.
     */
    OPER( "OPER" ),
    /**
     * MODE command.
     */
    MODE( "MODE" ),
    /**
     * SERVICE command.
     */
    SERVICE( "SERVICE" ),
    /**
     * QUIT command.
     */
    QUIT( "QUIT" ),
    /**
     * SQUIT command.
     */
    SQUIT( "SQUIT" ),
    /**
     * JOIN command.
     */
    JOIN( "JOIN" ),
    /**
     * PART command.
     */
    PART( "PART" ),
    /**
     * TOPIC command.
     */
    TOPIC( "TOPIC" ),
    /**
     * NAMES command.
     */
    NAMES( "NAMES" ),
    /**
     * LIST command.
     */
    LIST( "LIST" ),
    /**
     * INVITE command.
     */
    INVITE( "INVITE" ),
    /**
     * KICK command.
     */
    KICK( "KICK" ),
    /**
     * PRIVMSG command.
     */
    PRIVMSG( "PRIVMSG" ),
    /**
     * NOTICE command.
     */
    NOTICE( "NOTICE" ),
    /**
     * MOTD command.
     */
    MOTD( "MOTD" ),
    /**
     * LUSERS command.
     */
    LUSERS( "LUSERS" ),
    /**
     * VERSION command.
     */
    VERSION( "VERSION" ),
    /**
     * STATS command.
     */
    STATS( "STATS" ),
    /**
     * LINKS command.
     */
    LINKS( "LINKS" ),
    /**
     * TIME command.
     */
    TIME( "TIME" ),
    /**
     * CONNECT command.
     */
    CONNECT( "CONNECT" ),
    /**
     * TRACE command.
     */
    TRACE( "TRACE" ),
    /**
     * ADMIN command.
     */
    ADMIN( "ADMIN" ),
    /**
     * INFO command.
     */
    INFO( "INFO" ),
    /**
     * SERVLIST command.
     */
    SERVLIST( "SERVLIST" ),
    /**
     * SQUERY command.
     */
    SQUERY( "SQUERY" ),
    /**
     * WHO command.
     */
    WHO( "WHO" ),
    /**
     * WHOIS command.
     */
    WHOIS( "WHOIS" ),
    /**
     * WHOWAS command.
     */
    WHOWAS( "WHOWAS" ),
    /**
     * KILL command.
     */
    KILL( "KILL" ),
    /**
     * PING command.
     */
    PING( "PING" ),
    /**
     * PONG command.
     */
    PONG( "PONG" ),
    /**
     * ERROR command.
     */
    ERROR( "ERROR" ),
    /**
     * AWAY command.
     */
    AWAY( "AWAY" ),
    /**
     * REHASH command.
     */
    REHASH( "REHASH" ),
    /**
     * DIE command.
     */
    DIE( "DIE" ),
    /**
     * NS command.
     */
    NS( "NS" ),
    /**
     * RESTART command.
     */
    RESTART( "RESTART" ),
    /**
     * SUMMON command.
     */
    SUMMON( "SUMMON" ),
    /**
     * USERS command.
     */
    USERS( "USERS" ),
    /**
     * WALLOPS command.
     */
    WALLOPS( "WALLOPS" ),
    /**
     * USERHOST command.
     */
    USERHOST( "USERHOST" ),
    /**
     * ISON command.
     */
    ISON( "ISON" );

    /**
     * IRC command string.
     */
    private final String id;

    /**
     * Constructor.
     * 
     * @param id
     *            IRC command id
     */
    private Command( final String id ) {
        this.id = id;
    }

    /**
     * Commands cache.
     */
    private static final Map< String, Command > CACHE = new HashMap< String, Command >();
    static {
        for ( final Command command : values() ) {
            final String commandID = command.id;
            if ( commandID != null ) {
                CACHE.put( commandID, command );
            }
        }
    }

    /**
     * Factory method for obtaining instances of this class. Returns
     * <code>UNKNOWN</code> if the command is not recognized.
     * 
     * @param type
     *            command type
     * @return command instance
     */
    public static Command of( final String type ) {
        final Command element = CACHE.get( type );
        return element == null ? UNKNOWN : element;
    }

    /**
     * Returns command string.
     * 
     * @return command string
     */
    public String toString() {
        return id != null ? id : "";
    }
}

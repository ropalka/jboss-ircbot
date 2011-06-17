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
package org.jboss.ircbot.impl;

import static org.jboss.ircbot.Character.AT_SIGN;
import static org.jboss.ircbot.Character.COLON;
import static org.jboss.ircbot.Character.EXCLAMATION_MARK;

import java.util.StringTokenizer;

import org.jboss.ircbot.Sender;
import org.jboss.ircbot.User;

/**
 * Sender factory implementation.
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
final class SenderFactory {

    private SenderFactory() {
        // forbidden inheritance
    }

    static Sender newInstance( final String sender ) {
        return sender.contains( AT_SIGN ) ? new UserImpl( sender ) : new SenderImpl( sender );
    }

    /**
     * Default user implementation.
     * 
     * @author <a href="ropalka@redhat.com">Richard Opalka</a>
     */
    static final class UserImpl implements User {

        private static final int TOKEN_COUNT_INCLUDING_USER_INFO = 3;
        private final String host;
        private final String nick;
        private final String user;

        private UserImpl( final String prefix ) {
            final StringTokenizer st = new StringTokenizer( prefix, AT_SIGN + EXCLAMATION_MARK + COLON );
            if ( st.countTokens() == TOKEN_COUNT_INCLUDING_USER_INFO ) {
                nick = st.nextToken();
                user = st.nextToken();
                host = st.nextToken();
            } else {
                nick = st.nextToken();
                user = "";
                host = st.nextToken();
            }
        }

        public String getHost() {
            return host;
        }

        public String getNickName() {
            return nick;
        }

        public String getUserName() {
            return user;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append( nick );
            if ( user != null ) {
                sb.append( EXCLAMATION_MARK );
                sb.append( user );
            }
            sb.append( AT_SIGN );
            sb.append( host );
            return sb.toString();
        }
    }

    /**
     * Default sender implementation.
     * 
     * @author <a href="ropalka@redhat.com">Richard Opalka</a>
     */
    private static final class SenderImpl implements Sender {

        private final String host;

        private SenderImpl( final String host ) {
            final StringTokenizer st = new StringTokenizer( host, COLON );
            this.host = st.nextToken();
        }

        public String getHost() {
            return host;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append( host );
            return sb.toString();
        }
    }
}

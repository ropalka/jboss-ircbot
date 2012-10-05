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
package org.jboss.ircbot.plugins.github;

import static org.jboss.ircbot.Character.COLON;
import static org.jboss.ircbot.Character.LEFT_SQUARE_BRACKET;
import static org.jboss.ircbot.Character.PERIOD;
import static org.jboss.ircbot.Character.RIGHT_SQUARE_BRACKET;
import static org.jboss.ircbot.Character.SLASH;
import static org.jboss.ircbot.Character.SPACE;
import static org.jboss.ircbot.Color.BLUE;
import static org.jboss.ircbot.Color.OLIVE;
import static org.jboss.ircbot.Color.PURPLE;
import static org.jboss.ircbot.Font.BOLD;
import static org.jboss.ircbot.Font.NORMAL;

import java.util.StringTokenizer;

/**
 * For Github commit API see <a
 * href="http://developer.github.com/v3/git/commits/">this</a> page.
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
final class GithubCommit {

    private static final String GIT_HUB_API_PREFIX = "https://api.github.com/repos/ropalka/jboss-as/git/commits/";
    private static final int SHA_UPPER_BOUND = 7;
    private static final String GIT_KEYWORD = "git";
    private String commitId;
    private String commitIdFull;
    private String userId;
    private String userName;
    private String description;
    private String repository;
    private final String jsonCommitUrl;

    GithubCommit( final String htmlCommitUrl ) {
        final StringTokenizer st = new StringTokenizer( htmlCommitUrl, SLASH );
        // skipping https protocol
        st.nextToken();
        // skipping github.com
        st.nextToken();
        // setting user Id
        setUserId( st.nextToken() );
        // setting repository
        setRepository( st.nextToken() );
        // skipping commit string
        st.nextToken();
        // setting sha
        setCommitId( st.nextToken() );
        // construct commit URL
        final StringBuilder sb = new StringBuilder();
        sb.append( GIT_HUB_API_PREFIX );
        sb.append( commitIdFull );
        jsonCommitUrl = new String( sb );
    }

    String getJsonCommitUrl() {
        return jsonCommitUrl;
    }

    void setUserName( final String userName ) {
        this.userName = userName;
    }

    String getUserName() {
        return userName;
    }

    void setUserId( final String userId ) {
        this.userId = userId;
    }

    String getUserId() {
        return userId;
    }

    void setCommitId( final String commitId ) {
        this.commitIdFull = commitId;
        this.commitId = commitId.substring( 0, SHA_UPPER_BOUND );
    }

    String getCommitId() {
        return commitId;
    }

    void setDescription( final String description ) {
        this.description = description;
    }

    String getDescription() {
        return description;
    }

    void setRepository( final String repository ) {
        this.repository = repository;
    }

    String getRepository() {
        return repository;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        // GIT keyword
        sb.append( COLON ).append( BOLD ).append( GIT_KEYWORD ).append( BOLD ).append( SPACE );
        // Github project name
        sb.append( NORMAL ).append( LEFT_SQUARE_BRACKET ).append( BLUE );
        sb.append( repository ).append( NORMAL ).append( RIGHT_SQUARE_BRACKET ).append( SPACE );
        // commit unique prefix
        sb.append( OLIVE ).append( commitId ).append( PERIOD ).append( PERIOD ).append( SPACE );
        // author
        sb.append( PURPLE ).append( userName ).append( SPACE );
        // description
        sb.append( NORMAL ).append( description );
        return sb.toString();
    }
}

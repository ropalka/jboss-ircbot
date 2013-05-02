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
import static org.jboss.ircbot.Character.LEFT_PARENTHESIS;
import static org.jboss.ircbot.Character.LEFT_SQUARE_BRACKET;
import static org.jboss.ircbot.Character.RIGHT_PARENTHESIS;
import static org.jboss.ircbot.Character.RIGHT_SQUARE_BRACKET;
import static org.jboss.ircbot.Character.SLASH;
import static org.jboss.ircbot.Character.SPACE;
import static org.jboss.ircbot.Color.BLUE;
import static org.jboss.ircbot.Color.OLIVE;
import static org.jboss.ircbot.Color.TEAL;
import static org.jboss.ircbot.Color.PURPLE;
import static org.jboss.ircbot.Font.BOLD;
import static org.jboss.ircbot.Font.NORMAL;

import java.util.StringTokenizer;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
final class GithubPullRequest {

    private static final String NEW_KEYWORD = "new";
    private static final String GIT_KEYWORD = "git";
    private static final String PULL_KEYWORD = "pull";
    private static final String REQUEST_KEYWORD = "req";
    private static final String CLOSED_KEYWORD = "closed";
    private String repository;
    private String status;
    private String user;
    private String description;
    private String url;
    private boolean isNew;
    private String id;
    private String githubPullRequestJsonURL;

    GithubPullRequest( final String htmlPullRequestUrl ) {
        url = htmlPullRequestUrl;
        final StringTokenizer st = new StringTokenizer( htmlPullRequestUrl, SLASH );
        // skipping https protocol
        st.nextToken();
        // skipping github.com
        st.nextToken();
        // setting user Id
        user = st.nextToken();
        // setting repository
        repository = st.nextToken();
        // skipping pull string
        st.nextToken();
        // setting sha
        id = st.nextToken();
        // construct commit URL
        final StringBuilder sb = new StringBuilder();
        sb.append( "https://api.github.com/repos/" );
        sb.append( user ).append( "/" );
        sb.append( repository ).append( "/pulls/" );
        sb.append( id );
        githubPullRequestJsonURL = new String( sb );
    }

    GithubPullRequest( final boolean isNew, final String repository, final String status, final String author, final String description, final String url ) {
        this.isNew = isNew;
        this.repository = repository;
        this.status = status;
        this.user = author;
        this.description = description;
        this.url = url;
    }

    void setDescription( final String description ) {
        this.description = description;
    }

    String getDescription() {
        return description;
    }

    void setStatus( final String status ) {
        this.status = status;
    }

    String getJsonURL() {
        return githubPullRequestJsonURL;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        // NEW keyword
        sb.append( COLON ).append( BOLD );
        if ( isNew && !CLOSED_KEYWORD.equals( status ) ) {
            sb.append( NEW_KEYWORD ).append( SPACE );
        }
        // GIT keyword
        sb.append( GIT_KEYWORD ).append( SPACE );
        // PULL keyword
        sb.append( PULL_KEYWORD ).append( SPACE );
        // REQUEST keyword
        sb.append( REQUEST_KEYWORD ).append( SPACE );
        // Github project name
        sb.append( NORMAL ).append( LEFT_SQUARE_BRACKET ).append( BLUE );
        sb.append( repository ).append( NORMAL ).append( RIGHT_SQUARE_BRACKET ).append( SPACE );
        // status
        sb.append( LEFT_PARENTHESIS ).append( OLIVE ).append( status ).append( NORMAL ).append( RIGHT_PARENTHESIS ).append( SPACE );
        // author
        sb.append( PURPLE ).append( user ).append( SPACE );
        // description
        sb.append( NORMAL ).append( description ).append( SPACE );
        // url
        sb.append( TEAL ).append( url );
        return sb.toString();
    }
}

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
import static org.jboss.ircbot.Character.SPACE;
import static org.jboss.ircbot.Color.BLUE;
import static org.jboss.ircbot.Color.OLIVE;
import static org.jboss.ircbot.Color.TEAL;
import static org.jboss.ircbot.Color.PURPLE;
import static org.jboss.ircbot.Font.BOLD;
import static org.jboss.ircbot.Font.NORMAL;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
final class GithubPushCommit {

    private static final int SHA_UPPER_BOUND = 7;
    private static final String GIT_KEYWORD = "git";
    private static final String PUSH_KEYWORD = "push";
    private String branch;
    private String commitId;
    private String userName;
    private String description;
    private String repository;

    GithubPushCommit() {
    }

    void setUserName( final String userName ) {
        this.userName = userName;
    }

    String getUserName() {
        return userName;
    }

    void setBranch( final String branch ) {
        this.branch = branch;
    }

    String getBranch() {
        return branch;
    }

    void setCommitId( final String commitId ) {
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
        sb.append( COLON ).append( BOLD ).append( GIT_KEYWORD ).append( SPACE );
        // Github project name
        sb.append( NORMAL ).append( LEFT_SQUARE_BRACKET ).append( BLUE );
        sb.append( repository ).append( NORMAL ).append( RIGHT_SQUARE_BRACKET ).append( SPACE );
        // push keyword
        sb.append( BOLD ).append( PUSH_KEYWORD ).append( SPACE );
        // branch
        sb.append( NORMAL ).append( TEAL ).append( branch ).append( SPACE );
        if ( commitId != null ) {
            // commit unique prefix
            sb.append( OLIVE ).append( commitId ).append( PERIOD ).append( PERIOD ).append( SPACE );
        }
        if ( userName != null ) {
            // author
            sb.append( PURPLE ).append( userName ).append( SPACE );
        }
        // description
        sb.append( NORMAL ).append( description );
        return sb.toString();
    }
}

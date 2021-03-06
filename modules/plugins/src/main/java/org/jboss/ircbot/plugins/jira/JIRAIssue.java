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

import static org.jboss.ircbot.Character.COLON;
import static org.jboss.ircbot.Character.COMMA;
import static org.jboss.ircbot.Character.LEFT_PARENTHESIS;
import static org.jboss.ircbot.Character.LEFT_SQUARE_BRACKET;
import static org.jboss.ircbot.Character.RIGHT_PARENTHESIS;
import static org.jboss.ircbot.Character.RIGHT_SQUARE_BRACKET;
import static org.jboss.ircbot.Character.SPACE;
import static org.jboss.ircbot.Color.DARK_GREEN;
import static org.jboss.ircbot.Color.OLIVE;
import static org.jboss.ircbot.Color.PURPLE;
import static org.jboss.ircbot.Color.TEAL;
import static org.jboss.ircbot.Font.BOLD;
import static org.jboss.ircbot.Font.NORMAL;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
final class JIRAIssue {

    private final String jiraId;
    private final String jiraHtmlURL;
    private final String jiraJsonURL;
    private String description;
    private String assignee;
    private String status;
    private String resolution;
    private String type;
    private String priority;

    JIRAIssue( final String jiraId, final String jiraHtmlURL, final String jiraJsonURL ) {
        this.jiraId = jiraId;
        this.jiraHtmlURL = jiraHtmlURL;
        this.jiraJsonURL = jiraJsonURL;
    }

    String getHtmlURL() {
        return jiraHtmlURL;
    }
    
    String getJsonURL() {
        return jiraJsonURL;
    }

    String getDescription() {
        return description;
    }

    void setDescription( final String description ) {
        this.description = description;
    }

    String getAssignee() {
        return assignee;
    }

    void setAssignee( final String assignee ) {
        this.assignee = assignee;
    }

    String getStatus() {
        return status;
    }

    void setStatus( final String status ) {
        this.status = status;
    }

    String getResolution() {
        return resolution;
    }

    void setResolution( final String resolution ) {
        this.resolution = resolution;
    }

    String getType() {
        return type;
    }

    void setType( final String type ) {
        this.type = type;
    }

    String getPriority() {
        return priority;
    }

    void setPriority( final String priority ) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        // JIRA keyword
        sb.append( COLON ).append( BOLD ).append( "jira" ).append( BOLD ).append( SPACE );
        // JIRA id
        sb.append( NORMAL ).append( LEFT_SQUARE_BRACKET ).append( DARK_GREEN );
        sb.append( jiraId ).append( NORMAL ).append( RIGHT_SQUARE_BRACKET ).append( SPACE );
        // JIRA description
        sb.append( description ).append( SPACE );
        sb.append( LEFT_SQUARE_BRACKET ).append( TEAL );
        // JIRA status
        sb.append( status ).append( SPACE );
        // JIRA resolution
        sb.append( LEFT_PARENTHESIS ).append( resolution ).append( RIGHT_PARENTHESIS ).append( SPACE );
        // JIRA type
        sb.append( type );
        sb.append( NORMAL ).append( COMMA ).append( SPACE );
        // JIRA priority
        if ( priority != null ) {
            sb.append( OLIVE ).append( priority ).append( NORMAL ).append( COMMA ).append( SPACE );
        }
        // JIRA assignee
        sb.append( PURPLE ).append( assignee );
        // JIRA URL
        sb.append( NORMAL ).append( RIGHT_SQUARE_BRACKET ).append( SPACE ).append( jiraHtmlURL );
        return sb.toString();
    }
}

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

import static org.jboss.ircbot.Character.SLASH;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;

import org.fossnova.json.JsonObject;
import org.fossnova.json.JsonString;
import org.fossnova.json.JsonValueFactory;
import org.fossnova.json.stream.JsonReader;
import org.fossnova.json.stream.JsonStreamFactory;
import org.jboss.logging.Logger;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
final class JIRAIssuePageScraper {

    private static final Logger LOGGER = Logger.getLogger( JIRAIssuePageScraper.class );
    private static final JIRAIssuePageScraper INSTANCE = new JIRAIssuePageScraper();
    private static final String ASSIGNEE = "assignee";
    private static final String DISPLAY_NAME = "displayName";
    private static final String ISSUE_TYPE = "issuetype";
    private static final String NAME = "name";
    private static final String PRIORITY = "priority";
    private static final String RESOLUTION = "resolution";
    private static final String STATUS = "status";
    private static final String SUMMARY = "summary";
    private static final String UNASSIGNED = "Unassigned";
    private static final String UNRESOLVED = "Unresolved";
    static {
        CookieHandler.setDefault( new CookieManager() );
    }

    private JIRAIssuePageScraper() {
        // forbidden inheritance
    }

    static JIRAIssuePageScraper getInstance() {
        return INSTANCE;
    }

    JIRAIssue scrape( final String jiraId, final String httpURL, final String jsonURL ) {
        final JIRAIssue jiraIssue = new JIRAIssue( jiraId, httpURL + SLASH + jiraId, jsonURL + SLASH + jiraId );
        InputStream is = null;
        try {
            final URL jiraIssuePage = new URL( jiraIssue.getJsonURL() );
            is = jiraIssuePage.openConnection().getInputStream();
            final JsonReader jsonReader = JsonStreamFactory.newInstance().newJsonReader( is );
            final JsonObject jsonObject = ( JsonObject ) JsonValueFactory.newInstance().readFrom( jsonReader );
            final JsonObject fieldsObject = ( JsonObject ) jsonObject.get( "fields" );
            scrapeAssignee( jiraIssue, fieldsObject );
            scrapeDescription( jiraIssue, fieldsObject );
            scrapePriority( jiraIssue, fieldsObject );
            scrapeResolution( jiraIssue, fieldsObject );
            scrapeStatus( jiraIssue, fieldsObject );
            scrapeType( jiraIssue, fieldsObject );
        } catch ( final Exception e ) {
            LOGGER.error( e.getMessage(), e );
        } finally {
            safeClose( is );
        }
        return jiraIssue.getDescription() != null ? jiraIssue : null;
    }

    private void scrapeAssignee( final JIRAIssue issue, final JsonObject fieldsObject ) throws IOException {
        final JsonObject assignee = ( JsonObject ) fieldsObject.get( ASSIGNEE );
        if ( assignee != null ) {
            final JsonString value = ( JsonString ) assignee.get( DISPLAY_NAME );
            issue.setAssignee( value.getString() );
        } else {
            issue.setAssignee( UNASSIGNED );
        }
    }

    private void scrapeDescription( final JIRAIssue issue, final JsonObject fieldsObject ) throws IOException {
        final JsonString value = ( JsonString ) fieldsObject.get( SUMMARY );
        issue.setDescription( value.getString() );
    }

    private void scrapeStatus( final JIRAIssue issue, final JsonObject fieldsObject ) throws IOException {
        final JsonObject status = ( JsonObject ) fieldsObject.get( STATUS );
        final JsonString value = ( JsonString ) status.get( NAME );
        issue.setStatus( value.getString() );
    }

    private void scrapePriority( final JIRAIssue issue, final JsonObject fieldsObject ) throws IOException {
        final JsonObject priority = ( JsonObject ) fieldsObject.get( PRIORITY );
        if ( priority != null ) {
            final JsonString value = ( JsonString ) priority.get( NAME );
            issue.setPriority( value.getString() );
        }
    }

    private void scrapeType( final JIRAIssue issue, final JsonObject fieldsObject ) throws IOException {
        final JsonObject issueType = ( JsonObject ) fieldsObject.get( ISSUE_TYPE );
        final JsonString value = ( JsonString ) issueType.get( NAME );
        issue.setType( value.getString() );
    }

    private void scrapeResolution( final JIRAIssue issue, final JsonObject fieldsObject ) throws IOException {
        final JsonObject resolution = ( JsonObject ) fieldsObject.get( RESOLUTION );
        if ( resolution != null ) {
            final JsonString value = ( JsonString ) resolution.get( NAME );
            issue.setResolution( value.getString() );
        } else {
            issue.setResolution( UNRESOLVED );
        }
    }
    
    private static void safeClose( final Closeable closeable ) {
        if ( closeable != null ) {
            try {
                closeable.close();
            } catch ( final IOException e ) {
                LOGGER.error( e.getMessage(), e );
            }
        }
    }
}

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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.fossnova.json.JsonObject;
import org.fossnova.json.JsonString;
import org.fossnova.json.JsonValueFactory;
import org.jboss.logging.Logger;

/**
 * For Github pull API see <a
 * href="http://developer.github.com/v3/pulls/">this</a> page.
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
final class GithubPullRequestPageScraper {

    private static final Logger LOGGER = Logger.getLogger( GithubPullRequestPageScraper.class );
    private static final GithubPullRequestPageScraper INSTANCE = new GithubPullRequestPageScraper();
    private static final String STATE = "state";
    private static final String TITLE = "title";

    private GithubPullRequestPageScraper() {
        // forbidden inheritance
    }

    static GithubPullRequestPageScraper getInstance() {
        return INSTANCE;
    }

    GithubPullRequest scrape( final String pullRequestURL ) {
        final GithubPullRequest pullRequest = new GithubPullRequest( pullRequestURL );
        InputStream is = null;
        try {
            final URL githubPullRequestPage = new URL( pullRequest.getJsonURL() );
            is = githubPullRequestPage.openConnection().getInputStream();
            final JsonObject jsonPullRequest = ( JsonObject ) JsonValueFactory.getInstance().readFrom( is );
            LOGGER.info( jsonPullRequest.toString() );
            scrapeStatus( pullRequest, jsonPullRequest );
            scrapeDescription( pullRequest, jsonPullRequest );
        } catch ( final Exception e ) {
            LOGGER.error( e.getMessage(), e );
        } finally {
            safeClose( is );
        }
        return pullRequest.getDescription() != null ? pullRequest : null;
    }

    private static void scrapeDescription( final GithubPullRequest pullRequest, final JsonObject pullRequestData ) {
        final JsonString description = ( JsonString ) pullRequestData.get( TITLE );
        pullRequest.setDescription( description.getString() );
    }

    private static void scrapeStatus( final GithubPullRequest pullRequest, final JsonObject pullRequestData ) {
        final JsonString status = ( JsonString ) pullRequestData.get( STATE );
        pullRequest.setStatus( status.getString() );
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

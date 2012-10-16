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
 * For Github commit API see <a
 * href="http://developer.github.com/v3/git/commits/">this</a> page.
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
final class GithubCommitPageScraper {

    private static final Logger LOGGER = Logger.getLogger( GithubCommitPageScraper.class );
    private static final GithubCommitPageScraper INSTANCE = new GithubCommitPageScraper();
    private static final String AUTHOR = "author";
    private static final String MESSAGE = "message";
    private static final String NAME = "name";

    private GithubCommitPageScraper() {
        // forbidden inheritance
    }

    static GithubCommitPageScraper getInstance() {
        return INSTANCE;
    }

    GithubCommit scrape( final String commitURL ) {
        final GithubCommit commit = new GithubCommit( commitURL );
        InputStream is = null;
        try {
            final URL githubCommitPage = new URL( commit.getJsonCommitUrl() );
            is = githubCommitPage.openConnection().getInputStream();
            final JsonObject jsonCommit = ( JsonObject ) JsonValueFactory.newInstance().readFrom( is );
            scrapeAuthor( commit, jsonCommit );
            scrapeDescription( commit, jsonCommit );
        } catch ( final Exception e ) {
            LOGGER.error( e.getMessage(), e );
        } finally {
            safeClose( is );
        }
        return commit.getDescription() != null ? commit : null;
    }

    private static void scrapeDescription( final GithubCommit commit, final JsonObject commitData ) {
        final JsonString msg = ( JsonString ) commitData.get( MESSAGE );
        commit.setDescription( msg.getString() );
    }

    private static void scrapeAuthor( final GithubCommit commit, final JsonObject commitData ) {
        final JsonObject author = ( JsonObject ) commitData.get( AUTHOR );
        final JsonString authorName = ( JsonString ) author.get( NAME );
        commit.setUserName( authorName.getString() );
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

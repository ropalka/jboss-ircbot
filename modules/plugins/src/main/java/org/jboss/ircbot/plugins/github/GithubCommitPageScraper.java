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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.logging.Logger;
import org.w3c.dom.Document;
import org.x2jb.bind.XML2Java;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
final class GithubCommitPageScraper {

    private static final Logger LOGGER = Logger.getLogger( GithubCommitPageScraper.class );
    private static final GithubCommitPageScraper INSTANCE = new GithubCommitPageScraper();

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
            final URL githubCommitPage = new URL( commit.getAPICommitURL() );
            is = githubCommitPage.openConnection().getInputStream();
            final Document doc = getDocument( is );
            final GithubCommitData commitData = XML2Java.bind( doc, GithubCommitData.class );
            transform( commitData, commit );
        } catch ( final Exception e ) {
            LOGGER.fatal( e.getMessage(), e );
        } finally {
            safeClose( is );
        }
        return commit.getDescription() != null ? commit : null;
    }

    private static void transform( final GithubCommitData commitData, final GithubCommit commit ) {
        commit.setUserName( commitData.getAuthor().getName() );
        commit.setDescription( commitData.getMessage() );
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

    private static Document getDocument( final InputStream is ) throws Exception {
        final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setIgnoringComments( true );
        final DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse( is );
    }
}

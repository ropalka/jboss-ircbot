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

import static org.jboss.ircbot.Command.PRIVMSG;

import java.io.IOException;
import java.io.InputStream;

import org.fossnova.fue.stream.FueFactory;
import org.fossnova.fue.stream.FueReader;
import org.fossnova.json.JsonArray;
import org.fossnova.json.JsonObject;
import org.fossnova.json.JsonString;
import org.fossnova.json.JsonValueFactory;
import org.jboss.ircbot.MessageBuilder;
import org.jboss.ircbot.MessageFactory;
import org.jboss.ircbot.ServerConnection;
import org.jboss.ircbot.plugins.github.GithubPushConfig.Notify;
import org.jboss.logging.Logger;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
final class GithubPushHttpHandler implements HttpHandler {

    private static final String CONTENT_LENGTH_HEADER = "Content-Length";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String GITHUB_EVENT_HEADER = "X-Github-Event";
    private static final String GITHUB_PUSH_EVENT = "push";
    private static final String FORM_URL_ENCODING_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String PAYLOAD = "payload";
    private static final String AUTHOR = "author";
    private static final String COMMITS = "commits";
    private static final String COMPARE = "compare";
    private static final String ID = "id";
    private static final String MESSAGE = "message";
    private static final String NAME = "name";
    private static final String REF = "ref";
    private static final String REPOSITORY = "repository";
    private static final String URL = "url";
    private static final Logger LOGGER = Logger.getLogger( GithubPushHttpHandler.class );
    private final ServerConnection conn;
    private final MessageFactory msgFactory;
    private final Notify[] notifications;

    GithubPushHttpHandler( final ServerConnection conn, final MessageFactory msgFactory, final Notify[] notifications ) {
        this.conn = conn;
        this.msgFactory = msgFactory;
        this.notifications = notifications;
    }

    public void handle( final HttpExchange msg ) throws IOException {
        try {
            final Headers headers = msg.getRequestHeaders();
            if ( isGithubPushRequest( headers ) ) {
                final JsonObject pushData = getPushData( msg );
                if ( pushData != null ) {
                    final String repositoryURL = scrapeRepositoryURL( pushData );
                    final boolean sendNotification = isNotificationRequested( repositoryURL );
                    if ( sendNotification ) {
                        final String repository = scrapeRepository( pushData );
                        final String branch = scrapeBranch( pushData );
                        final GithubPushCommit[] commits = scrapeCommits( repository, branch, pushData );
                        final GithubPushCompare compare = scrapeCompare( repository, branch, pushData );
                        notifyChannels( commits, compare, repositoryURL );
                    }
                }
            }
        } finally {
            msg.sendResponseHeaders( 200, -1 );
        }
    }

    private boolean isGithubPushRequest( final Headers headers ) {
        // ensure X-Github-Event HTTP header is present and holding expected value
        if ( !headers.containsKey( GITHUB_EVENT_HEADER ) ) {
            LOGGER.warn( "Missing HTTP header: " + GITHUB_EVENT_HEADER );
            return false;
        }
        final String currentEvent = headers.getFirst( GITHUB_EVENT_HEADER );
        if ( !GITHUB_PUSH_EVENT.equals( currentEvent ) ) {
            LOGGER.warn( "Not Github push event. Current event is: '" + currentEvent + "'" );
            return false;
        }
        // ensure Content-Type HTTP header is present and holding expected value
        if ( !headers.containsKey( CONTENT_TYPE_HEADER ) ) {
            LOGGER.warn( "Missing HTTP header: " + CONTENT_TYPE_HEADER );
            return false;
        }
        final String contentType = headers.getFirst( CONTENT_TYPE_HEADER );
        if ( !FORM_URL_ENCODING_CONTENT_TYPE.equals( contentType ) ) {
            LOGGER.warn( "Not Github push event. Current event is: '" + contentType + "'" );
            return false;
        }
        // ensure Content-Length HTTP header is present
        if ( !headers.containsKey( CONTENT_LENGTH_HEADER ) ) {
            LOGGER.warn( "Missing HTTP header " + CONTENT_LENGTH_HEADER );
            return false;
        }
        return true;
    }

    private JsonObject getPushData( final HttpExchange msg ) throws IOException {
        InputStream is = null;
        try {
            is = msg.getRequestBody();
            final FueReader fueReader = FueFactory.newInstance().newFueReader( is );
            // read payload key
            fueReader.next();
            if ( !PAYLOAD.equals( fueReader.getKey() ) ) {
                LOGGER.warn( PAYLOAD + " form URL encoding key not found" );
                return null;
            }
            // read JSON value
            fueReader.next();
            final String jsonString = fueReader.getValue();
            return ( JsonObject ) JsonValueFactory.newInstance().readFrom( jsonString );
        } catch ( final Exception e ) {
            LOGGER.fatal( e.getMessage(), e );
        } finally {
            is.close();
        }
        return null;
    }

    private boolean isNotificationRequested( final String repositoryURL ) {
        for ( final Notify notification : notifications ) {
            if ( notification.getRepository().startsWith( repositoryURL ) ) {
                return true;
            }
        }
        return false;
    }

    private void notifyChannels( final GithubPushCommit[] commits, final GithubPushCompare compare, final String repositoryURL ) {
        for ( final Notify notification : notifications ) {
            if ( notification.getRepository().startsWith( repositoryURL ) ) {
                // send commits detail
                for ( final GithubPushCommit commit : commits ) {
                    final MessageBuilder msgBuilder = msgFactory.newMessage( PRIVMSG );
                    msgBuilder.addParam( notification.getChannel() );
                    msgBuilder.addParam( commit );
                    conn.send( msgBuilder.build() );
                }
                // send compare URL
                final MessageBuilder msgBuilder = msgFactory.newMessage( PRIVMSG );
                msgBuilder.addParam( notification.getChannel() );
                msgBuilder.addParam( compare );
                conn.send( msgBuilder.build() );
            }
        }
    }

    private String scrapeBranch( final JsonObject pushData ) {
        final String branchReference = ( ( JsonString ) pushData.get( REF ) ).getString();
        return branchReference.substring( branchReference.lastIndexOf( "/" ) + 1 );
    }

    private String scrapeRepository( final JsonObject pushData ) {
        final JsonObject repository = ( JsonObject ) pushData.get( REPOSITORY );
        final JsonString repositoryName = ( JsonString ) repository.get( NAME );
        return repositoryName.getString();
    }

    private String scrapeRepositoryURL( final JsonObject pushData ) {
        final JsonObject repository = ( JsonObject ) pushData.get( REPOSITORY );
        final JsonString repositoryURL = ( JsonString ) repository.get( URL );
        return repositoryURL.getString();
    }

    private GithubPushCommit[] scrapeCommits( final String repository, final String branch, final JsonObject pushData ) {
        final JsonArray commits = ( JsonArray ) pushData.get( COMMITS );
        final GithubPushCommit[] retVal = new GithubPushCommit[ commits.size() ];
        JsonObject commitData = null;
        for ( int i = 0; i < commits.size(); i++ ) {
            commitData = ( JsonObject ) commits.get( i );
            retVal[ i ] = scrapeCommit( repository, branch, commitData );
        }
        return retVal;
    }

    private GithubPushCompare scrapeCompare( final String repository, final String branch, final JsonObject pushData ) {
        final GithubPushCompare retVal = new GithubPushCompare();
        // set repository
        retVal.setRepository( repository );
        // set branch
        retVal.setBranch( branch );
        // set compare URL
        final JsonString compareURL = ( JsonString ) pushData.get( COMPARE );
        retVal.setCompareURL( compareURL.getString() );
        // return wrapper
        return retVal;
    }

    private GithubPushCommit scrapeCommit( final String repository, final String branch, final JsonObject commitData ) {
        final GithubPushCommit commit = new GithubPushCommit();
        // set repository
        commit.setRepository( repository );
        // set branch
        commit.setBranch( branch );
        // process commit id
        final JsonString commitId = ( JsonString ) commitData.get( ID );
        commit.setCommitId( commitId.getString() );
        // process commit description
        final JsonString commitDescription = ( JsonString ) commitData.get( MESSAGE );
        commit.setDescription( commitDescription.getString() );
        // process commit author
        final JsonObject commitAuthor = ( JsonObject ) commitData.get( AUTHOR );
        final JsonString authorName = ( JsonString ) commitAuthor.get( NAME );
        commit.setUserName( authorName.getString() );
        // return wrapper
        return commit;
    }
}

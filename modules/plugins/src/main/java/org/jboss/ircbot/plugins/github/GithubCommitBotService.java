/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.jboss.ircbot.plugins.github;

import static org.jboss.ircbot.Command.PRIVMSG;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.ircbot.AbstractBotService;
import org.jboss.ircbot.BotException;
import org.jboss.ircbot.MessageBuilder;
import org.jboss.ircbot.ServerMessage;
import org.jboss.ircbot.User;

/**
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public final class GithubCommitBotService extends AbstractBotService<Void>
{

    private static final Pattern GITHUB_COMMIT_PATTERN = Pattern.compile("https://github.com/.*/commit/[0-9a-f]*");

    public GithubCommitBotService()
    {
        super();
    }

    @Override
    public void onMessage(final ServerMessage msg) throws BotException
    {
        if (PRIVMSG == msg.getCommand())
        {
            final String msgTarget = getMessageTarget(msg);
            final Set<String> commitURLs = getGithubCommits(msg);

            for (final String commitURL : commitURLs)
            {
                final GithubCommit commit = GithubCommitPageScraper.getInstance().scrape(commitURL);
                if (commit != null)
                {
                    final MessageBuilder msgBuilder = getMessageFactory().newMessage(PRIVMSG);
                    msgBuilder.addParam(msgTarget);
                    msgBuilder.addParam(commit);
                    getConnection().send(msgBuilder.build());
                }
            }
        }
    }

    private String getMessageTarget(final ServerMessage msg)
    {
        String retVal = msg.getParams().get(0);
        if (getBotConfig().getBotNick().equals(retVal))
        {
            // private message to our bot, respond privately
            retVal = ((User) msg.getSender()).getNickName();
        }
        return retVal;
    }

    private static Set<String> getGithubCommits(final ServerMessage msg)
    {
        final Set<String> retVal = new HashSet<String>();
        final Matcher m = GITHUB_COMMIT_PATTERN.matcher(msg.getParamsString());
        while (m.find())
        {
            retVal.add(m.group());
        }
        return retVal;
    }

}

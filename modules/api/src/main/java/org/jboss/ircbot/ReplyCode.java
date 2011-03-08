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
package org.jboss.ircbot;

import java.util.HashMap;
import java.util.Map;

/**
 * See <a href="http://tools.ietf.org/html/rfc2812#section-5">IRC server command
 * responses and error codes</a>.
 * 
 * @author <a href="ropalka@redhat.com">Richard Opalka</a>
 */
public enum ReplyCode
{

    /**
     * UNKNOWN server reply code.
     */
    UNKNOWN(null, false),

    /**
     * 001 server reply code.
     */
    RPL_WELCOME("001", false),

    /**
     * 002 server reply code.
     */
    RPL_YOURHOST("002", false),

    /**
     * 003 server reply code.
     */
    RPL_CREATED("003", false),

    /**
     * 004 server reply code.
     */
    RPL_MYINFO("004", false),

    /**
     * 005 server reply code.
     */
    RPL_BOUNCE("005", false),

    /**
     * 200 server reply code.
     */
    RPL_TRACELINK("200", false),

    /**
     * 201 server reply code.
     */
    RPL_TRACECONNECTING("201", false),

    /**
     * 202 server reply code.
     */
    RPL_TRACEHANDSHAKE("202", false),

    /**
     * 203 server reply code.
     */
    RPL_TRACEUNKNOWN("203", false),

    /**
     * 204 server reply code.
     */
    RPL_TRACEOPERATOR("204", false),

    /**
     * 205 server reply code.
     */
    RPL_TRACEUSER("205", false),

    /**
     * 206 server reply code.
     */
    RPL_TRACESERVER("206", false),

    /**
     * 207 server reply code.
     */
    RPL_TRACESERVICE("207", false),

    /**
     * 208 server reply code.
     */
    RPL_TRACENEWTYPE("208", false),

    /**
     * 209 server reply code.
     */
    RPL_TRACECLASS("209", false),

    /**
     * 210 server reply code.
     */
    RPL_TRACERECONNECT("210", false),

    /**
     * 211 server reply code.
     */
    RPL_STATSLINKINFO("211", false),

    /**
     * 212 server reply code.
     */
    RPL_STATSCOMMANDS("212", false),

    /**
     * 213 server reply code.
     */
    RPL_STATSCLINE("213", false),

    /**
     * 214 server reply code.
     */
    RPL_STATSNLINE("214", false),

    /**
     * 215 server reply code.
     */
    RPL_STATSILINE("215", false),

    /**
     * 216 server reply code.
     */
    RPL_STATSKLINE("216", false),

    /**
     * 217 server reply code.
     */
    RPL_STATSQLINE("217", false),

    /**
     * 218 server reply code.
     */
    RPL_STATSYLINE("218", false),

    /**
     * 219 server reply code.
     */
    RPL_ENDOFSTATS("219", false),

    /**
     * 221 server reply code.
     */
    RPL_UMODEIS("221", false),

    /**
     * 231 server reply code.
     */
    RPL_SERVICEINFO("231", false),

    /**
     * 232 server reply code.
     */
    RPL_ENDOFSERVICES("232", false),

    /**
     * 233 server reply code.
     */
    RPL_SERVICE("233", false),

    /**
     * 234 server reply code.
     */
    RPL_SERVLIST("234", false),

    /**
     * 235 server reply code.
     */
    RPL_SERVLISTEND("235", false),

    /**
     * 240 server reply code.
     */
    RPL_STATSVLINE("240", false),

    /**
     * 241 server reply code.
     */
    RPL_STATSLLINE("241", false),

    /**
     * 242 server reply code.
     */
    RPL_STATSUPTIME("242", false),

    /**
     * 243 server reply code.
     */
    RPL_STATSOLINE("243", false),

    /**
     * 244 server reply code.
     */
    RPL_STATSHLINE("244", false),

    /**
     * 245 server reply code.
     */
    RPL_STATSSLINE("245", false),

    /**
     * 246 server reply code.
     */
    RPL_STATSPING("246", false),

    /**
     * 247 server reply code.
     */
    RPL_STATSBLINE("247", false),

    /**
     * 250 server reply code.
     */
    RPL_STATSDLINE("250", false),

    /**
     * 251 server reply code.
     */
    RPL_LUSERCLIENT("251", false),

    /**
     * 252 server reply code.
     */
    RPL_LUSEROP("252", false),

    /**
     * 253 server reply code.
     */
    RPL_LUSERUNKNOWN("253", false),

    /**
     * 254 server reply code.
     */
    RPL_LUSERCHANNELS("254", false),

    /**
     * 255 server reply code.
     */
    RPL_LUSERME("255", false),

    /**
     * 256 server reply code.
     */
    RPL_ADMINME("256", false),

    /**
     * 257 server reply code.
     */
    RPL_ADMINLOC1("257", false),

    /**
     * 258 server reply code.
     */
    RPL_ADMINLOC2("258", false),

    /**
     * 259 server reply code.
     */
    RPL_ADMINEMAIL("259", false),

    /**
     * 261 server reply code.
     */
    RPL_TRACELOG("261", false),

    /**
     * 262 server reply code.
     */
    RPL_TRACEEND("262", false),

    /**
     * 263 server reply code.
     */
    RPL_TRYAGAIN("263", false),

    /**
     * 265 server reply code.
     */
    RPL_LOCALUSERS("265", false),

    /**
     * 266 server reply code.
     */
    RPL_GLOBALUSERS("266", false),

    /**
     * 300 server reply code.
     */
    RPL_NONE("300", false),

    /**
     * 301 server reply code.
     */
    RPL_AWAY("301", false),

    /**
     * 302 server reply code.
     */
    RPL_USERHOST("302", false),

    /**
     * 303 server reply code.
     */
    RPL_ISON("303", false),

    /**
     * 305 server reply code.
     */
    RPL_UNAWAY("305", false),

    /**
     * 306 server reply code.
     */
    RPL_NOWAWAY("306", false),

    /**
     * 311 server reply code.
     */
    RPL_WHOISUSER("311", false),

    /**
     * 312 server reply code.
     */
    RPL_WHOISSERVER("312", false),

    /**
     * 313 server reply code.
     */
    RPL_WHOISOPERATOR("313", false),

    /**
     * 314 server reply code.
     */
    RPL_WHOWASUSER("314", false),

    /**
     * 315 server reply code.
     */
    RPL_ENDOFWHO("315", false),

    /**
     * 316 server reply code.
     */
    RPL_WHOISCHANOP("316", false),

    /**
     * 317 server reply code.
     */
    RPL_WHOISIDLE("317", false),

    /**
     * 318 server reply code.
     */
    RPL_ENDOFWHOIS("318", false),

    /**
     * 319 server reply code.
     */
    RPL_WHOISCHANNELS("319", false),

    /**
     * 321 server reply code.
     */
    RPL_LISTSTART("321", false),

    /**
     * 322 server reply code.
     */
    RPL_LIST("322", false),

    /**
     * 323 server reply code.
     */
    RPL_LISTEND("323", false),

    /**
     * 324 server reply code.
     */
    RPL_CHANNELMODEIS("324", false),

    /**
     * 325 server reply code.
     */
    RPL_UNIQOPIS("325", false),

    /**
     * 331 server reply code.
     */
    RPL_NOTOPIC("331", false),

    /**
     * 332 server reply code.
     */
    RPL_TOPIC("332", false),

    /**
     * 341 server reply code.
     */
    RPL_INVITING("341", false),

    /**
     * 342 server reply code.
     */
    RPL_SUMMONING("342", false),

    /**
     * 346 server reply code.
     */
    RPL_INVITELIST("346", false),

    /**
     * 347 server reply code.
     */
    RPL_ENDOFINVITELIST("347", false),

    /**
     * 348 server reply code.
     */
    RPL_EXCEPTLIST("348", false),

    /**
     * 349 server reply code.
     */
    RPL_ENDOFEXCEPTLIST("349", false),

    /**
     * 351 server reply code.
     */
    RPL_VERSION("351", false),

    /**
     * 352 server reply code.
     */
    RPL_WHOREPLY("352", false),

    /**
     * 353 server reply code.
     */
    RPL_NAMREPLY("353", false),

    /**
     * 361 server reply code.
     */
    RPL_KILLDONE("361", false),

    /**
     * 362 server reply code.
     */
    RPL_CLOSING("362", false),

    /**
     * 363 server reply code.
     */
    RPL_CLOSEEND("363", false),

    /**
     * 364 server reply code.
     */
    RPL_LINKS("364", false),

    /**
     * 365 server reply code.
     */
    RPL_ENDOFLINKS("365", false),

    /**
     * 366 server reply code.
     */
    RPL_ENDOFNAMES("366", false),

    /**
     * 367 server reply code.
     */
    RPL_BANLIST("367", false),

    /**
     * 368 server reply code.
     */
    RPL_ENDOFBANLIST("368", false),

    /**
     * 369 server reply code.
     */
    RPL_ENDOFWHOWAS("369", false),

    /**
     * 371 server reply code.
     */
    RPL_INFO("371", false),

    /**
     * 372 server reply code.
     */
    RPL_MOTD("372", false),

    /**
     * 373 server reply code.
     */
    RPL_INFOSTART("373", false),

    /**
     * 374 server reply code.
     */
    RPL_ENDOFINFO("374", false),

    /**
     * 375 server reply code.
     */
    RPL_MOTDSTART("375", false),

    /**
     * 376 server reply code.
     */
    RPL_ENDOFMOTD("376", false),

    /**
     * 381 server reply code.
     */
    RPL_YOUREOPER("381", false),

    /**
     * 382 server reply code.
     */
    RPL_REHASHING("382", false),

    /**
     * 383 server reply code.
     */
    RPL_YOURESERVICE("383", false),

    /**
     * 384 server reply code.
     */
    RPL_MYPORTIS("384", false),

    /**
     * 391 server reply code.
     */
    RPL_TIME("391", false),

    /**
     * 392 server reply code.
     */
    RPL_USERSSTART("392", false),

    /**
     * 393 server reply code.
     */
    RPL_USERS("393", false),

    /**
     * 394 server reply code.
     */
    RPL_ENDOFUSERS("394", false),

    /**
     * 395 server reply code.
     */
    RPL_NOUSERS("395", false),

    /**
     * 401 server error reply code.
     */
    ERR_NOSUCHNICK("401", true),

    /**
     * 402 server error reply code.
     */
    ERR_NOSUCHSERVER("402", true),

    /**
     * 403 server error reply code.
     */
    ERR_NOSUCHCHANNEL("403", true),

    /**
     * 404 server error reply code.
     */
    ERR_CANNOTSENDTOCHAN("404", true),

    /**
     * 405 server error reply code.
     */
    ERR_TOOMANYCHANNELS("405", true),

    /**
     * 406 server error reply code.
     */
    ERR_WASNOSUCHNICK("406", true),

    /**
     * 407 server error reply code.
     */
    ERR_TOOMANYTARGETS("407", true),

    /**
     * 408 server error reply code.
     */
    ERR_NOSUCHSERVICE("408", true),

    /**
     * 409 server error reply code.
     */
    ERR_NOORIGIN("409", true),

    /**
     * 411 server error reply code.
     */
    ERR_NORECIPIENT("411", true),

    /**
     * 412 server error reply code.
     */
    ERR_NOTEXTTOSEND("412", true),

    /**
     * 413 server error reply code.
     */
    ERR_NOTOPLEVEL("413", true),

    /**
     * 414 server error reply code.
     */
    ERR_WILDTOPLEVEL("414", true),

    /**
     * 415 server error reply code.
     */
    ERR_BADMASK("415", true),

    /**
     * 421 server error reply code.
     */
    ERR_UNKNOWNCOMMAND("421", true),

    /**
     * 422 server error reply code.
     */
    ERR_NOMOTD("422", true),

    /**
     * 423 server error reply code.
     */
    ERR_NOADMININFO("423", true),

    /**
     * 424 server error reply code.
     */
    ERR_FILEERROR("424", true),

    /**
     * 431 server error reply code.
     */
    ERR_NONICKNAMEGIVEN("431", true),

    /**
     * 432 server error reply code.
     */
    ERR_ERRONEUSNICKNAME("432", true),

    /**
     * 433 server error reply code.
     */
    ERR_NICKNAMEINUSE("433", true),

    /**
     * 436 server error reply code.
     */
    ERR_NICKCOLLISION("436", true),

    /**
     * 437 server error reply code.
     */
    ERR_UNAVAILRESOURCE("437", true),

    /**
     * 441 server error reply code.
     */
    ERR_USERNOTINCHANNEL("441", true),

    /**
     * 442 server error reply code.
     */
    ERR_NOTONCHANNEL("442", true),

    /**
     * 443 server error reply code.
     */
    ERR_USERONCHANNEL("443", true),

    /**
     * 444 server error reply code.
     */
    ERR_NOLOGIN("444", true),

    /**
     * 445 server error reply code.
     */
    ERR_SUMMONDISABLED("445", true),

    /**
     * 446 server error reply code.
     */
    ERR_USERSDISABLED("446", true),

    /**
     * 451 server error reply code.
     */
    ERR_NOTREGISTERED("451", true),

    /**
     * 461 server error reply code.
     */
    ERR_NEEDMOREPARAMS("461", true),

    /**
     * 462 server error reply code.
     */
    ERR_ALREADYREGISTRED("462", true),

    /**
     * 463 server error reply code.
     */
    ERR_NOPERMFORHOST("463", true),

    /**
     * 464 server error reply code.
     */
    ERR_PASSWDMISMATCH("464", true),

    /**
     * 465 server error reply code.
     */
    ERR_YOUREBANNEDCREEP("465", true),

    /**
     * 466 server error reply code.
     */
    ERR_YOUWILLBEBANNED("466", true),

    /**
     * 467 server error reply code.
     */
    ERR_KEYSET("467", true),

    /**
     * 471 server error reply code.
     */
    ERR_CHANNELISFULL("471", true),

    /**
     * 472 server error reply code.
     */
    ERR_UNKNOWNMODE("472", true),

    /**
     * 473 server error reply code.
     */
    ERR_INVITEONLYCHAN("473", true),

    /**
     * 474 server error reply code.
     */
    ERR_BANNEDFROMCHAN("474", true),

    /**
     * 475 server error reply code.
     */
    ERR_BADCHANNELKEY("475", true),

    /**
     * 476 server error reply code.
     */
    ERR_BADCHANMASK("476", true),

    /**
     * 477 server error reply code.
     */
    ERR_NOCHANMODES("477", true),

    /**
     * 478 server error reply code.
     */
    ERR_BANLISTFULL("478", true),

    /**
     * 481 server error reply code.
     */
    ERR_NOPRIVILEGES("481", true),

    /**
     * 482 server error reply code.
     */
    ERR_CHANOPRIVSNEEDED("482", true),

    /**
     * 483 server error reply code.
     */
    ERR_CANTKILLSERVER("483", true),

    /**
     * 484 server error reply code.
     */
    ERR_RESTRICTED("484", true),

    /**
     * 485 server error reply code.
     */
    ERR_UNIQOPPRIVSNEEDED("485", true),

    /**
     * 491 server error reply code.
     */
    ERR_NOOPERHOST("491", true),

    /**
     * 492 server error reply code.
     */
    ERR_NOSERVICEHOST("492", true),

    /**
     * 501 server error reply code.
     */
    ERR_UMODEUNKNOWNFLAG("501", true),

    /**
     * 502 server error reply code.
     */
    ERR_USERSDONTMATCH("502", true);

    /**
     * Server reply constant.
     */
    private final String id;

    /**
     * Error indicator.
     */
    private final boolean isError;

    /**
     * Constructor.
     * 
     * @param id
     *            server reply constant
     * @param isError
     *            error indicator
     */
    private ReplyCode(final String id, final boolean isError)
    {
        this.id = id;
        this.isError = isError;
    }

    /**
     * Returns server reply as string.
     * 
     * @return server reply as string
     */
    @Override
    public final String toString()
    {
        return id != null ? id : "";
    }

    /**
     * Whether this reply represents error server message.
     * 
     * @return true if error message, false otherwise
     */
    public final boolean isError()
    {
        return isError;
    }

    /**
     * Server replies cache.
     */
    private static final Map<String, ReplyCode> CACHE = new HashMap<String, ReplyCode>();

    static
    {
        for (final ReplyCode reply : values())
        {
            final String id = reply.id;
            if (id != null)
            {
                CACHE.put(id, reply);
            }
        }
    }

    /**
     * Factory method for obtaining instances of this class. Returns
     * <code>UNKNOWN</code> if the server reply type is not recognized.
     * 
     * @param type
     *            server reply type
     * @return server reply instance
     */
    public static ReplyCode of(final String type)
    {
        final ReplyCode element = CACHE.get(type);
        return element == null ? UNKNOWN : element;
    }

}

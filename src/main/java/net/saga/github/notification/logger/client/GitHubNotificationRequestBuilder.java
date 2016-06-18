/*
 * Copyright (C) 2016 summers.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package net.saga.github.notification.logger.client;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.validation.constraints.NotNull;


public class GitHubNotificationRequestBuilder {

    private boolean all = false;
    private boolean participating = false;
    private final GitHubToken token;
    private ZonedDateTime since = ZonedDateTime.now();
    private ZonedDateTime before = ZonedDateTime.now();
    private String eTag = "";
    private String lastModified = "";
    
    public GitHubNotificationRequestBuilder(GitHubToken token ) {
        this.token = token;
    }

    public GitHubNotificationRequestBuilder setAll(boolean all) {
        this.all = all;
        return this;
    }

    public GitHubNotificationRequestBuilder setParticipating(boolean participating) {
        this.participating = participating;
        return this;
    }

    public GitHubNotificationRequestBuilder setSince(@NotNull ZonedDateTime since) {
        if (since == null) {
            throw new IllegalArgumentException("since should not be null");
        }
        this.since = since;
        return this;
    }

    public GitHubNotificationRequestBuilder setBefore(@NotNull ZonedDateTime before) {
        if (before == null) {
            throw new IllegalArgumentException("before should not be null");
        }
        this.before = before;
        return this;
    }

    public void seteTag(@NotNull String eTag) {
        if (eTag == null) {
            throw new IllegalArgumentException("eTag should not be null");
        }
        this.eTag = eTag;
    }

    public void setLastModified(@NotNull String lastModified) {
        if (lastModified == null) {
            throw new IllegalArgumentException("lastModified should not be null");
        }
        this.lastModified = lastModified;
    }

    
    
    public GitHubRESTClient.GitHubNotificationRequest createGitHubNotificationRequest() {
        
        final String sinceString = since.format(DateTimeFormatter.ofPattern(GitHubRESTClient.DATE_TIME_FORMAT));
        final String beforeString = before.format(DateTimeFormatter.ofPattern(GitHubRESTClient.DATE_TIME_FORMAT));
                        
        return new GitHubRESTClient.GitHubNotificationRequest(all, participating, sinceString, beforeString, token.toString(), eTag, lastModified);
    }
    
}

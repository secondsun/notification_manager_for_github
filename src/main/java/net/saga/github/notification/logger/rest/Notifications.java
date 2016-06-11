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
package net.saga.github.notification.logger.rest;

import java.time.ZonedDateTime;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import net.saga.github.notification.logger.beans.NotificationsUser;
import net.saga.github.notification.logger.client.GitHubNotificationRequestBuilder;
import net.saga.github.notification.logger.client.GitHubRESTClient;
import net.saga.github.notification.logger.vo.GitHubResponse;

/**
 *
 * This service will be responsible for CRUD operations on notifications
 * downloaded from GitHub.
 *
 * @author summers
 */
@Path("/notifications")
public class Notifications {

    @Resource
    private ManagedExecutorService executor;

    @Inject
    private GitHubRESTClient githubClient;

    @Inject
    private NotificationsUser user;

    @GET
    @Produces(value = "application/json")
    /**
     * Currently, this loads notifications from GitHub, however it should load
     * notifications which have already been fetched from the database.
     */
    public GitHubResponse getNotifications() {
        try {
            return githubClient.getNotifications(
                    new GitHubNotificationRequestBuilder(user.getGitHubToken())
                    .setSince(ZonedDateTime.now().minusYears(1))
                    .setAll(true)
                    .setPartificpating(true)
                    .createGitHubNotificationRequest()
            ).get();
        } catch (Exception ignore) {
            return null;
        }

    }

}

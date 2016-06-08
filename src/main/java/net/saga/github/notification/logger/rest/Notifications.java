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
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import net.saga.github.notification.logger.client.GitHubNotificationRequestBuilder;
import net.saga.github.notification.logger.client.GitHubRESTClient;

/**
 *
 * @author summers
 */
@Path("/notifications")
public class Notifications {

    @Resource
    private ManagedExecutorService executor;

    @Inject
    private GitHubRESTClient githubClient;

    @GET
    public void getNotifications(@Suspended final AsyncResponse asyncResponse) {
        asyncResponse.setTimeout(5, TimeUnit.SECONDS);
        executor.submit(() -> {
            try {
                asyncResponse.resume(
                        githubClient.getNotifications(
                                new GitHubNotificationRequestBuilder()
                                        .setSince(ZonedDateTime.now().minusYears(1))
                                        .setAll(true)
                                        .setPartificpating(true)
                                    .createGitHubNotificationRequest()
                                ).get().toString()
                );
            } catch (Exception ex) {
                Logger.getLogger(Notifications.class.getName()).log(Level.SEVERE, null, ex);
                asyncResponse.resume(ex);
            }
        });
    }

}

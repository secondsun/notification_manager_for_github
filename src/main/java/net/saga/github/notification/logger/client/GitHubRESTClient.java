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

import java.net.URI;
import java.nio.charset.Charset;
import java.util.concurrent.Future;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import net.saga.github.notification.logger.vo.GitHubResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * This is a client for accessing GitHub APIs.
 *
 * @author summers
 */
@Stateless
public class GitHubRESTClient {

    private static final String ACCEPT_HEADER = "application/vnd.github.v3+json";
    private static final String API_URL_BASE = "https://api.github.com";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mmZ";
    private static final String AUTHZ_HEADER_TEMPLATE = " token %s";
    private static final String USER_AGENT = "Notification Fetcher by secondsun";

    private static final String TOKEN = "";
    
    @Resource
    private ManagedExecutorService executor;

    @Asynchronous
    public Future<GitHubResponse> getNotifications(GitHubNotificationRequest request) {

        final String notificiationPath = "/notifications";

        return executor.submit(() -> {
            GitHubResponse response = new GitHubResponse();
            URIBuilder uriBuilder = new URIBuilder(API_URL_BASE).setPath(notificiationPath);
            uriBuilder.addParameter("all", request.all + "");
            uriBuilder.addParameter("participating", request.participating + "");
            uriBuilder.addParameter("since", request.since);
            uriBuilder.addParameter("before", request.before);
            
            URI uri = uriBuilder.build();
            
            HttpGet get = new HttpGet(uri);
            get.addHeader(HttpHeaders.USER_AGENT, USER_AGENT);
            get.addHeader(HttpHeaders.AUTHORIZATION, String.format(AUTHZ_HEADER_TEMPLATE, TOKEN));
            get.addHeader(HttpHeaders.ACCEPT, ACCEPT_HEADER);
            
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                try (CloseableHttpResponse httpResponse = client.execute(get)) {
                    String stringResponse = IOUtils.toString(httpResponse.getEntity().getContent(), Charset.forName("UTF-8"));
                    response.setBody(stringResponse);
                    response.setHeaders(httpResponse.getAllHeaders());
                }
            }
            
            return response;
        });

    }

    /**
     * See https://developer.github.com/v3/activity/notifications/
     */
    public static class GitHubNotificationRequest {

        private final boolean all;
        private final boolean participating;
        private final String since;
        private final String before;
        private final String token;
        
        GitHubNotificationRequest(boolean all, boolean partificpating, String since, String before, String token) {
            this.all = all;
            this.participating = partificpating;
            this.since = since;
            this.before = before;
            this.token = token;
        }

    }

}

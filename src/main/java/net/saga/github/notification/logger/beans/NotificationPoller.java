package net.saga.github.notification.logger.beans;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import net.saga.github.notification.logger.beans.dao.AccountDao;
import net.saga.github.notification.logger.beans.dao.NotificationService;
import net.saga.github.notification.logger.client.GitHubNotificationRequestBuilder;
import net.saga.github.notification.logger.client.GitHubRESTClient;
import net.saga.github.notification.logger.vo.ApplicationAccount;
import net.saga.github.notification.logger.vo.GitHubResponse;
import net.saga.github.notification.logger.vo.notification.Notification;
import net.saga.github.notification.logger.vo.notification.NotificationMetaData;
import org.apache.http.HttpStatus;

/**
 *
 * This bean will periodically updates notifications for all of the users in the
 * system.
 *
 * @author summers
 */
@Stateless
public class NotificationPoller {

    @Inject
    private AccountDao accountDao;

    @Inject
    private NotificationService notificationDao;

    @Inject
    private GitHubRESTClient githubClient;

    
    @Schedule(minute = "*/1", hour = "*")
    public void update() {
        List<ApplicationAccount> accounts = accountDao.findAll();
        accounts.forEach((account) -> {
            NotificationMetaData metaData = notificationDao.getMetaDataFor(account);
            try {
                if (metaData.getRateLimitRemaining() > 10) {
                    GitHubRESTClient.GitHubNotificationRequest request = new GitHubNotificationRequestBuilder(account.getGitHubTokenValue())
                            .setSince(metaData.getLastTested())
                            .setParticipating(false)
                            .setAll(true)
                            .createGitHubNotificationRequest();

                    List<Notification> notifications = new ArrayList<>();

                    Future<GitHubResponse> result = githubClient.getNotifications(request);
                    GitHubResponse response = result.get(30, TimeUnit.SECONDS);

                    metaData.setRateLimit(response.getRateLimitLimit());
                    metaData.setRateLimitRemaining(response.getRateLimitRemaining());
                    metaData.setLastModified(response.getLastModified());
                    metaData.setETag(response.getETag());

                    if (response.getStatus() == HttpStatus.SC_OK) {
                        notifications.addAll(extractNotifications(response));
                    }

                    while (response.getNext().isPresent() && metaData.getRateLimitRemaining() > 10) {

                        result = githubClient.getNotifications(response.getNext().get(), account.getGitHubTokenValue());
                        response = result.get(30, TimeUnit.SECONDS);

                        if (response.getStatus() == HttpStatus.SC_OK) {
                            notifications.addAll(extractNotifications(response));
                        }
                    }

                    metaData.setLastTested(ZonedDateTime.now());
                    notificationDao.performUpdate(notifications, account.getUserName());
                }
                notificationDao.saveOrUpdate(metaData);

            } catch (InterruptedException | ExecutionException | TimeoutException | IOException ignore) {
                Logger.getAnonymousLogger().log(Level.SEVERE, ignore.getMessage(), ignore);
            }
        });
    }

    private Collection<? extends Notification> extractNotifications(GitHubResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Notification[] notifications = mapper.readValue(response.getBody(), Notification[].class);
        return Arrays.asList(notifications);
    }

}

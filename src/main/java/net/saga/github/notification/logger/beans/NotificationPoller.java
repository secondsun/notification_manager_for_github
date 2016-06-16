package net.saga.github.notification.logger.beans;

import net.saga.github.notification.logger.beans.dao.AccountDao;
import java.util.List;
import javax.ejb.Schedule;
import javax.inject.Inject;
import net.saga.github.notification.logger.client.GitHubRESTClient;
import net.saga.github.notification.logger.vo.ApplicationAccount;

/**
 *
 * This bean will periodically updates notifications for all of the users in the
 * system.
 * 
 * @author summers
 */
public class NotificationPoller {
   
    @Inject
    private AccountDao accountDao;
    
    @Inject
    private GitHubRESTClient githubClient;
    
    
    
    @Schedule(minute = "*/5", hour = "*")
    public void update() {
        List<ApplicationAccount> accounts =  accountDao.findAll();
        accounts.forEach((account)-> {
        
        });
    }
    
}

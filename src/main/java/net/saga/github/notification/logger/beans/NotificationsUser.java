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
package net.saga.github.notification.logger.beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.SecurityContext;
import net.saga.github.notification.logger.client.GitHubToken;
import static net.saga.github.notification.logger.util.AccountUtils.loadUserName;
import net.saga.github.notification.logger.vo.ApplicationAccount;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

/**
 *
 * @author summers
 */

@RequestScoped
public class NotificationsUser {

    private SecurityContext securityContext;

    private ApplicationAccount account;

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void loadToken() {
        securityContext = ResteasyProviderFactory.getContextData(SecurityContext.class);
        String kcUsername = loadUserName(securityContext);
        account = em.createQuery("from ApplicationAccount ac where ac.userName = :userName", 
                                 ApplicationAccount.class)
                .setParameter("userName", kcUsername)
                .getSingleResult();
    }

    public String getUserName() {
        return account.getUserName();
    }

    public GitHubToken getGitHubToken() {
        return account.getGitHubTokenValue();
    }
}

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

import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import net.saga.github.notification.logger.beans.AccountDao;
import static net.saga.github.notification.logger.util.AccountUtils.loadUserName;
import net.saga.github.notification.logger.vo.ApplicationAccount;

/**
 * This service manages Accounts.  This includes configuring an ApplicationAccount
 * with github information.
 * 
 * 
 * @author summers
 */
@Path("/accounts")
public class Accounts {
    
    @Inject
    private AccountDao accountDao;
    
    
    @Context
    private SecurityContext securityContext;

    
    /**
     * This will set a GitHubToken for the account of the logged in user.  This will
     * optionally create the user.
     * @param getHubToken the token to set
     * @return  the current account
     */
    @PUT
    @Produces(value = "application/json")
    public ApplicationAccount setToken(String getHubToken) {
        Optional<ApplicationAccount> accountOptional = accountDao.loadAccount(loadUserName(securityContext));
        ApplicationAccount account = accountOptional.orElse(new ApplicationAccount().setUserName(loadUserName(securityContext)));
        account.setGitHubToken(getHubToken);
        accountDao.saveOrUpdate(account);
        return account;
                
    }
    
    @GET
    @Produces(value = "application/json")
    public ApplicationAccount getAccount() {
        Optional<ApplicationAccount> accountOptional = accountDao.loadAccount(loadUserName(securityContext));
        ApplicationAccount account = accountOptional.orElse(new ApplicationAccount().setUserName(loadUserName(securityContext)));
        if (account.getId() == null) {
            accountDao.saveOrUpdate(account);
        }
        return account;
    }
    
    
}

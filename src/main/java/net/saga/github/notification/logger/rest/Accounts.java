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

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import net.saga.github.notification.logger.vo.ApplicationAccount;

/**
 * This service manages Accounts.  This includes configuring an ApplicationAccount
 * with github information.
 * 
 * It is the only Service that can be accessed anonymously.
 * 
 * @author summers
 */
@Path("/accounts")
public class Accounts {
    
    @PUT
    public void createAccount(ApplicationAccount accountInformation) {
        throw new IllegalStateException("Not implemented");
    }
    
}

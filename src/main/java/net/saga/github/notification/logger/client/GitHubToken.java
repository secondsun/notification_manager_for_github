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

import javax.validation.constraints.NotNull;

/**
 *
 * @author summers
 */
public class GitHubToken {
    
    private final String token;

    public GitHubToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return token + "";
    }
   
    
    
    public static final GitHubToken stringToGitHubToken(@NotNull String token) {
        if (token == null) {
            throw new IllegalArgumentException("token may not be null");
        }
        
        return new GitHubToken(token);
    }
    
}

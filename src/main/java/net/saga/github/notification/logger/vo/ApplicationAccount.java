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
package net.saga.github.notification.logger.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlTransient;
import net.saga.github.notification.logger.client.GitHubToken;

/**
 *
 * @author summers
 */
@Entity
@Access(AccessType.FIELD)
public class ApplicationAccount implements Serializable {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    public Long getId() {
        return id;
    }

    public ApplicationAccount setId(Long id) {
        this.id = id;
        return this;
    }
    
    private String userName;
    private String gitHubToken;
    
    public String getUserName() {
        return userName;
    }

    public ApplicationAccount setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getGitHubToken() {
        return gitHubToken;
    }

    public ApplicationAccount setGitHubToken(String gitHubToken) {
        this.gitHubToken = gitHubToken;
        return this;
    }
    
    @XmlTransient
    public GitHubToken getGitHubTokenValue() {
        return GitHubToken.stringToGitHubToken(gitHubToken);
    }
}

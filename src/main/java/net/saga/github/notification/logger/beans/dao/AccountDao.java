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
package net.saga.github.notification.logger.beans.dao;

import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import net.saga.github.notification.logger.vo.ApplicationAccount;

/**
 *
 * @author summers
 */
@Stateless
public class AccountDao {
    @PersistenceContext
    private EntityManager em;
    
    public Optional<ApplicationAccount> loadAccount(String userName) {
        try {
         return Optional.of(em.createQuery("from ApplicationAccount ac where ac.userName = :userName", 
                                 ApplicationAccount.class)
                .setParameter("userName", userName)
                .getSingleResult());
        } catch (NoResultException ignore) {
            return Optional.empty();
        }
    }

    @Transactional
    public void saveOrUpdate(ApplicationAccount account) {
        if (account.getId() != null) {
            em.merge(account);
        } else {
            em.persist(account);
        }
    }
    
    public List<ApplicationAccount> findAll() {
        return em.createQuery("from ApplicationAccount acct", ApplicationAccount.class).getResultList();
    }
    
}

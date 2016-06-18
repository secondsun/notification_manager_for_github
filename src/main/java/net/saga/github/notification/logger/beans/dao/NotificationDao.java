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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import net.saga.github.notification.logger.vo.ApplicationAccount;
import net.saga.github.notification.logger.vo.notification.Notification;
import net.saga.github.notification.logger.vo.notification.NotificationMetaData;

@Stateless
public class NotificationDao {

    @PersistenceContext
    private EntityManager em;

    public NotificationMetaData getMetaDataFor(ApplicationAccount account) {
        try {
        return em.createQuery("from NotificationMetaData meta where meta.applicationAccount.id = :accountId", 
                              NotificationMetaData.class)
                .setParameter("accountId", account.getId())
                .getSingleResult();
        } catch (NoResultException exception) {
            return new NotificationMetaData(account);
        }
    }

    
    @Transactional
    public void saveOrUpdate(NotificationMetaData meta) {
        if (meta.getId() != null) {
            em.merge(meta);
        } else {
            em.persist(meta);
        }
    }

    @Transactional
    public void saveAll(List<Notification> notifications, String userId) {
        notifications.forEach((notification)->{
            notification.setUserId(userId);
            em.persist(notification);
        });
    }

    public List<Notification> findForUser(String userName) {
        return em.createQuery("from Notification n where n.userId = :userId", Notification.class)
                .setParameter("userId", userName)
                .getResultList();
    }
    
    
}

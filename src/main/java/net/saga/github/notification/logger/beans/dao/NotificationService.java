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
import java.util.UUID;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import net.saga.github.notification.logger.realtime.signal.NewNotificationSignal;
import net.saga.github.notification.logger.realtime.signal.NewNotificationSignal.NewNotificationEvent;
import net.saga.github.notification.logger.realtime.signal.UpdatedNotificationSignal;
import net.saga.github.notification.logger.realtime.signal.UpdatedNotificationSignal.UpdatedNotificationEvent;
import net.saga.github.notification.logger.vo.ApplicationAccount;
import net.saga.github.notification.logger.vo.GitHubUser;
import net.saga.github.notification.logger.vo.Repository;
import net.saga.github.notification.logger.vo.notification.Notification;
import net.saga.github.notification.logger.vo.notification.NotificationMetaData;

@Stateless
public class NotificationService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    @NewNotificationEvent
    private Event<NewNotificationSignal> newNotification;

    @Inject
    @UpdatedNotificationEvent
    private Event<UpdatedNotificationSignal> updatedNotification;

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
    /**
     * This will emit the correct signals to the correct users as well as
     * coalesce sub objects.
     *
     * @param notifications notifications
     * @param userId userId
     */
    public synchronized void performUpdate(List<Notification> notifications, String userId) {
        notifications.forEach((notification) -> {

            List result = em.createQuery("from Notification n where n.id = :id and n.userId = :userId").setParameter("id", notification.getId()).setParameter("userId", userId).getResultList();
            notification.setUserId(userId);

            if (result.isEmpty()) {
                emitNew(notification);
                Repository repo = notification.getRepository();
                GitHubUser user = repo.getOwner();
                repo.setOwner(findOrCreate(user));
                notification.setRepository(findOrCreate(repo));

                em.persist(notification);
                em.flush();

            } else if (result.size() > 1) {
                throw new RuntimeException(String.format("Found multiple notifications for id %s and userId %s", notification.getId(), userId));
            } else {
                Notification persistedNotification = (Notification) result.get(0);
                persistedNotification.setLast_read_at(notification.getLast_read_at());
                persistedNotification.setReason(notification.getReason());
                persistedNotification.setUnread(notification.isUnread());
                persistedNotification.setSubject(notification.getSubject());
                persistedNotification.setUpdated_at(notification.getUpdated_at());
                persistedNotification.setUrl(notification.getUrl());
                em.merge(persistedNotification);
                em.flush();

                emitUpdate(persistedNotification);
            }

        });
    }

    public List<Notification> findForUser(String userName) {
        return em.createQuery("from Notification n where n.userId = :userId", Notification.class)
                .setParameter("userId", userName)
                .getResultList();
    }

    private GitHubUser findOrCreate(GitHubUser object) {
        GitHubUser persistedUser = em.find(GitHubUser.class, object.getId());
        if (persistedUser == null) {
            em.persist(object);
            em.flush();
            persistedUser = em.find(GitHubUser.class, object.getId());
        }
        return persistedUser;
    }

    private Repository findOrCreate(Repository object) {
        Repository persistedRepository = em.find(Repository.class, object.getId());
        if (persistedRepository == null) {
            em.persist(object);
            em.flush();
            persistedRepository = em.find(Repository.class, object.getId());
        }
        return persistedRepository;
    }

    private void emitNew(Notification notification) {
        newNotification.fire(new NewNotificationSignal(UUID.randomUUID().toString(), notification));
    }

    private void emitUpdate(Notification persistedNotification) {
        updatedNotification.fire(new UpdatedNotificationSignal(UUID.randomUUID().toString(), persistedNotification));
    }

}

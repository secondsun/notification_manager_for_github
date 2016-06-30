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
package net.saga.github.notification.logger.realtime.signal;

import java.lang.annotation.ElementType;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Qualifier;
import net.saga.github.notification.logger.vo.notification.Notification;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * This signal passes to the client a new notification.
 *
 *
 * @author summers
 */
public class NewNotificationSignal extends BaseSignal {

    public static final String NEW_NOTIFICATION_SIGNAL_TYPE = NewNotificationSignal.class.getSimpleName().toLowerCase();
    private final Notification notification;

    public NewNotificationSignal(String id, Notification notification) {
        super(id, NEW_NOTIFICATION_SIGNAL_TYPE);
        try {
            if (notification.getJpaId() != null) {
                throw new RuntimeException("The notification is already persisted and should have been sent.");
            }
            this.notification = (Notification) BeanUtils.cloneBean(notification);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            Logger.getLogger(NewNotificationSignal.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public Notification getNotification() {
        return notification;
    }

    @Qualifier
    @Retention(RUNTIME)
    @Target({METHOD, ElementType.FIELD, PARAMETER, ElementType.TYPE})
    public @interface NewNotificationEvent {
    }

}

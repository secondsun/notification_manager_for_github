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
import javax.inject.Qualifier;
import net.saga.github.notification.logger.vo.notification.Notification;
import static net.saga.github.notification.logger.vo.notification.Notification.shallowClone;

/**
 *
 * This signal passes to the client an updated notification.
 * 
 * 
 * @author summers
 */
public class UpdatedNotificationSignal extends BaseSignal {
   
    public static final String NOTIFICATION_UPDATED_SIGNAL_TYPE = UpdatedNotificationSignal.class.getSimpleName().toLowerCase();
    private final Notification notification;

    public UpdatedNotificationSignal(String id, Notification notification) {
        super(id, NOTIFICATION_UPDATED_SIGNAL_TYPE);
        this.notification = shallowClone(notification);
    }

    public Notification getNotification() {
        return notification;
    }
    
    @Qualifier
    @Retention(RUNTIME)
    @Target({METHOD, ElementType.FIELD, PARAMETER, ElementType.TYPE})
    public @interface UpdatedNotificationEvent {
    }
    
}

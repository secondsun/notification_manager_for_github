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

/**
 * This is a signal to the client that it needs to reload all of its data 
 * using data from the server.  This will be emitted when a new, authenticated 
 * client is detected.
 *
 * @author summers
 */
public class RefreshDataSignal extends BaseSignal {
    
    public static final String REFRESH_DATA_SIGNAL_TYPE = RefreshDataSignal.class.getSimpleName().toLowerCase();
    
    public RefreshDataSignal(String id) {
        super(id, REFRESH_DATA_SIGNAL_TYPE);
    }
    
}

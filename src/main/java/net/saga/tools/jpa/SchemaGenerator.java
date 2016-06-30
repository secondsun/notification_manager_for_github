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
package net.saga.tools.jpa;

import java.util.Properties;
import javax.persistence.Persistence;

/**
 * @author john.thompson
 *
 */
public class SchemaGenerator {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.DerbyTenSevenDialect");
        properties.setProperty("hibernate.connection.driver_class", "org.apache.derby.jdbc.EmbeddedDriver");
        properties.setProperty("hibernate.connection.url", "jdbc:derby:notifications;create=true");

        properties.setProperty("hibernate.connection.pool_size", "10");
        properties.setProperty("hibernate.show_sql", "true");

        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        Persistence.generateSchema("github_notification_service", properties);

    }

}

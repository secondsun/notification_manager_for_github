FROM jboss/wildfly:10.0.0.Final

ENV KEYCLOAK_VERSION 1.9.7.Final

## From Aerogear/wildfly
# Run everything below as root user
USER root

# Clean the metadata
RUN yum install -y unzip wget && yum -q clean all

## POSTGRES
ENV postgres_module_dir=/opt/jboss/wildfly/modules/system/layers/base/postgresql/main/
# add postgresql-jdbc jar and module.xml files
RUN mkdir -p ${postgres_module_dir}
ADD module.xml ${postgres_module_dir}
WORKDIR ${postgres_module_dir}
ADD postgresql-9.4-1201.jdbc41.jar ${postgres_module_dir}


# Rename the original configuration file
RUN mv $JBOSS_HOME/standalone/configuration/standalone-full.xml $JBOSS_HOME/standalone/configuration/standalone.xml

RUN sed -i -e 's/<security-realms>/& <security-realm name="AeroGearRealm">                <server-identities>                    <ssl>                        <keystore path="certs\/server.aerogear.dev.keystore" relative-to="jboss.server.config.dir" keystore-password="weak42" alias="server.aerogear.dev"\/>                    <\/ssl>                <\/server-identities>  <\/security-realm>/' $JBOSS_HOME/standalone/configuration/standalone.xml

# Add the certificate.sh script into $JBOSS_HOME/standalone/configuration/certs
ADD configuration/certs/ $JBOSS_HOME/standalone/configuration/certs

# Switch to $JBOSS_HOME/configuration/certs
WORKDIR /opt/jboss/wildfly/standalone/configuration/certs

# Execute the script to generate self signed certificates
RUN ./certificate.sh

# Switch to the working dir /opt/jboss/wildfly
WORKDIR /opt/jboss/wildfly

# Expose SSL default port
EXPOSE 8443
EXPOSE 8080
EXPOSE 9990

# Add a management user named 'admin' 
RUN /opt/jboss/wildfly/bin/add-user.sh -u admin -p admin --silent
# expose debug port
EXPOSE 8787

## From keycloak/adapter-wildfly
WORKDIR /opt/jboss/wildfly

RUN curl -L https://downloads.jboss.org/keycloak/$KEYCLOAK_VERSION/adapters/keycloak-oidc/keycloak-wildfly-adapter-dist-$KEYCLOAK_VERSION.tar.gz | tar zx

WORKDIR /opt/jboss

# Standalone.xml modifications.
RUN sed -i -e 's/<extensions>/&\n        <extension module="org.keycloak.keycloak-adapter-subsystem"\/>/' $JBOSS_HOME/standalone/configuration/standalone.xml && \
    sed -i -e 's/<profile>/&\n        <subsystem xmlns="urn:jboss:domain:keycloak:1.1"\/>/' $JBOSS_HOME/standalone/configuration/standalone.xml && \
    sed -i -e 's/<security-domains>/&\n                <security-domain name="keycloak">\n                    <authentication>\n                        <login-module code="org.keycloak.adapters.jboss.KeycloakLoginModule" flag="required"\/>\n                    <\/authentication>\n                <\/security-domain>/' $JBOSS_HOME/standalone/configuration/standalone.xml

RUN sed -i -e 's/<drivers>/&\n <driver name="postgresql" module="postgresql">                    	<driver-class>org.postgresql.Driver<\/driver-class>                    	<xa-datasource-class>org.postgresql.xa.PGXADataSource<\/xa-datasource-class>	                <\/driver>/' $JBOSS_HOME/standalone/configuration/standalone.xml

RUN sed -i -e 's/<datasources>/&\n<datasource jndi-name="java:jboss\/datasources\/GithubNotificationsDS" enabled="true" use-java-context="true" pool-name="GithubNotificationsDSDS" use-ccm="true">                     <connection-url>jdbc:mysql:\/\/${env.DOCKER_NOTIFICATIONDB_1_PORT_5432_TCP_ADDR}:${env.DOCKER_NOTIFICATIONDB_1_PORT_5432_TCP_PORT}\/${env.DOCKER_NOTIFICATIONDB_1_ENV_POSTGRES_DB}<\/connection-url>                     <driver>postgresql<\/driver>                     <security>                       <user-name>${env.DOCKER_NOTIFICATIONDB_1_ENV_POSTGRES_USER}<\/user-name>                       <password>${env.DOCKER_NOTIFICATIONDB_1_ENV_POSTGRES_PASSWORD}<\/password>                     <\/security>                     <pool>                         <flush-strategy>IdleConnections<\/flush-strategy>                     <\/pool>                 <\/datasource>/' $JBOSS_HOME/standalone/configuration/standalone.xml


RUN sed -i -e 's/<server name="default-server">/&<https-listener name="https" socket-binding="https" security-realm="AeroGearRealm"\/>/' $JBOSS_HOME/standalone/configuration/standalone.xml

EXPOSE 80
EXPOSE 443

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "--debug"]


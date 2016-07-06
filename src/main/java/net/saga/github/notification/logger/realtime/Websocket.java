/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.saga.github.notification.logger.realtime;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import net.saga.github.notification.logger.realtime.signal.NewNotificationSignal;
import net.saga.github.notification.logger.realtime.signal.NewNotificationSignal.NewNotificationEvent;
import net.saga.github.notification.logger.realtime.signal.UpdatedNotificationSignal;
import net.saga.github.notification.logger.realtime.signal.UpdatedNotificationSignal.UpdatedNotificationEvent;
import net.saga.github.notification.logger.vo.notification.Notification;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

@Stateless
@ServerEndpoint(value = "/api/wss/realtime", configurator = SecurityConfigurator.class, encoders = {NotificationCoder.class}, decoders = {NotificationCoder.class})
public class Websocket {

    private static final Map<Session, String> sessionMaps = new HashMap<>();

    @OnMessage
    public String onMessage(String message, Session webSocketSession) {
        return message;
    }

    @OnOpen
    public void myOnOpen(Session session, EndpointConfig config) {
        KeycloakPrincipal<KeycloakSecurityContext> userPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) config.getUserProperties().get("userPrincipal");
        System.out.println("WebSocket opened: " + session.getId());
        String userId = userPrincipal.getKeycloakSecurityContext().getToken().getPreferredUsername();
        sessionMaps.put(session, userId);

    }

    @OnClose
    public void myOnClose(CloseReason reason, Session webSocketSession) {
        System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());
        sessionMaps.remove(webSocketSession);
    }

    public void handleNewNotification(@Observes @NewNotificationEvent NewNotificationSignal signal) {
        Notification note = signal.getNotification();
        sessionMaps.entrySet().forEach((entry) -> {
            if (note.getUserId().equals(entry.getValue())) {
                try {
                    entry.getKey().getBasicRemote().sendText("New Notification");
                } catch (IOException ex) {
                    Logger.getLogger(Websocket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    public void handleUpdatedNotification(@Observes @UpdatedNotificationEvent UpdatedNotificationSignal signal) {
        Notification note = signal.getNotification();
        sessionMaps.entrySet().forEach((entry) -> {
            if (note.getUserId().equals(entry.getValue())) {
                try {
                    entry.getKey().getBasicRemote().sendText("New Notification");
                } catch (IOException ex) {
                    Logger.getLogger(Websocket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }
}

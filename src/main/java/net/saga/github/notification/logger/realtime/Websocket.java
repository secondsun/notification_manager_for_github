/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.saga.github.notification.logger.realtime;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.event.Observes;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
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
import net.saga.github.notification.logger.util.AccountUtils;
import net.saga.github.notification.logger.vo.notification.Notification;

@ServerEndpoint("/api/s/realtime")
public class Websocket {

    @Context
    private SecurityContext securityContext;

    private String userId;
    private Session sesison;

    @OnMessage
    public String onMessage(String message, Session webSocketSession) {
        return message;
    }

    @OnOpen
    public void myOnOpen(Session session) {
        System.out.println("WebSocket opened: " + session.getId());
        this.sesison = session;
        this.userId = AccountUtils.loadUserName(securityContext);
    }

    @OnClose
    public void myOnClose(CloseReason reason, Session webSocketSession) {
        System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());
        this.sesison = null;
    }

    public void handleNewNotification(@Observes @NewNotificationEvent NewNotificationSignal signal) {
        Notification note = signal.getNotification();
        if (note.getUserId().equals(userId)) {
            try {
                sesison.getBasicRemote().sendObject(note);
            } catch (IOException | EncodeException ex) {
                Logger.getLogger(Websocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void handleUpdatedNotification(@Observes @UpdatedNotificationEvent UpdatedNotificationSignal signal) {
        Notification note = signal.getNotification();
        if (note.getUserId().equals(userId)) {
            try {
                sesison.getBasicRemote().sendObject(note);
            } catch (IOException | EncodeException ex) {
                Logger.getLogger(Websocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}

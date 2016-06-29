/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.saga.github.notification.logger.realtime;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/api/s/realtime")
public class Websocket {

    @OnMessage
    public String onMessage(String message, Session webSocketSession) {
        return message;
    }

    @OnOpen
    public void myOnOpen(Session session) {
        System.out.println("WebSocket opened: " + session.getId());
    }

    @OnClose
    public void myOnClose(CloseReason reason, Session webSocketSession) {
        System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());

    }
}

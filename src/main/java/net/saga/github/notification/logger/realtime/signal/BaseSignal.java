package net.saga.github.notification.logger.realtime.signal;

import java.io.Serializable;


public abstract class BaseSignal implements Serializable {
    
    private final String id;
    private final String type;
    
    public BaseSignal(String id, String type) {
        this.id = id;
        this.type = type;
    }

    /**
     * The id is a way for servers and clients to coordinate request/repsonses 
     * over websocket.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * The type is a key which identifies the action.
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    
    
}

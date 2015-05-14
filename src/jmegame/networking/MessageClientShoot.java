/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Tells the server that the client has shot.
 *
 * @author campbell
 */
@Serializable
public class MessageClientShoot extends AbstractMessage {

    private boolean value;

    public MessageClientShoot() {
        super(true);
    }

    public MessageClientShoot(boolean value) {
        this();
        this.value = value;
    }

    public boolean isPressed() {
        return value;
    }
}

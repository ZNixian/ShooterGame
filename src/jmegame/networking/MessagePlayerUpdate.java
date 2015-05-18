/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Represents a client-to-server player position update.
 * This is sent over UDP.
 *
 * @author campbell
 */
@Serializable
public class MessagePlayerUpdate extends AbstractMessage {

    private Vector3f position;
    private Quaternion rotation;
    private int weaponID;

    public MessagePlayerUpdate() {
        super(false);
    }

    public MessagePlayerUpdate(Vector3f position, Quaternion rotation, int weaponID) {
        this();
        this.position = position;
        this.rotation = rotation;
        this.weaponID = weaponID;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public int getWeaponID() {
        return weaponID;
    }
}

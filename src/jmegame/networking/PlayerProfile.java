/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import java.util.UUID;

/**
 *
 * @author campbell
 */
@Serializable
public class PlayerProfile {
    private int weaponID;
    private Vector3f position;
    private Quaternion rotation;
    private long uuid_lsb;
    private long uuid_msb;

    public PlayerProfile() {
    }

    public PlayerProfile(Vector3f position, Quaternion rotation, int weaponID, UUID uuid) {
        this.position = position;
        this.rotation = rotation;
        this.weaponID = weaponID;
        this.uuid_lsb = uuid.getLeastSignificantBits();
        this.uuid_msb = uuid.getMostSignificantBits();
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public UUID getUuid() {
        return new UUID(uuid_lsb, uuid_msb);
    }

    public int getWeaponID() {
        return weaponID;
    }
}

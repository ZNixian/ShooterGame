/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import jmegame.common.SUUID;

/**
 *
 * @author campbell
 */
@Serializable
public class PlayerProfile {
    private int weaponID;
    private Vector3f position;
    private Quaternion rotation;
    private SUUID uuid;

    public PlayerProfile() {
    }

    public PlayerProfile(Vector3f position, Quaternion rotation, int weaponID,
            SUUID uuid) {
        this.position = position;
        this.rotation = rotation;
        this.weaponID = weaponID;
        this.uuid = uuid;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public SUUID getUUID() {
        return uuid;
    }

    public int getWeaponID() {
        return weaponID;
    }
}

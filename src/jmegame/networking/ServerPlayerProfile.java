/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.UUID;

/**
 * Stores detailed info on a player
 *
 * @author campbell
 */
public class ServerPlayerProfile {

    private Vector3f position;
    private Quaternion rotation;
    private final UUID uuid;
    private int health;

    private boolean unsentTCP;
    
    private Node root;

    public ServerPlayerProfile(UUID uuid) {
        this.uuid = uuid;
        health = 100;
        unsentTCP = true;
    }

    public PlayerProfile makeSendableVarsion() {
        return new PlayerProfile(position, rotation, uuid);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health < 0) {
            health = 0;
        } else if (health > 100) {
            health = 100;
        }
        
        this.health = health;
        unsentTCP = true;
    }

    public boolean isUnsentTCP() {
        return unsentTCP;
    }

    public void setUnsentTCP(boolean unsentTCP) {
        this.unsentTCP = unsentTCP;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}

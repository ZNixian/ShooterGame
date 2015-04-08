/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Stores the health/armor/weapons/ammo for a player. To be sent over TCP, as it
 * is very important.
 *
 * @author campbell
 */
@Serializable
public class MessageServerUpdateStats extends AbstractMessage {

    private int health;

    public MessageServerUpdateStats() {
        super(true);
    }

    public MessageServerUpdateStats(ServerPlayerProfile profile) {
        this();
        this.health = profile.getHealth();
    }

    public int getHealth() {
        return health;
    }
}

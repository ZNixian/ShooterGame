/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Stores the position and rotation of a player. Server-to-client only.
 *
 * @author campbell
 */
@Serializable
public class MessagePlayerServerUpdatePosition extends AbstractMessage {

    private PlayerProfile profile;

    public MessagePlayerServerUpdatePosition() {
        super(false);
    }

    public MessagePlayerServerUpdatePosition(PlayerProfile profile) {
        this();
        this.profile = profile;
    }

    public MessagePlayerServerUpdatePosition(ServerPlayerProfile profile) {
        this(profile.makeSendableVarsion());
    }

    public PlayerProfile getProfile() {
        return profile;
    }
}

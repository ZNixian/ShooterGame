/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.UUID;

/**
 *
 * @author campbell
 */
@Serializable
public class MessagePlayerDisconnect extends AbstractMessage {

    private PlayerProfile profile;

    public MessagePlayerDisconnect() {
        super(true);
    }

    public MessagePlayerDisconnect(PlayerProfile profile) {
        this();
        this.profile = profile;
    }

    public MessagePlayerDisconnect(ServerPlayerProfile profile) {
        this(profile.makeSendableVarsion());
    }

    public PlayerProfile getProfile() {
        return profile;
    }
    
    public UUID getUuid() {
        return profile.getUuid();
    }
}

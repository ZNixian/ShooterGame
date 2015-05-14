/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import jmegame.common.SUUID;

/**
 *
 * @author campbell
 */
@Serializable
public class MessageS2CParticle extends AbstractMessage {

    private SUUID uuid;

    public MessageS2CParticle() {
        super(true);
    }

    public MessageS2CParticle(SUUID uuid) {
        this();
        this.uuid = uuid;
    }

    public SUUID getUuid() {
        return uuid;
    }
}

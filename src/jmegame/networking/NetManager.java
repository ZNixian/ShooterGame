/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking;

import com.jme3.network.serializing.Serializer;
import jmegame.common.SUUID;
import jmegame.networking.messages.MessageC2SGunSwitch;
import jmegame.networking.messages.MessageS2CParticle;

/**
 *
 * @author campbell
 */
public class NetManager {

    private NetManager() {
    }

    public static void setup() {
        Serializer.registerClass(MessagePlayerUpdate.class);
        Serializer.registerClass(MessagePlayerServerUpdatePosition.class);
        Serializer.registerClass(MessageServerUpdateStats.class);
        Serializer.registerClass(MessageClientShoot.class);
        Serializer.registerClass(MessagePlayerDisconnect.class);
        Serializer.registerClass(MessageS2CParticle.class);
        Serializer.registerClass(MessageC2SGunSwitch.class);
        
        Serializer.registerClass(PlayerProfile.class);
        Serializer.registerClass(SUUID.class);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking;

import com.jme3.network.serializing.Serializer;

/**
 *
 * @author campbell
 */
public class NetManager {

    private NetManager() {
    }

    public static void setup() {
        Serializer.registerClass(MessagePlayerUpdate.class);
        Serializer.registerClass(MessagePlayerServerUpdate.class);
        
        Serializer.registerClass(PlayerProfile.class);
    }
}

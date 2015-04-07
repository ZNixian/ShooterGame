/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.server;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import java.util.Map;
import java.util.UUID;
import jmegame.networking.MessagePlayerUpdate;
import jmegame.networking.PlayerProfile;

/**
 *
 * @author campbell
 */
public class PacketListener implements MessageListener<HostedConnection> {

    private final Map<HostedConnection, PlayerProfile> profiles;

    public PacketListener(Map<HostedConnection, PlayerProfile> profiles) {
        this.profiles = profiles;
    }

    /**
     * Handle messages sent from the client.
     *
     * @param source
     * @param message
     */
    @Override
    public void messageReceived(HostedConnection source, Message message) {
        if (message instanceof MessagePlayerUpdate) {
            // do something with the message
            MessagePlayerUpdate mpu = (MessagePlayerUpdate) message;
//            System.out.println("Received '" + mpu.getPosition()
//                    + "' with rotation '" + mpu.getRotation()
//                    + "' from client #" + source.getId());
            UUID uuid;
            PlayerProfile prev = profiles.get(source);
            if (prev == null) {
                uuid = UUID.randomUUID();
            } else {
                uuid = prev.getUuid();
            }
            PlayerProfile newP = new PlayerProfile(mpu.getPosition(),
                    mpu.getRotation(), uuid);
            profiles.put(source, newP);
        }
    }
}

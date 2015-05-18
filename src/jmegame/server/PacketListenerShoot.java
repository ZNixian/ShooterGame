/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.server;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import jmegame.networking.IBulletSource;
import jmegame.networking.MessageClientShoot;
import jmegame.networking.ServerPlayerProfile;
import jmegame.weapons.Weapon;

/**
 *
 * @author campbell
 */
public class PacketListenerShoot implements MessageListener<HostedConnection> {

    private final PacketListener parent;

    public PacketListenerShoot(PacketListener parent) {
        this.parent = parent;
    }

    @Override
    public void messageReceived(HostedConnection source, Message message) {
        if (message instanceof MessageClientShoot) {
            // do something with the message
            MessageClientShoot mpu = (MessageClientShoot) message;
//            System.out.println("Received '" + mpu.getPosition()
//                    + "' with rotation '" + mpu.getRotation()
//                    + "' from client #" + source.getId());
            ServerPlayerProfile prof = parent.getProfiles().get(source);
            if (prof == null) {
                return; // shouldn't happen!
            }

            Weapon weapon = prof.getWeapon();

            prof.setTriggerPressed(mpu.isPressed());

            IBulletSource ibs = new BasicBulletSource(parent, prof);
            weapon.onTriggerStateChange(mpu.isPressed(), ibs);
            weapon.whileTriggerPressed(0, ibs);
        }
    }

}

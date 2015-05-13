/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.server;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.scene.Spatial;
import java.util.Map;
import java.util.UUID;
import jmegame.networking.MessageClientShoot;
import jmegame.networking.MessagePlayerUpdate;
import jmegame.networking.ServerPlayerProfile;

/**
 *
 * @author campbell
 */
public class PacketListener implements MessageListener<HostedConnection> {

    private final Map<HostedConnection, ServerPlayerProfile> profiles;
    private final GameServer server;

    public PacketListener(Map<HostedConnection, ServerPlayerProfile> profiles,
            GameServer server) {
        this.profiles = profiles;
        this.server = server;
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
            ServerPlayerProfile prof = profiles.get(source);
            if (prof == null) {
                prof = new ServerPlayerProfile(UUID.randomUUID(),
                        server.getAssetManager());

                server.getPlayersNode().attachChild(
                        prof.getController().getRoot());

                prof.setHealth(100);
            }

            prof.update(mpu);

            profiles.put(source, prof);
        } else if (message instanceof MessageClientShoot) {
            // do something with the message
            MessageClientShoot mpu = (MessageClientShoot) message;
//            System.out.println("Received '" + mpu.getPosition()
//                    + "' with rotation '" + mpu.getRotation()
//                    + "' from client #" + source.getId());
            ServerPlayerProfile prof = profiles.get(source);
            if (prof == null) {
                return; // shouldn't happen!
            }

            // 1. Reset results list.
            CollisionResults results = new CollisionResults();
            // 2. Aim the ray from cam loc to cam direction.
            Ray ray = new Ray(prof.getPosition(), prof.getRotation()
                    .getRotationColumn(2));
            // 3. Collect intersections between Ray and Shootables in results list.
            server.getPlayersNode().collideWith(ray, results);

            if (results.size() > 0) {
                ServerPlayerProfile hitplayer = null;
                int i = 0;
                do {
                    if (i >= results.size()) {
                        hitplayer = null;
                        continue; // could be break; , but meh.
                    }
                    // The closest collision point is what was truly hit:
                    CollisionResult closest = results.getCollision(i);
                    Spatial hit = closest.getGeometry();

                    hitplayer = findProfileForPlayer(hit);
                    i++;
                } while (hitplayer == prof);
                if (hitplayer == null) {
//                    System.out.println("No player hit!?");
                    return;
                }
//                System.out.println("prof: " + prof.getUuid()
//                        + ", hitPlayer: " + hitplayer.getUuid());
                hitplayer.setHealth(hitplayer.getHealth() - 10);
//                System.out.println("hit player " + hitplayer);
            }
        }
    }

    private ServerPlayerProfile findProfileForPlayer(Spatial hit) {
        while (hit.getParent() != server.getPlayersNode()) {
            hit = hit.getParent();
        }
        for (HostedConnection conn : server.getProfiles().keySet()) {
            ServerPlayerProfile possiblePlayer = server.getProfiles().get(conn);
            if (possiblePlayer.getController().getRoot() == hit) {
                return possiblePlayer;
            }
        }
        return null;
    }
}

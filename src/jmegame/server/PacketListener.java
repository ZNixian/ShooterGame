/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.server;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Map;
import java.util.UUID;
import jmegame.LevelManager;
import static jmegame.PlayerPhysicsData.PLAYER_PHYSICS_OFFSET;
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
            Node root;
            if (prof == null) {
                prof = new ServerPlayerProfile(UUID.randomUUID());

                root = new Node();
                root.attachChild(LevelManager.
                        getPlayerModel(server.getAssetManager()));

                prof.setRoot(root);

                server.getPlayersNode().attachChild(root);
            } else {
                root = prof.getRoot();
            }

            prof.setPosition(mpu.getPosition());
            prof.setRotation(mpu.getRotation());

            root.setLocalTranslation(mpu.getPosition().
                    add(0, PLAYER_PHYSICS_OFFSET, 0));

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
            // 4. Print the results
//            System.out.println("----- Collisions? " + results.size() + "-----");
//            for (int i = 0; i < results.size(); i++) {
//                // For each hit, we know distance, impact point, name of geometry.
//                float dist = results.getCollision(i).getDistance();
//                Vector3f pt = results.getCollision(i).getContactPoint();
//                String hit = results.getCollision(i).getGeometry().getName();
//                System.out.println("* Collision #" + i);
//                System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
//            }
            // 5. Use the results (we mark the hit object)
            if (results.size() > 0) {
                ServerPlayerProfile hitplayer = null;
                int i = 0;
                while (hitplayer == prof && i < results.size()) {
                    // The closest collision point is what was truly hit:
                    CollisionResult closest = results.getCollision(i);
                    Spatial hit = closest.getGeometry();

                    hitplayer = findProfileForPlayer(hit);
                    i++;
                }
                if (hitplayer == null) {
//                    System.out.println("No player hit!?");
                    return;
                }
                hitplayer.setHealth(10);
            }
        }
    }

    private ServerPlayerProfile findProfileForPlayer(Spatial hit) {
        while (hit.getParent() != server.getPlayersNode()) {
            hit = hit.getParent();
        }
        for (HostedConnection conn : server.getProfiles().keySet()) {
            ServerPlayerProfile possiblePlayer = server.getProfiles().get(conn);
            if (possiblePlayer.getRoot() == hit) {
                return possiblePlayer;
            }
        }
        return null;
    }
}

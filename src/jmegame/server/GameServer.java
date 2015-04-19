/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.server;

import jmegame.networking.MessagePlayerServerUpdatePosition;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.scene.Node;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmegame.LevelManager;
import jmegame.networking.MessageClientShoot;
import jmegame.networking.MessagePlayerDisconnect;
import jmegame.networking.MessagePlayerUpdate;
import jmegame.networking.MessageServerUpdateStats;
import jmegame.networking.NetConstants;
import jmegame.networking.NetManager;
import jmegame.networking.ServerPlayerProfile;

/**
 *
 * @author campbell
 */
public class GameServer extends SimpleApplication
        implements ConnectionListener {

    public static void main(String[] args) {
        GameServer app = new GameServer();
        app.start(JmeContext.Type.Headless);
    }

    private BulletAppState bulletAppState;
    private LevelManager manager;
    private Server networkServer;
    private float updateCounter;

    private Node players;

    private final Map<HostedConnection, ServerPlayerProfile> profiles = new HashMap<>();
    private final List<ServerPlayerProfile> disconnected = new ArrayList<>();

    @Override
    public void simpleInitApp() {

        try {
            /**
             * Set up Physics
             */
            bulletAppState = new BulletAppState();
            stateManager.attach(bulletAppState);

            manager = new LevelManager(assetManager);

            players = new Node();
            rootNode.attachChild(players);

//        // We set up collision detection for the player by creating
//        // a capsule collision shape and a CharacterControl.
//        // The CharacterControl offers extra settings for
//        // size, stepheight, jumping, falling, and gravity.
//        // We also put the player in its starting position.
//        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
//        player = new CharacterControl(capsuleShape, 0.05f);
//        player.setJumpSpeed(20);
//        player.setFallSpeed(30);
//        player.setGravity(30);
//        player.setPhysicsLocation(new Vector3f(0, 10, 0));
            // We attach the scene and the player to the rootnode and the physics space,
            // to make them appear in the game world.
            rootNode.attachChild(manager.getSceneModel());
            bulletAppState.getPhysicsSpace().add(manager.getLandscape());
//        bulletAppState.getPhysicsSpace().add(player);

            NetManager.setup();
            // make and start a server socket
            networkServer = Network.createServer(NetConstants.PORT);
            networkServer.start();
            PacketListener plistener = new PacketListener(profiles, this);
            networkServer.addMessageListener(plistener, MessagePlayerUpdate.class);
            networkServer.addMessageListener(plistener, MessageClientShoot.class);
            networkServer.addConnectionListener(this);
        } catch (IOException ex) {
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Send updates to the clients.
     *
     * @param tpf
     */
    @Override
    public void simpleUpdate(float tpf) {
        updateCounter += tpf;

        if (updateCounter > NetConstants.UPDATE_TIMER) {
            updateCounter -= NetConstants.UPDATE_TIMER;

            int conns = 0;

            for (HostedConnection connection : networkServer.getConnections()) {
                ServerPlayerProfile profile = profiles.get(connection);
                if (profile != null) {
                    for (HostedConnection connectionTo
                            : networkServer.getConnections()) {
                        if (connectionTo != connection) {
                            connectionTo.send(new MessagePlayerServerUpdatePosition(profile));
                        }
                    }

                    if (profile.isUnsentTCP()) {
                        connection.send(new MessageServerUpdateStats(profile));
                        profile.setUnsentTCP(false);
                    }

                    if (Math.random() > 0.025) {
                        profile.setHealth(profile.getHealth() + 1);
                    }
                }
                conns++;
            }

            if (profiles.size() != conns) {
                Set<HostedConnection> old = new HashSet<>(profiles.keySet());
                old.removeAll(networkServer.getConnections());
                for (HostedConnection conn : old) {
                    profiles.remove(conn);
                }
            }

            if (!disconnected.isEmpty()) {
                for (ServerPlayerProfile prof : disconnected) {
                    networkServer.broadcast(new MessagePlayerDisconnect(prof));
                }

                disconnected.clear();
            }
        }
    }

    @Override
    public void connectionAdded(Server server, HostedConnection conn) {
    }

    @Override
    public void connectionRemoved(Server server, HostedConnection conn) {
        ServerPlayerProfile removed = profiles.remove(conn);
        if (removed != null) {
            Node root = removed.getRoot();
            if (root != null) {
                rootNode.detachChild(root);
            }
            disconnected.add(removed);
        }
    }

    public Node getPlayersNode() {
        return players;
    }

    public Map<HostedConnection, ServerPlayerProfile> getProfiles() {
        return profiles;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking.client;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.scene.Node;
import java.util.Map;
import java.util.UUID;
import jmegame.JMEGame;
import jmegame.LevelManager;
import jmegame.networking.MessagePlayerServerUpdatePosition;
import jmegame.networking.MessageServerUpdateStats;
import jmegame.networking.SidedPlayerData;

/**
 *
 * @author campbell
 */
public class PacketListener implements MessageListener<Client> {

    private final JMEGame game;

    public PacketListener(JMEGame game) {
        this.game = game;
    }

    @Override
    public void messageReceived(Client source, Message message) {
        if (message instanceof MessagePlayerServerUpdatePosition) {
            // do something with the message
            MessagePlayerServerUpdatePosition update
                    = (MessagePlayerServerUpdatePosition) message;

            game.runOnUpdateThread(() -> {
                Map<UUID, SidedPlayerData> players = game.getPlayers();
                UUID uuid = update.getProfile().getUuid();
                SidedPlayerData player = players.get(uuid);
                Node root;
                RigidBodyControl body;
                if (player == null) {
                    player = new SidedPlayerData();
                    root = new Node();
//                    body = PlayerPhysicsData.
//                            makeRigidBody(game.getAssetManager());
//                    root.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                    player.setRender(root);
//                    player.setCollision(body);

//                    Box box1 = new Box(1, 1, 1);
//                    Geometry blue = new Geometry("Box", box1);
//                    blue.setLocalTranslation(new Vector3f(1, -1, 1));
//                    Material mat1 = new Material(game.getAssetManager(),
//                            "Common/MatDefs/Misc/Unshaded.j3md");
//                    mat1.setColor("Color", ColorRGBA.Blue);
//                    blue.setMaterial(mat1);
//                    root.attachChild(blue);
                    root.attachChild(LevelManager.
                            getPlayerModel(game.getAssetManager()));

                    game.getRootNode().attachChild(root);
//                    game.getBulletAppState().getPhysicsSpace().add(body);
                    players.put(uuid, player);
                } else {
                    root = player.getRender();
//                    body = player.getCollision();
                }

                root.setLocalTranslation(update.getProfile().getPosition());
//                root.setLocalRotation(update.getProfile().getRotation());

//                body.setPhysicsLocation(root.getLocalTranslation());
            });
        }

        if (message instanceof MessageServerUpdateStats) {
            // do something with the message
            final MessageServerUpdateStats update
                    = (MessageServerUpdateStats) message;

            game.runOnUpdateThread(() -> game.setHealth(update.getHealth()));
        }
    }
}

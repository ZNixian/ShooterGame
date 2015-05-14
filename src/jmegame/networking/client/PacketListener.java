/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking.client;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.scene.Node;
import java.util.Map;
import java.util.UUID;
import jmegame.AppStateIngame;
import jmegame.common.PlayerAnimationController;
import jmegame.networking.MessagePlayerDisconnect;
import jmegame.networking.MessagePlayerServerUpdatePosition;
import jmegame.networking.MessageServerUpdateStats;
import jmegame.networking.messages.MessageS2CParticle;

/**
 *
 * @author campbell
 */
public class PacketListener implements MessageListener<Client> {

    private final AppStateIngame game;

    public PacketListener(AppStateIngame game) {
        this.game = game;
    }

    @Override
    public void messageReceived(Client source, final Message message) {
        if (message instanceof MessagePlayerServerUpdatePosition) {
            // do something with the message
            MessagePlayerServerUpdatePosition update
                    = (MessagePlayerServerUpdatePosition) message;

            game.getGame().runOnUpdateThread(() -> {
                Map<UUID, PlayerAnimationController> players = game.getPlayers();
                UUID uuid = update.getProfile().getUUID().asUUID();
                PlayerAnimationController player = players.get(uuid);
                if (player == null) {
                    player = new PlayerAnimationController(game.
                            getGame().getAssetManager());

                    game.getRootNode().attachChild(player.getRoot());
//                    game.getBulletAppState().getPhysicsSpace().add(body);
                    players.put(uuid, player);
                }
                player.update(update.getProfile());
            });
        }

        if (message instanceof MessageServerUpdateStats) {
            // do something with the message
            final MessageServerUpdateStats update
                    = (MessageServerUpdateStats) message;

            game.getGame().runOnUpdateThread(() -> game.setHealth(update.getHealth()));
        }

        if (message instanceof MessagePlayerDisconnect) {
            // do something with the message
            final MessagePlayerDisconnect disconnect
                    = (MessagePlayerDisconnect) message;

            game.getGame().runOnUpdateThread(() -> {
                Map<UUID, PlayerAnimationController> players = game.getPlayers();
                UUID uuid = disconnect.getUUID().asUUID();
                PlayerAnimationController player = players.get(uuid);
                if (player != null) {
                    Node root = player.getRoot();

                    if (root != null) {
                        game.getRootNode().detachChild(root);
                    }
                }
            });
        }

        if (message instanceof MessageS2CParticle) {
            game.getGame().runOnUpdateThread(() -> {
                Map<UUID, PlayerAnimationController> players = game.getPlayers();
                UUID uuid = ((MessageS2CParticle) message).getUuid().asUUID();
                PlayerAnimationController player = players.get(uuid);
                if(player == null) {
                    player = game.getModel();
                }

                Material material = new Material(game.getGame().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
                material.setTexture("Texture", game.getGame().getAssetManager().loadTexture("Effects/Explosion/flame.png"));
                material.setFloat("Softness", 3f); // 

                //Fire
                ParticleEmitter fire = new ParticleEmitter("Fire", ParticleMesh.Type.Triangle, 30);
                fire.setMaterial(material);
                fire.setShape(new EmitterSphereShape(Vector3f.ZERO, 0.1f));
                fire.setImagesX(2);
                fire.setImagesY(2); // 2x2 texture animation
                fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f)); // red
                fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
                fire.setStartSize(0.6f);
                fire.setEndSize(0.01f);
                fire.setGravity(0, -0.3f, 0);
                fire.setLowLife(0.5f);
                fire.setHighLife(3f);

                fire.setLocalTranslation(player.getRoot().getLocalTranslation().
                        add(0, 5, 0));

                game.getParticleNode().attachChild(fire);
            });
        }
    }
}

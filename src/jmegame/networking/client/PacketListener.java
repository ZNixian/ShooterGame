/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking.client;

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
    public void messageReceived(Client source, Message message) {
        if (message instanceof MessagePlayerServerUpdatePosition) {
            // do something with the message
            MessagePlayerServerUpdatePosition update
                    = (MessagePlayerServerUpdatePosition) message;

            game.getGame().runOnUpdateThread(() -> {
                Map<UUID, PlayerAnimationController> players = game.getPlayers();
                UUID uuid = update.getProfile().getUuid();
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
                UUID uuid = disconnect.getProfile().getUuid();
                PlayerAnimationController player = players.get(uuid);
                if (player != null) {
                    Node root = player.getRoot();

                    if (root != null) {
                        game.getRootNode().detachChild(root);
                    }
                }
            });
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.common;

import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jmegame.AppStateIngame;
import jmegame.LevelManager;
import static jmegame.PlayerPhysicsData.PLAYER_PHYSICS_OFFSET;
import jmegame.networking.MessagePlayerUpdate;
import jmegame.networking.PlayerProfile;

/**
 *
 * @author campbell
 */
public class PlayerAnimationController {

    private final Node root;

    public PlayerAnimationController(AssetManager assetManager) {
        root = new Node();
//                    body = PlayerPhysicsData.
//                            makeRigidBody(game.getAssetManager());
        root.setShadowMode(RenderQueue.ShadowMode.Cast);
//                    player.setCollision(body);

//                    Box box1 = new Box(1, 1, 1);
//                    Geometry blue = new Geometry("Box", box1);
//                    blue.setLocalTranslation(new Vector3f(1, -1, 1));
//                    Material mat1 = new Material(game.getAssetManager(),
//                            "Common/MatDefs/Misc/Unshaded.j3md");
//                    mat1.setColor("Color", ColorRGBA.Blue);
//                    blue.setMaterial(mat1);
//                    root.attachChild(blue);
        Spatial model = LevelManager.getPlayerModel(assetManager);
        model.getLocalTranslation().addLocal(0,
                PLAYER_PHYSICS_OFFSET, 0);
        root.attachChild(model);
    }

    public Node getRoot() {
        return root;
    }

    public void update(MessagePlayerUpdate mpu) {
        update(mpu.getPosition(), mpu.getRotation());
    }

    public void update(PlayerProfile profile) {
        update(profile.getPosition(), profile.getRotation());
    }

    public void update(Vector3f pos, Quaternion rot) {
        root.setLocalTranslation(pos);

        Quaternion q = rot.clone();
        q.set(0, q.getY(), 0, q.getW());
        root.setLocalRotation(q);

//                body.setPhysicsLocation(root.getLocalTranslation());
    }
}

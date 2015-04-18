/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Node;

/**
 *
 * @author campbell
 */
public class PlayerPhysicsData {
    
    public static final float PLAYER_PHYSICS_OFFSET = -5.5f;

    private PlayerPhysicsData() {
    }

    public static RigidBodyControl makeRigidBody(AssetManager loader) {
//        CapsuleCollisionShape shape = new CapsuleCollisionShape(1.5f, 6f, 1);
//        BoxCollisionShape shape
//                = new BoxCollisionShape(new Vector3f(0.5f, 0.5f, 0.5f));
        CollisionShape shape = CollisionShapeFactory.
                createMeshShape((Node) LevelManager.getPlayerModel(loader));
        RigidBodyControl body = new RigidBodyControl(shape, 0);
        return body;
    }
}

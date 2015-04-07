/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;

/**
 *
 * @author campbell
 */
public class PlayerPhysicsData {

    private PlayerPhysicsData() {
    }

    public static RigidBodyControl makeRigidBody() {
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        RigidBodyControl body = new RigidBodyControl(capsuleShape, 0);
        return body;
    }
}

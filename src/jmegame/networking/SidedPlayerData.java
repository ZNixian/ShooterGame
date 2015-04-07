/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;

/**
 *
 * @author campbell
 */
public class SidedPlayerData {
    private Node render;
    private RigidBodyControl collision;

    public Node getRender() {
        return render;
    }

    public void setRender(Node render) {
        this.render = render;
    }

    public RigidBodyControl getCollision() {
        return collision;
    }

    public void setCollision(RigidBodyControl collision) {
        this.collision = collision;
    }
}

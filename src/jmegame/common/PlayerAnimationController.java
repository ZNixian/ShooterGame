/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.common;

import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.material.MaterialList;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.OgreMeshKey;
import static jmegame.PlayerPhysicsData.PLAYER_PHYSICS_OFFSET;
import jmegame.networking.MessagePlayerUpdate;
import jmegame.networking.PlayerProfile;
import jmegame.weapons.Weapon;
import jmegame.weapons.WeaponRegistry;

/**
 *
 * @author campbell
 */
public class PlayerAnimationController {

    private final Node root;
    private final AnimControl playerControl;
    private final AssetManager assetManager;
    private Weapon weapon;
    private Spatial gun;

    public PlayerAnimationController(AssetManager assetManager, Weapon weapon) {
        this.assetManager = assetManager;

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
        MaterialList matList = (MaterialList) assetManager.loadAsset(
                new AssetKey("Models/players old/players.material"));

        ModelKey key1 = new OgreMeshKey("Models/players old/players.mesh.xml", matList);
        Spatial model = assetManager.loadAsset(key1);
        model.setLocalScale(0.35859431f);

        model.getLocalTranslation().addLocal(0,
                PLAYER_PHYSICS_OFFSET, 0);
        root.attachChild(model);

        playerControl = model.getControl(AnimControl.class);

        setWeapon(weapon, assetManager);
    }

    public Node getRoot() {
        return root;
    }

    public void update(MessagePlayerUpdate mpu) {
        update(mpu.getPosition(), mpu.getRotation());
    }

    public void update(PlayerProfile profile) {
        Weapon w = WeaponRegistry.getInstance().getWeaponByID(profile.getWeaponID());
        if (w != null) {
            setWeapon(w, assetManager);
        }
        update(profile.getPosition(), profile.getRotation());
    }

    public void update(Vector3f pos, Quaternion rot) {
        root.setLocalTranslation(pos);

//        Quaternion q = rot.clone();
//        q.set(0, q.getY(), 0, q.getW());
//        root.setLocalRotation(q);
//        long startTime = System.nanoTime();

        Vector3f v3f = rot.getRotationColumn(2);
        v3f.y = 0;
        root.setLocalRotation(new Quaternion(0, 0, 0, 1));
//        System.out.println("ok: " + root.getWorldRotation());
        root.getLocalRotation().lookAt(v3f, new Vector3f(0, 1, 0));

//        System.out.println("time taken: " + (System.nanoTime() - startTime));
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon, AssetManager assetManager) {
        if (this.weapon == weapon) {
            return;
        }
        this.weapon = weapon;

        if (gun != null) {
            root.detachChild(gun);
        }
        gun = weapon.load(assetManager);
        root.attachChild(gun);

//        System.out.println("ok");
//        weapon.applyToSkeleton(playerControl.getSkeleton(), assetManager);
    }
}

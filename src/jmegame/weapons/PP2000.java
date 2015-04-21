/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.weapons;

import com.jme3.animation.Bone;
import com.jme3.animation.Skeleton;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.material.MaterialList;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.OgreMeshKey;

/**
 *
 * @author campbell
 */
public class PP2000 implements Gun {

    private static Node model;
    public static final PP2000 INSTANCE = new PP2000();

    @Override
    public Spatial load(AssetManager assetManager) {
        if (model == null) {
            model = new Node();
            MaterialList matList = (MaterialList) assetManager.loadAsset(
                    new AssetKey("Models/guns/pp2000/pp2000.mtl"));

            ModelKey key1 = new OgreMeshKey("Models/guns/pp2000/pp2000.mesh.xml", matList);
            Spatial submodel1 = assetManager.loadAsset(key1);

            model.setLocalScale(0.25f);
            model.attachChild(submodel1);
            model.setLocalTranslation(-2, -2, 3);
            model.setLocalRotation(new Quaternion(0, 1, 0, 1));
            model.setCullHint(Spatial.CullHint.Never);
        }
        return model.clone();
    }

//    @Override
//    public void setSkeleton(Skeleton skel) {
//        Bone bone;
//
//        bone = skel.getBone("upper_arm.R");
//        translateBone(bone, 0, -0.25f, 0);
//        rotateBone(bone, new Quaternion(0.338f, 0.038f, -0.089f, 0.899f));
//
//        bone = skel.getBone("forearm.R");
//        translateBone(bone, 0, 0, 0.125f);
//        rotateBone(bone, new Quaternion(-0.387f, 0.17f, 0.279f, 0.862f));
//
//        bone = skel.getBone("hand.R");
////        translateBone(bone, 0, 0, 0.125f);
//        rotateBone(bone, new Quaternion(-0.075f, 0.036f, -0.145f, 0.986f));
//        
//        
//        
//
//        bone = skel.getBone("upper_arm.L");
////        translateBone(bone, 0, -0.25f, 0);
//        rotateBone(bone, new Quaternion(-0.477f, -0.429f, -0.089f, 0.762f));
//
//        bone = skel.getBone("forearm.L");
////        translateBone(bone, 0, 0, 0.125f);
//        rotateBone(bone, new Quaternion(0f, 0f, 0f, 1f));
//
//        bone = skel.getBone("hand.L");
////        translateBone(bone, 0, 0, 0.125f);
//        rotateBone(bone, new Quaternion(-0.049f, -0.475f, 0.153f, 0.865f));
//    }
//
//    private void translateBone(Bone bone, float x, float y, float z) {
//        bone.setUserControl(true);
//        bone.setBindTransforms(bone.getBindPosition().add(x, y, z),
//                bone.getBindRotation(), bone.getBindScale());
//    }
//
//    private void rotateBone(Bone bone, Quaternion rot) {
//        bone.setUserControl(true);
//        bone.setBindTransforms(bone.getBindPosition(), rot, bone.getBindScale());
//    }
}

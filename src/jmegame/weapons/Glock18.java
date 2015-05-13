/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.weapons;

import com.jme3.animation.Skeleton;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.material.MaterialList;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.OgreMeshKey;

/**
 *
 * @author campbell
 */
public class Glock18 implements Gun {

    private static Node model;
    public static final Glock18 INSTANCE = new Glock18();

    @Override
    public Spatial load(AssetManager assetManager) {
        if (model == null) {
            model = new Node();
            MaterialList matList = (MaterialList) assetManager.loadAsset(
                    new AssetKey("Models/guns/glock18/glock18.material"));

            ModelKey key1 = new OgreMeshKey("Models/guns/glock18/Mesh1_Group1_Model.mesh.xml", matList);
            Spatial submodel1 = assetManager.loadAsset(key1);

            ModelKey key2 = new OgreMeshKey("Models/guns/glock18/Mesh2_Model.mesh.xml", matList);
            Spatial submodel2 = assetManager.loadAsset(key2);

            model.setLocalScale(0.05f);
            model.attachChild(submodel1);
            model.attachChild(submodel2);
//            model.setLocalRotation(new Quaternion(0, 0, 1, 1));
        }
        return model.clone();
    }

    @Override
    public void applyToSkeleton(Skeleton skel, AssetManager assetManager) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

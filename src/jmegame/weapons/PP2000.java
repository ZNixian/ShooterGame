/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.weapons;

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
}

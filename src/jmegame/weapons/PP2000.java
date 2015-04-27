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
                    new AssetKey("Models/players old/players.material"));

            ModelKey key1 = new OgreMeshKey("Models/players old/players.mesh.xml", matList);
            Spatial player = assetManager.loadAsset(key1);
            player.setLocalScale(0.35859431f);

            model.attachChild(player);
            model.setCullHint(Spatial.CullHint.Never);
        }
        return model.clone();
    }

    @Override
    public void setSkeleton(Skeleton skel, AssetManager assetManager) {
    }
}

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
import java.util.ArrayList;
import java.util.Arrays;
import jmegame.networking.IBulletSource;
import libskel.SkelUtils;

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
                    new AssetKey("Models/guns/pp2000/pp2000.material"));

            ModelKey key1 = new OgreMeshKey("Models/guns/pp2000/pp2000.mesh.xml", matList);
            Spatial player = assetManager.loadAsset(key1);
//            player.setLocalScale(0.35859431f);

            model.setLocalScale(0.125f);
            model.attachChild(player);
            model.setLocalTranslation(-1f, -2.125f, 1.90f);
            model.setLocalRotation(new Quaternion(0, 1, 0, 1));
            model.attachChild(player);
            model.setCullHint(Spatial.CullHint.Never);
        }
        return model.clone();
    }

    @Override
    public void applyToSkeleton(Skeleton skel, AssetManager assetManager) {
        String str = (String) assetManager.loadAsset("Models/guns/pp2000/Models.players.players.offset");
        ArrayList<String> strs = new ArrayList<>();
        strs.addAll(Arrays.asList(str.split("\n")));
        SkelUtils.loadSkeleton(strs, skel);
    }

    @Override
    public void onTriggerStateChange(boolean newState, IBulletSource basicBulletSource) {
    }

    @Override
    public void whileTriggerPressed(float tpf, IBulletSource basicBulletSource) {
        if (basicBulletSource.getCooldown() > 0) {
            return;
        }
        
        basicBulletSource.addCooldown(0.1f);
        basicBulletSource.fireBullet(5);
    }
}

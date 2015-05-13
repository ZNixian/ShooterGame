/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.weapons;

import com.jme3.animation.Skeleton;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

/**
 *
 * @author campbell
 */
public interface Gun {

    public Spatial load(AssetManager manager);

    public void applyToSkeleton(Skeleton skel, AssetManager assetManager);
}

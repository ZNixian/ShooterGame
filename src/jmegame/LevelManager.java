/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame;

import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.MaterialList;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.OgreMeshKey;

/**
 *
 * @author campbell
 */
public class LevelManager {

    private final Spatial sceneModel;
    private final RigidBodyControl landscape;

    public LevelManager(AssetManager assetManager) {

        // We load the scene from the zip file and adjust its size.
        assetManager.registerLocator("town.zip", ZipLocator.class);
//        assetManager.registerLocator("players.zip", ZipLocator.class);
//        assetManager.registerLocator("lib/assets.jar", ZipLocator.class);
        sceneModel = assetManager.loadModel("main.scene");
        sceneModel.setLocalScale(2f);

        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.
        CollisionShape sceneShape
                = CollisionShapeFactory.createMeshShape((Node) sceneModel);
        landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);
    }

    public Spatial getSceneModel() {
        return sceneModel;
    }

    public RigidBodyControl getLandscape() {
        return landscape;
    }

    public static Spatial getPlayerModel(AssetManager assetManager) {
//        Spatial model = assetManager.loadModel("players/players.obj");
//        Material mat_default = new Material(
//                assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
//        model.setMaterial(mat_default);

//        MaterialList matList = (MaterialList) assetManager.
//                loadAsset("Models/players/players.material");
//        ModelKey key = new OgreMeshKey("Models/players/players.mesh.xml", matList);
//        Spatial model = assetManager.loadAsset(key);
//        model.setLocalScale(0.35859431f); // see assets/Models/players/README_SCALE
        
        MaterialList matList = (MaterialList) assetManager.
                loadAsset("Models/players/players.material");
        ModelKey key = new OgreMeshKey("Models/players/players.scene", matList);
        Spatial model = assetManager.loadAsset(key);
        model.setLocalScale(3.5863718f); // see assets/Models/players/README_SCALE
        
//        MaterialList matList = (MaterialList) assetManager.
//                loadAsset("Models/players/player.material");
//        ModelKey key = new OgreMeshKey("Models/players/player.scene", matList);
//        Spatial model = assetManager.loadAsset(key);
//        model.setLocalScale(3.5863718f); // see assets/Models/players/README_SCALE
//        model.setLocalRotation(new Quaternion(-1, 0, 0, 1));

        return model;
    }

    public static Spatial getPlayerModelForGun(AssetManager assetManager) {
        Spatial model = assetManager.loadModel("Models/poses/pp2000/pose.obj");
//        Material mat_default = new Material(
//                assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
//        model.setMaterial(mat_default);

//        MaterialList matList = (MaterialList) assetManager.
//                loadAsset("Models/players/players.material");
//        ModelKey key = new OgreMeshKey("Models/players/players.mesh.xml", matList);
//        Spatial model = assetManager.loadAsset(key);
//        model.setLocalScale(0.35859431f); // see assets/Models/players/README_SCALE
        return model;
    }
}

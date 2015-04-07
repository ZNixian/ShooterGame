/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Network;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import jmegame.networking.MessagePlayerServerUpdate;
import jmegame.networking.MessagePlayerUpdate;
import jmegame.networking.NetConstants;
import jmegame.networking.NetManager;
import jmegame.networking.SidedPlayerData;
import jmegame.networking.client.PacketListener;

public class JMEGame extends SimpleApplication
        implements ActionListener {

    private BulletAppState bulletAppState;
    private LevelManager manager;
    private CharacterControl player;
    private final Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;

    //Temporary vectors used on each frame.
    //They here to avoid instanciating new vectors on each frame
    private final Vector3f camDir = new Vector3f();
    private final Vector3f camLeft = new Vector3f();

    private Client connection;
    private float updateCounter;

    private final Map<UUID, SidedPlayerData> players = new HashMap<>();

    private final Stack<Runnable> runOnUpdateThread = new Stack<>();

    public static void main(String[] args) {
//        GameServer.main(args);
        JMEGame app = new JMEGame();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        try {
            inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
            inputManager.setCursorVisible(true);
            flyCam.setEnabled(false);

            /**
             * Set up Physics
             */
            bulletAppState = new BulletAppState();
            stateManager.attach(bulletAppState);
            //bulletAppState.getPhysicsSpace().enableDebug(assetManager);

            // We re-use the flyby camera for rotation, while positioning is handled by physics
            viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
            flyCam.setMoveSpeed(100);
            setUpKeys();
            flyCam.setRotationSpeed(3);
            setUpLight();

            manager = new LevelManager(bulletAppState, assetManager);

            // We set up collision detection for the player by creating
            // a capsule collision shape and a CharacterControl.
            // The CharacterControl offers extra settings for
            // size, stepheight, jumping, falling, and gravity.
            // We also put the player in its starting position.
            CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
            player = new CharacterControl(capsuleShape, 0.05f);
            player.setJumpSpeed(20);
            player.setFallSpeed(30);
            player.setGravity(30);
            player.setPhysicsLocation(new Vector3f(0, 10, 0));

            // We attach the scene and the player to the rootnode and the physics space,
            // to make them appear in the game world.
            rootNode.attachChild(manager.getSceneModel());
            bulletAppState.getPhysicsSpace().add(manager.getLandscape());
            bulletAppState.getPhysicsSpace().add(player);

            NetManager.setup();
            connection = Network.connectToServer("localhost", NetConstants.PORT);
            connection.start();
            connection.addMessageListener(new PacketListener(this),
                    MessagePlayerServerUpdate.class);
            if (!connection.isConnected()) {
//                throw new IllegalStateException("connection is not connected!");
            }
        } catch (IOException ex) {
//            Logger.getLogger(JMEGame.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

    }

    private void setUpLight() {
        // We add light so we see the scene
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);
    }

    /**
     * We over-write some navigational key mappings here, so we can add
     * physics-controlled walking and jumping:
     */
    private void setUpKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("Exit", new KeyTrigger(KeyInput.KEY_END));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
        inputManager.addListener(this, "Pause");
        inputManager.addListener(this, "Exit");
    }

    /**
     * These are our custom actions triggered by key presses. We do not walk
     * yet, we just keep track of the direction the user pressed.
     *
     * @param binding
     * @param value
     * @param tpf
     */
    @Override
    public void onAction(String binding, boolean value, float tpf) {
        switch (binding) {
            case "Left":
                left = value;
                break;
            case "Right":
                right = value;
                break;
            case "Up":
                up = value;
                break;
            case "Down":
                down = value;
                break;
            case "Jump":
                player.jump();
                break;
            case "Pause":
//                inputEnabled = !inputEnabled;
                if (value) {
                    boolean state = inputManager.isCursorVisible();
                    inputManager.setCursorVisible(!state);
                    flyCam.setEnabled(state);
                }
                break;
            case "Exit":
                stop();
                break;
        }
    }

    /**
     * This is the main event loop--walking happens here. We check in which
     * direction the player is walking by interpreting the camera direction
     * forward (camDir) and to the side (camLeft). The setWalkDirection()
     * command is what lets a physics-controlled player walk. We also make sure
     * here that the camera moves with player.
     *
     * @param tpf
     */
    @Override
    public void simpleUpdate(float tpf) {
        camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        make2d(camDir);
        make2d(camLeft);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());

        updateCounter += tpf;

        if (connection != null && updateCounter > NetConstants.UPDATE_TIMER) {
            updateCounter -= NetConstants.UPDATE_TIMER;

            MessagePlayerUpdate message = new MessagePlayerUpdate(
                    player.getPhysicsLocation(), cam.getRotation());
            connection.send(message);
        }

        while (true) {
            Runnable item;
            synchronized (runOnUpdateThread) {
                if (runOnUpdateThread.empty()) {
                    break;
                }
                item = runOnUpdateThread.pop();
            }
            item.run();
        }
    }

    public static void make2d(Vector3f vec) {
        float total3d = vec.length();
        vec.y = 0;
        float total2d = vec.length();
        float diff = total3d / total2d;
        vec.multLocal(diff);
//        System.out.println("ok: " + vec.length());
    }

    @Override
    public void stop(boolean bool) {
        if (connection.isConnected()) {
            connection.close();
        }
        connection = null;
        super.stop(bool);
    }

    public Map<UUID, SidedPlayerData> getPlayers() {
        return players;
    }

    public void runOnUpdateThread(Runnable run) {
        synchronized (runOnUpdateThread) {
            runOnUpdateThread.push(run);
        }
    }

    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }
}

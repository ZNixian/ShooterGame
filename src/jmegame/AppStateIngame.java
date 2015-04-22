/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Parameters;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static jmegame.JMEGame.make2d;
import jmegame.common.PlayerAnimationController;
import jmegame.networking.MessageClientShoot;
import jmegame.networking.MessagePlayerDisconnect;
import jmegame.networking.MessagePlayerServerUpdatePosition;
import jmegame.networking.MessagePlayerUpdate;
import jmegame.networking.MessageServerUpdateStats;
import jmegame.networking.NetConstants;
import jmegame.networking.NetManager;
import jmegame.networking.client.PacketListener;

/**
 *
 * @author campbell
 */
public class AppStateIngame extends AbstractAppState
        implements ActionListener, ScreenController, Controller {

    private final JMEGame game;

    private final InputManager inputManager;
    private final FlyByCamera flyCam;
    private final LevelManager manager;
    private final Node rootNode;
    private final AssetManager assetManager;
    private final Camera cam;

    private BulletAppState bulletAppState;
    private CharacterControl player;
    private final String adress;
    private Nifty nifty;
    private final Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;

    //Temporary vectors used on each frame.
    //They here to avoid instanciating new vectors on each frame
    private final Vector3f camDir = new Vector3f();
    private final Vector3f camLeft = new Vector3f();

    private Client connection;
    private float updateCounter;

    private int health = 100;
    private Element healthBarElement;

    private PlayerAnimationController model;

    private final Map<UUID, PlayerAnimationController> players = new HashMap<>();

    public AppStateIngame(JMEGame game) {
        this.game = game;

        inputManager = game.getInputManager();
        flyCam = game.getFlyByCamera();
        manager = game.getManager();
//        rootNode = game.getRootNode();
        rootNode = new Node();
        game.getRootNode().attachChild(rootNode);
        adress = game.getAdress();
        assetManager = game.getAssetManager();
        cam = game.getCamera();

        setEnabled(true);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (isEnabled() && !enabled) {
            game.getRootNode().detachChild(rootNode);
        }
        if (!isEnabled() && enabled) {
            game.getRootNode().attachChild(rootNode);
        }

        super.setEnabled(enabled);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

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
        game.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(100);
        setUpKeys();
        flyCam.setRotationSpeed(3);
        setUpLight();

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
        player.setPhysicsLocation(new Vector3f(0, 7.5f, 0));

        model = new PlayerAnimationController(assetManager);
        rootNode.attachChild(model.getRoot());
//        model.getRoot().setCullHint(Spatial.CullHint.Always);

        // We attach the scene and the player to the rootnode and the physics space,
        // to make them appear in the game world.
        manager.getSceneModel().setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(manager.getSceneModel());
        bulletAppState.getPhysicsSpace().add(manager.getLandscape());
        bulletAppState.getPhysicsSpace().add(player);

        initGui();
        initNetwork();
    }

    private void setUpLight() {
        // We add light so we see the scene
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(sun);

        /* Drop shadows */
        final int SHADOWMAP_SIZE = 1024;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setLight(sun);
        game.getViewPort().addProcessor(dlsr);

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        game.getViewPort().addProcessor(fpp);
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
        inputManager.addMapping("Shoot",
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
        inputManager.addListener(this, "Pause");
        inputManager.addListener(this, "Exit");
        inputManager.addListener(this, "Shoot");
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
                game.stop();
                break;
            case "Shoot":
                connection.send(new MessageClientShoot());
                break;
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
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
        Vector3f pos = player.getPhysicsLocation().add(0, 1f, 0);
        cam.setLocation(pos);
        model.update(pos, cam.getRotation());

        updateCounter += tpf;

        if (connection != null && updateCounter > NetConstants.UPDATE_TIMER) {
            updateCounter -= NetConstants.UPDATE_TIMER;

            MessagePlayerUpdate message = new MessagePlayerUpdate(
                    cam.getLocation(), cam.getRotation());
            // was player.getPhysicsLocation().subtract(0, 4.5f, 0)
            connection.send(message);

//            connection.send(new MessageClientShoot());
        }
    }

    private void initGui() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                inputManager, game.getAudioRenderer(), game.getGuiViewPort());
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/HUD.xml", "hud", this);
        game.getGuiViewPort().addProcessor(niftyDisplay);
//        new TextBuilder().va
        Texture tex = assetManager.loadTexture("Interface/crosshairs.png");
        Picture crosshairs = new Picture("cursor");
        crosshairs.setTexture(assetManager, (Texture2D) tex, true);
        crosshairs.setWidth(64);
        crosshairs.setHeight(64);
        crosshairs.setPosition(game.getSettings().getWidth() / 2 - 32,
                game.getSettings().getHeight() / 2 - 32);
        game.getGuiNode().attachChild(crosshairs);
    }

    private void initNetwork() {
        try {
            NetManager.setup();
            connection = Network.connectToServer(adress, NetConstants.PORT);
            connection.start();
            PacketListener packetListener = new PacketListener(this);
            connection.addMessageListener(packetListener,
                    MessagePlayerServerUpdatePosition.class);

            connection.addMessageListener(packetListener,
                    MessageServerUpdateStats.class);

            connection.addMessageListener(packetListener,
                    MessagePlayerDisconnect.class);
//            if (!connection.isConnected()) {
//                throw new IllegalStateException("connection is not connected!");
//            }
        } catch (IOException ex) {
//            Logger.getLogger(JMEGame.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        healthBarElement = nifty.getScreen("hud").findElementById("healthbar")
                .findElementById("bar");
//        healthBarElement = nifty.getScreen("hud").findElementById("hudlayer")
//                .findElementById("hudpanel").findElementById("healthbar");
//        healthBarElement = healthBarElement
//                .findElementById("outline").findElementById("bar");
//        System.out.println("ok: " + healthBarElement);
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;

        final int MIN_WIDTH = 32;
        final int MAX_WIDTH = healthBarElement.getParent().getWidth();
        int pixelWidth = (int) (MIN_WIDTH + (MAX_WIDTH - MIN_WIDTH) * health / 100.0);
        healthBarElement.setConstraintWidth(new SizeValue(pixelWidth + "px"));
        healthBarElement.getParent().layoutElements();
    }

    @Override
    public void bind(Nifty nifty, Screen screen, Element elmnt, Parameters prmtrs) {
    }

    @Override
    public void init(Parameters prmtrs) {
    }

    @Override
    public void onFocus(boolean bln) {
    }

    @Override
    public boolean inputEvent(NiftyInputEvent nie) {
        return false;
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
        super.stateDetached(stateManager);

        if (connection.isConnected()) {
            connection.close();
        }
        connection = null;
    }

    public JMEGame getGame() {
        return game;
    }

    public Map<UUID, PlayerAnimationController> getPlayers() {
        return players;
    }

    public Node getRootNode() {
        return rootNode;
    }

}

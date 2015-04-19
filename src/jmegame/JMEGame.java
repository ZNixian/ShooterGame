/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import java.util.Stack;
import jmegame.server.GameServer;

public class JMEGame extends SimpleApplication {

    private LevelManager manager;

    private final Stack<Runnable> runOnUpdateThread = new Stack<>();
    private final String adress;
    private AppStateIngame stateIngame;

    public JMEGame(String adress) {
        this.adress = adress;
    }

    public static void main(String[] args) {
//        GameServer.main(args);
        String adress;
        if (args.length == 0) {
            adress = "localhost";
        } else if (args[0].equals("--server")) {
            GameServer.main(args);
            return;
        } else {
            adress = args[0];
        }
        JMEGame app = new JMEGame(adress);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        manager = new LevelManager(assetManager);
        
        stateIngame = new AppStateIngame(this);
//        stateIngame.setEnabled(false);
        stateManager.attach(stateIngame);
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
    public void destroy() {
        super.destroy();
        stateManager.detach(stateIngame);
    }

    public void runOnUpdateThread(Runnable run) {
        synchronized (runOnUpdateThread) {
            runOnUpdateThread.push(run);
        }
    }

    public LevelManager getManager() {
        return manager;
    }

    public String getAdress() {
        return adress;
    }

    public AppSettings getSettings() {
        return settings;
    }
}

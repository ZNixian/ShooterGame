/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame;

import com.jme3.asset.AssetManager;
import jmegame.common.PlayerAnimationController;
import jmegame.networking.messages.MessageC2SGunSwitch;
import jmegame.weapons.Glock18;
import jmegame.weapons.PP2000;
import jmegame.weapons.Weapon;

/**
 *
 * @author campbell
 */
public class PlayerManagment {

    private final PlayerAnimationController model;
    private final Weapon[] guns = new Weapon[3];
    private int selectedGun;
    private final AppStateIngame stateIngame;

    public PlayerManagment(AssetManager assetManager, AppStateIngame stateIngame) {
        model = new PlayerAnimationController(assetManager, PP2000.INSTANCE);
        this.stateIngame = stateIngame;

        guns[0] = PP2000.INSTANCE;
        guns[1] = Glock18.INSTANCE;
    }

    public PlayerAnimationController getModel() {
        return model;
    }

    public void weaponSelect(boolean fwd, AssetManager assetManager) {
//        System.out.println("ok: " + fwd);

        do {
            if (fwd) {
                selectedGun++;
            } else {
                selectedGun--;
            }

            if (selectedGun >= guns.length) {
                selectedGun = 0;
            }

            if (selectedGun < 0) {
                selectedGun = guns.length - 1;
            }
        } while (guns[selectedGun] == null);

        model.setWeapon(guns[selectedGun], assetManager);
        stateIngame.getConnection().send(new MessageC2SGunSwitch(selectedGun));
    }
}

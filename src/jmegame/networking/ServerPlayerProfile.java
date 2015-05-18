/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking;

import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.UUID;
import jmegame.common.PlayerAnimationController;
import jmegame.common.SUUID;
import jmegame.weapons.PP2000;
import jmegame.weapons.Weapon;
import jmegame.weapons.WeaponRegistry;

/**
 * Stores detailed info on a player
 *
 * @author campbell
 */
public class ServerPlayerProfile {

    private Vector3f position;
    private Quaternion rotation;
    private Weapon weapon;
    private final UUID uuid;
    private int health;

    private boolean unsentTCP;

    private boolean triggerPressed;
    private float fireCooldown;

    private final PlayerAnimationController controller;

    public ServerPlayerProfile(UUID uuid, AssetManager assetManager, Weapon weapon) {
        this.uuid = uuid;
        this.weapon = weapon;
        health = 100;
        unsentTCP = true;

        controller = new PlayerAnimationController(assetManager, PP2000.INSTANCE);
    }

    public PlayerProfile makeSendableVarsion() {
        return new PlayerProfile(position, rotation,
                WeaponRegistry.getInstance().getWeaponID(weapon),
                new SUUID(uuid));
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health < 0) {
            health = 0;
        } else if (health > 100) {
            health = 100;
        }

        this.health = health;
        unsentTCP = true;
    }

    public boolean isUnsentTCP() {
        return unsentTCP;
    }

    public void setUnsentTCP(boolean unsentTCP) {
        this.unsentTCP = unsentTCP;
    }

    public PlayerAnimationController getController() {
        return controller;
    }

    public void update(MessagePlayerUpdate mpu) {
        position = mpu.getPosition();
        rotation = mpu.getRotation();

        controller.update(position, rotation);
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
//        unsentTCP = true;
        this.weapon = weapon;
    }

    public boolean isTriggerPressed() {
        return triggerPressed;
    }

    public void setTriggerPressed(boolean isTriggerPressed) {
        this.triggerPressed = isTriggerPressed;
    }

    public float getFireCooldown() {
        return fireCooldown;
    }

    public void setFireCooldown(float fireCooldown) {
        if (fireCooldown < 0) {
            fireCooldown = 0;
        }
        this.fireCooldown = fireCooldown;
    }
}

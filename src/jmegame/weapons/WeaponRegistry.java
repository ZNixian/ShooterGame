/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.weapons;

import java.util.ArrayList;

/**
 *
 * @author campbell
 */
public final class WeaponRegistry {

    private WeaponRegistry() {
        registerWeapon(PP2000.INSTANCE);
        registerWeapon(Glock18.INSTANCE);
    }

    public static WeaponRegistry getInstance() {
        return WeaponRegistryHolder.INSTANCE;
    }

    private static class WeaponRegistryHolder {

        private static final WeaponRegistry INSTANCE = new WeaponRegistry();
    }

    /////////
    private final ArrayList<Weapon> weapons = new ArrayList<>();

    public void registerWeapon(Weapon weapon) {
        weapons.add(weapon);
    }

    public Weapon getWeaponByID(int id) {
        return weapons.get(id);
    }

    public int getWeaponID(Weapon weapon) {
        if (weapon == null) {
            throw new IllegalArgumentException("Argument 'weapon' cannot be null!");
        }
        int id = weapons.indexOf(weapon);
        if (id == -1) {
            throw new IllegalStateException("Weapon " + weapon + " not registered!");
        }
        return id;
    }
}

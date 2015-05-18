/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author campbell
 */
@Serializable
public class MessageC2SGunSwitch extends AbstractMessage {
    private int newWeapon;

    public MessageC2SGunSwitch() {
        super(true);
    }

    public MessageC2SGunSwitch(int newWeapon) {
        this();
        this.newWeapon = newWeapon;
    }

    public int getNewWeapon() {
        return newWeapon;
    }
}

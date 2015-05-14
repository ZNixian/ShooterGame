/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.networking;

/**
 *
 * @author campbell
 */
public interface IBulletSource {
    void fireBullet(int damage);
    void addCooldown(float cooldown);
    void setCooldown(float cooldown);
    float getCooldown();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.server;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.scene.Spatial;
import jmegame.networking.IBulletSource;
import jmegame.networking.ServerPlayerProfile;

/**
 *
 * @author campbell
 */
public class BasicBulletSource implements IBulletSource {

    private final PacketListener listener;
    private final ServerPlayerProfile prof;

    public BasicBulletSource(PacketListener listener, ServerPlayerProfile prof) {
        this.listener = listener;
        this.prof = prof;
    }

    @Override
    public void fireBullet(int damage) {
        // 1. Reset results list.
        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.
        Ray ray = new Ray(prof.getPosition(), prof.getRotation()
                .getRotationColumn(2));
        // 3. Collect intersections between Ray and Shootables in results list.
        listener.getServer().getPlayersNode().collideWith(ray, results);

        if (results.size() > 0) {
            ServerPlayerProfile hitplayer = null;
            int i = 0;
            do {
                if (i >= results.size()) {
                    hitplayer = null;
                    continue; // could be break; , but meh.
                }
                // The closest collision point is what was truly hit:
                CollisionResult closest = results.getCollision(i);
                Spatial hit = closest.getGeometry();

                hitplayer = listener.findProfileForPlayer(hit);
                i++;
            } while (hitplayer == prof);
            if (hitplayer == null) {
//                    System.out.println("No player hit!?");
                return;
            }
//                System.out.println("prof: " + prof.getUuid()
//                        + ", hitPlayer: " + hitplayer.getUuid());
            hitplayer.setHealth(hitplayer.getHealth() - 10);
//                System.out.println("hit player " + hitplayer);
        }

//            source.send(new MessageS2CParticle(new SUUID(prof.getUuid())));
    }

    @Override
    public void addCooldown(float cooldown) {
        setCooldown(getCooldown() + cooldown);
    }

    @Override
    public void setCooldown(float cooldown) {
        prof.setFireCooldown(cooldown);
    }

    @Override
    public float getCooldown() {
        return prof.getFireCooldown();
    }
}

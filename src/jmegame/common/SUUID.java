/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmegame.common;

import com.jme3.network.serializing.Serializable;
import java.util.UUID;

/**
 *
 * @author campbell
 */
@Serializable
public class SUUID {

    private long uuid_lsb;
    private long uuid_msb;

    public SUUID() {
    }

    public SUUID(UUID uuid) {
        this.uuid_lsb = uuid.getLeastSignificantBits();
        this.uuid_msb = uuid.getMostSignificantBits();
    }

    public UUID asUUID() {
        return new UUID(uuid_lsb, uuid_msb);
    }
}

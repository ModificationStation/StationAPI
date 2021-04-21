package net.modificationstation.stationapi.api.entity.player;

import java.util.*;

public interface PlayerBaseSettersGettersInvokers {

    void doFall(float fallDist);

    float getFallDistance();

    void setFallDistance(float f);

    boolean getSleeping();

    boolean getJumping();

    void doJump();

    Random getRandom();

    void setYSize(float f);

    void setActionState(float newMoveStrafing, float newMoveForward, boolean newIsJumping);

    void setMoveForward(float value);

    void setMoveStrafing(float value);

    void setIsJumping(boolean value);
}

package net.mine_diver.testmod.proxy;

import net.mine_diver.testmod.TestMod;

public class ClientProxy extends CommonProxy{
    @Override
    public void onInit() {
        TestMod.INSTANCE.LOGGER.info("ClientProxy initialiazed!");
        super.onInit();
    }
}

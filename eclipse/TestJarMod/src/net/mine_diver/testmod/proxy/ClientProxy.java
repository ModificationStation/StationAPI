package net.mine_diver.testmod.proxy;

public class ClientProxy extends CommonProxy{
    @Override
    public void onInit() {
        System.out.println("ClientProxy initialiazed!");
        super.onInit();
    }
}

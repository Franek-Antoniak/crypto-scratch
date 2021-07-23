package blockchain;

import blockchain.controller.MineController;
import blockchain.messenger.controller.MessengerController;

import java.util.concurrent.TimeUnit;

public class Main {
    private static final MineController mineController = MineController.getInstance();
    private static final MessengerController messengerController = MessengerController.getInstance();

    public static void main(String[] args) throws InterruptedException {
        mineController.addNewMainers(4);
        TimeUnit.SECONDS.sleep(20);
        mineController.removeAllMainers();
        System.out.println(Blockchain.getInstance());
    }
}

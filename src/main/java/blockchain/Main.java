package blockchain;

import blockchain.cryptomine.controller.MineController;
import blockchain.messenger.Messenger;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final MineController mineController = MineController.getInstance();
    private static final Messenger messenger = Messenger.getInstance();

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        mineController.addNewMainers(4);
        messenger.addNewUser();
        TimeUnit.SECONDS.sleep(20);
        mineController.removeAllMainers();
    }
}

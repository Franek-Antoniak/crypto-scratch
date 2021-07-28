package blockchain;

import blockchain.messenger.Messenger;
import blockchain.mine.controller.MineController;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final MineController mineController = MineController.getInstance();
    private static final Messenger messenger = Messenger.getInstance();

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        messenger.addNewUser();
        mineController.addNewMainers(4);
    }
}

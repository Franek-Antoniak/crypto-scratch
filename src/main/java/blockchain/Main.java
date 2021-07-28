package blockchain;

import blockchain.messenger.Messenger;
import blockchain.mine.controller.MineController;

import java.util.concurrent.TimeUnit;

public class Main {
    private static final MineController mineController = MineController.getInstance();
    private static final Messenger messenger = Messenger.getInstance();

    public static void main(String[] args) throws InterruptedException {
        mineController.addNewMainers(1);
        messenger.addPeople(3);
        TimeUnit.SECONDS.sleep(30);
        mineController.removeAllMainers();
        messenger.joinUsers();
        System.out.println(Blockchain.getInstance());
    }
}

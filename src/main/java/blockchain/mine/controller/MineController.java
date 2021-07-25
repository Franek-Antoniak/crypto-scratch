package blockchain.mine.controller;

import blockchain.mine.facade.MineFacade;

public class MineController {
    private final MineFacade mainersFacade = MineFacade.getInstance();

    private MineController() {

    }

    public static MineController getInstance() {
        return MineControllerSingleton.instance;
    }

    /**
     * @param howManyToAdd - Integer that indicates the number of mainers wanted to be added by the client
     */
    public void addNewMainers(int howManyToAdd) {
        mainersFacade.addNewMainers(howManyToAdd);
    }

    /**
     * @param howManyToRemove - Integer that indicates the number of mainers wanted to be removed by the client
     */
    public void removeMainers(int howManyToRemove) {
        mainersFacade.removeMainers(howManyToRemove);
    }

    /**
     * Removes all mainers and stop their work.
     */
    public void removeAllMainers() {
        mainersFacade.removeAllMainers();
    }

    private static class MineControllerSingleton {
        private static final MineController instance = new MineController();
    }
}

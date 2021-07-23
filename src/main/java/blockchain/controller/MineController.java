package blockchain.controller;

import blockchain.mine.facade.MineFacade;

public class MineController {
    private final MineFacade mainersFacade = MineFacade.getInstance();

    private MineController() {

    }

    private static class MineControllerSingleton {
        private static final MineController instance = new MineController();
    }

    public static MineController getInstance() {
        return MineControllerSingleton.instance;
    }

    /**
     * @param howManyToAdd - Integer that indicates the number of mainers wanted to be added by the client
     * @return integer value that indicates how many mainers were actually added.
     */
    public int addNewMainers(int howManyToAdd) {
        return mainersFacade.addNewMainers(howManyToAdd);
    }


    /**
     * @param howManyToRemove - Integer that indicates the number of mainers wanted to be removed by the client
     * @return integer value that shows how many of removers where deleted
     */
    public int removeMainers(int howManyToRemove) {
        return mainersFacade.removeMainers(howManyToRemove);
    }

    /**
     * Removes all mainers and stop their work.
     */
    public void removeAllMainers() {
        mainersFacade.removeAllMainers();
    }
}

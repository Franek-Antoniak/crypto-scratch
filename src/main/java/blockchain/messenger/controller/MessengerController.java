package blockchain.messenger.controller;

public class MessengerController {

    private MessengerController() {

    }

    private static class MessengerControllerSingleton {
        private static final MessengerController instance = new MessengerController();
    }
    public static MessengerController getInstance() {
        return MessengerControllerSingleton.instance;
    }

    public void sendNewMessage(String message) {

    }
}

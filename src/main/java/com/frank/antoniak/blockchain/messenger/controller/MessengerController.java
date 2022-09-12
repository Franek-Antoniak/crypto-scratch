package com.frank.antoniak.blockchain.messenger.controller;

import com.frank.antoniak.blockchain.messenger.Messenger;

public class MessengerController {
    private final Messenger messenger = Messenger.getInstance();

    private MessengerController() {

    }

    public static MessengerController getInstance() {
        return MessengerControllerSingleton.instance;
    }

    public void sendNewMessage(String message) {
        messenger.addMessage(message);
    }

    private static class MessengerControllerSingleton {
        private static final MessengerController instance = new MessengerController();
    }
}

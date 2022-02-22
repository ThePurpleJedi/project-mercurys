package com.mercurys;

import com.mercurys.handlers.ServerSocketHandler;

import java.io.IOException;

public class MercurysServer {

    private ServerSocketHandler serverSocketHandler;

    private MercurysServer(final int port) {
        try {
            serverSocketHandler = new ServerSocketHandler(port);
        } catch (final IOException e) {
            System.out.println("Exception occurred!");
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        final MercurysServer server = new MercurysServer(4444);
        server.startTalking();
        System.out.println("Thank you for using Project Mercurys!");
    }

    private void startTalking() {
        try {
            serverSocketHandler.talkToClient();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}

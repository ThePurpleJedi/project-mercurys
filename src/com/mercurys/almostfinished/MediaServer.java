package com.mercurys.almostfinished;

import java.io.IOException;

public class MediaServer {

    private ServerSocketHandler serverSocketHandler;

    private MediaServer(final int port) {
        try {
            serverSocketHandler = new ServerSocketHandler(port);
        } catch (final IOException e) {
            System.out.println("Exception occurred!");
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        final MediaServer mediaServer = new MediaServer(4444);
        mediaServer.startTalking();
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

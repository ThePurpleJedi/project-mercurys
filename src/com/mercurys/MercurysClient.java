package com.mercurys;

import com.mercurys.handlers.ClientSocketHandler;

import java.io.IOException;
import java.util.Scanner;

public class MercurysClient {

    ClientSocketHandler clientSocketHandler;

    private MercurysClient(final String address, final int port) {
        try {
            clientSocketHandler = new ClientSocketHandler(address, port);
        } catch (final IOException u) {
            u.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        final MercurysClient client = new MercurysClient(getHostAddress(), 4444);
        client.startTalking();
        System.out.println("Thank you for using Project Mercurys!");
    }

    private static String getHostAddress() {
        final Scanner sc = new Scanner(System.in);
        System.out.println("Enter server host address [IP] or press 1 for localhost:");
        final String hostAddress = sc.next();
        return (hostAddress.equals("1"))? "192.168.0.151" : hostAddress;
    }

    private void startTalking() {
        try {
            clientSocketHandler.talkToServer();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
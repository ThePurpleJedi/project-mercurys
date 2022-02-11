package com.mercurys;

import com.mercurys.encryption.Encryption;
import com.mercurys.threads.ReadThread;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TextClient {

    private Socket socket;
    private DataOutputStream outputStream;

    private TextClient(final String address, final int port) {
        try {
            this.socket = new Socket(address, port);
            System.out.println("Connected to server!" + this.socket.getRemoteSocketAddress());

            this.outputStream = new DataOutputStream(this.socket.getOutputStream());

        } catch (final IOException u) {
            System.out.println("Woops!");
            u.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        final TextClient client = new TextClient(getHostAddress(), 4444);
        client.talk();
        System.out.println("Thank you for using Project Mercurys!");
    }

    private static String getHostAddress() {
        final Scanner sc = new Scanner(System.in);
        System.out.println("Enter server host IP address or press 1 for default address:");
        final String hostAddress = sc.next();
        return (hostAddress.equals("1"))? "192.168.0.151" : hostAddress;
    }

    private void talk() {
        try {
            final ReadThread readThread = new ReadThread(this.socket,
                    "[MHost" + this.socket.getLocalAddress().toString() + "]");

            readThread.start();
            this.writeToServer();

            this.closeConnection(readThread);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToServer() {
        final Encryption encryption = new Encryption();
        final PrintWriter writer = new PrintWriter(this.outputStream, true);
        final Scanner scanner = new Scanner(System.in);
        String outGoingLine = "";

        while (!outGoingLine.equals("-x-")) {
            outGoingLine = scanner.nextLine();
            writer.println(encryption.encrypt(outGoingLine));
        }
        writer.println(encryption.encrypt("Connection closed by client."));
    }

    private void closeConnection(final ReadThread readThread) throws IOException {
        readThread.exitThread();
        this.socket.close();
        this.outputStream.close();
        System.out.println("Closing connection... Goodbye!");
    }
}
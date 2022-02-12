package com.mercurys;

import com.mercurys.encryption.Encryption;
import com.mercurys.threads.ReadThread;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TextServer {

    private Socket socket;
    private DataOutputStream outputStream;

    private TextServer(final int port) {
        try (final ServerSocket serverSocket = new ServerSocket()) {
            String hostAddress = "192.168.0.151"; //replace with one's own LAN IP address
            serverSocket.bind(new InetSocketAddress(hostAddress, port));
            System.out.println("""
                    Server initialised.
                    Waiting for client...""");

            this.socket = serverSocket.accept();
            System.out.println("Client accepted!");
            this.outputStream = new DataOutputStream(this.socket.getOutputStream());

        } catch (final IOException e) {
            System.out.println("Woops!");
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        final TextServer textServer = new TextServer(4444);
        textServer.talk();

        System.out.println("Thank you for using Project Mercurys!");
    }

    private void talk() {
        try {
            final ReadThread readThread = new ReadThread(this.socket,
                    "[MClient1" + this.socket.getInetAddress().toString() + "]");

            readThread.start();
            this.writeToClient();

            this.closeConnection(readThread);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToClient() {
        final Encryption encryption = new Encryption();
        final PrintWriter writer = new PrintWriter(this.outputStream, true);
        final Scanner scanner = new Scanner(System.in);
        String outGoingLine = scanner.nextLine();

        while (!outGoingLine.equals("-x-")) {
            writer.println(encryption.encrypt(outGoingLine));
            outGoingLine = scanner.nextLine();
        }
        writer.println(encryption.encrypt("Connection closed by server."));
    }

    private void closeConnection(final ReadThread readThread) throws IOException {
        readThread.exitThread();
        this.socket.close();
        this.outputStream.close();
        System.out.println("Closing connection... Goodbye!");
    }
}
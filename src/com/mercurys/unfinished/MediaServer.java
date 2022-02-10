package com.mercurys.unfinished;

import com.mercurys.encryption.Encryption;
import com.mercurys.threads.ReadThread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MediaServer {

    private Socket socket;
    private DataOutputStream outputStream;

    private MediaServer(final int port) {
        try (final ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress("localhost", port));
            System.out.println("""
                    Server initiated.
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
        final MediaServer mediaServer = new MediaServer(4444);
        mediaServer.talk();

        System.out.println("Thank you for using Project Iris!");
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

    private void talk() {
        try {
            final ReadThread readThread = new ReadThread(this.socket, "client1" + this.socket.getLocalAddress().toString());

            readThread.start();
            this.writeToClient();

            this.closeConnection(readThread);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}

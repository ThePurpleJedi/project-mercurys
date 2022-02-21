package com.mercurys.almostfinished;

import com.mercurys.threads.ReadMediaThread;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServerSocketHandler {

    private final ServerSocket serverSocket;
    private Socket socket;
    private DataOutputStream outputStream;
    private MessageSender messageSender;
    private ReadMediaThread incomingReaderThread;

    public ServerSocketHandler(int serverPort) throws IOException {
        serverSocket = new ServerSocket();
        initialiseServerSocket(serverPort);
        acceptClientRequest();
        initialiseIO();
    }

    private void initialiseServerSocket(int serverPort) throws IOException {
        String hostAddress = "192.168.0.151"; //replace with one's own LAN IP address
        serverSocket.bind(new InetSocketAddress(hostAddress, serverPort));
        System.out.println("""
                Server initiated.
                Waiting for client...""");
    }

    private void acceptClientRequest() throws IOException {
        this.socket = serverSocket.accept();
        System.out.println("Client accepted!");
    }

    private void initialiseIO() throws IOException {
        this.outputStream = new DataOutputStream(this.socket.getOutputStream());
        PrintWriter writer = new PrintWriter(this.outputStream, true);
        messageSender = new MessageSender(new Scanner(System.in), writer, outputStream);
    }

    public void talkToClient() throws IOException {
        initialiseReaderThread();
        messageSender.sendMessagesToOtherUser();
        closeConnection();
    }

    private void initialiseReaderThread() throws IOException {
        incomingReaderThread = new ReadMediaThread(this.socket.getInputStream(),
                "M_Client");
        incomingReaderThread.start();
    }

    private void closeConnection() throws IOException {
        incomingReaderThread.exitThread();
        this.socket.close();
        this.outputStream.close();
        System.out.println("Closing connection... Goodbye!");
    }
}

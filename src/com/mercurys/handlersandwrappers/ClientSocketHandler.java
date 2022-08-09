package com.mercurys.handlersandwrappers;

import com.mercurys.readers.MediaReaderThread;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientSocketHandler {

    private Socket socket;

    private final DeviceWrapper clientDeviceWrapper;
    private DataOutputStream outputStream;
    private MediaReaderThread incomingReaderThread;
    private MessageHandler messageHandler;

    public ClientSocketHandler(DeviceWrapper clientDeviceWrapper) throws IOException {
        this.clientDeviceWrapper = clientDeviceWrapper;
        connectToServer();
        initialiseIO();
    }

    private void connectToServer() throws IOException {
        this.socket = new Socket(clientDeviceWrapper.getDeviceIP(), clientDeviceWrapper.getDevicePort());
        System.out.println("Connected to server!");
    }

    private void initialiseIO() throws IOException {
        this.outputStream = new DataOutputStream(this.socket.getOutputStream());
        PrintWriter writer = new PrintWriter(this.outputStream, true);
        messageHandler = new MessageHandler(new Scanner(System.in), writer, outputStream);
        messageHandler.setReceiverName(clientDeviceWrapper.getDeviceName());
    }

    public void talkToServer() throws IOException {
        initialiseReaderThread();
        messageHandler.sendMessagesToOtherUser();
        closeConnection();
    }

    private void initialiseReaderThread() throws IOException {
        incomingReaderThread = new MediaReaderThread(this.socket.getInputStream());
        incomingReaderThread.start();
    }

    private void closeConnection() throws IOException {
        incomingReaderThread.exitThread();
        this.socket.close();
        this.outputStream.close();
        System.out.println("Closing connection... Goodbye!");
    }
}

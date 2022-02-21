package com.mercurys.readers;

import com.mercurys.encryption.Decryption;

import java.io.*;
import java.net.*;

public class TextReaderThread extends Thread {

    private final String sentBy;
    private final Decryption decryption = new Decryption();
    private DataInputStream clientStream;
    private boolean exit;

    public TextReaderThread(final Socket socket, final String sentBy) throws IOException {
        if (!socket.isClosed()) {
            this.clientStream = new DataInputStream(socket.getInputStream());
        }
        this.sentBy = sentBy;
        this.exit = false;
    }

    @Override
    public void run() {
        try {
            if (this.exit)
                exitThread();
            decryptAndPrintIncomingMessages();
            this.exitThread();
        } catch (final SocketException s) {
            System.exit(0);
        } catch (final IOException e) {
            System.out.println("Interrupted sleep");
        }
    }

    private void decryptAndPrintIncomingMessages() throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(this.clientStream));
        String inBoundLine;
        while ((inBoundLine = reader.readLine()) != null && !inBoundLine.equals("-x-")) {
            System.out.println(this.sentBy + ": " + decryption.decrypt(inBoundLine));
        }
    }

    public void exitThread() {
        this.exit = true;
        this.interrupt();
    }
}

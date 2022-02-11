package com.mercurys.threads;

import com.mercurys.encryption.Decryption;

import java.io.*;
import java.net.*;

public class ReadThread extends Thread {

    private final String sentBy;
    private DataInputStream clientStream;
    private boolean exit;

    public ReadThread(final Socket socket, final String sentBy) throws IOException {
        if (!socket.isClosed()) {
            this.clientStream = new DataInputStream(socket.getInputStream());
        }
        this.sentBy = sentBy;
        this.exit = false;
    }

    @Override
    public void run() {
        final Decryption decryption = new Decryption();
        try {
            if (this.exit) {
                this.interrupt();
            }

            final BufferedReader reader = new BufferedReader(new InputStreamReader(this.clientStream));
            String inBoundLine = "";

            while (!inBoundLine.equals("-x-")) {
                inBoundLine = reader.readLine();
                if (inBoundLine == null) {
                    break;
                }
                System.out.println(this.sentBy + ": " + decryption.decrypt(inBoundLine));
            }
            this.exitThread();

        } catch (final SocketException s) {
            System.exit(0);
        } catch (final IOException e) {
            System.out.println("Interrupted sleep");
        }
    }

    public void exitThread() {
        this.exit = true;
        this.interrupt();
    }
}

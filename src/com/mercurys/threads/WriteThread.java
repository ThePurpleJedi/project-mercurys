package com.mercurys.threads;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread {

    private final DataOutputStream outputStream;
    private final Scanner scanner;
    private boolean isActive = true;

    public WriteThread(final Socket socket, final Scanner scanner) throws IOException {
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.scanner = scanner;
    }

    @Override
    public void run() {
        try {
            final PrintWriter writer = new PrintWriter(this.outputStream);
            String outGoingLine = this.scanner.nextLine();

            while (outGoingLine != null) {
                writer.println(outGoingLine);
                outGoingLine = this.scanner.nextLine();

                if (outGoingLine.equals("~~~")) {
                    this.exitThread();
                    break;
                }
            }
            Thread.sleep(1000);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void exitThread() {
        this.interrupt();
        this.isActive = false;
    }

    public boolean isActive() {
        return this.isActive;
    }
}

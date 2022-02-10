package com.mercurys.threads;

import com.mercurys.encryption.Decryption;
import com.mercurys.unfinished.Imagician;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ReadMediaThread extends Thread {

    private final String sentBy;
    private DataInputStream clientStream;
    private boolean exit;

    public ReadMediaThread(final Socket socket, final String sentBy) throws IOException {
        if (!socket.isClosed()) {
            this.clientStream = new DataInputStream(socket.getInputStream());
        }
        this.sentBy = sentBy;
        this.exit = false;
    }

    @Override
    public void run() {
        final Decryption decryption = new Decryption();
        final Scanner sc = new Scanner(System.in);
        final Imagician imagician = new Imagician();
        int i = 1;
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
                if (decryption.decrypt(inBoundLine).startsWith("/image")) {
                    System.out.println("Note: The user is attempting to send you an image file. " +
                            "Do you wish to download it? [Y/n]");
                    final String perm = sc.next();
                    if (perm.equals("Y") || perm.equals("y")) {
                        final String downloadPath = System.getProperty("user.home") + "/ReceivedImage%d.jpg".formatted(i++);
                        imagician.convertTextToImage(inBoundLine.split(" +")[1], downloadPath);
                        //What's the best way to get the download path
                        System.out.println("The file has been downloaded, please check your images folder");
                    }
                }
                System.out.println(this.sentBy + ": " + decryption.decrypt(inBoundLine));
            }
            this.exitThread();

        } catch (final SocketException s) {
            System.out.println("Socket is closed");
            System.exit(0);
        } catch (final IOException e) {
            System.out.println("Interrupted sleep");
        }
    }

    private void exitThread() {
        this.exit = true;
        this.interrupt();
    }
}

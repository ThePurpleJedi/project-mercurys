package com.mercurys;

import com.mercurys.handlersandwrappers.*;

import java.io.IOException;
import java.util.Scanner;

public class MercurysServer {

    private ServerSocketHandler serverSocketHandler;
    Scanner sc = new Scanner(System.in);

    private MercurysServer(int port) {
        try {
            DeviceWrapper deviceWrapper = createServerWrapper(port);
            serverSocketHandler = new ServerSocketHandler(deviceWrapper);
        } catch (final IOException e) {
            System.out.println("Exception occurred!");
            e.printStackTrace();
        }
    }

    private DeviceWrapper createServerWrapper(int port) {
        DeviceWrapper serverDeviceWrapper = new DeviceWrapper();
        serverDeviceWrapper.setDeviceIP(getIPAddress());
        serverDeviceWrapper.setDevicePort(port);
        serverDeviceWrapper.setDeviceName(getServerName());
        return serverDeviceWrapper;
    }

    private String getIPAddress() {
        System.out.println("Enter your device's LAN IP address: ");
        return sc.nextLine();
    }

    private String getServerName() {
        System.out.println("Enter server name: ");
        return sc.nextLine();
    }

    public static void main(final String[] args) {
        final MercurysServer server = new MercurysServer(4444);
        server.startTalking();
        System.out.println("Thank you for using Project Mercurys!");
    }

    private void startTalking() {
        try {
            serverSocketHandler.talk();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}

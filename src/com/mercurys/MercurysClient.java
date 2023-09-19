package com.mercurys;

import com.mercurys.handlersandwrappers.ClientSocketHandler;
import com.mercurys.handlersandwrappers.DeviceWrapper;

import java.io.IOException;
import java.util.Scanner;

public class MercurysClient {

    ClientSocketHandler clientSocketHandler;
    Scanner sc = new Scanner(System.in);

    private MercurysClient(final int port) {
        try {
            DeviceWrapper deviceWrapper = createServerWrapper(port);
            clientSocketHandler = new ClientSocketHandler(deviceWrapper);
        } catch (final IOException u) {
            u.printStackTrace();
        }
    }

    private DeviceWrapper createServerWrapper(int port) {
        DeviceWrapper deviceWrapper = new DeviceWrapper();
        deviceWrapper.setDeviceIP(getHostAddress());
        deviceWrapper.setDevicePort(port);
        deviceWrapper.setDeviceName(getUsername());
        return deviceWrapper;
    }

    private String getHostAddress() {
        System.out.println("Enter server host address [IP]:");
        final String hostAddress = new Scanner(System.in).next();
        return (hostAddress.equals("1"))? "192.168.0.151" : hostAddress;
    }

    private String getUsername() {
        System.out.println("Enter unique username: ");
        return sc.nextLine();
    }

    public static void main(final String[] args) {
        final MercurysClient client = new MercurysClient(4444);
        client.startTalking();
        System.out.println("Thank you for using Project Mercurys!");
    }

    private void startTalking() {
        try {
            clientSocketHandler.talkToServer();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
package com.aueb.opabus.CodeFolder;

import com.aueb.opabus.CodeFolder.DataTypes.Topic;

import java.util.Scanner;

public class TestSub {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Subscriber su = new Subscriber("192.168.1.103",4202);
        su.setBrokerIp("192.168.1.103");
        su.setBrokerport(4202);
        su.EstablishConnection();
        su.disconnect();
        System.out.print("Find new bus y/n: ");
        String option=in.nextLine();
        while (option.equals("y")||option.equals("Y")){
            su.printAvailableBusLines();
            System.out.print("Please enter BusLine: ");
            String busline= in.nextLine();
            Thread t = new Thread(()->{
                System.out.println("searching for: "+ busline);
                su.register(new Topic(busline));
            });
            t.start();
            System.out.println("Find new bus y/n: ");
            option=in.nextLine();
        }
    }
}

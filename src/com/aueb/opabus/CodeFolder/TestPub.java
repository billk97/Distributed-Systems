package com.aueb.opabus.CodeFolder;

public class TestPub {
    public static void main(String[] args) {
        //com.aueb.opabus.CodeFolder.Publisher p1 = new com.aueb.opabus.CodeFolder.Publisher(4201,"192.168.1.65",0,20);
        Publisher p1 = new Publisher(4202,"172.16.2.22",0,20);
        p1.setBrokerIp("172.16.2.22");
        p1.setBrokerPort(4202);
        p1.startPublisher();
    }
}


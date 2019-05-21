package com.aueb.opabus.CodeFolder;

public class TestPub {
    public static void main(String[] args) {
        //com.aueb.opabus.CodeFolder.Publisher p1 = new com.aueb.opabus.CodeFolder.Publisher(4201,"192.168.1.65",0,20);
        Publisher p1 = new Publisher(4202,"192.168.1.72",0,20);
        p1.setBrokerIp("192.168.1.72");
        p1.setBrokerPort(4202);
        p1.startPublisher();
    }
}


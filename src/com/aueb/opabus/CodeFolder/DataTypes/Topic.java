package com.aueb.opabus.CodeFolder.DataTypes;

import java.io.Serializable;

public class Topic implements Serializable {
    private static final long serialVersionUID= -7640567997349948895L;
    private String busLine;

    public Topic(String busLine) {
        this.busLine = busLine;
    }

    public String getBusLine() {
        return busLine;
    }

    public void setBusLine(String busLine) {
        this.busLine = busLine;
    }
}

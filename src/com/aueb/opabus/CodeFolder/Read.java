package com.aueb.opabus.CodeFolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Read {
    /**careful the the linux and windows don't share the same path system **/
    private String busLinesPath= "src\\com\\aueb\\opabus\\CodeFolder\\DS_project_dataset\\busLinesNew.txt";
    private String busPositionsPath = "src\\com\\aueb\\opabus\\CodeFolder\\DS_project_dataset\\busPositionsNew.txt";
    private String routeCodesPath = "src\\com\\aueb\\opabus\\CodeFolder\\DS_project_dataset\\RouteCodesNew.txt";
    private String busLineTable[] = new String[3];

    public ArrayList<String []> readBusLines(){
        FileReader fr = null;
        String busLineTable[] = new String[3];
        String word = "";
        ArrayList<String []> busLinesArray = new ArrayList<>();
        try {
            fr= new FileReader(busLinesPath);
            int i;
            int k = 0;
            int j = 0;
            while ((i = fr.read()) != -1) {
                if (i == 44) {
                    busLineTable[k] = word;
                    word = "";
                    k = k + 1;
                } else if (i == 10) {
                    busLineTable[k] = word;
                    busLinesArray.add(busLineTable);
                    busLineTable = new String[3];
                    k = 0;
                    word = "";
                } else {
                    word = word + (char) i;
                }
            }//end while
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return busLinesArray;
    }//end readBusLines


    public ArrayList<String[]> readBusPosition(String lineCode) {
        FileReader fileReader= null;
        String word="";
        String busPositionTable[]= new String[6];
        ArrayList<String[]> busPositionList =new ArrayList<String[]>();
        try {
            fileReader = new FileReader(busPositionsPath);
            int i;
            int k=0;
            int stop=0;
            while ((i = fileReader.read()) != -1) {
                if (i == 44) {
                    busPositionTable[k]=word;
                    word = "";
                    k=k+1;
                } else if(i == 10) {
                    busPositionTable[k]=word;
                    k=0;
                    word= "";
                    if(lineCode.equals(busPositionTable[0])) {
                        busPositionList.add(busPositionTable);
                    }
                    busPositionTable= new String[6];
                    stop=stop+1;
                } else {
                    word = word + (char)i;
                }

            }//end While
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return busPositionList;
    }//end readBusPosition

    public ArrayList<String []> readRouteCodes()  {
        FileReader fileReader= null;
        String word="";
        ArrayList<String []> routeCodesList= new ArrayList<String[]>();
        File file = new File(routeCodesPath);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String routeCodesTable[]= new String[5];
                String line = scanner.nextLine();
                String [] temp =line.split("\\[");
                String[] temp2=temp[0].split(",");
                for(int i=0; i<temp2.length; i++){
                    routeCodesTable[i]=temp2[i];
                }
                if (temp.length==2){
                    temp[1]="["+temp[1];
                    routeCodesTable[4]=temp[1];
                }
                else {
                    routeCodesTable[4]="empty";
                }
                routeCodesList.add(routeCodesTable);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return routeCodesList;
    }//end readRouteCodes



    public void printBusLineList() {
        for(int i=0;i<20;i++){
                System.out.println(readBusLines().get(i)[0]+" "+readBusLines().get(i)[1]+" "+readBusLines().get(i)[2]);
        }
    }



    public void printBusPosition(){
        ArrayList<String []> array = readBusPosition("817");
        for(int i=0;i<=20;i++){
            System.out.println(array.get(i)[0]+" "+array.get(i)[1]+" "+array.get(i)[2]+" "+array.get(i)[3]+" "+readBusPosition("817").get(i)[4]+" "+array.get(i)[5]);
        }
    }

}

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Read {
    private String busLinesPath= "src\\DS_project_dataset\\busLinesNew.txt";
    private String busPositionsPath = "src\\DS_project_dataset\\busPositionsNew.txt";
    private String routeCodesPath = "src\\DS_project_dataset\\RouteCodesNew.txt";
    private String busLineTable[][] = new String[20][3];

    public String[][] readBusLines() throws IOException {
        FileReader fr = new FileReader(busLinesPath);
        String word = "";
        int i;
        int k = 0;
        int j = 0;
        while ((i = fr.read()) != -1) {
            //System.out.print(i+" ");
            if (i == 44) {
                busLineTable[j][k]=word;
                word = "";
                k=k+1;
            } else if(i == 10) {
                busLineTable[j][k]=word;
                j=j+1;
                k=0;
                word= "";
            } else {
                word = word + (char)i;
            }
        }
        return busLineTable;
    }//end readBusLines

    public String[][] getBusLinesTable(){
        return busLineTable;
    }

    public ArrayList<String[]> readBusPosition() {
        FileReader fileReader= null;
        String word="";
        String busPositionTable[]= new String[6];
        ArrayList<String[]> busPositioonList =new ArrayList<String[]>();
        try {
            fileReader = new FileReader(busPositionsPath);
            int i;
            int k=0;
            int stop=0;
            while ((i = fileReader.read()) != -1) {
                //System.out.print(i+" ");
                if (i == 44) {
                    busPositionTable[k]=word;
                    word = "";
                    k=k+1;
                } else if(i == 10) {
                    busPositionTable[k]=word;
                    k=0;
                    word= "";
                    System.out.println("lineid: "+busPositionTable[0]+" routecode: "+busPositionTable[1]+" vehicleid: "+busPositionTable[2]+" latitude: "+busPositionTable[3]+" longtitude: "+busPositionTable[4]+" timestamp: "+busPositionTable[5]);
                    busPositioonList.add(busPositionTable);
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
        return busPositioonList;
    }//end readBusPosition
    ArrayList<String[]> routeCodesList= new ArrayList<String[]>();
    public ArrayList readRouteCodes()  {
        FileReader fileReader= null;
        String word="";
        String routeCodesTable[];

        try {
            fileReader = new FileReader(routeCodesPath);
            int i;
            int k=0;
            int count=0;
            while ((i = fileReader.read()) != -1) {
                routeCodesTable= new String[4];
                //System.out.print(i+" ");
                if (i == 44) {
                    routeCodesTable[k]=word;
                    word = "";
                    k=k+1;
                } else if(i == 10) {
                    routeCodesTable[k]=word;
                    k=0;
                    word= "";
                    System.out.println("routecode: "+routeCodesTable[0]+" linecode: "+routeCodesTable[1]+" routetype: "+routeCodesTable[2]+" descriptionenglish: "+routeCodesTable[3]);
                    routeCodesList.add(count,routeCodesTable);
                    count++;
                } else {
                    word = word + (char)i;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return routeCodesList;
    }//end readRouteCodes


    public void printBusLineTable(){
        for(int i=0;i<busLineTable.length;i++){
                System.out.println(busLineTable[i][0]+" "+busLineTable[i][1]+" "+busLineTable[i][2]);
        }
        //System.out.println("line leoforiou= "+busLineTable[2][0]+"perioxi leof= "+busLineTable[2][3]);

    }

    public static void main(String[] args) throws IOException {
        Read r = new Read();
//        r.readBusLines();
         r.printBusLineTable();
//        r.readBusPosition();
        r.readRouteCodes();
    }
}

import java.io.FileReader;
import java.io.IOException;

public class Read {
    private String busLinesPath= "src\\DS_project_dataset\\busLines.txt";
    private String busPositionsPath = "src\\DS_project_dataset\\busPositions.txt";
    private String routeCodesPath = "src\\DS_project_dataset\\RouteCodes.txt";
    private String busLineTable[][] = new String[20][4];



    public void readBusLines() throws IOException {
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
    }

    public void readBusPosition() throws IOException {
        FileReader fileReader= new FileReader(busPositionsPath);
        String word="";
        String busPositionTable[]= new String[6];
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
                stop=stop+1;
            } else {
                word = word + (char)i;
            }
        }

    }

    public void readRouteCodes() throws IOException {
        FileReader fileReader= new FileReader(routeCodesPath);
        String word="";
        String routeCodesTable[]= new String[5];
        int i;
        int k=0;
        int stop=0;
        while ((i = fileReader.read()) != -1) {
            //System.out.print(i+" ");
            if (i == 44) {
                routeCodesTable[k]=word;
                word = "";
                k=k+1;
            } else if(i == 10) {
                routeCodesTable[k]=word;
                k=0;
                word= "";
                System.out.println("routecode: "+routeCodesTable[0]+" linecode: "+routeCodesTable[1]+" routetype: "+routeCodesTable[2]+" descriptiongreek: "+routeCodesTable[3]+" descriptionenglish: "+routeCodesTable[4]);
                stop=stop+1;
            } else {
                word = word + (char)i;
            }
            if(stop==10){
                break;
            }
        }

    }


    public void printBusLineTable(){
        for(int i=0;i<busLineTable.length;i++){
                System.out.println(busLineTable[i][0]+" "+busLineTable[i][1]+" "+busLineTable[i][2]+" "+busLineTable[i][3]);
        }
        //System.out.println("line leoforiou= "+busLineTable[2][0]+"perioxi leof= "+busLineTable[2][3]);

    }

    public static void main(String[] args) throws IOException {
        Read r = new Read();
        //r.readBusLines();
        //r.printBusLineTable();
        //r.readBusPosition();
        r.readRouteCodes();
    }
}

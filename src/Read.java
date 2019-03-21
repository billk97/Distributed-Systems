import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Read {
    private String busLinesPath= "src\\DS_project_dataset\\busLines.txt";
    private String busLineTable[][] = new String[20][4];

//    public void ReadBusLine() throws FileNotFoundException {
//        File file = new File(busLinesPath);
//        Scanner scanner = new Scanner(file);
//        while(scanner.hasNextByte()){
//            Character word = (char)scanner.nextByte();
//            System.out.println(" "+word);
//        }
//        scanner.close();
//    }


    public void ReadBusLines() throws IOException {
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


    public void printBusLineTable(){
        for(int i=0;i<busLineTable.length;i++){
                System.out.println(busLineTable[i][0]+" "+busLineTable[i][1]+" "+busLineTable[i][2]+" "+busLineTable[i][3]);
        }
        //System.out.println("line leoforiou= "+busLineTable[2][0]+"perioxi leof= "+busLineTable[2][3]);

    }

    public static void main(String[] args) throws IOException {
        Read r = new Read();
        r.ReadBusLines();
        r.printBusLineTable();
    }
}

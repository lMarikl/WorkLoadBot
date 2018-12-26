import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

class TextHelper {

    String getText(String fileName) {
        try{
            FileInputStream fstream = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            while ((strLine = br.readLine()) != null){
                return strLine;
            }
        }catch (IOException e){
            System.out.println("Ошибка");
        }
        return null;
    }
}

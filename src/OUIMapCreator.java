import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OUIMapCreator
{
    public static HashMap<String, String> OUI_VENDOR_MAP = readOuiMap("oui.txt");

    public static void main(String[] args)
    {

        for (Map.Entry entry : OUI_VENDOR_MAP.entrySet()) {
//            if (entry.getKey().toString().length() != 8)
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

//        saveOuiMapToFile(ouiMap);
    }

    public static HashMap<String, String> createHashMap(String fileName) {
        HashMap<String, String> ouiMap = new HashMap<String, String>();
        try
        {
            Scanner s = new Scanner(new File("oui.txt"));
            while (s.hasNextLine())
            {
                String oui = s.next(); s.next();
                String companyName = s.nextLine().trim();

                for (int i = 0; i < (companyName.equals("Private") ? 1 : 4); i++) s.nextLine();

                ouiMap.put(oui, companyName);
            }
        } catch (FileNotFoundException e)
        {
            System.out.println("Error while finding OUI text file.");
            e.printStackTrace();
        }

        return ouiMap;
    }

    public static void saveOuiMapToFile(HashMap<String, String> ouiMap) {
        try {
            FileOutputStream fileOut = new FileOutputStream("ouiMap.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(ouiMap);
            out.close();
            fileOut.close();

            System.out.println("OUI Map has been saved to 'ouiMap.ser'");
        } catch (IOException e) {
            System.out.println("Error saving map to file");
            e.printStackTrace();
        }
    }

    public static HashMap<String, String> readOuiMap(String fileName) {
        HashMap<String, String> ouiMap = null;
        try
        {
            FileInputStream fileIn = new FileInputStream("ouiMap.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ouiMap = (HashMap<String, String>) in.readObject();

            in.close();
            fileIn.close();
        } catch (IOException e) {
            System.out.println("Error reading ouiMap from file.");
            e.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("No class found to correspond with Object read from file.");
            c.printStackTrace();
        }

        return ouiMap;
    }
}

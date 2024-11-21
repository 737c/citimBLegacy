package org.citmb2.citmb;

import java.io.*;

public class citm_stdio
{
    public static void saveFile(String fileName, Object obj)throws IOException
    {
        //String cd = System.getProperty("user.dir");
        //cd = cd + "\\plugins\\Citm2B\\";
        //String cd2 = new File(".").getAbsolutePath();
        //Bukkit.getServer().getConsoleSender().sendMessage("Dir :"+cd+fileName);

        String cd = "plugins\\Citm2B\\";

        FileOutputStream fos = new FileOutputStream(cd+fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.close();
    }

    public static Object loadFile(String fileName) throws IOException, ClassNotFoundException
    {
        //String cd = System.getProperty("user.dir");
        // cd = cd + "\\plugins\\Citm2B\\";

        String cd = "plugins\\Citm2B\\";

        //Bukkit.getServer().getConsoleSender().sendMessage("Dir :"+cd+"/plugins/Citm2B/"+fileName);
        FileInputStream fin = new FileInputStream(cd+fileName);
        ObjectInputStream ois = new ObjectInputStream(fin);
        Object obj= ois.readObject();
        ois.close();

        return obj;
    }
}

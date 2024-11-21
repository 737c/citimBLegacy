package org.citmb2.citmb;

import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.UUID;

public class citmSystem
{
    public static citmUserSave getUserSaveData(UUID playerID)
    {
        String fileName = "userData\\"+playerID+".data";
        citmUserSave userSaveData = new citmUserSave();
        userSaveData.pUID = null;
        try
        {
            userSaveData = (citmUserSave) citm_stdio.loadFile(fileName);
        }
        catch (IOException e)
        {
            return null;
        }
        catch (ClassNotFoundException e)
        {
            return null;
        }
        return userSaveData;
    }
    
    public static boolean setUserSaveData(UUID playerID, citmUserSave saveData)
    {
        String fileName = "userData\\"+playerID+".data";

        try
        {
            citm_stdio.saveFile(fileName, saveData);
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }
}

package org.citmb2.citmb.CitmScript.dummy;

public class dummy
{
    public static int[] aad_getPlayerAxisXZ()
    {
        int[] playerXZ = new int[2];
        playerXZ[0] = 851;
        playerXZ[1] = -5275;
        // 1:(848,-5264) 2:(863,-5249)

        return playerXZ;
    }
    public static void aad_applyPUBArea(int axisX1,int axisZ1,int axisX2,int axisZ2,int middleGroundY)
    {
        System.out.println(
            "axisX1: "+axisX1+"\n"+
            "axisZ1: "+axisZ1+"\n"+
            "axisX2: "+axisX2+"\n"+
            "axisZ2: "+axisZ2+"\n"+
            "mgY: "+middleGroundY
            );
    }

    public static boolean[] aad_getSolidStateOnPoint(int axisX,int axisZ)
    {
        boolean[] stateArray = new boolean[256];
        for (int i = 0; i < 256; i++)
        {
            stateArray[i] = false;
        }

        stateArray[79] = false;
        stateArray[78] = true;
        stateArray[77] = false;
        stateArray[76] = false;
        stateArray[75] = true;
        stateArray[74] = false;
        stateArray[73] = true;
        stateArray[72] = true;
        stateArray[71] = true;
        stateArray[70] = true;

        return stateArray;
    }

    public static void aad_sendSMG(String str)
    {
        System.out.println(str);
    }
}

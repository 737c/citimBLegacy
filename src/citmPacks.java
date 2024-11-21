package org.citmb2.citmb;

public class citmPacks
{
    public static void adminTools_areaAssignTool_assignPUB(String areaName,int pChunk_chunkX, int pChunk_chunkZ)
    {
        int XOperator = 1;
        if (pChunk_chunkX < 0)
        {
            XOperator = -1;
        }
        int ZOperator = 1;
        if (pChunk_chunkZ < 0)
        {
            ZOperator = -1;
        }

        int pChunk_chunkAX = pChunk_chunkX * XOperator;
        int pChunk_chunkAZ = pChunk_chunkZ * ZOperator;

        // get chunk axis XZ
        int pChunk_axisX1 = pChunk_chunkAX*16;
        int pChunk_axisZ1 = pChunk_chunkAZ*16;
        int pChunk_axisX2 = ((pChunk_chunkAX+1)*16)-1;
        int pChunk_axisZ2 = ((pChunk_chunkAZ+1)*16)-1;

        // fix the operand
        pChunk_axisX1 = pChunk_axisX1 * XOperator;
        pChunk_axisZ1 = pChunk_axisZ1 * ZOperator;
        pChunk_axisX2 = pChunk_axisX2 * XOperator;
        pChunk_axisZ2 = pChunk_axisZ2 * ZOperator;

        boolean[][] blockSolidState = new boolean[4][];

        blockSolidState[0] = adaptor.aad_getSolidStateOnPoint(pChunk_axisX1,pChunk_axisZ1);
        blockSolidState[1] = adaptor.aad_getSolidStateOnPoint(pChunk_axisX1,pChunk_axisZ2);
        blockSolidState[2] = adaptor.aad_getSolidStateOnPoint(pChunk_axisX2,pChunk_axisZ1);
        blockSolidState[3] = adaptor.aad_getSolidStateOnPoint(pChunk_axisX2,pChunk_axisZ2);

        int chunkGroundX1Z1 = citmPacks.getGroundYHeight(blockSolidState[0]);
        int chunkGroundX1Z2 = citmPacks.getGroundYHeight(blockSolidState[1]);
        int chunkGroundX2Z1 = citmPacks.getGroundYHeight(blockSolidState[2]);
        int chunkGroundX2Z2 = citmPacks.getGroundYHeight(blockSolidState[3]);

        int middleGroundY = (chunkGroundX1Z1+chunkGroundX1Z2+chunkGroundX2Z1+chunkGroundX2Z2)/4;

        adaptor.aad_applyPUBArea(areaName,pChunk_axisX1,pChunk_axisZ1,pChunk_axisX2,pChunk_axisZ2,middleGroundY);
    }

    public static void adminTools_areaAssignTool_deletePUBChunk(int pChunk_chunkX, int pChunk_chunkZ)
    {
        adaptor.aad_deletePUB(pChunk_chunkX*16, pChunk_chunkZ*16);
    }
    public static int[] adminTools_areaAssignTool_convertToChunk(int axisX, int axisZ)
    {
        int[] chunkAxisXZ = new int[2];
        chunkAxisXZ[0] = axisX/16;
        chunkAxisXZ[1] = axisZ/16;

        return chunkAxisXZ;
    }
    public static int getGroundYHeight(boolean[] blockSolidState)
    {
        // find a solid block that goes 5blocks in a raw(and assumes that's a ground)
        int blockCount = 0;
        int blockY = -1;

        for (int i = 255; i >= 0; i--)
        {
            if (blockSolidState[i])
            {
                blockCount++;
            }
            else
            {
                //reset the count when it hits air block
                blockCount = 0;
            }

            if (blockCount == 5)
            {
                // calc a top block of the stable ground
                blockY = i + (blockCount -1);
                break;
            }
        }

        // if this couldn't find andy ground, replace with 64, just uin case
        if (blockY == -1)
        {
            blockY = 64;
        }

        return blockY;
    }

}

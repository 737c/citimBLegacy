package org.citmb2.citmb;

public class adminTools
{
    /*


    public static void adminTools_areaAssignTool_apcTest()
    {
        int playerAxisXZ[] = adaptor.aad_getPlayerAxisXZ();
        int[] cAxisXZ = citmPacks.adminTools_areaAssignTool_convertToChunk(playerAxisXZ[0], playerAxisXZ[1]);
        citmPacks.adminTools_areaAssignTool_assignPUB("test",cAxisXZ[0], cAxisXZ[1]);
    }
    public static void adminTools_areaAssignTool_deletePubArea(int axisX, int axisY, int sizeX, int sizeZ)
    {
        int[] cAxisXZ1 = citmPacks.adminTools_areaAssignTool_convertToChunk(axisX, axisY);
        int[] cAxisXZ2 = citmPacks.adminTools_areaAssignTool_convertToChunk(axisX+sizeX, axisY+sizeZ);

        int cSizeX = cAxisXZ2[0] - cAxisXZ1[0];
        int cSizeZ = cAxisXZ2[1] - cAxisXZ1[1];
        int cSizeSq = cSizeX * cSizeZ;

        for (int cZi = 0; cZi < cSizeZ; cZi++)
        {
            for (int cXi = 0; cXi < cSizeX; cXi++)
            {
                citmPacks.adminTools_areaAssignTool_deletePUBChunk(
                        cAxisXZ1[0]+cXi,
                        cAxisXZ1[1]+cZi
                );
            }
        }
    }
    // multiple chunks will be assigned
    public static void adminTools_areaAssignTool_mcaa(int axisX, int axisY, int sizeX, int sizeZ)
    {
        int[] cAxisXZ1 = citmPacks.adminTools_areaAssignTool_convertToChunk(axisX, axisY);
        int[] cAxisXZ2 = citmPacks.adminTools_areaAssignTool_convertToChunk(axisX+sizeX, axisY+sizeZ);

        int cSizeX = cAxisXZ2[0] - cAxisXZ1[0];
        int cSizeZ = cAxisXZ2[1] - cAxisXZ1[1];
        int cSizeSq = cSizeX * cSizeZ;

        int newPUBID = 0//shareFanc.getConfigInt("newPUBID");

        for (int cZi = 0; cZi < cSizeZ; cZi++)
        {
            for (int cXi = 0; cXi < cSizeX; cXi++)
            {
                citmPacks.adminTools_areaAssignTool_assignPUB(
                        "pub-"+newPUBID,
                        cAxisXZ1[0]+cXi,
                        cAxisXZ1[1]+cZi
                );

                newPUBID++;
            }
        }

        shareFanc.setConfigInt("newPUBID",newPUBID);
    }
    public static void addLandSq(UUID uuid_pid, String sqValueString)
    {
        int sqValue = Integer.parseInt(sqValueString);
        userOnlineData playerOnline = citmUDB.getPlayerOnlineData(uuid_pid);
        if ((playerOnline.usData.PRISqAll += sqValue) > 0)
        {
            playerOnline.usData.PRISqAll +=  sqValue;
            citmUDB.setPlayerOnlineData(uuid_pid, playerOnline);
        }


    }
    public static void adminTools_areaAssignTool_PRI(UUID uuid_pid, boolean adminMode, int axisX, int axisZ, int sizeX, int sizeZ)
    {
        Player player = Bukkit.getPlayer(uuid_pid);

        // find that the area is within PUB area
        if (!adaptor.checkWithinPUB(axisX, axisZ, sizeX, sizeZ))
        {
            // return message "out of world bounce"
            return;
        }

        // find pre occupated PRI
        List<String> foundPRI = adaptor.findPRI(axisX, axisZ, sizeX, sizeZ);
        if (0 < foundPRI.size())
        {
            // return message "there is pre occupated PRI"
            return;
        }

        // get PUB middle ground
        List<String>[] regionID = new List[4];
        regionID[0] = adaptor.findPUB(axisX, axisZ,1,1);
        regionID[1] = adaptor.findPUB(axisX, (axisZ+sizeZ)-1,1,1);
        regionID[2] = adaptor.findPUB((axisX+sizeX)-1, axisZ,1,1);
        regionID[3] = adaptor.findPUB((axisX+sizeX)-1, (axisZ+sizeZ)-1,1,1);

        World world = player.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));

        int[] PUBmidY = new int[4];
        for (int i = 0; i < 4; i++)
        {
            PUBmidY[i] = regions.getRegion(regionID[i].get(0)).getMinimumPoint().getBlockY();
        }
        int PRIMidY = (PUBmidY[0]+PUBmidY[1]+PUBmidY[2]+PUBmidY[3])/4;

        BlockVector3 cMin = BlockVector3.at(axisX, PRIMidY -2,axisZ);
        BlockVector3 cMax = BlockVector3.at(axisX+sizeX, PRIMidY +6,axisZ+sizeZ);

        Plugin plugin = getServer().getPluginManager().getPlugin("CitmPlugin");
        int newPRIID = shareFanc.getConfigInt("newPRIID");

        String areaName = "pri-" + newPRIID;
        ProtectedRegion region = new ProtectedCuboidRegion(areaName, cMin, cMax);
        region.setPriority(20);
        //region.setFlag(Flags.BUILD, StateFlag.State.ALLOW);

        DefaultDomain members = region.getMembers();
        members.addPlayer(uuid_pid);
        region.setMembers(members);

        regions.addRegion(region);

        shareFanc.setConfigInt("newPRIID", newPRIID+1);
    }
    public static void adminTools_areaAssignTool_deletePRIArea(UUID PlayerID, int axisX, int axisZ, int sizeX, int sizeZ)
    {
        Player player = Bukkit.getPlayer(PlayerID);

        List<String> foundPRI = adaptor.findPRI(axisX, axisZ, sizeX, sizeZ);
        World world = player.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));

        String regionID = foundPRI.get(0);
        ProtectedRegion region = regions.getRegion(regionID);
        if (region.getOwners().getUniqueIds().contains(PlayerID))
        {
            regions.removeRegion(regionID);
        }
    }

     */
}

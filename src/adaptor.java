package org.citmb2.citmb;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class adaptor
{
    public static int[] aad_getPlayerAxisXZ()
    {
        Player player = Bukkit.getPlayer("lll0_0lll");

        int[] playerXZ = new int[2];
        playerXZ[0] = player.getLocation().getBlockX();
        playerXZ[1] = player.getLocation().getBlockZ();

        return playerXZ;
    }
    public static void aad_applyPUBArea(String areaName,int axisX1,int axisZ1,int axisX2,int axisZ2,int middleGroundY)
    {
        Player player = Bukkit.getPlayer("lll0_0lll");

        BlockVector3 cMin = BlockVector3.at(axisX1, middleGroundY +0,axisZ1);
        BlockVector3 cMax = BlockVector3.at(axisX2,middleGroundY +3,axisZ2);

        World world = player.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));

        // find already assigned Pub
        if (0 < findPUB(cMin.getX(), cMin.getZ(), (cMax.getX() - cMin.getX())+1, (cMax.getZ() - cMin.getZ())+1).size())
        {
            return;
        }

        ProtectedRegion region = new ProtectedCuboidRegion(areaName, cMin, cMax);
        region.setPriority(10);
        region.setFlag(Flags.BUILD, StateFlag.State.ALLOW);
        regions.addRegion(region);
    }

    public static List<String> findPUB(int axisX,int axisZ, int sizeX, int sizeZ)
    {
        return findAnyArea(10, axisX, axisZ, sizeX, sizeZ);
    }

    public static List<String> findAnyArea(int priority,int axisX,int axisZ, int sizeX, int sizeZ)
    {
        Player player = Bukkit.getPlayer("lll0_0lll");

        World world = player.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));

        BlockVector3 cMin = BlockVector3.at(axisX, 0,axisZ);
        BlockVector3 cMax = BlockVector3.at(axisX+sizeX,256,axisZ+sizeZ);
        ProtectedRegion sRegion = new ProtectedCuboidRegion("searchRegion", cMin, cMax);

        Set<ProtectedRegion> foundResult = regions.getApplicableRegions(sRegion).getRegions();
        Iterator<ProtectedRegion> foundResult_Iterator = foundResult.iterator();

        List<String> AID_List = new ArrayList<String>();
        while (foundResult_Iterator.hasNext())
        {
            ProtectedRegion iRegion = foundResult_Iterator.next();
            if (iRegion.getPriority() == priority)
            {
                AID_List.add(iRegion.getId());
            }
        }

        return  AID_List;
    }

    public static Iterator<ProtectedRegion> findPUB2(int axisX1,int axisZ1)
    {
        Player player = Bukkit.getPlayer("lll0_0lll");

        World world = player.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));

        BlockVector3 cMin = BlockVector3.at(axisX1, 0,axisZ1);
        BlockVector3 cMax = BlockVector3.at(axisX1,256,axisZ1);
        ProtectedRegion sRegion = new ProtectedCuboidRegion("searchRegion", cMin, cMax);

        Set<ProtectedRegion> foundResult = regions.getApplicableRegions(sRegion).getRegions();
        Iterator<ProtectedRegion> foundResult_Iterator = foundResult.iterator();

        return  foundResult_Iterator;
    }

    public static List<String> findPRI(int axisX,int axisZ, int sizeX, int sizeZ)
    {
        return findAnyArea(20, axisX, axisZ, sizeX, sizeZ);
    }

    public static boolean checkWithinPUB(int axisX, int axisZ, int sizeX, int sizeZ)
    {
        List<String>[] XZPUBArea = new List[4];
        XZPUBArea[0] = findAnyArea(10, axisX, axisZ, 1, 1);
        XZPUBArea[1] = findAnyArea(10, axisX, axisZ+sizeX, 1, 1);
        XZPUBArea[2] = findAnyArea(10, axisX+sizeX, axisZ, 1, 1);
        XZPUBArea[3] = findAnyArea(10, axisX+sizeX, axisZ+sizeZ, 1, 1);

        for (int i=0; i < 4; i++)
        {
            if (XZPUBArea[i].size() == 0)
            {
               return false;
            }
        }
        return true;
    }

    public static void aad_deletePUB(int axisX1,int axisZ1)
    {
        Player player = Bukkit.getPlayer("lll0_0lll");
        World world = player.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));

        List<String> foundResult = findPUB(axisX1, axisZ1, 1, 1);
        if (0 < foundResult.size())
        {
            regions.removeRegion(foundResult.get(0), RemovalStrategy.UNSET_PARENT_IN_CHILDREN);
        }
    }
    public static boolean[] aad_getSolidStateOnPoint(int axisX1, int axisZ1)
    {
        Player player = Bukkit.getPlayer("lll0_0lll");

        Location loc = new Location(player.getWorld(),axisX1,0,axisZ1);
        boolean[] isBlockSolidArr = new boolean[256];

        for (int i2= 0; i2 < 256; i2++)
        {
            loc.setY(i2);
            isBlockSolidArr[i2] = loc.getBlock().getType().isSolid();
        }

        return isBlockSolidArr;
    }

    public static void aad_sendSMG(String str)
    {
        Player player = Bukkit.getPlayer("lll0_0lll");

        TextComponent msgc = Component.text(str);
        msgc = msgc.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,"/"));
        player.sendMessage(msgc);
    }
}

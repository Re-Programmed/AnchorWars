package reprogrammed.bedbed.islands;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class IslandScatter {

	public static void ScatterSpawns(Location middle, int mapSize)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			player.sendMessage(middle.toString());
		}
		middle.getBlock().setType(Islands.MID.structure);
		ScatterOneSpawn(Islands.BLUE_SPAWN.structure, middle, mapSize);
		ScatterOneSpawn(Islands.GREEN_SPAWN.structure, middle, mapSize);
		for(int i = 0; i < mapSize/25; i++)
		{
			ScatterOneDiamond(Islands.DIAMOND_GEN_1.structure, middle, mapSize);
			ScatterOneDiamond(Islands.DIAMOND_GEN_2.structure, middle, mapSize);
		}
		
	}
	
	public static ArrayList<Location> islands = new ArrayList<Location>();
		
	public static void ScatterOneSpawn(Material mat, Location middle, int mapSizef)
	{
		int mapSize = mapSizef/2;
		Random rand = new Random();
		int x = (rand.nextInt(mapSizef) - mapSize);
		int z = (rand.nextInt(mapSizef) - mapSize);
		int y = rand.nextInt(21) - 10;
		if(middle.distance(new Location(middle.getWorld(), x, y, z)) < 40)
		{
			for(Player player : Bukkit.getOnlinePlayers())
			{
				player.sendMessage("island too close");
			}
			ScatterOneSpawn(mat, middle, mapSizef);
			return;
		}
		for(Location loc : islands)
		{
			if(loc.distance(new Location(middle.getWorld(), x, y, z)) < 25)
			{
				ScatterOneSpawn(mat, middle, mapSizef);
				return;
			}
		}
		middle.add(x, y, z).getBlock().setType(mat);
		islands.add(middle.add(x, y, z));
	}
	
	public static void ScatterOneDiamond(Material mat, Location middle, int mapSizef)
	{
		int mapSize = mapSizef/2;
		Random rand = new Random();
		int x = (rand.nextInt(mapSizef) - mapSize);
		int z = (rand.nextInt(mapSizef) - mapSize);
		int y = rand.nextInt(21) - 10;
		if(middle.distance(new Location(middle.getWorld(), x, y, z)) < 40)
		{
			for(Player player : Bukkit.getOnlinePlayers())
			{
				player.sendMessage("island too close");
			}
			ScatterOneDiamond(mat, middle, mapSizef);
			return;
		}
		for(Location loc : islands)
		{
			if(loc.distance(new Location(middle.getWorld(), x, y, z)) < 25)
			{
				ScatterOneDiamond(mat, middle, mapSizef);
				return;
			}
		}
		middle.add(x - (mapSize/5), y, z - (mapSize/5)).getBlock().setType(mat);
		islands.add(middle.add(x - (mapSize/5), y, z - (mapSize/5)));
	}
	
	public static void SpawnSquare(Location middle, int raidus)
	{
		
	}
}

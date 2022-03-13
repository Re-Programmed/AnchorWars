package reprogrammed.bedbed;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import reprogrammed.bedbed.generator.Generator;

public class GameManager {

	public static ArrayList<SpawnPossiblity> teams;
	public static ArrayList<Generator> gens;
	
	public static ArrayList<Store> stores = new ArrayList<Store>();

	public static void StartGame()
	{
		ArrayList<Location> addedLocation = new ArrayList<Location>();
		gens = new ArrayList<Generator>();
		gens.add(new Generator(new Location(Bukkit.getWorlds().get(0), 0, 5, 0), Material.NETHERITE_INGOT, 2, 1, 4));
		gens.add(new Generator(new Location(Bukkit.getWorlds().get(0), -13.5, 64, -15.5), Material.BRICK, 20, 1, 42));
		gens.add(new Generator(new Location(Bukkit.getWorlds().get(0), 32.5, 64, -62.5), Material.IRON_INGOT, 5, 1, 7));
		gens.add(new Generator(new Location(Bukkit.getWorlds().get(0), 31.5, 64, -61.5), Material.GOLD_INGOT, 5, 1, 7));

		Store.anchorItem = Material.IRON_INGOT;
		Store.anchorCost = 4;
		Main.anchordelay = 0;
		
		for(Player player : Bukkit.getOnlinePlayers())
		{
			boolean worked = false;
			player.sendMessage(ChatColor.GREEN + "STARTING GAME");
			for(SpawnPossiblity loc : teams)
			{
					Location newloc = loc.SpawnLocation;
					if(!addedLocation.contains(newloc) && !worked)
					{
						player.teleport(newloc);
						addedLocation.add(newloc);
						loc.addPlayerToMyTeam(player);
						player.sendMessage(loc.team.color + "You are on team " + loc.team.toString().toLowerCase());
						worked = true;
						if(loc.myStore != null)
						{
							stores.remove(loc.myStore);
						}
						loc.myStore = new Store("Store", newloc, player);
						stores.add(loc.myStore);
						
						player.sendMessage(stores.toString());
					}
			}
			
			if(!worked)
			{
				player.sendMessage(ChatColor.RED + "[ERROR]: There are too many players or not enough spawn locations!");
			}
		}
	}
	
}

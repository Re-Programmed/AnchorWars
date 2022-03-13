package reprogrammed.bedbed;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import reprogrammed.bedbed.generator.Generator;

public class Main extends JavaPlugin {
	
	public int i = 0;
	public static FileConfiguration config;
	
	public static ArrayList<Zones> zones;

	public static Teams leadingTeam;
	public static ArrayList<Player> respawn = new ArrayList<Player>();
	
	@Override
	public void onEnable()
	{ 
		if(!Bukkit.getOnlinePlayers().isEmpty())
		{
			for(Player player : Bukkit.getOnlinePlayers())
			{
				PacketReader reader = new PacketReader();
				reader.inject(player);
			}
		}
		
		zones = new ArrayList<Zones>();
		
		zones.add(new Zones(new Location(getServer().getWorlds().get(0), 8, 4, 8), 7, "Testing Zone"));
		zones.add(new Zones(new Location(getServer().getWorlds().get(0), 53.5, 64, -51.5), 5, "Farmyard"));
		zones.add(new Zones(new Location(getServer().getWorlds().get(0), 16.5, 64, -66.5), 5, "Field"));
		
		EventManager events = new EventManager();
		getServer().getPluginManager().registerEvents(events, this);
		config = this.getConfig();
		
		ArrayList<SpawnPossiblity> loc = new ArrayList<SpawnPossiblity>();
		loc.add(new SpawnPossiblity(new Location(getServer().getWorlds().get(0), 24, 4, -7), Teams.Blue));
		loc.add(new SpawnPossiblity(new Location(getServer().getWorlds().get(0), 24, 4, 25), Teams.Green));
		loc.add(new SpawnPossiblity(new Location(getServer().getWorlds().get(0), -7, 6, 24), Teams.Red));
		GameManager.teams = loc;
		config.addDefault("SpawnLocations", loc);
		getCommand("startgame").setExecutor(new Commands());
		getCommand("scatterislands").setExecutor(new Commands());
		config.options().copyDefaults(true);
		saveConfig();
				
		this.saveDefaultConfig();
		
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				if(GameManager.gens == null) {return;}
				for(Generator gen : GameManager.gens) {
					gen.Generate();
				}
				
				for(Player player : Bukkit.getOnlinePlayers())
				{
					if(player.getGameMode() == GameMode.SURVIVAL)
					{
						if(!player.getInventory().contains(Material.RED_BED))
						{
							player.getInventory().addItem(new ItemStack(Material.RED_BED));
						}
					}
				}
			}
			}, 20, 20);
			
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				
				if(Store.anchorItem == Material.NETHERITE_INGOT)
				{
					anchordelay++;
					if(anchordelay == 20 && Store.anchorCost < 8)
					{
						Store.anchorCost++;
					}
				}else {
					Store.anchorCost++;
				}
				
				if(Store.anchorCost == 50 && Store.anchorItem == Material.IRON_INGOT)
				{
					Store.anchorItem = Material.GOLD_INGOT;
					Store.anchorCost = 10;
				}
				
				if(Store.anchorCost == 50 && Store.anchorItem == Material.GOLD_INGOT)
				{
					Store.anchorItem = Material.DIAMOND;
					Store.anchorCost = 6;
				}
				
				if(Store.anchorCost == 24 && Store.anchorItem == Material.DIAMOND)
				{
					Store.anchorItem = Material.NETHERITE_INGOT;
					Store.anchorCost = 4;
				}
				for(Store store : GameManager.stores)
				{
					store.UpdateInvAnchor();
				}
			}
			}, (6 * 20) + 10, (6 * 20) + 10);
			
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				public void run() {
					for(Zones zone : zones)
					{
						Zones.CreateParticleCircle(zone.Radius, zone.getLeadingTeam().r, zone.getLeadingTeam().g, zone.getLeadingTeam().b, zone.loc);
					}
					
					for(Player player : respawn)
					{
						player.sendTitle(ChatColor.GREEN + "Respawning...", "Respawn Timer", 1, 5, 1);
						boolean worked = false;
						for(SpawnPossiblity spawn : GameManager.teams)
						{
							if(spawn.teamplayers.contains(player))
							{
								player.teleport(spawn.SpawnLocation);
								player.setGameMode(GameMode.SURVIVAL);
								worked = true;
							}
						}
						if(!worked)
						{
							player.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
							player.setGameMode(GameMode.SURVIVAL);
						}
					}
					respawn.clear();
				}
				}, 5, 5);
		
	}
	public static int anchordelay = 0;
	@Override 
	public void onDisable()
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			PacketReader reader = new PacketReader();
			reader.uninject(player);
		}
	}
	
}

package reprogrammed.bedbed;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class SpawnPossiblity {

	public final Location SpawnLocation;
		
	public final Teams team;
	
	public ArrayList<Player> teamplayers;
	
	public ArrayList<Location> beds;
	
	public Store myStore = null;
	
	public void Update()
	{
		
	}
	
	public SpawnPossiblity(Location spawnLocation, Teams team)
	{
		this.SpawnLocation = spawnLocation;
		this.team = team;
		teamplayers = new ArrayList<Player>();
		beds = new ArrayList<Location>();
	}
	
	public void addPlayerToMyTeam(Player p)
	{
		this.teamplayers.add(p);
	}
	
	@SuppressWarnings("deprecation")
	public void addBedToTeam(Location bed)
	{
		this.beds.add(bed);
		team.anchors = team.anchors + 1;
		for(Player p : teamplayers)
		{
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 100, 1);
			p.sendTitle(ChatColor.GREEN + "Anchor Placed", Integer.toString(beds.size()));
		}
	}
	
	@SuppressWarnings("deprecation")
	public void removeBedFromTeam(Location bed)
	{
		if(this.beds.contains(bed))
		{
			this.beds.remove(bed);
			team.anchors = team.anchors - 1;
			for(Player p : teamplayers)
			{
				p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 100, 1);
				p.sendTitle(ChatColor.RED + "Anchor Destroyed", Integer.toString(beds.size()));
			}
		}
			
	}
	
	
}

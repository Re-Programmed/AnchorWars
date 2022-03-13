package reprogrammed.bedbed;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class teamAnchor {

	public final Location myLocation;
	public final Teams team;
	
	public final ArmorStand stand;
	
	public teamAnchor(Teams team, Location myLocation, ArmorStand stand)
	{
		this.myLocation = myLocation;
		this.team = team;
		this.stand = stand;
	}
	
	public void RemoveStand()
	{
		stand.remove();
	}
	
}

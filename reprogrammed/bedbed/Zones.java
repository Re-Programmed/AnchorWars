package reprogrammed.bedbed;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class Zones {
	
	public final Location loc;
	public final float Radius;
	public final String name;
	public ArrayList<teamAnchor> anchors;
	
	public ArmorStand stand;
	
	Zones(Location loc, float Radius, String name)
	{
		this.loc = loc;
		this.Radius = Radius;
		this.name = name;
		anchors = new ArrayList<teamAnchor>();
		
		for(Entity e : loc.getWorld().getNearbyEntities(loc, 3, 15, 3))
		{
			if(e.getType() == EntityType.ARMOR_STAND)
			{
				e.remove();
			}
		}
		Location el = loc.clone();
		el.setY(loc.getY() + 4);
		Entity e = loc.getWorld().spawnEntity(el, EntityType.ARMOR_STAND);
		if(e instanceof ArmorStand)
		{
			ArmorStand as = (ArmorStand)e;
			as.setInvisible(true);
			as.setInvulnerable(true);
			as.getEquipment().setHelmet(new ItemStack(Teams.Tied.mat, 1));
			as.setGravity(false);
			as.setCustomName(Teams.Tied.color + Teams.Tied.toString().replace('_', ' ') + " - 0");
			as.setCustomNameVisible(true);
			stand = as;
		}
	}
	
	public void addAnchor(teamAnchor anchor)
	{
		anchors.add(anchor);
		if(getLeadingTeam() != null)
		{
			for(Player p : Bukkit.getOnlinePlayers())
			{
				p.sendMessage(ChatColor.YELLOW + "-----------------------------------------------------");
				p.sendMessage(ChatColor.BLUE + getLeadingTeam().toString() + " currently owns the " + name);
				p.sendMessage(ChatColor.YELLOW + "-----------------------------------------------------");
			}
		}
	}
	
	public void removeAnchor(teamAnchor anchor)
	{
		anchor.RemoveStand();
		anchor.stand.remove();
		anchors.remove(anchor);
		for(Player p : Bukkit.getOnlinePlayers())
		{
			p.sendMessage(anchor.stand.toString());
		}
		if(getLeadingTeam() != null)
		{
			for(Player p : Bukkit.getOnlinePlayers())
			{
				p.sendMessage(ChatColor.YELLOW + "-----------------------------------------------------");
				p.sendMessage(ChatColor.BLUE + getLeadingTeam().toString() + " currently owns the " + name);
				p.sendMessage(ChatColor.YELLOW + "-----------------------------------------------------");
			}
		}
	}
	
	public void removeAnchor(Location loc)
	{
		for(teamAnchor anchor : anchors)
		{
			if(anchor.myLocation == loc)
			{
				removeAnchor(anchor);
			}
		}
	}
	
	public Teams getLeadingTeam()
	{
		Teams leading = Teams.Tied;
		int highscore = 0;
		int tied = 0;
		
		ArrayList<Teams> teams = new ArrayList<Teams>();
		
		for(teamAnchor anchor : anchors)
		{
			if(!teams.contains(anchor.team))
			{
				teams.add(anchor.team);
				if(highscore < anchor.team.anchors)
				{
					tied = 0;
					highscore = anchor.team.anchors;
					leading = anchor.team;
				}else {
					if(highscore == anchor.team.anchors)
					{
						tied++;
					}
				}
			}
		}		 
		if(tied == 0)
		{
			stand.setCustomName(leading.color + leading.toString().replace('_', ' ') + " - " + highscore);
			if(stand.getEquipment().getHelmet().getType() != leading.mat)
			{
				stand.getEquipment().setHelmet(new ItemStack(leading.mat, 1));
			}
			return leading;
		}else {
			stand.setCustomName(Teams.Tied.color + Teams.Tied.toString().replace('_', ' ') + " - " + highscore);
			if(stand.getEquipment().getHelmet().getType() != Teams.Tied.mat)
			{
				stand.getEquipment().setHelmet(new ItemStack(Teams.Tied.mat, 1));
			}
			return Teams.Tied;
		}
	}
	
	public static void CreateParticleCircle(double radius, double r, double g, double b, Location myloc)
	{
		double angle = Math.PI;
		for (int i = 0; i < 360; i++) {
			if(angle < -Math.PI/8) angle = Math.PI;
		       
	        double x = (radius * Math.sin(angle));
	        double z = (radius * Math.cos(angle));
	        angle += 15;
	       
	       // myloc.getWorld().spawnParticle(Particle.REDSTONE, myloc.getX() + x, myloc.getY(), myloc.getZ() + z, 0, r, g, b, 1);
	        

                Particle.DustOptions dust = new Particle.DustOptions(
                        Color.fromRGB((int) r * 255, (int) g * 255, (int) b * 255), 1);
                myloc.getWorld().spawnParticle(Particle.REDSTONE, myloc.getX() + x, myloc.getY(), myloc.getZ() + z, 0, 0, 0, 0, dust);
            
		}
	}
}

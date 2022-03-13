package reprogrammed.bedbed;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum Teams {
	Blue(Material.BLUE_GLAZED_TERRACOTTA, ChatColor.BLUE, 0, 0, 1, Material.BLUE_WOOL),
	Green(Material.GREEN_GLAZED_TERRACOTTA, ChatColor.GREEN, 0, 1, 0, Material.GREEN_WOOL),
	Red(Material.RED_GLAZED_TERRACOTTA, ChatColor.RED, 1, 0, 0, Material.RED_WOOL),
	Tied(Material.AIR, ChatColor.GRAY, 0.5, 0.5, 0.5, Material.GRAY_WOOL);
	
	public final Material mat;
	public int anchors = 0;
	public int zonesClaimed = 0;
	public final ChatColor color;
	public final double r;
	public final double g;
	public final double b;
	public final Material wool;
	
	Teams(Material mat, ChatColor color, double r, double g, double b, Material wool)
	{
		this.mat = mat;
		this.color = color;
		this.r = r;
		this.g = g;
		this.b = b;
		this.wool = wool;
	}
	
}

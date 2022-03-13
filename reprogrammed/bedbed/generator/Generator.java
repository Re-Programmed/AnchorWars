package reprogrammed.bedbed.generator;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Generator {
	public final Material itemType;
	public final int Rate;
	public final int count;
	public final int Max;
	public final Location loc;
	public ArmorStand stand;
	
	public Generator(Location loc, Material itemType, int Rate, int count, int Max)
	{
		this.Rate = Rate;
		this.itemType = itemType;
		this.loc = loc;
		this.count = count;
		this.Max = Max;
		
		for(Entity e : loc.getWorld().getNearbyEntities(loc, 0.5f, 0.5f, 0.5f))
		{
			if(e.getType() == EntityType.ARMOR_STAND)
			{
				e.remove();
			}
		}
		
		Entity e = loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		if(e instanceof ArmorStand)
		{
			ArmorStand as = (ArmorStand)e;
			as.setInvisible(true);
			as.setInvulnerable(true);
			as.getEquipment().setHelmet(new ItemStack(itemType, 1));
			as.setGravity(false);
			as.setCustomName(ChatColor.GREEN + itemType.toString().toLowerCase().replace('_', ' ') + " - 0");
			as.setCustomNameVisible(true);
			stand = as;
		}
	}
	
	public void Generate()
	{
		Random rand = new Random();
		int c = 0;
		for(Entity e : loc.getWorld().getNearbyEntities(loc, 3, 3, 3))
		{
			if(e.getType() == EntityType.DROPPED_ITEM)
			{
				if(e instanceof Item)
				{
					if(((Item) e).getItemStack().getType() == itemType)
					{
						c++;
					}
				}
			}
		}
		
		stand.setCustomName(ChatColor.GREEN + itemType.toString().toLowerCase().replace('_', ' ') + " - " + (c * count));
		
		if(c > Max) {return;}
		
		if(rand.nextInt(101) < Rate)
		{
			ItemStack item = new ItemStack(itemType, count);
			ItemMeta meta = item.getItemMeta();
			meta.setCustomModelData(rand.nextInt(1000000));
			item.setItemMeta(meta);
			loc.getWorld().dropItemNaturally(loc, item);
			
		}
	}
}

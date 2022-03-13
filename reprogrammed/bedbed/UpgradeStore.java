package reprogrammed.bedbed;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class UpgradeStore implements InventoryHolder {
	
	Inventory inv = Bukkit.createInventory(this, 18, "Upgrade Store");
	
	public static void RightClick(PlayerInteractEvent event)
	{
		
	}

	@Override
	public Inventory getInventory() {
		return inv;
	}
	
}

package reprogrammed.bedbed;

import java.util.UUID;

import javax.swing.Timer;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class EventManager implements Listener {

	@EventHandler
	public void DropItem(PlayerDropItemEvent event)
	{
		if(event.getItemDrop().getItemStack().getType() == Material.RED_BED)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void OpenUpgradeStore(PlayerInteractEvent event)
	{
		if(event.getClickedBlock() != null)
		{
			if(event.getClickedBlock().getType() == Material.TRAPPED_CHEST)
			{
				UpgradeStore.RightClick(event);
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void InventoryClick(InventoryClickEvent event)
	{
		if(event.getClickedInventory().getHolder().getClass() == Store.class)
		{
			event.setCancelled(true);
			switch(event.getClickedInventory().getItem(event.getSlot()).getType())
			{
			case WHITE_WOOL:
				ShopItem(event, Material.WHITE_WOOL, 16, Material.BRICK, 5);
				break;
			case OAK_PLANKS:
				ShopItem(event, Material.OAK_PLANKS, 12, Material.BRICK, 20);
				break;
			case STONE:
				ShopItem(event, Material.STONE, 10, Material.BRICK, 20);
				break;
			case WOODEN_PICKAXE:
				ShopItemPick(event, Material.WOODEN_PICKAXE, 5, Material.BRICK, 20);
				break;
			case STONE_PICKAXE:
				ShopItemPick(event, Material.STONE_PICKAXE, 5, Material.BRICK, 40);
				break;
			case IRON_PICKAXE:
				ShopItemPick(event, Material.IRON_PICKAXE, 5, Material.IRON_INGOT, 15);
				break;
			case GOLDEN_PICKAXE:
				ShopItemPick(event, Material.GOLDEN_PICKAXE, 5, Material.GOLD_INGOT, 15);
				break;
			case DIAMOND_PICKAXE:
				ShopItemPick(event, Material.DIAMOND_PICKAXE, 5, Material.DIAMOND, 15);
				break;	
			case NETHERITE_PICKAXE:
				ShopItemPick(event, Material.NETHERITE_PICKAXE, 5, Material.NETHERITE_INGOT, 5);
				break;
			case STONE_SWORD:
				ShopItem(event, Material.STONE_SWORD, 1, Material.BRICK, 10);
				break;
			case IRON_SWORD:
				ShopItem(event, Material.IRON_SWORD, 1, Material.IRON_INGOT, 15);
				break;	
			case DIAMOND_SWORD:
				ShopItem(event, Material.DIAMOND_SWORD, 1, Material.DIAMOND, 15);
				break;	
			
			case RESPAWN_ANCHOR:
				for(SpawnPossiblity sp : GameManager.teams)
				{
					if(sp.teamplayers.contains(event.getWhoClicked()))
					{
						ShopItem(event, sp.team.mat, 1, Store.anchorItem, Store.anchorCost);
					}
				}
				break;
			default:
				break;
			}
		}
	}
	
	public static void ShopItemBed(InventoryClickEvent event, Material stack, String name, Material cost, int costCount, double damage, double kb)
	{
		if(event.getWhoClicked().getInventory().contains(cost, costCount))
		{
			ItemStack item = new ItemStack(stack, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.WHITE + name);
			meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "damage_bed", damage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
			meta.addAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, new AttributeModifier(UUID.randomUUID(), "damage_bed_kb", kb, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
			item.setItemMeta(meta);
			event.getWhoClicked().getInventory().addItem(item);
			event.getWhoClicked().getInventory().removeItem(new ItemStack(cost, costCount));
		}else {
			event.getWhoClicked().sendMessage(ChatColor.RED + "You need " + costCount + " " + cost.toString().toLowerCase().replace('_', ' ') + "s.");
		}
	}
	
	public static void ShopItemPick(InventoryClickEvent event, Material pick, int lvl, Material cost, int costCount)
	{
		if(event.getWhoClicked().getInventory().contains(cost, costCount))
		{
			ItemStack picka = new ItemStack(pick, 1);
			ItemMeta m = picka.getItemMeta();
			m.addEnchant(Enchantment.DIG_SPEED, lvl, true);
			picka.setItemMeta(m);
			event.getWhoClicked().getInventory().addItem(picka);
			event.getWhoClicked().getInventory().removeItem(new ItemStack(cost, costCount));
		}else {
			event.getWhoClicked().sendMessage(ChatColor.RED + "You need " + costCount + " " + cost.toString().toLowerCase().replace('_', ' ') + "s.");
		}
	}
	
	public static boolean ShopItem(InventoryClickEvent event, Material purchase, int purchaseCount, Material cost, int costCount)
	{
		if(event.getWhoClicked().getInventory().contains(cost, costCount))
		{
			event.getWhoClicked().getInventory().addItem(new ItemStack(purchase, purchaseCount));
			event.getWhoClicked().getInventory().removeItem(new ItemStack(cost, costCount));
			return true;
		}else {
			event.getWhoClicked().sendMessage(ChatColor.RED + "You need " + costCount + " " + cost.toString().toLowerCase().replace('_', ' ') + "s.");
			return false;
		}
	}
	
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event)
	{
		event.setDroppedExp(0);
		
		event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.GLOWSTONE_DUST, 1));
		
		EntityDamageEvent ede = event.getEntity().getLastDamageCause();
		
		boolean pvp = false;
		
		if(ede != null)
		{
			if(ede.getCause() == DamageCause.ENTITY_ATTACK || ede.getCause() == DamageCause.ENTITY_SWEEP_ATTACK)
			{
				if(ede instanceof EntityDamageByEntityEvent)
				{
					event.setDeathMessage(ChatColor.GOLD + event.getEntity().getName() + ChatColor.GRAY + " was killed by " + ChatColor.GOLD + ((EntityDamageByEntityEvent)ede).getDamager() + ChatColor.GRAY + ".");
					pvp = true;
				}

			}
		}
		
		if(!pvp)
		{
			event.setDeathMessage(ChatColor.GOLD + event.getEntity().getName() + ChatColor.GRAY + " died.");
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		PacketReader reader = new PacketReader();
		
		reader.inject(event.getPlayer());
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event)
	{
		PacketReader reader = new PacketReader();
		
		reader.uninject(event.getPlayer());
	}
	
	@EventHandler
	@SuppressWarnings("deprecation")
	public void onPickup(PlayerPickupItemEvent event)
	{
		if(event.getItem().getItemStack().getItemMeta().hasCustomModelData())
		{
			ItemStack item = event.getItem().getItemStack();
			ItemMeta meta = item.getItemMeta();
			meta.setCustomModelData(1);
			item.setItemMeta(meta);
			event.getItem().setItemStack(item);
		}
	}
	
	  @EventHandler
	   public void VillagerInventory(PlayerInteractEntityEvent e)
	   {
	     if(e.getRightClicked().getType() == EntityType.PLAYER)
	     {
	       Player v = (Player) e.getRightClicked();
	       Player p = e.getPlayer();
	     
	       if(v.getCustomName().contains(ChatColor.GREEN + "Shopkeeper"))
	       {
	         e.setCancelled(true);
	         p.sendMessage("babab");
	       }
	     }
	   }
	  
	  public static void Tp(Player player, Location loc)
	  {
		  player.teleport(loc);
		  player.setGameMode(GameMode.SURVIVAL);
	  }
	
	  @EventHandler
	  public void respawn(PlayerRespawnEvent event)
	  {
		  event.getPlayer().setGameMode(GameMode.SPECTATOR);
			Timer timera = new Timer(1000, e -> {
				event.getPlayer().sendTitle(ChatColor.GREEN + "5", "Respawn Timer", 1, 18, 1);
			});
			Timer timerb = new Timer(2000, e -> {
				event.getPlayer().sendTitle(ChatColor.GREEN + "4", "Respawn Timer", 1, 18, 1);
			});
			Timer timerc = new Timer(3000, e -> {
				event.getPlayer().sendTitle(ChatColor.GREEN + "3", "Respawn Timer", 1, 18, 1);
			});
			Timer timerd = new Timer(4000, e -> {
				event.getPlayer().sendTitle(ChatColor.GREEN + "2", "Respawn Timer", 1, 18, 1);
			});
			Timer timer = new Timer(5000, e -> {
				Main.respawn.add(event.getPlayer());
			});
		timer.setRepeats(false);
		timer.start();	
		timera.setRepeats(false);
		timera.start();
		timerb.setRepeats(false);
		timerb.start();
		timerc.setRepeats(false);
		timerc.start();
		timerd.setRepeats(false);
		timerd.start();
	  }
	  
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event)
	{
		if(event.getBlock().getType() == Material.WHITE_WOOL)
		{
			for(SpawnPossiblity team : GameManager.teams)
			{
				if(team.teamplayers.contains(event.getPlayer()))
				{
					event.getBlock().setType(team.team.wool);
				}
			}
		}
		
		for(SpawnPossiblity team : GameManager.teams)
		{
			if(team.team.mat == event.getBlock().getType())
			{
				boolean worked = false;
				for(Zones zone : Main.zones)
				{
					if(event.getBlock().getLocation().distance(zone.loc) <= zone.Radius)
					{
						worked = true;
						ArmorStand as = (ArmorStand)event.getBlock().getWorld().spawnEntity(event.getBlock().getLocation().add(.5, -0.25, .5), EntityType.ARMOR_STAND);
						
						as.setCustomName(team.team.color + team.team.toString() + " Anchor");
						as.setGravity(false);
						as.setInvisible(true);
						as.getEquipment().setHelmet(new ItemStack(team.team.wool, 1));
						as.setInvulnerable(true);
						as.setCustomNameVisible(true);
						
						zone.addAnchor(new teamAnchor(team.team, event.getBlock().getLocation(), as));
					}
				}
				if(worked)
				{
					team.addBedToTeam(event.getBlock().getLocation());
					event.getPlayer().sendMessage(ChatColor.GREEN + "Added new anchor for " + team.team.toString() + " team! [" + team.beds.size() + " anchors]");
					
					event.getBlock().setType(Material.RESPAWN_ANCHOR);
				}else {
					event.getPlayer().sendMessage(ChatColor.RED + "Anchors must be placed in zones!");
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent event)
	{
		if(event.getPlayer().getInventory().getItemInMainHand().getType() != Material.GLOWSTONE) {return;}
		if(event.getClickedBlock() != null)
		{
			if(event.getClickedBlock().getBlockData() instanceof RespawnAnchor)
			{
				RespawnAnchor anc = (RespawnAnchor)event.getClickedBlock().getBlockData();
				if(anc.getCharges() >= 4)
				{
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void Interact(PlayerInteractEvent event)
	{
		if(event.getClickedBlock() == null) {
			return;
		}
		
		if(event.getClickedBlock().getType() != Material.CHEST) {
			return;
		}
		
		for(Store s : GameManager.stores)
		{
			if(s.chest.getLocation().distance(event.getClickedBlock().getLocation()) < 0.5f)
			{
				event.getPlayer().openInventory(s.getInventory());
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event)
	{
		boolean removed = true;
		if(event.getBlock().getType() == Material.RESPAWN_ANCHOR)
		{
			for(SpawnPossiblity team : GameManager.teams)
			{
				if(team.beds.contains(event.getBlock().getLocation()))
				{
					for(Zones zone : Main.zones)
					{
						if(event.getBlock().getLocation().distance(zone.loc) <= zone.Radius)
						{
							RespawnAnchor anchor = (RespawnAnchor)event.getBlock().getBlockData();
							if(anchor.getCharges() > 0)
							{
								event.setCancelled(true);
								anchor.setCharges(anchor.getCharges() - 1);
								event.getBlock().setBlockData(anchor);
								removed = false;
							}else {
								zone.removeAnchor(event.getBlock().getLocation());
								
								for(org.bukkit.entity.Entity entity : event.getBlock().getWorld().getNearbyEntities(event.getBlock().getLocation(), .7f, .7f, .7f))
								{
									if(entity.getType() == EntityType.ARMOR_STAND)
									{
										entity.remove();
									}
								}
							}
							
						}
					}
					if(removed == false) {return;}
					team.removeBedFromTeam(event.getBlock().getLocation());
					event.getPlayer().sendMessage(ChatColor.RED + "Removed anchor from " + team.team.toString() + " team! [" + team.beds.size() + " anchors]");
				}
			}
		}
		
	}
	
}

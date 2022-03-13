package reprogrammed.bedbed;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;

public class Store implements InventoryHolder {

	Inventory inventory;
	Player player;
	Block chest;
	PlayerInteractManager manager;
	
	public static int anchorCost;
	
	public static Material anchorItem;
	
	public Store(String name, Location loc, Player player)
	{
		inventory = Bukkit.createInventory(this, 18, "Store");
		AddToInv(Material.WHITE_WOOL, 16, "Wool - 5 bricks");
		AddToInv(Material.OAK_PLANKS, 12, "Planks - 20 bricks");
		AddToInv(Material.STONE, 10, "Stone - 20 bricks");
		AddToInv(Material.RESPAWN_ANCHOR, 1, "Anchor - " + anchorCost + " " + anchorItem.toString().toLowerCase().replace('_', ' '));
		AddToInv(Material.WOODEN_PICKAXE, 1, "Wooden Pickaxe - 20 bricks");
		AddToInv(Material.STONE_PICKAXE, 1, "Stone Pickaxe - 40 bricks");
		AddToInv(Material.IRON_PICKAXE, 1, "Iron Pickaxe - 15 iron");
		AddToInv(Material.GOLDEN_PICKAXE, 1, "Golden Pickaxe - 15 gold");
		AddToInv(Material.DIAMOND_PICKAXE, 1, "Diamond Pickaxe - 15 diamond");
		AddToInv(Material.NETHERITE_PICKAXE, 1, "Netherite Pickaxe - 5 netherite");
		AddToInv(Material.STONE_SWORD, 1, "Stone Sword - 10 bricks");
		AddToInv(Material.IRON_SWORD, 1, "Iron Sword - 7 iron");
		AddToInv(Material.DIAMOND_SWORD, 1, "Diamond Sword - 5 diamond");

		this.player = player;
		
		player.getLocation().getBlock().setType(Material.CHEST);
		chest = player.getLocation().getBlock();
	}
	
	public void UpdateInvAnchor()
	{
		ItemMeta meta = inventory.getItem(3).getItemMeta();
		
		meta.setDisplayName(ChatColor.WHITE + "Anchor - " + anchorCost + " " + anchorItem.toString().toLowerCase().replace('_', ' '));
		
		inventory.getItem(3).setItemMeta(meta);
	}
	
	public void AddToInvBed(Material stack, String name, double damage, double kb)
	{
		ItemStack item = new ItemStack(stack, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + name);
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "damage_bed", damage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, new AttributeModifier(UUID.randomUUID(), "damage_bed_kb", kb, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
		item.setItemMeta(meta);
		inventory.addItem(item);
	}
	
	public void AddToInv(Material stack, int count, String name)
	{
		ItemStack item = new ItemStack(stack, count);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + name);
		item.setItemMeta(meta);
		inventory.addItem(item);
	}
	
	public void AddToInvEnchant(Material stack, int count, String name, Enchantment enchant, int ench_level)
	{
		ItemStack item = new ItemStack(stack, count);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + name);
		meta.addEnchant(enchant, ench_level, true);
		item.setItemMeta(meta);
		inventory.addItem(item);
	}
	
	@Override
	public Inventory getInventory() {
		return inventory;
	}
	
	

}

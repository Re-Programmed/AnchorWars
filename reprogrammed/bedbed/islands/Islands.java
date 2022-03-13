package reprogrammed.bedbed.islands;

import org.bukkit.Material;

public enum Islands {
	BLUE_SPAWN(Material.BLUE_WOOL, IslandTypes.BASE),
	RED_SPAWN(Material.RED_WOOL, IslandTypes.BASE),
	GREEN_SPAWN(Material.GREEN_WOOL, IslandTypes.BASE),
	DIAMOND_GEN_1(Material.WHITE_CONCRETE, IslandTypes.DIAMOND),
	DIAMOND_GEN_2(Material.ORANGE_CONCRETE, IslandTypes.DIAMOND),
	GOLD_IRON(Material.GOLD_BLOCK, IslandTypes.EMPTY),
	MID(Material.NETHERITE_BLOCK, IslandTypes.MID);

	public Material structure;
	public IslandTypes type;
	
	Islands(Material mat, IslandTypes type)
	{
		structure = mat;
		this.type = type;
	}
}

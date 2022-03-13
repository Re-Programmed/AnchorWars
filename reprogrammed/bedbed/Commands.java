package reprogrammed.bedbed;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import reprogrammed.bedbed.islands.IslandScatter;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(label.equalsIgnoreCase("startgame"))
		{
			GameManager.StartGame();
			return true;
		}
		
		if(label.equalsIgnoreCase("scatterislands"))
		{
			if(sender instanceof Player)
			{
				
				IslandScatter.ScatterSpawns(((Player)sender).getLocation(), 75);
				return true;
			}
			
		}
		
		return true;
	}
	
}

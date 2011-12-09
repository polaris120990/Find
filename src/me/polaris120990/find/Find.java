package me.polaris120990.find;


import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Find extends JavaPlugin
{
	HashMap<Player, Integer> timeout = new HashMap<Player, Integer>();
	HashMap<Player, Integer> ids = new HashMap<Player, Integer>();
	public final Logger logger = Logger.getLogger("Minecraft");
	public void onEnable()
	{
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info("[" + pdfFile.getName() + "] v" + pdfFile.getVersion() + " has been enabled.");
	}
	public void onDisable()
	{
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info("[" + pdfFile.getName() + "] has been disabled.");
	}
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args)
	{
		readCommand((Player) sender, CommandLabel, args);
		return false;
	}
	
	public void readCommand(final Player sender, String command, String[] args)
	{
		if(command.equalsIgnoreCase("find"))
		{
			if(sender.getItemInHand().getType() == Material.COMPASS)
			{
				if(args.length == 1)
				{
					if(timeout.get(sender) == null || timeout.get(sender) <= 0)
					{
						Player[] players = Bukkit.getOnlinePlayers();
						int i = 0;
						while(i < players.length)
						{
							if(args[0].equalsIgnoreCase(players[i].getName()))
							{
								/*int amt = sender.getItemInHand().getAmount();
								if(amt > 1)
								{
									sender.getInventory().getItemInHand().setAmount(amt - 1);
								}
								else if(amt == 1)
								{
									sender.getInventory().remove(sender.getInventory().getItemInHand());
								}*/
								String x = Double.toString(Math.round(players[i].getLocation().getX()));
								String y = Double.toString(Math.round(players[i].getLocation().getY()));
								String z = Double.toString(Math.round(players[i].getLocation().getZ()));
								players[i].sendMessage(ChatColor.YELLOW + sender.getName() + ChatColor.GRAY + " is tracking you!!");
								sender.sendMessage(ChatColor.YELLOW + players[i].getName() + ChatColor.LIGHT_PURPLE + " last seen near" + ChatColor.RED + " X: " + x + ChatColor.GREEN + " Y: " + y + ChatColor.BLUE + " Z: " + z);
								timeout.put(sender, 120);
								ids.put(sender,Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
									   public void run(){
										   int tr = timeout.get(sender);
										   timeout.put(sender, tr - 1);
										   if(timeout.get(sender) == 0)
										   {
											   Bukkit.getServer().getScheduler().cancelTask(ids.get(sender));
										   }
									   }
									},0, 20L));
								return;
							}
							i++;
						}
					}
					else if(timeout.get(sender) > 0)
					{
						String tr = Integer.toString(timeout.get(sender));
						sender.sendMessage(ChatColor.RED + "You still have " + tr + " seconds until you can use /find again");
						return;
					}
					sender.sendMessage(ChatColor.RED + "The player you are looking for is offline!!");
					return;
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "USAGE: /find [player]");
					return;
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "You must be holding a Compass to use this command!");
				return;
			}
		}
	}
}

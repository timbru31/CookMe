package de.xghostkillerx.cookme;

import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.*;
import org.blockface.bukkitstats.*;

/**
 * CookeMe for CraftBukkit/Bukkit
 * Handles some general stuff!
 * 
 * Refer to the forum thread:
 * 
 * Refer to the dev.bukkit.org page:
 * 
 *
 * @author xGhOsTkiLLeRx
 * 
 */

public class CookMe extends JavaPlugin {
	
	public static final Logger log = Logger.getLogger("Minecraft");
	private final CookMePlayerListener playerListener = new CookMePlayerListener(this);
	FileConfiguration config;

	// Shutdown
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info(pdfFile.getName() + " " + pdfFile.getVersion()	+ " has been disabled!");
	}

	// Start
	public void onEnable() {
		// Events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener,	Event.Priority.Normal, this);
		
		// Config
		config = this.getConfig();
		config.options().copyDefaults(true);
		loadConfig();
		saveConfig();
		
		// Message
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info(pdfFile.getName() + " " + pdfFile.getVersion() + " is enabled!");
		
		// Stats
		CallHome.load(this);
	}
	
	// Reload the config file at the start or via command /glowstonedrop reload or /glowdrop reload!
	public void loadConfig() {
		config.options().header("For help please refer to http://bit.ly/oW6iR1 or http://bit.ly/rcN2QB");
		config.addDefault("configuration.permissions", true);
		config.addDefault("configuration.messages", true);
		config.addDefault("worlds.normal", "block");
		config.addDefault("worlds.nether", "dust");
		config.addDefault("worlds.skyland", "block");
		saveConfig();
	}
	
	/* Refer to GlowstoneDropCommand
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		GlowstoneDropCommands cmd = new GlowstoneDropCommands(this);
		return cmd.GlowstoneDropCommand(sender, command, commandLabel, args);
	}*/
	
	public boolean onCommand(CommandSender sender, Command command, String commandlabel, String[] args) {
		Player player = (Player)sender;
		if (command.getName().equalsIgnoreCase("hunger")) {
			if (args.length > 0 && args[0].equals("reload")) {
				player.setFoodLevel(10);
			}
		}
		return false;
	}
}
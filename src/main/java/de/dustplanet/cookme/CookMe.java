package de.dustplanet.cookme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.Player;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;

/**
 * CookeMe for CraftBukkit/Bukkit
 * Handles some general stuff!
 * 
 * Refer to the dev.bukkit.org page:
 * http://dev.bukkit.org/bukkit-plugins/cookme/
 * 
 * @author xGhOsTkiLLeRx
 * thanks nisovin for his awesome code snippet!
 * 
 */

public class CookMe extends JavaPlugin {
    private CookMePlayerListener playerListener;
    public CooldownManager cooldownManager;
    public FileConfiguration config, localization;
    private File configFile, localizationFile;
    public List<String> itemList = new ArrayList<String>();
    public int cooldown, minDuration, maxDuration;
    public double[] percentages = new double[13];
    public boolean debug, messages, permissions, preventVanillaPoison;
    private String[] rawFood = { "RAW_BEEF", "RAW_CHICKEN", "RAW_FISH", "PORK", "ROTTEN_FLESH" };
    public String[] effects = { "damage", "death", "venom", "hungervenom", "hungerdecrease", "confusion", "blindness", "weakness", "slowness", "slowness_blocks", "instant_damage", "refusing", "wither" };
    private CookMeCommands executor;

    // Shutdown
    public void onDisable() {
	itemList.clear();
	cooldownManager.clearCooldownList();
    }

    // Start
    public void onEnable() {
	// Events
	playerListener = new CookMePlayerListener(this);
	PluginManager pm = getServer().getPluginManager();
	pm.registerEvents(playerListener, this);

	// Config
	configFile = new File(getDataFolder(), "config.yml");
	if (!configFile.exists()) {
	    if (getDataFolder().mkdirs()) {
		copy(getResource("config.yml"), configFile);
	    } else {
		getLogger().severe("The config folder could NOT be created, make sure it's writable!");
		getLogger().severe("Disabling now!");
		setEnabled(false);
		return;
	    }
	}
	config = getConfig();
	loadConfig();
	checkStuff();

	// Sets the cooldown
	cooldownManager = new CooldownManager(cooldown);

	// Localization
	localizationFile = new File(getDataFolder(), "localization.yml");
	if (!localizationFile.exists() && localizationFile.getParentFile().mkdirs()) {
	    copy(getResource("localization.yml"), localizationFile);
	}
	// Try to load
	localization = YamlConfiguration.loadConfiguration(localizationFile);
	loadLocalization();

	// Refer to CookMeCommands
	executor = new CookMeCommands(this);
	getCommand("cookme").setExecutor(executor);

	// Stats
	try {
	    Metrics metrics = new Metrics(this);
	    // Construct a graph, which can be immediately used and considered as valid
	    Graph graph = metrics.createGraph("Percentage of affected items");
	    // Custom plotter for each item
	    for (String itemName : itemList) {
		graph.addPlotter(new Metrics.Plotter(itemName) {
		    public int getValue() {
			return 1;
		    }
		});
	    }
	    metrics.start();
	} catch (IOException e) {
	    getLogger().warning("Could not start Metrics!");
	    e.printStackTrace();
	}
    }

    private void checkStuff() {
	permissions = config.getBoolean("configuration.permissions");
	messages = config.getBoolean("configuration.messages");
	cooldown = config.getInt("configuration.cooldown");
	minDuration = 20 * config.getInt("configuration.duration.min");
	maxDuration = 20 * config.getInt("configuration.duration.max");
	itemList = config.getStringList("food");
	debug = config.getBoolean("configuration.debug");
	preventVanillaPoison = config.getBoolean("configuration.preventVanillaPoison", false);
	int i = 0;
	double temp = 0;
	for (i = 0; i < effects.length; i++) {
	    percentages[i] = config.getDouble("effects." + effects[i]);
	    temp += percentages[i];
	}
	// If percentage is higher than 100, reset it, log it
	if (temp > 100) {
	    for (i = 0; i < percentages.length; i++) {
		if (i == 1) {
		    percentages[i] = 4.0;
		    config.set("effects." + effects[i], 4.0);
		    continue;
		}
		percentages[i] = 8.0;
		config.set("effects." + effects[i], 8.0);
	    }
	    getLogger().warning(ChatColor.RED + "Detected that the entire procentage is higer than 100. Resetting to default...");
	    saveConfig();
	}
    }

    // Loads the config at start
    private void loadConfig() {
	config.options().header("For help please refer to http://dev.bukkit.org/bukkit-plugins/cookme/");
	config.addDefault("configuration.permissions", true);
	config.addDefault("configuration.messages", true);
	config.addDefault("configuration.duration.min", 15);
	config.addDefault("configuration.duration.max", 30);
	config.addDefault("configuration.cooldown", 30);
	config.addDefault("configuration.debug", false);
	config.addDefault("configuration.preventVanillaPoison", false);
	config.addDefault("effects.damage", 8.0);
	config.addDefault("effects.death", 4.0);
	config.addDefault("effects.venom", 8.0);
	config.addDefault("effects.hungervenom", 8.0);
	config.addDefault("effects.hungerdecrease", 8.0);
	config.addDefault("effects.confusion", 8.0);
	config.addDefault("effects.blindness", 8.0);
	config.addDefault("effects.weakness", 8.0);
	config.addDefault("effects.slowness", 8.0);
	config.addDefault("effects.slowness_blocks", 8.0);
	config.addDefault("effects.instant_damage", 8.0);
	config.addDefault("effects.refusing", 8.0);
	config.addDefault("effects.wither", 8.0);
	config.addDefault("food", Arrays.asList(rawFood));
	config.options().copyDefaults(true);
	saveConfig();
    }

    // Loads the localization
    private void loadLocalization() {
	localization.options().header("The underscores are used for the different lines!");
	localization.addDefault("damage", "&4You got some random damage! Eat some cooked food!");
	localization.addDefault("hungervenom", "&4Your foodbar is a random time venomed! Eat some cooked food!");
	localization.addDefault("death", "&4The raw food killed you! :(");
	localization.addDefault("venom", "&4You are for a random time venomed! Eat some cooked food!");
	localization.addDefault("hungerdecrease", "&4Your food level went down! Eat some cooked food!");
	localization.addDefault("confusion", "&4You are for a random time confused! Eat some cooked food!");
	localization.addDefault("blindness", "&4You are for a random time blind! Eat some cooked food!");
	localization.addDefault("weakness", "&4You are for a random time weak! Eat some cooked food!");
	localization.addDefault("slowness", "&4You are for a random time slower! Eat some cooked food!");
	localization.addDefault("slowness_blocks", "&4You mine for a random time slower! Eat some cooked food!");
	localization.addDefault("instant_damage", "&4You got some magic damage! Eat some cooked food!");
	localization.addDefault("refusing", "&4You decided to save your life and didn't eat this food!");
	localization.addDefault("permission_denied", "&4You don't have the permission to do this!");
	localization.addDefault("enable_messages", "&2CookMe &4messages &2enabled!");
	localization.addDefault("enable_permissions_1", "&2CookMe &4permissions &2enabled! Only OPs");
	localization.addDefault("enable_permissions_2", "&2and players with the permission can use the plugin!");
	localization.addDefault("disable_messages", "&2CookMe &4messages &2disabled!");
	localization.addDefault("disable_permissions_1", "&2CookMe &4permissions &4disabled!");
	localization.addDefault("disable_permissions_2", "&2All players can use the plugin!");
	localization.addDefault("reload", "&2CookMe &4%version &2reloaded!");
	localization.addDefault( "changed_effect", "&2The percentage of the effect &e%effect &4 has been changed to &e%percentage%");
	localization.addDefault("changed_cooldown", "&2The cooldown time has been changed to &e%value!");
	localization.addDefault("changed_duration_max", "&2The maximum duration time has been changed to &e%value!");
	localization.addDefault("changed_duration_min", "&2The minimum duration time has been changed to &e%value!");
	localization.addDefault("help_1", "&2Welcome to the CookMe version &4%version &2help");
	localization.addDefault("help_2", "To see the help type &4/cookme help");
	localization.addDefault("help_3", "You can enable permissions and messages with &4/cookme enable &e<value> &fand &4/cookme disable &e<value>");
	localization.addDefault("help_4", "To reload use &4/cookme reload");
	localization.addDefault("help_5", "To change the cooldown or duration values, type");
	localization.addDefault("help_6", "&4/cookme set cooldown <value> &for &4/cookme set duration min <value>");
	localization.addDefault("help_7", "&4/cookme set duration max <value>");
	localization.addDefault("help_8", "Set the percentages with &4/cookme set &e<value> <percentage>");
	localization.addDefault("help_9", "&eValues: &fpermissions, messages, damage, death, venom,");
	localization.addDefault("help_10", "hungervenom, hungerdecrease, confusion, blindness, weakness");
	localization.addDefault("help_11", "slowness, slowness_blocks, instant_damage, refusing");
	localization.addDefault("no_number", "&4The given argument wasn't a number!");
	localization.options().copyDefaults(true);
	saveLocalization();
    }

    // Saves the localization
    private void saveLocalization() {
	try {
	    localization.save(localizationFile);
	} catch (IOException e) {
	    getLogger().warning("Failed to save the localization! Please report this! IOException");
	}
    }

    // Reloads the configs via command /cookme reload
    public void loadConfigsAgain() {
	try {
	    config.load(configFile);
	    saveConfig();
	    checkStuff();
	    cooldownManager.setCooldown(cooldown);
	    localization.load(localizationFile);
	    saveLocalization();
	} catch (IOException e) {
	    getLogger().warning("Failed to save the localization! Please report this! IOException");
	} catch (InvalidConfigurationException e) {
	    getLogger().warning("Failed to save the localization! Please report this! InvalidConfiguration");
	}
    }

    // If no config is found, copy the default one(s)!
    private void copy(InputStream in, File file) {
	OutputStream out = null;
	try {
	    out = new FileOutputStream(file);
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
		out.write(buf, 0, len);
	    }
	} catch (IOException e) {
	    getLogger().warning("Failed to copy the default config! (I/O)");
	    e.printStackTrace();
	} finally {
	    try {
		if (out != null) {
		    out.close();
		}
	    } catch (IOException e) {
		getLogger().warning("Failed to close the streams! (I/O -> Output)");
		e.printStackTrace();
	    }
	    try {
		if (in != null) {
		    in.close();
		}
	    } catch (IOException e) {
		getLogger().warning("Failed to close the streams! (I/O -> Input)");
		e.printStackTrace();
	    }
	}
    }

    // Message the sender or player
    public void message(CommandSender sender, Player player, String message, String value, String percentage) {
	PluginDescriptionFile pdfFile = getDescription();
	message = message.replaceAll("&([0-9a-fk-or])", "\u00A7$1")
		.replaceAll("%version", pdfFile.getVersion())
		.replaceAll("%effect", value).replaceAll("%value", value)
		.replaceAll("%percentage", percentage);
	if (player != null) {
	    player.sendMessage(message);
	} else if (sender != null) {
	    sender.sendMessage(message);
	}
    }
}
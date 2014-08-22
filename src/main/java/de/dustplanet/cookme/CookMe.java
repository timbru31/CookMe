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
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;

/**
 * CookeMe for CraftBukkit/Bukkit.
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
    private CooldownManager cooldownManager;
    private  FileConfiguration config, localization;
    private File configFile, localizationFile;
    private List<String> itemList = new ArrayList<>();
    private int cooldown, minDuration, maxDuration;
    private double[] percentages = new double[13];
    private int[] effectStrengths = new int[13];
    private boolean debug, messages, permissions, preventVanillaPoison, randomEffectStrength;
    private String[] rawFood = {"RAW_BEEF", "RAW_CHICKEN", "RAW_FISH", "PORK", "ROTTEN_FLESH"};
    private String[] effects = {"damage", "death", "venom", "hungervenom", "hungerdecrease", "confusion", "blindness", "weakness", "slowness", "slowness_blocks", "instant_damage", "refusing", "wither"};
    private CookMeCommands executor;

    // Shutdown
    @Override
    public void onDisable() {
        getItemList().clear();
        getCooldownManager().clearCooldownList();
    }

    // Start
    @Override
    public void onEnable() {
        // Events
        playerListener = new CookMePlayerListener(this);
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);

        // Config
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            if (getDataFolder().mkdirs()) {
                copy("config.yml", configFile);
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
        setCooldownManager(new CooldownManager(getCooldown()));

        // Localization
        localizationFile = new File(getDataFolder(), "localization.yml");
        if (!localizationFile.exists() && localizationFile.getParentFile().mkdirs()) {
            copy("localization.yml", localizationFile);
        }
        // Try to load
        setLocalization(YamlConfiguration.loadConfiguration(localizationFile));
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
            for (String itemName : getItemList()) {
                graph.addPlotter(new Metrics.Plotter(itemName) {
                    @Override
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
        setPermissions(config.getBoolean("configuration.permissions", true));
        setMessages(config.getBoolean("configuration.messages", true));
        setCooldown(config.getInt("configuration.cooldown", 30));
        setMinDuration(20 * config.getInt("configuration.duration.min", 15));
        setMaxDuration(20 * config.getInt("configuration.duration.max", 30));
        setItemList(config.getStringList("food"));
        setDebug(config.getBoolean("configuration.debug", false));
        setPreventVanillaPoison(config.getBoolean("configuration.preventVanillaPoison", false));
        setRandomEffectStrength(config.getBoolean("configuration.randomEffectStrength", true));

        int i = 0;
        double temp = 0;
        for (i = 0; i < getEffects().length; i++) {
            getPercentages()[i] = config.getDouble("effects." + getEffects()[i]);
            temp += getPercentages()[i];
        }
        // If percentage is higher than 100, reset it, log it
        if (temp > 100) {
            for (i = 0; i < getPercentages().length; i++) {
                if (i == 1) {
                    getPercentages()[i] = 4.0;
                    config.set("effects." + getEffects()[i], 4.0);
                    continue;
                }
                getPercentages()[i] = 8.0;
                config.set("effects." + getEffects()[i], 8.0);
            }
            getLogger().warning(ChatColor.RED + "Detected that the entire procentage is higer than 100. Resetting to default...");
            saveConfig();
        }

        if (!isRandomEffectStrength()) {
            for (i = 0; i < getEffects().length; i++) {
                getEffectStrengths()[i] = config.getInt("effectStrength." + getEffects()[i], 0);
            }
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
        config.addDefault("configuration.randomEffectStrength", true);
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
        // EffectStrength
        config.addDefault("effectStrength.venom", 8);
        config.addDefault("effectStrength.hungervenom", 8);
        config.addDefault("effectStrength.confusion", 8);
        config.addDefault("effectStrength.blindness", 8);
        config.addDefault("effectStrength.weakness", 8);
        config.addDefault("effectStrength.slowness", 8);
        config.addDefault("effectStrength.slowness_blocks", 8);
        config.addDefault("effectStrength.instant_damage", 8);
        config.addDefault("effectStrength.wither", 8);
        //wither
        config.addDefault("food", Arrays.asList(rawFood));
        config.options().copyDefaults(true);
        saveConfig();
    }

    // Loads the localization
    private void loadLocalization() {
        getLocalization().options().header("The underscores are used for the different lines!");
        getLocalization().addDefault("damage", "&4You got some random damage! Eat some cooked food!");
        getLocalization().addDefault("hungervenom", "&4Your foodbar is a random time venomed! Eat some cooked food!");
        getLocalization().addDefault("death", "&4The raw food killed you! :(");
        getLocalization().addDefault("venom", "&4You are for a random time venomed! Eat some cooked food!");
        getLocalization().addDefault("hungerdecrease", "&4Your food level went down! Eat some cooked food!");
        getLocalization().addDefault("confusion", "&4You are for a random time confused! Eat some cooked food!");
        getLocalization().addDefault("blindness", "&4You are for a random time blind! Eat some cooked food!");
        getLocalization().addDefault("weakness", "&4You are for a random time weak! Eat some cooked food!");
        getLocalization().addDefault("slowness", "&4You are for a random time slower! Eat some cooked food!");
        getLocalization().addDefault("slowness_blocks", "&4You mine for a random time slower! Eat some cooked food!");
        getLocalization().addDefault("instant_damage", "&4You got some magic damage! Eat some cooked food!");
        getLocalization().addDefault("refusing", "&4You decided to save your life and didn't eat this food!");
        getLocalization().addDefault("permission_denied", "&4You don't have the permission to do this!");
        getLocalization().addDefault("enable_messages", "&2CookMe &4messages &2enabled!");
        getLocalization().addDefault("enable_permissions_1", "&2CookMe &4permissions &2enabled! Only OPs");
        getLocalization().addDefault("enable_permissions_2", "&2and players with the permission can use the plugin!");
        getLocalization().addDefault("disable_messages", "&2CookMe &4messages &2disabled!");
        getLocalization().addDefault("disable_permissions_1", "&2CookMe &4permissions &4disabled!");
        getLocalization().addDefault("disable_permissions_2", "&2All players can use the plugin!");
        getLocalization().addDefault("reload", "&2CookMe &4%version &2reloaded!");
        getLocalization().addDefault("changed_effect", "&2The percentage of the effect &e%effect &4 has been changed to &e%percentage%");
        getLocalization().addDefault("changed_cooldown", "&2The cooldown time has been changed to &e%value!");
        getLocalization().addDefault("changed_duration_max", "&2The maximum duration time has been changed to &e%value!");
        getLocalization().addDefault("changed_duration_min", "&2The minimum duration time has been changed to &e%value!");
        getLocalization().addDefault("help_1", "&2Welcome to the CookMe version &4%version &2help");
        getLocalization().addDefault("help_2", "To see the help type &4/cookme help");
        getLocalization().addDefault("help_3", "You can enable permissions and messages with &4/cookme enable &e<value> &fand &4/cookme disable &e<value>");
        getLocalization().addDefault("help_4", "To reload use &4/cookme reload");
        getLocalization().addDefault("help_5", "To change the cooldown or duration values, type");
        getLocalization().addDefault("help_6", "&4/cookme set cooldown <value> &for &4/cookme set duration min <value>");
        getLocalization().addDefault("help_7", "&4/cookme set duration max <value>");
        getLocalization().addDefault("help_8", "Set the percentages with &4/cookme set &e<value> <percentage>");
        getLocalization().addDefault("help_9", "&eValues: &fpermissions, messages, damage, death, venom,");
        getLocalization().addDefault("help_10", "hungervenom, hungerdecrease, confusion, blindness, weakness");
        getLocalization().addDefault("help_11", "slowness, slowness_blocks, instant_damage, refusing");
        getLocalization().addDefault("no_number", "&4The given argument wasn't a number!");
        getLocalization().options().copyDefaults(true);
        saveLocalization();
    }

    // Saves the localization
    private void saveLocalization() {
        try {
            getLocalization().save(localizationFile);
        } catch (IOException e) {
            getLogger().warning("Failed to save the localization! Please report this! IOException");
            e.printStackTrace();
        }
    }

    // Reloads the configs via command /cookme reload
    public void loadConfigsAgain() {
        try {
            config.load(configFile);
            saveConfig();
            checkStuff();
            getCooldownManager().setCooldown(getCooldown());
            getLocalization().load(localizationFile);
            saveLocalization();
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().warning("Failed to save the localization! Please report this!");
            e.printStackTrace();
        }
    }

    // If no config is found, copy the default one(s)!
    private void copy(String yml, File file) {
        try (OutputStream out = new FileOutputStream(file);
                InputStream in = getResource(yml)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            getLogger().warning("Failed to copy the default config! (I/O)");
            e.printStackTrace();
        }
    }

    // Message the sender or player
    public void message(CommandSender sender, Player player, String message, String value, String percentage) {
        value = value == null ? "" : value;
        percentage = percentage == null ? "" : percentage;
        PluginDescriptionFile pdfFile = getDescription();
        message = message.replace("\u0025version", pdfFile.getVersion())
                .replace("\u0025effect", value).replace("\u0025value", value)
                .replace("\u0025percentage", percentage);
        message = ChatColor.translateAlternateColorCodes('\u0026', message);
        if (player != null) {
            player.sendMessage(message);
        } else if (sender != null) {
            sender.sendMessage(message);
        }
    }

    public FileConfiguration getLocalization() {
        return localization;
    }

    public void setLocalization(FileConfiguration localization) {
        this.localization = localization;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public void setCooldownManager(CooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public boolean isPreventVanillaPoison() {
        return preventVanillaPoison;
    }

    public void setPreventVanillaPoison(boolean preventVanillaPoison) {
        this.preventVanillaPoison = preventVanillaPoison;
    }

    public boolean isMessages() {
        return messages;
    }

    public void setMessages(boolean messages) {
        this.messages = messages;
    }

    public boolean isRandomEffectStrength() {
        return randomEffectStrength;
    }

    public void setRandomEffectStrength(boolean randomEffectStrength) {
        this.randomEffectStrength = randomEffectStrength;
    }

    public double[] getPercentages() {
        return percentages;
    }

    public void setPercentages(double[] percentages) {
        this.percentages = percentages;
    }

    public int getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(int minDuration) {
        this.minDuration = minDuration;
    }

    public int[] getEffectStrengths() {
        return effectStrengths;
    }

    public void setEffectStrengths(int[] effectStrengths) {
        this.effectStrengths = effectStrengths;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public List<String> getItemList() {
        return itemList;
    }

    public void setItemList(List<String> itemList) {
        this.itemList = itemList;
    }

    public boolean isPermissions() {
        return permissions;
    }

    public void setPermissions(boolean permissions) {
        this.permissions = permissions;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String[] getEffects() {
        return effects;
    }

    public void setEffects(String[] effects) {
        this.effects = effects;
    }
}

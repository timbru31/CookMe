package de.dustplanet.cookme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bstats.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;

/**
 * CookeMe for CraftBukkit/Spigot.
 * Handles some general stuff!
 *
 * Refer to the dev.bukkit.org page:
 * https://dev.bukkit.org/projects/cookme/
 *
 * @author xGhOsTkiLLeRx
 * thanks nisovin for his awesome code snippet
 *
 */

public class CookMe extends JavaPlugin {
    @Getter
    @Setter
    private CooldownManager cooldownManager;
    @Getter
    private FileConfiguration config, localization;
    private File configFile, localizationFile;
    @Getter
    @Setter
    private List<String> itemList = new ArrayList<>();
    @Getter
    @Setter
    private int cooldown, minDuration, maxDuration;
    @Getter
    @Setter
    private boolean debug, messages, permissions, preventVanillaPoison, randomEffectStrength;
    private String[] rawFood = { "RAW_BEEF", "RAW_CHICKEN", "RAW_FISH", "PORK", "ROTTEN_FLESH", "MUTTON", "RABBIT" };
    private String[] effects = { "damage", "death", "venom", "hungervenom", "hungerdecrease", "confusion", "blindness",
            "weakness", "slowness", "slowness_blocks", "instant_damage", "refusing", "wither", "levitation", "unluck" };
    private double[] percentages = new double[effects.length];
    private int[] effectStrengths = new int[effects.length];

    @Override
    public void onDisable() {
        getItemList().clear();
        getCooldownManager().clearCooldownList();
    }

    @Override
    public void onEnable() {
        CookMePlayerListener playerListener = new CookMePlayerListener(this);
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);

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

        setCooldownManager(new CooldownManager(getCooldown()));

        localizationFile = new File(getDataFolder(), "localization.yml");
        if (!localizationFile.exists()) {
            copy("localization.yml", localizationFile);
        }
        localization = ScalarYamlConfiguration.loadConfiguration(localizationFile);
        loadLocalization();

        getCommand("cookme").setExecutor(new CookMeCommands(this));

        Metrics metrics = new Metrics(this);
        metrics.addCustomChart(new Metrics.AdvancedPie("percentage_of_affected_items") {
            @Override
            public HashMap<String, Integer> getValues(HashMap<String, Integer> valueMap) {
                for (String itemName : getItemList()) {
                    valueMap.put(itemName, 1);
                }
                return valueMap;
            }});
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
            percentages[i] = config.getDouble("effects." + getEffects()[i]);
            temp += percentages[i];
        }

        if (Math.round(temp) * 100.0 / 100 > 100) {
            for (i = 0; i < getPercentages().length; i++) {
                if (i == 1) {
                    percentages[i] = 3.4;
                    config.set("effects." + getEffects()[i], 3.4);
                    continue;
                }
                percentages[i] = 6.9;
                config.set("effects." + getEffects()[i], 6.9);
            }
            getLogger().warning(
                    ChatColor.RED + "Detected that the entire procentage is higer than 100. Resetting to default...");
            saveConfig();
        }

        if (!isRandomEffectStrength()) {
            for (i = 0; i < getEffects().length; i++) {
                getEffectStrengths()[i] = config.getInt("effectStrength." + getEffects()[i], 0);
            }
        }
    }

    private void loadConfig() {
        config.options().header("For help please refer to https://dev.bukkit.org/projects/cookme/");
        config.addDefault("configuration.permissions", true);
        config.addDefault("configuration.messages", true);
        config.addDefault("configuration.duration.min", 15);
        config.addDefault("configuration.duration.max", 30);
        config.addDefault("configuration.cooldown", 30);
        config.addDefault("configuration.debug", false);
        config.addDefault("configuration.preventVanillaPoison", false);
        config.addDefault("configuration.randomEffectStrength", true);
        config.addDefault("effects.damage", 6.9);
        config.addDefault("effects.death", 3.4);
        config.addDefault("effects.venom", 6.9);
        config.addDefault("effects.hungervenom", 6.9);
        config.addDefault("effects.hungerdecrease", 6.9);
        config.addDefault("effects.confusion", 6.9);
        config.addDefault("effects.blindness", 6.9);
        config.addDefault("effects.weakness", 6.9);
        config.addDefault("effects.slowness", 6.9);
        config.addDefault("effects.slowness_blocks", 6.9);
        config.addDefault("effects.instant_damage", 6.9);
        config.addDefault("effects.refusing", 6.9);
        config.addDefault("effects.wither", 6.9);
        config.addDefault("effects.levitation", 6.9);
        config.addDefault("effects.unluck", 6.9);
        config.addDefault("effectStrength.venom", 8);
        config.addDefault("effectStrength.hungervenom", 8);
        config.addDefault("effectStrength.confusion", 8);
        config.addDefault("effectStrength.blindness", 8);
        config.addDefault("effectStrength.weakness", 8);
        config.addDefault("effectStrength.slowness", 8);
        config.addDefault("effectStrength.slowness_blocks", 8);
        config.addDefault("effectStrength.instant_damage", 8);
        config.addDefault("effectStrength.wither", 8);
        config.addDefault("effectStrength.levitation", 8);
        config.addDefault("effectStrength.unluck", 8);
        config.addDefault("food", Arrays.asList(rawFood));
        config.options().copyDefaults(true);
        saveConfig();
    }

    private void loadLocalization() {
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
        getLocalization().addDefault("refusing", "&4You decided to save your life and did not eat this food!");
        getLocalization().addDefault("wither", "&4The poison of the wither weakens you! Eat some cooked food!");
        getLocalization().addDefault("levitation", "&4You fly up high in the air! Eat some cooked food!");
        getLocalization().addDefault("unluck", "&4You are for a random time unlucky! Eat some cooked food!");
        getLocalization().addDefault("permission_denied", "&4You do not have the permission to do this!");
        getLocalization().addDefault("enable_messages", "&2CookMe &4messages &2enabled!");
        getLocalization().addDefault("enable_permissions", "&2CookMe &4permissions &2enabled!\n" +
                "&2Only OPs and players with the permission can use the plugin!");
        getLocalization().addDefault("disable_messages", "&2CookMe &4messages &2disabled!");
        getLocalization().addDefault("disable_permissions", "&2CookMe &4permissions &4disabled!\n" +
                "&4All players can use the plugin!");
        getLocalization().addDefault("reload", "&2CookMe &4%version &2reloaded!");
        getLocalization().addDefault("changed_effect",
                "&2The percentage of the effect &e%effect &4has been changed to &e%percentage%");
        getLocalization().addDefault("changed_cooldown", "&2The cooldown time has been changed to &e%value!");
        getLocalization().addDefault("changed_duration_max",
                "&2The maximum duration time has been changed to &e%value!");
        getLocalization().addDefault("changed_duration_min",
                "&2The minimum duration time has been changed to &e%value!");
        getLocalization().addDefault("help", "&2Welcome to the CookMe version &4%version &2help\n" +
                "To see the help type &4/cookme help\n" +
                "You can enable permissions and messages with &4/cookme enable &e<value> &fand &4/cookme disable &e<value>\n" +
                "To reload use &4/cookme reload\n" +
                "To change the cooldown or duration values, type\n" +
                "&4/cookme set cooldown <value>&f, &4/cookme set duration min <value> &for\n" +
                "&4/cookme set duration max <value>\n" +
                "Set the percentages with &4/cookme set &e<value> <percentage>\n" +
                "&eValues: &fpermissions, messages, damage, death, venom,\n" +
                "hungervenom, hungerdecrease, confusion, blindness, weakness\n" +
                "slowness, slowness_blocks, instant_damage, refusing, wither, levitation, unluck");
        getLocalization().addDefault("no_number", "&4The given argument was not a number!");
        getLocalization().options().copyDefaults(true);
        saveLocalization();
    }

    private void saveLocalization() {
        try {
            getLocalization().save(localizationFile);
        } catch (IOException e) {
            getLogger().warning("Failed to save the localization! Please report this! IOException");
            e.printStackTrace();
        }
    }

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

    private void copy(String yml, File file) {
        try (OutputStream out = new FileOutputStream(file); InputStream in = getResource(yml)) {
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

    public void message(CommandSender sender, Player player, String message, String value, String percentage) {
        String tempValue = value == null ? "" : value;
        String tempPercentage = percentage == null ? "" : percentage;
        PluginDescriptionFile pdfFile = getDescription();
        String tempMessage = message.replace("\u0025version", pdfFile.getVersion()).replace("\u0025effect", tempValue)
                .replace("\u0025value", tempValue).replace("\u0025percentage", tempPercentage);
        String newMessage[] = ChatColor.translateAlternateColorCodes('\u0026', tempMessage).split("\n");
        if (player != null) {
            player.sendMessage(newMessage);
        } else if (sender != null) {
            sender.sendMessage(newMessage);
        }
    }

    public double[] getPercentages() {
        return percentages.clone();
    }

    public void setPercentages(double[] percentages) {
        this.percentages = percentages.clone();
    }

    public int[] getEffectStrengths() {
        return effectStrengths.clone();
    }

    public void setEffectStrengths(int[] effectStrengths) {
        this.effectStrengths = effectStrengths.clone();
    }

    public String[] getEffects() {
        return effects.clone();
    }

    public void setEffects(String[] effects) {
        this.effects = effects.clone();
    }
}

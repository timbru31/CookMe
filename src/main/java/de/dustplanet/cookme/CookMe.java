package de.dustplanet.cookme;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedPie;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;

@SuppressFBWarnings({ "IMC_IMMATURE_CLASS_NO_TOSTRING", "FCCD_FIND_CLASS_CIRCULAR_DEPENDENCY", "CD_CIRCULAR_DEPENDENCY" })
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class CookMe extends JavaPlugin {
    @Getter
    @Setter
    private CooldownManager cooldownManager;
    private FileConfiguration config;
    @Getter
    private FileConfiguration localization;
    private File configFile;
    private File localizationFile;
    @Getter
    @Setter
    private List<String> itemList = new ArrayList<>();
    @Getter
    @Setter
    private int cooldown;
    @Getter
    @Setter
    private int minDuration;
    @Getter
    @Setter
    private int maxDuration;
    @Getter
    @Setter
    private boolean debug;
    @Getter
    @Setter
    private boolean messages;
    @Getter
    @Setter
    private boolean permissions;
    @Getter
    @Setter
    private boolean preventVanillaPoison;
    @Getter
    @Setter
    private boolean randomEffectStrength;
    private final String[] rawFood = { "BEEF", "CHICKEN", "PORKCHOP", "ROTTEN_FLESH", "MUTTON", "RABBIT", "COD", "SALMON", "PUFFERFISH" };
    private String[] effects = { "damage", "death", "venom", "hungervenom", "hungerdecrease", "confusion", "blindness", "weakness",
            "slowness", "slowness_blocks", "instant_damage", "refusing", "wither", "levitation", "unluck", "bad_omen" };
    private double[] percentages = new double[effects.length];
    private int[] effectStrengths = new int[effects.length];

    private static final int BUFFER_SIZE = 1024;
    private static final int BSTATS_PLUGIN_ID = 279;
    private static final double EFFECT_PERCENTAGE = 6.25;
    private static final int SECOND_IN_MILLIS = 20;

    @Override
    public void onDisable() {
        getItemList().clear();
        getCooldownManager().clearCooldownList();
    }

    @Override
    public void onEnable() {
        final CookMePlayerListener playerListener = new CookMePlayerListener(this);
        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(playerListener, this);

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

        final Metrics metrics = new Metrics(this, BSTATS_PLUGIN_ID);
        metrics.addCustomChart(new AdvancedPie("percentage_of_affected_items", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() throws Exception {
                @SuppressWarnings("PMD.UseConcurrentHashMap")
                final Map<String, Integer> valueMap = new HashMap<>();
                for (final String itemName : getItemList()) {
                    valueMap.put(itemName, 1);
                }
                return valueMap;
            }
        }));

    }

    private void checkStuff() {
        setPermissions(config.getBoolean("configuration.permissions", true));
        setMessages(config.getBoolean("configuration.messages", true));
        setCooldown(config.getInt("configuration.cooldown", 30));
        setMinDuration(SECOND_IN_MILLIS * config.getInt("configuration.duration.min", 15));
        setMaxDuration(SECOND_IN_MILLIS * config.getInt("configuration.duration.max", 30));
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
                percentages[i] = EFFECT_PERCENTAGE;
                config.set("effects." + getEffects()[i], EFFECT_PERCENTAGE);
            }
            getLogger().warning("Detected that the entire procentage is higer than 100. Resetting to default...");
            saveConfig();
        }

        if (!isRandomEffectStrength()) {
            for (i = 0; i < getEffects().length; i++) {
                getEffectStrengths()[i] = config.getInt("effectStrength." + getEffects()[i], 0);
            }
        }
    }

    private void loadConfig() {
        config.options().setHeader(Collections.singletonList("For help please refer to https://dev.bukkit.org/projects/cookme/"));
        config.addDefault("configuration.permissions", true);
        config.addDefault("configuration.messages", true);
        config.addDefault("configuration.duration.min", 15);
        config.addDefault("configuration.duration.max", 30);
        config.addDefault("configuration.cooldown", 30);
        config.addDefault("configuration.debug", false);
        config.addDefault("configuration.preventVanillaPoison", false);
        config.addDefault("configuration.randomEffectStrength", true);
        config.addDefault("effects.damage", EFFECT_PERCENTAGE);
        config.addDefault("effects.death", EFFECT_PERCENTAGE);
        config.addDefault("effects.venom", EFFECT_PERCENTAGE);
        config.addDefault("effects.hungervenom", EFFECT_PERCENTAGE);
        config.addDefault("effects.hungerdecrease", EFFECT_PERCENTAGE);
        config.addDefault("effects.confusion", EFFECT_PERCENTAGE);
        config.addDefault("effects.blindness", EFFECT_PERCENTAGE);
        config.addDefault("effects.weakness", EFFECT_PERCENTAGE);
        config.addDefault("effects.slowness", EFFECT_PERCENTAGE);
        config.addDefault("effects.slowness_blocks", EFFECT_PERCENTAGE);
        config.addDefault("effects.instant_damage", EFFECT_PERCENTAGE);
        config.addDefault("effects.refusing", EFFECT_PERCENTAGE);
        config.addDefault("effects.wither", EFFECT_PERCENTAGE);
        config.addDefault("effects.levitation", EFFECT_PERCENTAGE);
        config.addDefault("effects.unluck", EFFECT_PERCENTAGE);
        config.addDefault("effects.bad_omen", EFFECT_PERCENTAGE);
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
        getLocalization().addDefault("bad_omen", "'&4You feel a bad omen! Eat some cooked food!");
        getLocalization().addDefault("permission_denied", "&4You do not have the permission to do this!");
        getLocalization().addDefault("enable_messages", "&2CookMe &4messages &2enabled!");
        getLocalization().addDefault("enable_permissions",
                "&2CookMe &4permissions &2enabled!\n" + "&2Only OPs and players with the permission can use the plugin!");
        getLocalization().addDefault("disable_messages", "&2CookMe &4messages &2disabled!");
        getLocalization().addDefault("disable_permissions", "&2CookMe &4permissions &4disabled!\n" + "&4All players can use the plugin!");
        getLocalization().addDefault("reload", "&2CookMe &4%version &2reloaded!");
        getLocalization().addDefault("changed_effect", "&2The percentage of the effect &e%effect &4has been changed to &e%percentage%");
        getLocalization().addDefault("changed_cooldown", "&2The cooldown time has been changed to &e%value!");
        getLocalization().addDefault("changed_duration_max", "&2The maximum duration time has been changed to &e%value!");
        getLocalization().addDefault("changed_duration_min", "&2The minimum duration time has been changed to &e%value!");
        getLocalization().addDefault("help",
                "&2Welcome to the CookMe version &4%version &2help\n" + "To see the help type &4/cookme help\n"
                        + "You can enable permissions and messages with &4/cookme enable &e<value> &fand &4/cookme disable &e<value>\n"
                        + "To reload use &4/cookme reload\n" + "To change the cooldown or duration values, type\n"
                        + "&4/cookme set cooldown <value>&f, &4/cookme set duration min <value> &for\n"
                        + "&4/cookme set duration max <value>\n" + "Set the percentages with &4/cookme set &e<value> <percentage>\n"
                        + "&eValues: &fpermissions, messages, damage, death, venom,\n"
                        + "hungervenom, hungerdecrease, confusion, blindness, weakness\n"
                        + "slowness, slowness_blocks, instant_damage, refusing, wither, levitation, unluck");
        getLocalization().addDefault("no_number", "&4The given argument was not a number!");
        getLocalization().options().copyDefaults(true);
        saveLocalization();
    }

    private void saveLocalization() {
        try {
            getLocalization().save(localizationFile);
        } catch (final IOException e) {
            getLogger().log(Level.WARNING, "Failed to save the localization! Please report this! IOException", e);
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
            getLogger().log(Level.WARNING, "Failed to save the localization! Please report this!", e);
        }
    }

    @SuppressWarnings({ "PMD.AssignmentInOperand", "PMD.DataflowAnomalyAnalysis" })
    @SuppressFBWarnings({ "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE", "RCN_REDUNDANT_NULLCHECK_OF_NULL_VALUE",
            "NP_LOAD_OF_KNOWN_NULL_VALUE", "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE", "RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE" })
    public void copy(final String yml, final File file) {
        try (OutputStream out = Files.newOutputStream(file.toPath()); InputStream inputStream = getResource(yml)) {
            final byte[] buf = new byte[BUFFER_SIZE];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (final IOException e) {
            getLogger().log(Level.WARNING, "Failed to copy the default config!", e);
        }
    }

    public void message(final CommandSender sender, final Player player, final String message, final String value,
            final String percentage) {
        final String tempValue = value == null ? "" : value;
        final String tempPercentage = percentage == null ? "" : percentage;
        final PluginDescriptionFile pdfFile = getDescription();
        final String tempMessage = message.replace("\u0025version", pdfFile.getVersion()).replace("\u0025effect", tempValue)
                .replace("\u0025value", tempValue).replace("\u0025percentage", tempPercentage);
        final String[] newMessage = ChatColor.translateAlternateColorCodes('\u0026', tempMessage).split("\n");
        if (player != null) {
            player.sendMessage(newMessage);
        } else if (sender != null) {
            sender.sendMessage(newMessage);
        }
    }

    public double[] getPercentages() {
        return percentages.clone();
    }

    public void setPercentages(final double... percentages) {
        this.percentages = percentages.clone();
    }

    public int[] getEffectStrengths() {
        return effectStrengths.clone();
    }

    public void setEffectStrengths(final int... effectStrengths) {
        this.effectStrengths = effectStrengths.clone();
    }

    public String[] getEffects() {
        return effects.clone();
    }

    public void setEffects(final String... effects) {
        this.effects = effects.clone();
    }
}

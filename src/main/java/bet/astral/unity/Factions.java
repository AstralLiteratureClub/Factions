package bet.astral.unity;

import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.messenger.Messenger;
import bet.astral.messenger.message.MessageType;
import bet.astral.messenger.message.adventure.AdventureMessage;
import bet.astral.messenger.message.adventure.part.AdventureMessagePart;
import bet.astral.messenger.message.adventure.serializer.ComponentTypeSerializer;
import bet.astral.messenger.message.message.IMessage;
import bet.astral.messenger.message.part.DefaultMessagePart;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.messenger.translation.TranslationKey;
import bet.astral.shine.ShineHandler;
import bet.astral.unity.commands.core.Init;
import bet.astral.unity.configuration.Config;
import bet.astral.unity.configuration.FactionConfig;
import bet.astral.unity.database.Database;
import bet.astral.unity.database.impl.DatabaseType;
import bet.astral.unity.database.impl.sql.source.HikariDatabaseSource;
import bet.astral.unity.database.impl.sql.source.SQLDatabaseSource;
import bet.astral.unity.database.model.HikariLoginMaster;
import bet.astral.unity.database.model.LoginMaster;
import bet.astral.unity.database.model.SQLLoginMaster;
import bet.astral.unity.handlers.ChatHandler;
import bet.astral.unity.managers.FactionManager;
import bet.astral.unity.managers.PlayerManager;
import bet.astral.unity.messenger.FactionPlaceholderManager;
import bet.astral.unity.messenger.TranslationKeys;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.Command;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.incendo.cloud.minecraft.extras.RichDescription;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static bet.astral.unity.utils.Resource.loadResourceAsTemp;
import static bet.astral.unity.utils.Resource.loadResourceToFile;

@Getter
public final class Factions extends JavaPlugin implements CommandRegisterer<Factions> {
    private static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    private final ShineHandler shineHandler = new ShineHandler(this);

    private PaperCommandManager<CommandSender> commandManager;
    // faction
    private Command.Builder<CommandSender> rootFactionCommand;
    // faction force
    private Command.Builder<CommandSender> rootForceFactionCommand;

    // faction ally
    private Command.Builder<CommandSender> rootFactionAllyCommand;
    // faction force ally
    private Command.Builder<CommandSender> rootFactionForceAllyCommand;
    // /ally
    private Command.Builder<CommandSender> rootAllyCommand;
    // /ally force
    private Command.Builder<CommandSender> rootAllyForceCommand;
    private MinecraftHelp<CommandSender> rootHelp;
    private MinecraftHelp<CommandSender> allyRootHelp;
    private Messenger<Factions> messenger;
    private Database database;
    private PlayerManager playerManager;
    private FactionManager factionManager;
    private ChatHandler chatHandler;
    private FactionConfig factionConfig;
    private DateFormat dateFormat;
    private boolean commandsRegistered = false;
    private final boolean isFolia = isFolia();
    private boolean isDebug = false;

    @Override
    public void onEnable() {
        uploadUploads();
        reloadConfig();
        Config config = new Config(super.getConfig());
        isDebug = super.getConfig().getBoolean("isDebug", false);

        shineHandler.onEnable();

        factionConfig = new FactionConfig(getConfig(new File(getDataFolder(), "config.yml")));

        playerManager = new PlayerManager(this);
        factionManager = new FactionManager(this);

        config.setIfNotString("database.type", "SQLite");
        config.setIfNotString("database.ip", "198.0.0.0.1");
        config.setIfNotInt("database.ip", 123456);
        config.setIfNotString("database.ip", "UnityFactions");
        config.setIfNotString("database.user", "root");
        config.setIfNotString("database.password", "MakeStrongPasswords");
        config.setIfNotLong("database.timeout", 10000L);
        config.setIfNotInt("database.hikari.minimum", 1);
        config.setIfNotInt("database.hikari.maximum", 1);
        try {
            config.save(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LoginMaster loginMaster;
        String databaseType = getConfig().getString("database.type");
        if (databaseType == null) {
            setEnabled(false);
            throw new IllegalStateException("The plugin cannot initialize, as the database type is not correct!");
        }
        DatabaseType databaseTypeEnum = DatabaseType.valueOf(databaseType.toUpperCase());
        switch (databaseTypeEnum) {
            case MYSQL -> {
                database = new HikariDatabaseSource(this, databaseTypeEnum);
                loginMaster = HikariLoginMaster.load(new Config(
                        (MemorySection) config.get("database")));
            }
            case MONGODB -> {
                getLogger().severe("Mongodb is not supported currently! Please use MySQL or SQLite for now! Disabling the plugin!");
                setEnabled(false);
                return;
            }
            case SQLITE -> {
                database = new SQLDatabaseSource(this, databaseTypeEnum);
                loginMaster = SQLLoginMaster.load(new Config((MemorySection) config.get("database")));
            }
            default -> {
                getLogger().severe("Currently " + getName() + " does not support the database type " + databaseType + ". Please try another database type! Disabling the plugin!");
                setEnabled(false);
                return;
            }
        }

        try {
//            database.connect(loginMaster);
        } catch (IllegalArgumentException e){
            getComponentLogger().error("Couldn't enable UNITY as database login master is not correct!", e);
            getServer().getPluginManager().disablePlugin(this);
        }
        // TODO enable connecting to database
//        database.connect(loginMaster);

        commandManager = new PaperCommandManager<>(this, ExecutionCoordinator.asyncCoordinator(), SenderMapper.identity());

        // Reload messengers
        reloadMessengers();

        FileConfiguration messengerConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "messages.yml"));
//        messenger = new Messenger<>(this, new HashMap<>(), new IMessageTypeSerializer<CommandSender>(), messengerConfig);
//        debugMessenger = new Messenger<>(this, messengerConfig, new HashMap<>());
        dateFormat = new SimpleDateFormat(messengerConfig.getString(TranslationKeys.DATE_FORMAT.key(), "mm:HH dd/MM/yyyy"));
        messenger = new Messenger<>(this, commandManager, new HashMap<>(), new ComponentTypeSerializer(), messengerConfig);
        FactionPlaceholderManager placeholderManager = new FactionPlaceholderManager();
        placeholderManager.setDefaults(placeholderManager.loadPlaceholders("placeholders", messengerConfig));
        messenger.setPlaceholderManager(placeholderManager);

        for (Field field : TranslationKeys.class.getFields()) {
            try {
                if (field.isAnnotationPresent(TranslationKeys.IsCaption.class) && field.getAnnotation(TranslationKeys.IsCaption.class).value()) {
                    Caption caption = (Caption) field.get(null);
                    IMessage<?, Component> message = messenger.loadMessage(caption.key());
                    if (message == null || message.parts().get(MessageType.CHAT) == null) {
                        messengerConfig.set(caption.key(), caption.key());
                    }
                } else {
                    String fieldValue = (String) field.get(null);
                    messenger.loadMessage(fieldValue);
                    IMessage<?, Component> message = messenger.loadMessage(fieldValue);
                    if (message == null || message.parts().get(MessageType.CHAT) == null) {
                        messengerConfig.set(fieldValue, fieldValue);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            messengerConfig.save(new File(getDataFolder(), "messages.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Init rootCommand = new Init(this, commandManager);


        // /faction
        rootFactionCommand = rootCommand.root;
        // /faction force
        rootForceFactionCommand = rootCommand.rootForceFaction;
        // /alliance
        rootAllyCommand = rootCommand.rootAlly;
        // /alliance force
        rootAllyForceCommand = rootCommand.rootForceAlly;
        // /factions alliance
        rootFactionAllyCommand = rootCommand.rootFactionAlly;
        // /factions force alliance
        rootFactionForceAllyCommand = rootCommand.rootForceFactionAlly;


        rootHelp = rootCommand.help;
        allyRootHelp = rootCommand.helpAlly;

        registerCommands("bet.astral.unity.commands", commandManager);

        registerChatHandler((player, faction, receiver, message, type) -> {
            PlaceholderList placeholders = new PlaceholderList();
            placeholders.addAll(placeholderManager.offlinePlayerPlaceholders("player", player.offlinePlayer()));
            placeholders.addAll(placeholderManager.factionPlaceholders("faction", faction));
            placeholders.add("message", message);
            IMessage<?, Component> iMessage = null;
            switch (type) {
                case FACTION -> {
                    iMessage = messenger.getMessage(TranslationKeys.FORMAT_CHAT);
                }
                case ALLY -> {
                    iMessage = messenger.getMessage(TranslationKeys.FORMAT_ALLY_CHAT);
                }
            }
            if (iMessage == null || iMessage.parts().get(MessageType.CHAT) == null) {
                return null;
            }

            return messenger.parse(iMessage, MessageType.CHAT, placeholders);
        });

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            PlaceholderAPIPlugin.getInstance().getLocalExpansionManager()
                    .register(new UnityPlaceholderExpansion(this));
        }

        getLogger().info("Factions has enabled!");
    }

    @Override
    public @NotNull ComponentLogger getComponentLogger() {
        return super.getComponentLogger();
    }

    @Override
    public @NotNull Logger getSLF4JLogger() {
        return super.getSLF4JLogger();
    }


    @Override
    public void onDisable() {
        shineHandler.onDisable();
        if (database != null){
            if (database.isConnected()){
                // TODO
                database.disconnect();
                getLogger().info("Disabling the database..!");
            }
        }
        getLogger().info("Factions has disabled!");
    }

    public void registerChatHandler(ChatHandler chatHandler){
        this.chatHandler = chatHandler;
    }

    @NotNull
    public ChatHandler allyChatHandler(){
        return chatHandler;
    }

    public Component formatDate(Date date){
        return MiniMessage.miniMessage().deserialize(dateFormat.format(date));
    }
    public Component formatDate(long date){
        return MiniMessage.miniMessage().deserialize(dateFormat.format(Date.from(Instant.ofEpochMilli(date))));
    }

    private void uploadUploads(){
        String[] files = new String[]{
                "config|yml",
                "messages|yml",
        };
        for (String name : files){
            name = name.replace("dm/", "discord-messages/");

            String[] split = name.split("\\|");
            String fileName = split[0];
            String ending = split[1];
            File fileTemp = loadResourceAsTemp("/upload/"+fileName, ending);
            File file = loadResourceToFile("/upload/"+fileName, ending, new File(getDataFolder(), fileName+"."+ending), true);
            if (ending.matches("(?i)yml") || ending.matches("(?i)yaml")){
                loadConfig(getConfig(fileTemp), getConfig(file), file);
            }
        }
    }

    private void loadConfig(FileConfiguration tempConfig, FileConfiguration config, File file){
        Set<String> keys = tempConfig.getKeys(false);
        for (String key : keys){
            addDefaults(key, tempConfig, config);
        }
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addDefaults(String key, Configuration tempConfig, Configuration config) {
        List<String> comment = tempConfig.getComments(key);
        if (!comment.isEmpty() && config.getInlineComments(key).isEmpty()) {
            config.setComments(key, comment);
        }
        comment = tempConfig.getInlineComments(key);
        if (!comment.isEmpty() && config.getInlineComments(key).isEmpty()) {
            config.setInlineComments(key, comment);
        }
        Object value = tempConfig.get(key); // Retrieve the value from the tempConfig
        if (value instanceof ConfigurationSection section) {
            for (String k : section.getKeys(false)) {
                addDefaults(key + "." + k, tempConfig, config); // Append current key
            }
        }
    }


    private FileConfiguration getConfig(File file){
        return YamlConfiguration.loadConfiguration(file);
    }


    @ApiStatus.Internal
    @Override
    public void reloadMessengers() {
//        CommandRegisterer.super.reloadMessengers();
    }

    @Override
    public Factions plugin() {
        return this;
    }

    @Override
    public Messenger<Factions> commandMessenger() {
        return messenger;
    }

    @Override
    public Messenger<Factions> debugMessenger() {
        return messenger;
    }

    @Override
    public boolean isDebug() {
        return false;
    }

    @Override
    public void registerCommands(List<String> packages, PaperCommandManager<?> commandManager) {
        CommandRegisterer.super.registerCommands(packages, commandManager);
    }

    @Override
    public boolean cannotInject(Class<?> clazz) {
        return CommandRegisterer.super.cannotInject(clazz);
    }

    @Override
    public void registerCommand(Class<?> clazz, PaperCommandManager<?> commandManager) {
        CommandRegisterer.super.registerCommand(clazz, commandManager);
    }

    @Override
    public Constructor<?> getConstructor(@NotNull Class<?> clazz, Class<?>... params) throws NoSuchMethodException {
        return CommandRegisterer.super.getConstructor(clazz, params);
    }


    public RichDescription loadDescription(@NotNull TranslationKey translationKey, String command, Placeholder... placeholders){
        PlaceholderList placeholderList = new PlaceholderList();
        placeholderList.add("command", command);
        placeholderList.addAll(placeholders);
        IMessage<?, Component> message = messenger.getMessage(translationKey); // getMessage(Caption)
        if (message == null){
            message = new AdventureMessage(translationKey.key(), new AdventureMessagePart(MessageType.CHAT, translationKey.asComponent()));
        }
        Component component = messenger.parse(message, MessageType.CHAT, placeholderList);
        if (component == null){
            return null;
        }
        return RichDescription.of(component);
    }
    @Deprecated(forRemoval = true)
    public RichDescription loadDescription(String name, String command, Placeholder... placeholders){
        PlaceholderList placeholderList = new PlaceholderList();
        placeholderList.add("command", command);
        placeholderList.addAll(placeholders);
        IMessage<?, Component> message = messenger.getMessage(name);
        if (message == null){
            message = new AdventureMessage(name, new DefaultMessagePart<>(MessageType.CHAT, Component.text(name)));
        }
        Component component = messenger.parse(message, MessageType.CHAT, placeholderList);
        if (component == null){
            return null;
        }
        return RichDescription.of(component);
    }

    public Messenger<Factions> messenger(){
        return messenger;
    }
}

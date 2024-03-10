package bet.astral.unity;

import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.messenger.Message;
import bet.astral.messenger.Messenger;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.configuration.FactionConfig;
import bet.astral.unity.database.PlayerDatabase;
import bet.astral.unity.handlers.ChatHandler;
import bet.astral.unity.commands.root.FactionRootCommands;
import bet.astral.unity.managers.FactionManager;
import bet.astral.unity.managers.PlayerManager;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.TranslationKey;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
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
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static bet.astral.unity.utils.Resource.loadResourceAsTemp;
import static bet.astral.unity.utils.Resource.loadResourceToFile;

@Getter
public final class Factions extends JavaPlugin implements CommandRegisterer<Factions> {
    private PaperCommandManager<CommandSender> commandManager;
    private Command.Builder<CommandSender> rootFactionCommand;
    private Command.Builder<CommandSender> rootForceFactionCommand;
    private Command.Builder<CommandSender> rootAllyCommand;
    private MinecraftHelp<CommandSender> minecraftHelp;
    private Messenger<Factions> messenger;
    private Messenger<Factions> debugMessenger;
    // TODO Implement this database
    private PlayerDatabase playerDatabase;
    private PlayerManager playerManager;
    private FactionManager factionManager;
    private ChatHandler chatHandler;
    private FactionConfig factionConfig;

    @Override
    public void onEnable() {
        uploadUploads();
        factionConfig = new FactionConfig(getConfig(new File(getDataFolder(), "config.yml")));

        reloadMessengers();
        for (Field field : TranslationKey.class.getFields()){
	        try {
                if (field.isAnnotationPresent(TranslationKey.IsCaption.class) && field.getAnnotation(TranslationKey.IsCaption.class).value()){
                    Caption caption = (Caption) field.get(null);
                    messenger.loadMessage(caption.key());
                } else {
                    String fieldValue = (String) field.get(null);
                    messenger.loadMessage(fieldValue);
                }
	        } catch (IllegalAccessException e) {
		        throw new RuntimeException(e);
	        }
        }

        playerManager = new PlayerManager(this);
        factionManager = new FactionManager(this);

        commandManager = new PaperCommandManager<>(this, ExecutionCoordinator.asyncCoordinator(), SenderMapper.identity());
        FactionRootCommands rootCommand = new FactionRootCommands(this, commandManager);
        rootFactionCommand = rootCommand.root;
        rootForceFactionCommand = rootCommand.rootForceFaction;
        rootAllyCommand = rootCommand.rootAlly;
        minecraftHelp = rootCommand.help;

        registerChatHandler((player, faction, receiver, message, type) -> {
            PlaceholderList placeholders = new PlaceholderList();
            placeholders.addAll(messenger.createPlaceholders(player.player()));
            placeholders.addAll(Faction.factionPlaceholders("faction", faction));
            placeholders.add("message", message);
            Message messengerMessage = null;
            switch (type){
                case FACTION -> {
                    messengerMessage = messenger.getMessage(TranslationKey.FORMAT_CHAT);
                }
                case ALLY ->  {
                    messengerMessage = messenger.getMessage(TranslationKey.FORMAT_ALLY_CHAT);
                }
            }
            if (messengerMessage== null){
                return null;
            }

	        return messenger.parse(messengerMessage, Message.Type.CHAT, placeholders);
        });

        getLogger().info("Loading commands..!");
        registerCommands(List.of(
                "bet.astral.unity.commands.basic",
                "bet.astral.unity.commands.invite"
                ), commandManager);
        reloadMessengers();
        getLogger().info("Loaded commands!");

        getLogger().info("Factions has enabled!");
    }

    @Override
    public void onDisable() {

        getLogger().info("Factions has disabled!");
    }

    public void registerChatHandler(ChatHandler chatHandler){
        this.chatHandler = chatHandler;
    }

    @NotNull
    public ChatHandler allyChatHandler(){
        return chatHandler;
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


    @Override
    public void reloadMessengers() {
        FileConfiguration messengerConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "messages.yml"));
        messenger = new Messenger<>(this, messengerConfig, new HashMap<>());
        debugMessenger = new Messenger<>(this, messengerConfig, new HashMap<>());
        messenger.overrideDefaultPlaceholders(messenger.loadPlaceholders("placeholders"));
        debugMessenger.overrideDefaultPlaceholders(debugMessenger.loadPlaceholders("placeholders"));

        CommandRegisterer.super.reloadMessengers();
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
        return debugMessenger;
    }



    public RichDescription loadDescription(String name, String command){
        PlaceholderList placeholders = new PlaceholderList();
        placeholders.add("command", command);
        Message message = messenger.getMessage(name);
        Component component = messenger.parse(message, Message.Type.CHAT, placeholders);

        return RichDescription.of(component);
    }

    public Messenger<Factions> messenger(){
        return messenger;
    }
}

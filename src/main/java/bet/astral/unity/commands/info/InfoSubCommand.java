package bet.astral.unity.commands.info;

import bet.astral.cloudplusplus.annotations.Cloud;
import bet.astral.messenger.message.MessageType;
import bet.astral.messenger.message.message.IMessage;
import bet.astral.messenger.placeholder.Placeholder;
import bet.astral.messenger.placeholder.PlaceholderList;
import bet.astral.unity.Factions;
import bet.astral.unity.commands.FactionCloudCommand;
import bet.astral.unity.commands.arguments.FactionParser;
import bet.astral.unity.commands.arguments.FactionPlayerParser;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.FPrefix;
import bet.astral.unity.model.FRole;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.PermissionUtils;
import bet.astral.unity.utils.TranslationKey;
import bet.astral.unity.utils.refrence.FactionReference;
import bet.astral.unity.utils.refrence.OfflinePlayerReference;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.permission.PredicatePermission;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@Cloud
public class InfoSubCommand extends FactionCloudCommand {
	public InfoSubCommand(Factions plugin, PaperCommandManager<CommandSender> commandManager) {
		super(plugin, commandManager);
		Command.Builder<Player> builder = root
				.literal("info",
						loadDescription(TranslationKey.DESCRIPTION_INFO, "/factions info"))
				.commandDescription(loadDescription(TranslationKey.DESCRIPTION_INFO, "/factions info"))
				.senderType(Player.class)
				.handler(context -> {
					Player sender = context.sender();
					FPlayer player = plugin.getPlayerManager().convert(sender);
					if (player.getFaction() != null){
						Faction faction = plugin.getFactionManager().get(player.getFactionId());
						info(sender, faction);
					} else {
						messenger.message(sender, TranslationKey.MESSAGE_INFO_NO_FACTION);
					}
				});
		commandPlayer(builder);
		commandPlayer(builder
				.required(
						FactionParser.factionComponent(FactionParser.Mode.NAME)
								.name("faction")
								.description(loadDescription(TranslationKey.DESCRIPTION_INFO_FACTION, "/factions info <faction>"))
				)
				.commandDescription(loadDescription(TranslationKey.DESCRIPTION_INFO_FACTION, "/factions info <faction>"))
				.handler(context -> {
					Player sender = context.sender();
					Faction faction = context.get("faction");
					info(sender, faction);
				})
		);
		commandPlayer(builder.literal("-player",
				loadDescription(TranslationKey.DESCRIPTION_INFO_PLAYER_LITERAL, "/factions info -player"),
				"-p")
				.commandDescription(loadDescription(TranslationKey.DESCRIPTION_INFO_PLAYER_LITERAL, "/factions info -player"))
				.permission(
						PredicatePermission.of(sender -> {
							boolean can = Bukkit.getOnlinePlayers().stream()
									.filter(p->!p.equals(sender))
									.filter(sender::canSee)
									.map(p->plugin.getPlayerManager().convert(p))
									.anyMatch(p->p.getFaction() != null);
							if (!plugin.getFactionConfig().getPerformance()
									.isAllowOfflinePlayerSearch() &&
									commandManager
											.testPermission(sender, PermissionUtils.of("load-offline")).denied()) {
								return can;
							}
							return true;
						})
				)
				.required(
						FactionPlayerParser.factionPlayerComponent()
								.name("player")
								.description(loadDescription(TranslationKey.DESCRIPTION_INFO_PLAYER, "/factions info -player <player>"))
				)
				.handler(context->{
					Player sender = context.sender();
					Player player = context.get("player");
					FPlayer fPlayer = plugin.getPlayerManager().convert(player);
					Faction faction = fPlayer.getFaction();
					assert faction != null;
					info(sender, faction);
				})
		);
	}


	private void info(Player player, @NotNull FactionReference factionReference){
		Faction faction = factionReference.getFaction();
		assert faction != null;


		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(placeholderManager.factionPlaceholders("faction", faction));
		placeholders.add(roleEntryPlaceholder(player, faction, FRole.OWNER, TranslationKey.ENTRY_INFO_ROLE_OWNER, "owners"));
		placeholders.add(roleEntryPlaceholder(player, faction, FRole.CO_OWNER, TranslationKey.ENTRY_INFO_ROLE_CO_OWNER, "co-owners"));
		placeholders.add(roleEntryPlaceholder(player, faction, FRole.ADMIN, TranslationKey.ENTRY_INFO_ROLE_ADMIN, "admins"));
		placeholders.add(roleEntryPlaceholder(player, faction, FRole.MODERATOR, TranslationKey.ENTRY_INFO_ROLE_MOD, "moderators"));
		placeholders.add(roleEntryPlaceholder(player, faction, FRole.ADMIN, TranslationKey.ENTRY_INFO_ROLE_MEMBER, "members"));
		placeholders.add(roleEntryPlaceholder(player, faction, FRole.MEMBER, TranslationKey.ENTRY_INFO_ROLE_GUEST, "guests"));
		placeholders.add("created", plugin.formatDate(faction.getFirstCreated()));
		placeholders.add("power", 0);

		messenger.message(player, TranslationKey.MESSAGE_INFO, placeholders);
	}
	@Contract(pure = true)
	private Placeholder roleEntryPlaceholder(Player whoSees, @NotNull Faction faction, @NotNull FRole role, String translationKey, String name){

		PlaceholderList placeholders = new PlaceholderList();

		List<OfflinePlayerReference> members = faction.getMembersWithRole(role).toUnmodifiableReferenceList();
		if (members.isEmpty()){
			return new Placeholder(name, "");
		}

		IMessage<?, Component> memberCommaMSG = messenger.loadMessage(TranslationKey.ENTRY_INFO_USER_COMMA);
		Component memberCommaComponent = messenger.parse(memberCommaMSG, MessageType.CHAT);
		Component membersComponent = null;

		for (OfflinePlayerReference reference : members) {
			if (membersComponent != null) {
				membersComponent = membersComponent.append(memberCommaComponent)
						.append(userPlaceholder(whoSees, faction, reference));
			} else {
				membersComponent = userPlaceholder(whoSees, faction, reference);
			}
		}
		placeholders.add("members", membersComponent);

		FPrefix prefix = faction.getPrivatePrefix(role);
		placeholders.addAll(prefix.asPlaceholder("prefix"));
		placeholders.add("members_size", members.size());
		placeholders.add("amount", members.size());

		IMessage<?, Component> entryMsg = messenger.loadMessage(translationKey);
		Component entryComp = messenger.parse(entryMsg, MessageType.CHAT, placeholders);
		return new Placeholder(name, entryComp);
	}

	@NotNull
	private Component userPlaceholder(Player whoSees, Faction faction, @NotNull OfflinePlayerReference reference){
		IMessage<?, Component> message;
		if (!(reference.offlinePlayer() instanceof Player player) || (reference.offlinePlayer() instanceof Player p && !whoSees.canSee(p))){
			message = messenger.loadMessage(TranslationKey.ENTRY_INFO_USER_OFFLINE);
		} else {
			message = messenger.loadMessage(TranslationKey.ENTRY_INFO_USER_ONLINE);
		}

		FRole role = faction.getRole(reference);
		if (role == null){
			role = FRole.GUEST;
		}

		OfflinePlayer player = reference.offlinePlayer();

		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(role.asPlaceholder("role"));
		placeholders.add("role", role);
		placeholders.addAll(reference.asPlaceholder("player"));
		placeholders.add("player", reference.name());
		placeholders.add("offline_since", plugin.formatDate(player.getLastSeen()));
		placeholders.add("online_since", plugin.formatDate(player.getLastLogin()));

		FPrefix prefix = faction.getPrivatePrefix(reference);
		placeholders.addAll(prefix.asPlaceholder("prefix", reference));

		Component parsed = messenger.parse(message, MessageType.CHAT,
				placeholders);
		assert parsed != null;
		return parsed;
	}
}
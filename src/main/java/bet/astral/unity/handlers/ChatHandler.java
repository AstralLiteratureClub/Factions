package bet.astral.unity.handlers;

import bet.astral.unity.model.FChat;
import bet.astral.unity.model.FPlayer;
import bet.astral.unity.model.Faction;
import bet.astral.unity.utils.refrence.PlayerReference;
import net.kyori.adventure.text.Component;

@FunctionalInterface
public interface ChatHandler {
	Component handle(FPlayer player, Faction faction, PlayerReference audience, Component message, FChat.Toggleable chat);
}

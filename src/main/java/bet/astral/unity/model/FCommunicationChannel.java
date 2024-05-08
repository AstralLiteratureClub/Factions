package bet.astral.unity.model;

import lombok.Getter;

public enum FCommunicationChannel {
	/**
	 * This is only used by other plugins. When this is used, the faction plugin will not try to use the messaging features.
	 */
	CUSTOM,
	/**
	 * This means that the chat is global. This means that the plugin will, not try to modify the chat.
	 */
	GLOBAL,
	/**
	 * This means that the plugin should try to find a new formatting for the chat for all clan members.
	 */
	FACTION,
	/**
	 * This means that the plugin should try to find a new formatting for the chat for all allies.
	 */
	ALLY,
	/**
	 * This means that the plugin should try to find a new formatting for the TRUCE factions
	 */
	TRUCE

	;


	@Getter
	public enum Toggleable {
		GLOBAL(FCommunicationChannel.GLOBAL),
		FACTION(FCommunicationChannel.FACTION),
		ALLY(FCommunicationChannel.ALLY)

		;
		private final FCommunicationChannel chat;

		Toggleable(FCommunicationChannel chat) {
			this.chat = chat;
		}

	}
}

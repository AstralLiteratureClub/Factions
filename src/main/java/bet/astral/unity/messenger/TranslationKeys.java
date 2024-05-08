package bet.astral.unity.messenger;

import bet.astral.messenger.translation.TranslationKey;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class TranslationKeys {
	public final static TranslationKey DATE_FORMAT = TranslationKey.of("date-format");

	public final static TranslationKey DESCRIPTION_ROOT = TranslationKey.of("root.description");
	public final static TranslationKey DESCRIPTION_ROOT_FORCE = TranslationKey.of("root.description-force");
	public final static TranslationKey DESCRIPTION_ROOT_ALLY = TranslationKey.of("root.description-ally");
	public final static TranslationKey DESCRIPTION_ROOT_ALLY_FORCE = TranslationKey.of("root.description-ally");
	public final static TranslationKey DESCRIPTION_HELP = TranslationKey.of("root.description-help");
	public final static TranslationKey DESCRIPTION_HELP_QUERY = TranslationKey.of("root.description-help-query");

	public final static TranslationKey DEFAULT_FACTION_DESCRIPTION = TranslationKey.of("faction-object.default-description");
	public final static TranslationKey DEFAULT_FACTION_JOIN_INFO = TranslationKey.of("faction-object.default-join-info");

	public final static TranslationKey DESCRIPTION_CREATE = TranslationKey.of("create.description");
	public final static TranslationKey DESCRIPTION_CREATE_NAME = TranslationKey.of("create.description-name-arg");
	public final static TranslationKey MESSAGE_CREATE_TOO_LONG = TranslationKey.of("create.message-name-is-too-long");
	public final static TranslationKey MESSAGE_CREATE_TOO_SHORT = TranslationKey.of("create-message-name-is-too-short");
	public final static TranslationKey MESSAGE_CREATE_ALREADY_EXISTS = TranslationKey.of("create-message-name-already-exists");
	public final static TranslationKey MESSAGE_CREATE_BANNED = TranslationKey.of("create-message-name-is-banned");

	public final static TranslationKey MESSAGE_CREATE_FACTION = TranslationKey.of("create.message-created-faction");
	public final static TranslationKey BROADCAST_CREATE_FACTION = TranslationKey.of("create.broadcast-public-of-creation");


	// Base
	public final static TranslationKey DELETE_DESCRIPTION = TranslationKey.of("delete.description");
	// Force
	public final static TranslationKey DESCRIPTION_FORCE_DELETE = TranslationKey.of("delete.description-force");
	public final static TranslationKey DESCRIPTION_FORCE_DELETE_FACTION = TranslationKey.of("delete.description-force-arg-faction");
	public final static TranslationKey DESCRIPTION_FORCE_DELETE_REASON = TranslationKey.of("delete.description-force-arg-reason");
	public final static TranslationKey DESCRIPTION_FORCE_DELETE_SILENT = TranslationKey.of("delete.description-force-arg-silent");
	public final static TranslationKey MESSAGE_DELETE_REQUEST = TranslationKey.of("delete.message-requested-confirm");
	// Base - Message
	public final static TranslationKey BROADCAST_DELETE_CONFIRM_FACTION = TranslationKey.of("delete.broadcast-public-of-deletion");
	public final static TranslationKey BROADCAST_DELETE_CONFIRM_FACTION_INTERNAL = TranslationKey.of("delete.broadcast-faction-of-deletion");
	public final static TranslationKey MESSAGE_DELETE_CONFIRM_FACTION = TranslationKey.of("delete.message-sender-of-deletion");
	public final static TranslationKey MESSAGE_DELETE_TIME_RAN_OUT = TranslationKey.of("delete.message-time-ran-out");
	// Force - Message
	public final static TranslationKey MESSAGE_FORCE_DELETE_FACTION = TranslationKey.of("delete.broadcast-faction-about-force-disband");
	public final static TranslationKey MESSAGE_FORCE_DELETE_ADMIN = TranslationKey.of("delete.broadcast-admins-about-force-disband");
	public final static TranslationKey MESSAGE_FORCE_DELETE_PUBLIC = TranslationKey.of("delete.broadcast-public-about-force-disband");
	public final static TranslationKey MESSAGE_FORCE_DELETE_SENDER = TranslationKey.of("delete.message-inform-sender-about-force-disband");

	// Invite
	public final static TranslationKey DESCRIPTION_INVITE = TranslationKey.of("invite.description");
	public static final TranslationKey DESCRIPTION_INVITE_ALL = TranslationKey.of("invite.description--all-arg");
	public final static TranslationKey DESCRIPTION_INVITE_WHO = TranslationKey.of("invite.description-who-arg");
	// Invites
	public static final TranslationKey DESCRIPTION_INVITES = TranslationKey.of("invite.description-invites");
	public static final TranslationKey DESCRIPTION_INVITES_PLAYER_FLAG = TranslationKey.of("invite.description-invites--own-arg");
	// Accept
	public final static TranslationKey DESCRIPTION_INVITE_ACCEPT = TranslationKey.of(" invite.description-accept");
	public final static TranslationKey DESCRIPTION_INVITE_ACCEPT_FACTION = TranslationKey.of(" invite.description-accept-faction-arg");
	// Deny
	public final static TranslationKey DESCRIPTION_INVITE_DENY = TranslationKey.of(" invite.description-deny");
	public final static TranslationKey DESCRIPTION_INVITE_DENY_FACTION = TranslationKey.of(" invite.description-deny-faction-arg");
	public final static TranslationKey DESCRIPTION_INVITE_DENY_ALL = TranslationKey.of(" invite.description-deny--all-arg");
	// Cancel
	public final static TranslationKey DESCRIPTION_INVITE_CANCEL = TranslationKey.of(" invite.description-cancel");
	public static final TranslationKey DESCRIPTION_INVITE_CANCEL_ALL = TranslationKey.of(" invite.description-cancel--all-arg");
	public final static TranslationKey DESCRIPTION_INVITE_CANCEL_PLAYER = TranslationKey.of(" invite.description-cancel-player-arg");
	public final static TranslationKey DESCRIPTION_INVITE_CANCEL_REASON = TranslationKey.of(" invite.description-cancel-reason-arg");
	// Messages
	public final static TranslationKey BROADCAST_INVITE_TO_FACTION = TranslationKey.of(" invite.broadcast-faction-of-invitation");
	public final static TranslationKey MESSAGE_INVITE_RECEIVER = TranslationKey.of(" invite.message-receiver-of-invitation");
	public final static TranslationKey MESSAGE_INVITE_EXPIRED_FACTION = TranslationKey.of(" invite.broadcast-faction-of-invitation-expiring");
	public final static TranslationKey MESSAGE_INVITE_EXPIRED = TranslationKey.of(" invite.message-receiver-of-invitation-expiring");
	public final static TranslationKey MESSAGE_INVITE_ACCEPT = TranslationKey.of(" invite.message-receiver-accepted");
	public final static TranslationKey BROADCAST_INVITE_ACCEPT = TranslationKey.of(" invite.broadcast-faction-of-receiver-accepting");
	public final static TranslationKey MESSAGE_INVITE_DENY = TranslationKey.of(" invite.message-receiver-denied");
	public final static TranslationKey MESSAGE_INVITE_DENIED_ALL = TranslationKey.of(" invite.message-receiver-denied-all");
	public final static TranslationKey BROADCAST_INVITE_DENY = TranslationKey.of(" invite.broadcast-faction-of-receiver-denying");
	public final static TranslationKey MESSAGE_INVITE_CANCEL = TranslationKey.of(" invite.message-cancel");
	public final static TranslationKey MESSAGE_INVITE_CANCEL_PLAYER = TranslationKey.of(" invite.message-player-of-cancel");
	public final static TranslationKey BROADCAST_INVITE_CANCEL = TranslationKey.of(" invite.broadcast-faction-of-cancel");
	public final static TranslationKey MESSAGE_INVITES_HEADER_FACTION = TranslationKey.of(" invite.message-invites-header-faction");
	public final static TranslationKey MESSAGE_INVITES_VALUE_FACTION = TranslationKey.of(" invite.message-invites-value-faction");
	public static final TranslationKey MESSAGE_INVITES_EMPTY_FACTION = TranslationKey.of(" invite.message-invite-none-faction");
	public static final TranslationKey MESSAGE_INVITES_HEADER_PLAYER = TranslationKey.of(" invite.message-invites-header-player");
	public static final TranslationKey MESSAGE_INVITES_VALUE_PLAYER = TranslationKey.of(" invite.message-invites-value-player");
	public static final TranslationKey MESSAGE_INVITES_EMPTY_PLAYER = TranslationKey.of(" invite.message-invites-empty-player");
	public final static TranslationKey DESCRIPTION_FORCE_INVITE = TranslationKey.of(" invite.description-force-invite");
	public final static TranslationKey DESCRIPTION_FORCE_INVITE_FACTION = TranslationKey.of(" invite.description-force-invite-faction-arg");
	public final static TranslationKey DESCRIPTION_FORCE_INVITE_PLAYER = TranslationKey.of(" invite.description-force-invite-player-arg");
	public final static TranslationKey DESCRIPTION_FORCE_INVITE_ALL = TranslationKey.of(" invite.description-force-invite--all-arg");

	public final static TranslationKey BROADCAST_FORCE_INVITE_FACTION = TranslationKey.of(" invite.broadcast-force-faction-of-invite");
	public final static TranslationKey MESSAGE_FORCE_INVITE_SENDER = TranslationKey.of(" invite.message-force-invite-sent");
	public final static TranslationKey MESSAGE_FORCE_INVITE_RECEIVER = TranslationKey.of(" invite.message-force-invite-receiver");

	public final static TranslationKey DESCRIPTION_FORCE_INVITE_CANCEL = TranslationKey.of(" invite.description-invite-force");
	public final static TranslationKey DESCRIPTION_FORCE_INVITE_CANCEL_FACTION = TranslationKey.of(" invite.description-force-invite-faction");
	public final static TranslationKey DESCRIPTION_FORCE_INVITE_CANCEL_PLAYER = TranslationKey.of(" invite.description-force-invite-player");
	public final static TranslationKey BROADCAST_FORCE_INVITE_CANCEL_FACTION = TranslationKey.of(" invite.broadcast-force-faction-of-invite");
	public final static TranslationKey MESSAGE_FORCE_INVITE_CANCEL_RECEIVER = TranslationKey.of(" invite.message-force-receive-invite");

	public final static TranslationKey DESCRIPTION_CHAT = TranslationKey.of(" chat.description");
	public final static TranslationKey DESCRIPTION_CHAT_MESSAGE = TranslationKey.of(" chat.description-message-arg");
	public final static TranslationKey DESCRIPTION_FORCE_CHAT = TranslationKey.of(" chat.description-force");
	public final static TranslationKey DESCRIPTION_FORCE_CHAT_MESSAGE = TranslationKey.of(" chat.description-force-message-arg");

	public final static TranslationKey FORMAT_CHAT = TranslationKey.of(" chat.format-chat");
	public final static TranslationKey FORMAT_ALLY_CHAT = TranslationKey.of(" chat.format-ally-chat");
	public final static TranslationKey MESSAGE_CHAT_SWITCH_GLOBAL = TranslationKey.of(" chat.format-switch-global");
	public final static TranslationKey MESSAGE_CHAT_SWITCH_FACTION = TranslationKey.of(" chat.format-switch-clan");
	public final static TranslationKey MESSAGE_CHAT_SWITCH_ALLY = TranslationKey.of(" chat.format-switch-ally");

	public final static TranslationKey DESCRIPTION_INFO = TranslationKey.of(" info.description");
	public final static TranslationKey DESCRIPTION_INFO_FACTION = TranslationKey.of(" info.description-faction-arg");
	public final static TranslationKey DESCRIPTION_INFO_PLAYER_LITERAL = TranslationKey.of(" info.description-player-subcommand-arg");
	public final static TranslationKey DESCRIPTION_INFO_PLAYER = TranslationKey.of(" info.description-player-arg");
	public final static TranslationKey MESSAGE_INFO = TranslationKey.of(" info.message-info");
	public final static TranslationKey MESSAGE_INFO_NO_FACTION = TranslationKey.of(" info.message-no-faction");
	public final static TranslationKey ENTRY_INFO_ROLE_OWNER = TranslationKey.of(" info.entry-role-owner");
	public final static TranslationKey ENTRY_INFO_ROLE_CO_OWNER = TranslationKey.of(" info.entry-role-co-owner");
	public final static TranslationKey ENTRY_INFO_ROLE_ADMIN = TranslationKey.of(" info.entry-role-admin");
	public final static TranslationKey ENTRY_INFO_ROLE_MOD = TranslationKey.of(" info.entry-role-moderator");
	public final static TranslationKey ENTRY_INFO_ROLE_MEMBER = TranslationKey.of(" info.entry-role-member");
	public final static TranslationKey ENTRY_INFO_ROLE_GUEST = TranslationKey.of(" info.entry-role-guest");
	public final static TranslationKey ENTRY_INFO_USER_OFFLINE = TranslationKey.of(" info.entry-user-offline");
	public final static TranslationKey ENTRY_INFO_USER_ONLINE = TranslationKey.of(" info.entry-user-online");
	public final static TranslationKey ENTRY_INFO_USER_COMMA = TranslationKey.of(" info.entry-user-comma");

	public final static TranslationKey DESCRIPTION_LEAVE = TranslationKey.of(" leave.description");
	public final static TranslationKey MESSAGE_LEAVE = TranslationKey.of(" leave.message-leaving-player");
	public final static TranslationKey BROADCAST_LEAVE = TranslationKey.of(" leave.broadcast-leaving-faction-of-leave");

	public final static TranslationKey DESCRIPTION_KICK = TranslationKey.of(" kick.description");
	public final static TranslationKey DESCRIPTION_KICK_MEMBER = TranslationKey.of(" kick.description-member-arg");
	public final static TranslationKey DESCRIPTION_KICK_REASON = TranslationKey.of(" kick.description-reason-arg");
	public final static TranslationKey MESSAGE_KICK_CANNOT_HIGHER = TranslationKey.of(" kick.message-cannot-kick");
	public final static TranslationKey MESSAGE_KICK = TranslationKey.of(" kick.message-kicked-of-kick");
	public final static TranslationKey BROADCAST_KICK = TranslationKey.of(" kick.broadcast-faction-of-kick");

	public final static TranslationKey DESCRIPTION_FORCE_KICK = TranslationKey.of(" kick.description-force");
	public final static TranslationKey DESCRIPTION_FORCE_KICK_FACTION = TranslationKey.of(" kick.description-force-faction-arg");
	public final static TranslationKey DESCRIPTION_FORCE_KICK_PLAYER = TranslationKey.of(" kick.description-force-player-arg");
	public final static TranslationKey DESCRIPTION_FORCE_KICK_REASON = TranslationKey.of(" kick.description-force-reason-arg");
	public final static TranslationKey MESSAGE_FORCE_KICK_SENDER = TranslationKey.of(" kick.message-force-kick-success");
	public final static TranslationKey MESSAGE_FORCE_KICK_KICKED = TranslationKey.of(" kick.message-force-kicked-about-kick");
	public final static TranslationKey BROADCAST_FORCE_KICK = TranslationKey.of(" kick.broadcast-faction-about-force-kick");

	public final static TranslationKey DESCRIPTION_JOIN = TranslationKey.of(" join.description");
	public final static TranslationKey DESCRIPTION_JOIN_FACTION = TranslationKey.of(" join.description-faction-arg");
	public final static TranslationKey MESSAGE_FACTION_PRIVATE = TranslationKey.of(" join.message-how-to-join");
	public final static TranslationKey MESSAGE_JOINED = TranslationKey.of(" join.message-joined-public");
	public final static TranslationKey BROADCAST_JOINED = TranslationKey.of(" join.broadcast-faction-about-new-member");
	public final static TranslationKey DESCRIPTION_FORCE_JOIN = TranslationKey.of(" join.description-force");
	public final static TranslationKey DESCRIPTION_FORCE_JOIN_FACTION = TranslationKey.of(" join.description-force");
	public final static TranslationKey DESCRIPTION_FORCE_JOIN_ROLE = TranslationKey.of(" join.description-force");
	public final static TranslationKey MESSAGE_FORCE_JOIN_DEFAULT = TranslationKey.of(" join.message-force-joined");
	public final static TranslationKey MESSAGE_FORCE_JOIN_NOT_DEFAULT = TranslationKey.of(" join.broadcast-faction-of-force-join");

	@IsCaption(true)
	public final static TranslationKey CAPTION_UNKNOWN_ROLE = TranslationKey.of("caption.unknown-role");
	@IsCaption(true)
	public final static TranslationKey CAPTION_PRIVATE_FACTION = TranslationKey.of("caption.private-faction");
	@IsCaption(true)
	public final static TranslationKey CAPTION_UNKNOWN_MEMBER = TranslationKey.of("caption.unknown-member");
	@IsCaption(true)
	public final static TranslationKey CAPTION_NOT_INVITED = TranslationKey.of("caption.player-has-not-been-invited");
	@IsCaption(true)
	public final static TranslationKey CAPTION_ALREADY_INVITED = TranslationKey.of("caption.player-has-already-been-invited");
	@IsCaption(true)
	public final static TranslationKey CAPTION_HAS_A_FACTION = TranslationKey.of("caption.has-a-faction");
	@IsCaption(true)
	public final static TranslationKey CAPTION_HAS_NO_FACTION = TranslationKey.of("caption.has-no-faction");
	@IsCaption(true)
	public final static TranslationKey CAPTION_CANNOT_CHECK_OFFLINE_PLAYER = TranslationKey.of("caption.cannot-check-offline");
	@IsCaption(true)
	public final static TranslationKey CAPTION_UNKNOWN_FACTION = TranslationKey.of("caption.unknown-faction");
	@IsCaption(true)
	public final static TranslationKey CAPTION_SELF_HAS_NO_FACTION = TranslationKey.of("caption.sender-has-no-faction");
	@IsCaption(true)
	public final static TranslationKey CAPTION_PLAYER_HAS_NOT_BEEN_INVITED = TranslationKey.of("caption.faction-has-not-invited-player");
	@IsCaption(true)
	public final static TranslationKey CAPTION_BANNED = TranslationKey.of("caption.faction-has-banned-player");
	@NotDone
	@IsCaption(true)
	public static final TranslationKey CAPTION_UNKNOWN_SUPER_OWNER = TranslationKey.of("caption.unknown-super-owner");
	@NotDone
	@IsCaption(true)
	public static final TranslationKey CAPTION_NO_SUPER_OWNER = TranslationKey.of("caption.no-super-owner-found");

	@NotDone
	@IsCaption(true)
	public static final TranslationKey CAPTION_FACTION_NAME_BANNED = TranslationKey.of("caption.banned-faction-name");
	@NotDone
	@IsCaption(true)
	public static final TranslationKey CAPTION_FACTION_NAME_EXISTS = TranslationKey.of("caption.banned-faction-name");



	public final static TranslationKey DESCRIPTION_PREFIX = TranslationKey.of(" prefix.description");
	public final static TranslationKey DESCRIPTION_PREFIX_SET = TranslationKey.of(" prefix.description-set-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_SET_PUBLIC = TranslationKey.of(" prefix.description-set-public-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_SET_PUBLIC_ROLE = TranslationKey.of(" prefix.description-set-public-role-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_SET_PUBLIC_PREFIX = TranslationKey.of(" prefix.description-set-public-prefix-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_SET_PRIVATE = TranslationKey.of(" prefix.description-set-private-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_SET_PRIVATE_PLAYER = TranslationKey.of(" prefix.description-set-private-player-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_SET_PRIVATE_PLAYER_MEMBER = TranslationKey.of(" prefix.description-set-private-player-member-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_SET_PRIVATE_ROLE = TranslationKey.of(" prefix.description-set-private-role-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_SET_PRIVATE_ROLE_ROLE = TranslationKey.of(" prefix.description-set-private-role-role-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_SET_PRIVATE_PREFIX = TranslationKey.of(" prefix.description-private-prefix-arg");
	public final static TranslationKey MESSAGE_PREFIX_SET_PRIVATE_ROLE_CHANGED = TranslationKey.of(" prefix.message-changed-private-role");
	public final static TranslationKey MESSAGE_PREFIX_SET_PRIVATE_MEMBER_CHANGED = TranslationKey.of(" prefix.message-changed-private-member");
	public final static TranslationKey MESSAGE_PREFIX_SET_PUBLIC_TOO_SHORT = TranslationKey.of(" prefix.message-public-prefix-too-short");
	public final static TranslationKey MESSAGE_PREFIX_SET_PUBLIC_TOO_LONG = TranslationKey.of(" prefix.message-public-prefix-too-long");
	public final static TranslationKey MESSAGE_PREFIX_SET_PUBLIC_ILLEGAL = TranslationKey.of(" prefix.message-public-prefix-does-not-match");
	public final static TranslationKey MESSAGE_PREFIX_SET_PUBLIC_CHANGED = TranslationKey.of(" prefix.message-changed-public-prefix");

	public final static TranslationKey DESCRIPTION_PREFIX_RESET = TranslationKey.of(" prefix.description-reset-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_RESET_PUBLIC = TranslationKey.of(" prefix.description-reset-public-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_RESET_PUBLIC_ROLE = TranslationKey.of(" prefix.description-reset-public-role-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_RESET_PRIVATE = TranslationKey.of(" prefix.description-reset-private-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_RESET_PRIVATE_ROLE = TranslationKey.of(" prefix.description-reset-private-role-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_RESET_PRIVATE_ROLE_ROLE = TranslationKey.of(" prefix.description-reset-private-role-role-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_RESET_PRIVATE_PLAYER = TranslationKey.of(" prefix.description-reset-private-player-arg");
	public final static TranslationKey DESCRIPTION_PREFIX_RESET_PRIVATE_PLAYER_MEMBER = TranslationKey.of(" prefix.description-reset-private-player-member-arg");
	public final static TranslationKey MESSAGE_PREFIX_RESET_PRIVATE_ROLE = TranslationKey.of(" prefix.message-reset-private-role-prefix");
	public final static TranslationKey MESSAGE_PREFIX_RESET_PRIVATE_PLAYER = TranslationKey.of(" prefix.message-reset-private-member-prefix");
	public final static TranslationKey MESSAGE_PREFIX_RESET_PUBLIC_ROLE = TranslationKey.of(" prefix.message-reset-public-role-prefix");

	public final static TranslationKey DESCRIPTION_FORCE_BAN = TranslationKey.of(" ban.force-description");
	public final static TranslationKey DESCRIPTION_FORCE_BAN_PLAYER = TranslationKey.of(" ban.force.player.description");
	public final static TranslationKey DESCRIPTION_FORCE_BAN_PLAYER_FACTION = TranslationKey.of(" ban.force.player.description");
	public final static TranslationKey DESCRIPTION_FORCE_BAN_PLAYER_PLAYER = TranslationKey.of(" ban.force.player.description");
	public final static TranslationKey DESCRIPTION_FORCE_BAN_PLAYER_REASON = TranslationKey.of(" ban.force.player.description");
	public final static TranslationKey DESCRIPTION_FORCE_BAN_PLAYER_SILENT = TranslationKey.of(" ban.force.player.description");
	public final static TranslationKey DESCRIPTION_FORCE_BAN_FACTION = TranslationKey.of(" ban.force.faction.description");
	public final static TranslationKey DESCRIPTION_FORCE_BAN_FACTION_FACTION = TranslationKey.of(" ban.force.faction.description-faction-arg");
	public final static TranslationKey DESCRIPTION_FORCE_BAN_FACTION_REASON = TranslationKey.of(" ban.force.faction.description-reason-arg");
	public final static TranslationKey DESCRIPTION_FORCE_BAN_FACTION_SILENT = TranslationKey.of(" ban.force.faction.description-silent-arg");
	public final static TranslationKey MESSAGE_FORCE_BAN_ALREADY_BANNED = TranslationKey.of(" ban.force.faction.message-already-banned");
	public final static TranslationKey MESSAGE_FORCE_BAN_BANNED = TranslationKey.of(" ban.force.faction.message-banned");
	public final static TranslationKey BROADCAST_FORCE_BAN_BANNED_STAFF = TranslationKey.of(" ban.force.faction.broadcast-admins-of-ban");
	public final static TranslationKey BROADCAST_FORCE_BAN_BANNED_PUBLIC = TranslationKey.of(" ban.force.faction.broadcast-public-of-ban");
	public final static TranslationKey BROADCAST_FORCE_BAN_BANNED_FACTION = TranslationKey.of(" ban.force.faction.broadcast-faction-of-ban");

	public static final TranslationKey DESCRIPTION_TAKEOVER = TranslationKey.of(" takeover.description");
	public static final TranslationKey DESCRIPTION_TAKEOVER_OWNER = TranslationKey.of(" takeover.description-owner-arg");

	public static final TranslationKey DESCRIPTION_COLOR = TranslationKey.of("color.description");
	public static final TranslationKey DESCRIPTION_COLOR_ARG = TranslationKey.of("color.description-color-arg");
	public static final TranslationKey BROADCAST_COLOR_CHANGED = TranslationKey.of("color.broadcast-faction-of-color-change");
	public static final TranslationKey DESCRIPTION_FORCE_COLOR = TranslationKey.of("color.description-force");
	public static final TranslationKey DESCRIPTION_FORCE_COLOR_FACTION_ARG = TranslationKey.of("color.description-force-faction-arg");
	public static final TranslationKey DESCRIPTION_FORCE_COLOR_COLOR_ARG = TranslationKey.of("color.description-force-color-arg");
	public static final TranslationKey BROADCAST_FORCE_COLOR_CHANGED_ADMIN = TranslationKey.of("color.broadcast-admins-of-color-changing");
	public static final TranslationKey BROADCAST_FORCE_COLOR_CHANGED_FACTION = TranslationKey.of("color.broadcast-faction-of-color-changing");

	// Rename
	public final static TranslationKey DESCRIPTION_RENAME_NAME = TranslationKey.of("change-name.description");
	public final static TranslationKey DESCRIPTION_RENAME_NAME_NAME_ARG = TranslationKey.of("change-name.description-name-arg");
	public final static TranslationKey DESCRIPTION_RENAME_DISPLAYNAME = TranslationKey.of("change-displayname.description");
	public final static TranslationKey DESCRIPTION_RENAME_DISPLAYNAME_ARG = TranslationKey.of("change-displayname.description-name-arg");
	public final static TranslationKey MESSAGE_RENAME_ALREADY_EXISTS = MESSAGE_CREATE_ALREADY_EXISTS;
	public final static TranslationKey MESSAGE_RENAME_BANNED = MESSAGE_CREATE_BANNED;
	public final static TranslationKey MESSAGE_RENAME_NAME_TOO_SHORT = MESSAGE_CREATE_TOO_SHORT;
	public final static TranslationKey MESSAGE_RENAME_NAME_TOO_LONG = MESSAGE_CREATE_TOO_LONG;
	public final static TranslationKey MESSAGE_RENAME_DISPLAYNAME_TOO_SHORT = MESSAGE_CREATE_TOO_SHORT;
	public final static TranslationKey MESSAGE_RENAME_DISPLAYNAME_TOO_LONG = MESSAGE_CREATE_TOO_LONG;
	public static final TranslationKey MESSAGE_RENAME_NAME_CANNOT = TranslationKey.of("change-name.message-cannot-change-name");
	public static final TranslationKey MESSAGE_RENAME_DISPLAYNAME_CANNOT = TranslationKey.of("change-name.message-cannot-change-name");
	public static final TranslationKey BROADCAST_RENAME_NAME_FACTION = ;
	public static final TranslationKey BROADCAST_RENAME_NAME_PUBLIC = ;
	public static final TranslationKey BROADCAST_RENAME_DISPLAYNAME_FACTION = ;
	public static final TranslationKey BROADCAST_RENAME_DISPLAYNAME_PUBLIC = ;
	// Rename - Force

	@Retention(RetentionPolicy.RUNTIME)
	public @interface IsCaption {
		boolean value() default false;
	}

	@Retention(RetentionPolicy.SOURCE)
	public @interface NotDone {
	}
}

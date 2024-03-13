package bet.astral.unity.utils;

import org.incendo.cloud.caption.Caption;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class TranslationKey {
	public final static String DESCRIPTION_ROOT = "root.description";
	public final static String DESCRIPTION_ROOT_FORCE = "root.description-force";
	public final static String DESCRIPTION_ROOT_ALLY = "root.description-ally";
	public final static String DESCRIPTION_ROOT_ALLY_FORCE = "root.description-ally";
	public final static String DESCRIPTION_HELP = "root.description-help";
	public final static String DESCRIPTION_HELP_QUERY = "root.description-help-query";

	public final static String DEFAULT_FACTION_DESCRIPTION = "faction-object.default-description";
	public final static String DEFAULT_FACTION_JOIN_INFO = "faction-object.default-join-info";

	public final static String DESCRIPTION_CREATE = "create.description";
	public final static String DESCRIPTION_CREATE_NAME = "create.description-name-arg";
	public final static String MESSAGE_CREATE_TOO_LONG = "create.message-name-is-too-long";
	public final static String MESSAGE_CREATE_TOO_SHORT = "create-message-name-is-too-short";
	public final static String MESSAGE_CREATE_ALREADY_EXISTS = "create-message-name-already-exists";
	public final static String MESSAGE_CREATE_BANNED = "create-message-name-is-banned";

	public final static String MESSAGE_CREATE_FACTION = "create.message-created-faction";
	public final static String BROADCAST_CREATE_FACTION = "create.broadcast-public-of-creation";



	// Base
	public final static String DELETE_DESCRIPTION = "delete.description";
	// Force
	public final static String DESCRIPTION_FORCE_DELETE = "delete.description-force";
	public final static String DESCRIPTION_FORCE_DELETE_FACTION = "delete.description-force-arg-faction";
	public final static String DESCRIPTION_FORCE_DELETE_REASON = "delete.description-force-arg-reason";
	public final static String DESCRIPTION_FORCE_DELETE_SILENT = "delete.description-force-arg-silent";
	public final static String MESSAGE_DELETE_REQUEST = "delete.message-requested-confirm";
	// Base - Message
	public final static String BROADCAST_DELETE_CONFIRM_FACTION = "delete.broadcast-public-of-deletion";
	public final static String BROADCAST_DELETE_CONFIRM_FACTION_INTERNAL = "delete.broadcast-faction-of-deletion";
	public final static String MESSAGE_DELETE_CONFIRM_FACTION = "delete.message-sender-of-deletion";
	public final static String MESSAGE_DELETE_TIME_RAN_OUT = "delete.message-time-ran-out";
	// Force - Message
	public final static String MESSAGE_FORCE_DELETE_FACTION = "delete.broadcast-faction-about-force-disband";
	public final static String MESSAGE_FORCE_DELETE_ADMIN = "delete.broadcast-admins-about-force-disband";
	public final static String MESSAGE_FORCE_DELETE_PUBLIC = "delete.broadcast-public-about-force-disband";
	public final static String MESSAGE_FORCE_DELETE_SENDER = "delete.message-inform-sender-about-force-disband";



	// Invite
	public final static String DESCRIPTION_INVITE = "invite.description";
	public static final String DESCRIPTION_INVITE_ALL = "invite.description--all-arg";
	public final static String DESCRIPTION_INVITE_WHO = "invite.description-who-arg";
	// Invites
	public static final String DESCRIPTION_INVITES = "invite.description-invites";
	// Accept
	public final static String DESCRIPTION_INVITE_ACCEPT = "invite.description-accept";
	public final static String DESCRIPTION_INVITE_ACCEPT_FACTION = "invite.description-accept-faction-arg";
	// Deny
	public final static String DESCRIPTION_INVITE_DENY = "invite.description-deny";
	public final static String DESCRIPTION_INVITE_DENY_FACTION = "invite.description-deny-faction-arg";
	public final static String DESCRIPTION_INVITE_DENY_ALL = "invite.description-deny--all-arg";
	// Cancel
	public final static String DESCRIPTION_INVITE_CANCEL = "invite.description-cancel";
	public static final String DESCRIPTION_INVITE_CANCEL_ALL = "invite.description-cancel--all-arg";
	public final static String DESCRIPTION_INVITE_CANCEL_PLAYER = "invite.description-cancel-player-arg";
	public final static String DESCRIPTION_INVITE_CANCEL_REASON = "invite.description-cancel-reason-arg";
	// Messages
	public final static String BROADCAST_INVITE_TO_FACTION = "invite.broadcast-faction-of-invite";
	public final static String MESSAGE_INVITE_RECEIVER = "invite.message-receiver-of-invitation";
	public final static String MESSAGE_INVITE_EXPIRED_FACTION = "invite.broadcast-faction-of-invitation-expiring";
	public final static String MESSAGE_INVITE_EXPIRED = "invite.message-receiver-of-invitation-expiring";
	public final static String MESSAGE_INVITE_ACCEPT = "invite.message-receiver-accepted";
	public final static String BROADCAST_INVITE_ACCEPT = "invite.broadcast-faction-of-receiver-accepting";
	public final static String MESSAGE_INVITE_DENY = "invite.message-receiver-denied";
	public final static String MESSAGE_INVITE_DENIED_ALL = "invite.message-receiver-denied-all";
	public final static String BROADCAST_INVITE_DENY = "invite.broadcast-faction-of-receiver-denying";
	public final static String MESSAGE_INVITE_CANCEL = "invite.message-cancel";
	public final static String MESSAGE_INVITE_CANCEL_PLAYER = "invite.message-player-of-cancel";
	public final static String BROADCAST_INVITE_CANCEL = "invite.broadcast-faction-of-cancel";
	public final static String MESSAGE_INVITES_HEADER = "invite.message-invites-header";
	public final static String MESSAGE_INVITES_VALUE = "invite.message-invites-value";
	public final static String DESCRIPTION_FORCE_INVITE = "invite.description-force-invite";
	public final static String DESCRIPTION_FORCE_INVITE_FACTION = "invite.description-force-invite-faction-arg";
	public final static String DESCRIPTION_FORCE_INVITE_PLAYER = "invite.description-force-invite-player-arg";
	public final static String DESCRIPTION_FORCE_INVITE_ALL = "invite.description-force-invite--all-arg";

	public final static String BROADCAST_FORCE_INVITE_FACTION = "invite.broadcast-force-faction-of-invite";
	public final static String MESSAGE_FORCE_INVITE_SENDER = "invite.message-force-invite-sent";
	public final static String MESSAGE_FORCE_INVITE_RECEIVER = "invite.message-force-invite-receiver";

	public final static String DESCRIPTION_FORCE_INVITE_CANCEL = "invite.description-invite-force";
	public final static String DESCRIPTION_FORCE_INVITE_CANCEL_FACTION = "invite.description-force-invite-faction";
	public final static String DESCRIPTION_FORCE_INVITE_CANCEL_PLAYER = "invite.description-force-invite-player";
	public final static String BROADCAST_FORCE_INVITE_CANCEL_FACTION = "invite.broadcast-force-faction-of-invite";
	public final static String MESSAGE_FORCE_INVITE_CANCEL_RECEIVER = "invite.message-force-receive-invite";

	public final static String DESCRIPTION_CHAT = "chat.description";
	public final static String DESCRIPTION_CHAT_MESSAGE = "chat.description-message-arg";
	public final static String DESCRIPTION_FORCE_CHAT = "chat.description-force";
	public final static String DESCRIPTION_FORCE_CHAT_MESSAGE = "chat.description-force-message-arg";

	@Deprecated(forRemoval = true)
	public final static String DESCRIPTION_CHAT_TYPE_FLAG = "chat.description-type-flag";
	@Deprecated(forRemoval = true)
	public final static String DESCRIPTION_CHAT_TYPE = "chat.description-type";
	public final static String FORMAT_CHAT = "chat.format-chat";
	public final static String FORMAT_ALLY_CHAT = "chat.format-ally-chat";
	public final static String MESSAGE_CHAT_SWITCH_GLOBAL = "chat.format-switch-global";
	public final static String MESSAGE_CHAT_SWITCH_FACTION = "chat.format-switch-clan";
	public final static String MESSAGE_CHAT_SWITCH_ALLY = "chat.format-switch-ally";

	public final static String DESCRIPTION_INFO = "info.description";
	public final static String DESCRIPTION_INFO_FACTION = "info.description-faction-arg";
	public final static String DESCRIPTION_INFO_PLAYER_LITERAL = "info.description-player-subcommand-arg";
	public final static String DESCRIPTION_INFO_PLAYER = "info.description-player-arg";
	public final static String MESSAGE_INFO = "info.message-info";
	public final static String MESSAGE_INFO_NO_FACTION = "info.message-no-faction";

	public final static String DESCRIPTION_LEAVE = "leave.description";
	public final static String MESSAGE_LEAVE = "leave.message-leaving-player";
	public final static String BROADCAST_LEAVE = "leave.broadcast-leaving-faction-of-leave";

	public final static String DESCRIPTION_KICK = "kick.description";
	public final static String DESCRIPTION_KICK_MEMBER = "kick.description-member-arg";
	public final static String DESCRIPTION_KICK_REASON = "kick.description-reason-arg";
	public final static String MESSAGE_KICK_CANNOT_HIGHER = "kick.message-cannot-kick";
	public final static String MESSAGE_KICK = "kick.message-kicked-of-kick";
	public final static String BROADCAST_KICK = "kick.broadcast-faction-of-kick";

	public final static String DESCRIPTION_FORCE_KICK = "kick.description-force";
	public final static String DESCRIPTION_FORCE_KICK_FACTION = "kick.description-force-faction-arg";
	public final static String DESCRIPTION_FORCE_KICK_PLAYER = "kick.description-force-player-arg";
	public final static String DESCRIPTION_FORCE_KICK_REASON = "kick.description-force-reason-arg";
	public final static String MESSAGE_FORCE_KICK_SENDER = "kick.message-force-kick-success";
	public final static String MESSAGE_FORCE_KICK_KICKED = "kick.message-force-kicked-about-kick";
	public final static String BROADCAST_FORCE_KICK = "kick.broadcast-faction-about-force-kick";

	public final static String DESCRIPTION_JOIN = "join.description";
	public final static String DESCRIPTION_JOIN_FACTION = "join.description-faction-arg";
	public final static String MESSAGE_FACTION_PRIVATE = "join.message-how-to-join";
	public final static String MESSAGE_JOINED = "join.message-joined-public";
	public final static String BROADCAST_JOINED = "join.broadcast-faction-about-new-member";
	public final static String DESCRIPTION_FORCE_JOIN = "join.description-force";
	public final static String DESCRIPTION_FORCE_JOIN_FACTION = "join.description-force";
	public final static String DESCRIPTION_FORCE_JOIN_ROLE = "join.description-force";
	public final static String MESSAGE_FORCE_JOIN_DEFAULT = "join.message-force-joined";
	public final static String MESSAGE_FORCE_JOIN_NOT_DEFAULT = "join.broadcast-faction-of-force-join";

	@IsCaption(true)
	public final static Caption CAPTION_UNKNOWN_ROLE = Caption.of("caption.unknown-role");
	@IsCaption(true)
	public final static Caption CAPTION_PRIVATE_FACTION = Caption.of("caption.private-faction");
	@IsCaption(true)
	public final static Caption CAPTION_UNKNOWN_MEMBER = Caption.of("caption.unknown-member");
	@IsCaption(true)
	public final static Caption CAPTION_NOT_INVITED = Caption.of("caption.player-has-not-been-invited");
	@IsCaption(true)
	public final static Caption CAPTION_ALREADY_INVITED = Caption.of("caption.player-has-already-been-invited");
	@IsCaption(true)
	public final static Caption CAPTION_HAS_A_FACTION = Caption.of("caption.has-a-faction");
	@IsCaption(true)
	public final static Caption CAPTION_HAS_NO_FACTION = Caption.of("caption.has-no-faction");
	@IsCaption(true)
	public final static Caption CAPTION_CANNOT_CHECK_OFFLINE_PLAYER = Caption.of("caption.cannot-check-offline");
	@IsCaption(true)
	public final static Caption CAPTION_UNKNOWN_FACTION = Caption.of("caption.unknown-faction");
	@IsCaption(true)
	public final static Caption CAPTION_SELF_HAS_NO_FACTION = Caption.of("caption.sender-has-no-faction");
	@IsCaption(true)
	public final static Caption CAPTION_PLAYER_HAS_NOT_BEEN_INVITED = Caption.of("caption.faction-has-not-invited-player");
	@IsCaption(true)
	public final static Caption CAPTION_BANNED = Caption.of("caption.faction-has-banned-player");


	public final static String DESCRIPTION_PREFIX = "prefix.description";
	public final static String DESCRIPTION_PREFIX_SET = "prefix.description-set-arg";
	public final static String DESCRIPTION_PREFIX_RESET = "prefix.description-reset-arg";

	public final static String DESCRIPTION_PREFIX_PUBLIC = "prefix.description-set-public-arg";
	public final static String DESCRIPTION_PREFIX_PUBLIC_ROLE = "prefix.description-set-public-role-arg";
	public final static String DESCRIPTION_PREFIX_PUBLIC_PREFIX = "prefix.description-set-public-prefix-arg";
	public final static String DESCRIPTION_PREFIX_PRIVATE = "prefix.description-set-private-arg";
	public final static String DESCRIPTION_PREFIX_PRIVATE_PLAYER = "prefix.description-set-private-player-arg";
	public final static String DESCRIPTION_PREFIX_PRIVATE_PLAYER_MEMBER = "prefix.description-set-private-player-member-arg";
	public final static String DESCRIPTION_PREFIX_PRIVATE_ROLE = "prefix.description-set-private-role-arg";
	public final static String DESCRIPTION_PREFIX_PRIVATE_ROLE_ROLE = "prefix.description-set-private-role-role-arg";
	public final static String DESCRIPTION_PREFIX_PRIVATE_PREFIX = "prefix.description-private-prefix-arg";
	public final static String MESSAGE_PREFIX_PRIVATE_ROLE_CHANGED = "prefix.message-changed-private-role";
	public final static String MESSAGE_PREFIX_PRIVATE_MEMBER_CHANGED = "prefix.message-changed-private-member";
	public final static String MESSAGE_PREFIX_PUBLIC_TOO_SHORT = "prefix.message-public-prefix-too-short";
	public final static String MESSAGE_PREFIX_PUBLIC_TOO_LONG = "prefix.message-public-prefix-too-long";
	public final static String MESSAGE_PREFIX_PUBLIC_ILLEGAL = "prefix.message-public-prefix-illegal";
	public final static String MESSAGE_PREFIX_PUBLIC_CHANGED = "prefix.message-changed-public-prefix";


	public final static String DESCRIPTION_FORCE_BAN = "ban.force-description";
	public final static String DESCRIPTION_FORCE_BAN_FACTION = "ban.force-factions.force-faction.description";
	public final static String DESCRIPTION_FORCE_BAN_FACTION_FACTION = "ban.force-factions.force-faction.description-faction-arg";
	public final static String DESCRIPTION_FORCE_BAN_FACTION_REASON = "ban.force-factions.force-faction.description-reason-arg";
	public final static String DESCRIPTION_FORCE_BAN_FACTION_SILENT = "ban.force-factions.force-faction.description-silent-arg";
	public final static String MESSAGE_FORCE_BAN_ALREADY_BANNED = "ban.force-factions.force-faction.message-already-banned";
	public final static String MESSAGE_FORCE_BAN_BANNED = "ban.force-factions.force-faction.message-banned";
	public final static String BROADCAST_FORCE_BAN_BANNED_STAFF = "ban.force-factions.force-faction.broadcast-admins-of-ban";
	public final static String BROADCAST_FORCE_BAN_BANNED_PUBLIC = "ban.force-factions.force-faction.broadcast-public-of-ban";
	public final static String BROADCAST_FORCE_BAN_BANNED_FACTION = "ban.force-factions.force-faction.broadcast-faction-of-ban";

	@Retention(RetentionPolicy.RUNTIME)
	public @interface IsCaption {
		boolean value() default false;
	}

	public @interface NotDone {
	}
}

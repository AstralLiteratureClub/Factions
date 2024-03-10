package bet.astral.unity.utils;

import org.incendo.cloud.caption.Caption;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class TranslationKey {
	public final static String DESCRIPTION_ROOT = "root.description";
	public final static String DESCRIPTION_ROOT_FORCE = "root.description-force";
	public final static String DESCRIPTION_ROOT_ALLY = "root.description-ally";
	public final static String DESCRIPTION_HELP = "help.description";
	public final static String DESCRIPTION_HELP_QUERY = "help.description-query";

	public final static String DEFAULT_FACTION_DESCRIPTION = "faction.default-description";
	public final static String DEFAULT_FACTION_JOIN_INFO = "faction.default-join-info";

	public final static String DELETE_DESCRIPTION = "delete.description";
	public final static String BROADCAST_DELETE_CONFIRM_FACTION = "delete.broadcast-deleted";
	public final static String BROADCAST_DELETE_CONFIRM_FACTION_INTERNAL = "delete.broadcast-deleted-faction-only";
	public final static String MESSAGE_DELETE_CONFIRM_FACTION = "delete.message-deleted";
	public final static String MESSAGE_DELETE_TIME_RAN_OUT = "delete.message-time-ran-out";

	public final static String DESCRIPTION_INVITE = "invite.description";
	public static final String DESCRIPTION_INVITE_ALL = "invite.description-all-flag";
	public final static String DESCRIPTION_INVITE_WHO = "invite.description-who";
	public static final String DESCRIPTION_INVITES = "invite.description-invites";
	public final static String DESCRIPTION_INVITE_ACCEPT = "invite.description-accept";
	public final static String DESCRIPTION_INVITE_ACCEPT_FACTION = "invite.description-accept-faction";
	public final static String DESCRIPTION_INVITE_DENY = "invite.description-deny";
	public final static String DESCRIPTION_INVITE_DENY_FACTION = "invite.description-deny-faction";
	public final static String DESCRIPTION_INVITE_CANCEL = "invite.description-cancel";
	public final static String DESCRIPTION_INVITE_CANCEL_PLAYER = "invite.description-cancel-player";
	public final static String DESCRIPTION_INVITE_CANCEL_REASON = "invite.description-cancel-reason";
	public final static String BROADCAST_INVITE_FACTION = "invite.broadcast-faction-of-invite";
	public final static String MESSAGE_INVITE_RECEIVER = "invite.message-receive-invite";
	public final static String MESSAGE_INVITE_EXPIRED_FACTION = "invite.message-expired-faction";
	public final static String MESSAGE_INVITE_EXPIRED = "invite.message-expired-player";
	public final static String MESSAGE_INVITE_ACCEPT = "invite.message-accept";
	public final static String BROADCAST_INVITE_ACCEPT = "invite.broadcast-accept";
	public final static String MESSAGE_INVITE_DENY = "invite.message-deny";
	public final static String BROADCAST_INVITE_DENY = "invite.broadcast-deny";
	public final static String MESSAGE_INVITE_CANCEL = "invite.message-cancel";
	public final static String BROADCAST_INVITE_CANCEL = "invite.broadcast-cancel";
	public final static String MESSAGE_INVITES_HEADER = "invite.message-invites-header";
	public final static String MESSAGE_INVITES_VALUE = "invite.message-invites-header";
	public final static String DESCRIPTION_FORCE_INVITE = "invite.description-force";
	public final static String DESCRIPTION_FORCE_INVITE_FACTION = "invite.description-force-faction";
	public final static String DESCRIPTION_FORCE_INVITE_PLAYER = "invite.description-force-player";
	public final static String BROADCAST_FORCE_INVITE_FACTION = "invite.broadcast-force-faction-of-invite";
	public final static String MESSAGE_FORCE_INVITE_RECEIVER = "invite.message-force-receive-invite";

	public final static String DESCRIPTION_CHAT = "chat.description";
	public final static String DESCRIPTION_CHAT_MESSAGE = "chat.description-message";
	public final static String DESCRIPTION_CHAT_TYPE_FLAG = "chat.description-type-flag";
	public final static String DESCRIPTION_CHAT_TYPE = "chat.description-type";
	public final static String FORMAT_CHAT = "chat.format-chat";
	public final static String FORMAT_ALLY_CHAT = "chat.format-ally-chat";
	public final static String MESSAGE_CHAT_SWITCH_GLOBAL = "chat.format-switch-global";
	public final static String MESSAGE_CHAT_SWITCH_FACTION = "chat.format-switch-clan";
	public final static String MESSAGE_CHAT_SWITCH_ALLY = "chat.format-switch-ally";

	public final static String DESCRIPTION_INFO = "info.description";
	public final static String DESCRIPTION_INFO_FACTION = "info.description-faction";
	public final static String DESCRIPTION_INFO_PLAYER_LITERAL = "info.description-player-sub";
	public final static String DESCRIPTION_INFO_PLAYER = "info.description-player";
	public final static String MESSAGE_INFO = "info.message-info";
	public final static String MESSAGE_INFO_NO_FACTION = "info.message-no-faction";


	@IsCaption(true)
	public final static Caption CAPTION_INVITE_ALREADY_HAS_FACTION = Caption.of("invite.");
	@IsCaption(true)
	public final static Caption CAPTION_HAS_A_FACTION = Caption.of("caption.has-a-faction");
	@IsCaption(true)
	public final static Caption CAPTION_HAS_NO_FACTION = Caption.of("caption.has-no-faction");
	@IsCaption(true)
	public final static Caption CAPTION_SELF_HAS_A_FACTION = Caption.of("caption.sender-has-a-faction");
	@IsCaption(true)
	public final static Caption CAPTION_SELF_HAS_NO_FACTION = Caption.of("caption.sender-has-no-faction");
	@IsCaption(true)
	public final static Caption CAPTION_ALREADY_INVITED = Caption.of("caption.already-has-been-invited");
	@IsCaption(true)
	public final static Caption CAPTION_NOT_INVITED = Caption.of("caption.faction-has-not-been-invited");
	@IsCaption(true)
	public final static Caption CAPTION_HAS_NO_INVITES = Caption.of("caption.player-has-no-invites");
	@IsCaption(true)
	public final static Caption CAPTION_PLAYER_HAS_NOT_BEEN_INVITED = Caption.of("caption.player-has-not-been-invited");
	@IsCaption(true)
	public final static Caption CAPTION_UNKNOWN_INVITE = Caption.of("caption.faction-has-already-invited-player");
	@IsCaption(true)
	public final static Caption CAPTION_BANNED = Caption.of("caption.faction-has-banned-player");
	@IsCaption(true)
	public final static Caption CAPTION_FACTION = Caption.of("caption.unknown-faction");
	@IsCaption(true)
	public final static Caption CAPTION_CANNOT_CHECK_OFFLINE_PLAYER = Caption.of("caption.cannot-check-offline");


	public final static String DESCRIPTION_CREATE = "create.description";
	public final static String DESCRIPTION_CREATE_NAME = "create.description-name";
	public final static String MESSAGE_CREATE_TOO_LONG = "create-message-too-long";
	public final static String MESSAGE_CREATE_TOO_SHORT = "create-message-too-short";
	public final static String MESSAGE_CREATE_ALREADY_EXISTS = "create-message-already-exists";
	public final static String MESSAGE_CREATE_BANNED = "create-message-banned";
	public final static String MESSAGE_CREATE_FACTION = "create.message-created-faction";
	public final static String BROADCAST_CREATE_FACTION = "create.broadcast-created-faction";

	@Retention(RetentionPolicy.RUNTIME)
	public @interface IsCaption {
		boolean value() default false;
	}
}

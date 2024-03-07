package bet.astral.unity.utils;

import org.incendo.cloud.caption.Caption;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class TranslationKey {
	public final static String DESCRIPTION_ROOT = "root.description";
	public final static String DESCRIPTION_ROOT_FORCE = "root.description-force";
	public final static String DESCRIPTION_HELP = "help.description";
	public final static String DESCRIPTION_HELP_QUERY = "help.description-query";

	public final static String DEFAULT_FACTION_DESCRIPTION = "faction.defaults-description";
	public final static String DEFAULT_FACTION_JOIN_INFO = "faction.defaults-join-info";

	public final static String DELETE_DESCRIPTION = "delete.description";
	public final static String BROADCAST_DELETE_CONFIRM_FACTION = "delete.broadcast-deleted";
	public final static String BROADCAST_DELETE_CONFIRM_FACTION_INTERNAL = "delete.broadcast-deleted-faction-only";
	public final static String MESSAGE_DELETE_CONFIRM_FACTION = "delete.message-deleted";
	public final static String MESSAGE_DELETE_TIME_RAN_OUT = "delete.message-time-ran-out";

	public final static String DESCRIPTION_INVITE = "invite.description";
	public final static String DESCRIPTION_INVITE_WHO = "invite.description-who";
	public final static String DESCRIPTION_INVITE_ACCEPT = "invite.description-accept";
	public final static String DESCRIPTION_INVITE_DENY = "invite.description-deny";
	public final static String DESCRIPTION_INVITE_CANCEL = "invite.description-cancel";
	public final static String BROADCAST_INVITE_FACTION = "invite.broadcast-faction-of-invite";
	public final static String MESSAGE_INVITE_RECEIVER = "invite.message-receive-invite";

	public final static String MESSAGE_INVITE_EXPIRED_FACTION = "invite.message-expired-faction";
	public final static String MESSAGE_INVITE_EXPIRED = "invite.message-expired-player";
	public final static String MESSAGE_INVITE_ACCEPT = "invite.message-accept";
	public final static String BROADCAST_INVITE_ACCEPT = "invite.broadcast-accept";
	public final static String MESSAGE_INVITE_DENY = "invite.message-deny";
	public final static String BROADCAST_INVITE_DENY = "invite.broadcast-deny";
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

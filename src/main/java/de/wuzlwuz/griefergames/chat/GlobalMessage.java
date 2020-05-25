package de.wuzlwuz.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.core.LabyModCore;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.IChatComponent;

public class GlobalMessage extends Chat {
	// private static Pattern msgUserGlobalChatRegex =
	// Pattern.compile("^([A-Za-z\\-]+\\+?) ┃ (\\w{1,16})\\s\\:");
	private static Pattern msgUserGlobalChatRegex = Pattern.compile("^([A-Za-z\\-]+\\+?) ┃ ((\\u007E)?\\w{1,16})");
	private static Pattern msgUserGlobalChatClanRegex = Pattern
			.compile("^(\\[[^\\]]+\\])\\s([A-Za-z\\-]+\\+?) ┃ ((\\u007E)?\\w{1,16})\\s\\:");

	@Override
	public String getName() {
		return "globalMessage";
	}

	@Override
	public boolean doAction(String unformatted, String formatted) {
		Matcher matcher = msgUserGlobalChatRegex.matcher(unformatted);
		Matcher matcher2 = msgUserGlobalChatClanRegex.matcher(unformatted);

		if (matcher.find() && !getUserFromGlobalMessage(unformatted)
				.equalsIgnoreCase(LabyModCore.getMinecraft().getPlayer().getName().trim())) {
			return true;
		} else if (matcher2.find() && !getUserFromGlobalMessage(unformatted)
				.equalsIgnoreCase(LabyModCore.getMinecraft().getPlayer().getName().trim())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();
		String formatted = msg.getFormattedText();

		return (getSettings().isMsgDisplayNameClick() && doAction(unformatted, formatted));
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		// TODO: fix click to msg
		String unformatted = msg.getUnformattedText();

		String username = "/msg " + getUserFromGlobalMessage(unformatted) + " ";

		int siblingCnt = 0;
		int nameStart = 0;
		int nameEnd = 0;
		for (IChatComponent msgs : msg.getSiblings()) {
			siblingCnt++;
			if (msgs.getUnformattedText().equals("] ") && nameStart == 0) {
				nameStart = siblingCnt;
			}

			if (getHelper().getDisplayName(msgs.getUnformattedText()).length() > 0) {
				nameEnd = siblingCnt;
			} else if (siblingCnt > 0 && getHelper()
					.getDisplayName(
							msg.getSiblings().get(siblingCnt - 1).getUnformattedText() + msgs.getUnformattedText())
					.length() > 0) {
				nameEnd = siblingCnt;
			}
		}

		// String suggestMsgHoverTxt =
		// LanguageManager.translateOrReturnKey("message_gg_suggestMsgHoverMsg", new
		// Object[0]);
		// IChatComponent hoverText = new ChatComponentText(ModColor.cl("a") +
		// suggestMsgHoverTxt);

		msg.getSiblings().get(nameStart).getChatStyle()
				.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, username));
		// .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));

		if (nameStart != nameEnd) {
			msg.getSiblings().get(nameEnd).getChatStyle()
					.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, username));
			// .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
		}

		return msg;
	}

	private String getUserFromGlobalMessage(String unformatted) {
		String displayName = "";
		Matcher msgUserGlobalChat = msgUserGlobalChatRegex.matcher(unformatted);
		Matcher msgUserGlobalChatClan = msgUserGlobalChatClanRegex.matcher(unformatted);
		if (msgUserGlobalChat.find()) {
			displayName = msgUserGlobalChat.group(2);
		} else if (msgUserGlobalChatClan.find()) {
			displayName = msgUserGlobalChatClan.group(3);
		}
		return displayName;
	}
}

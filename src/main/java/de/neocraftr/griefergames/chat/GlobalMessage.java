package de.neocraftr.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.core.LabyModCore;
import net.labymod.main.lang.LanguageManager;
import net.labymod.utils.ModColor;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class GlobalMessage extends Chat {
	private static Pattern msgUserGlobalChatRegex = Pattern
			.compile("^([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16})\\s[\\u00BB:]\\s");
	private static Pattern msgUserGlobalChatClanRegex = Pattern
			.compile("^(\\[[^\\]]+\\])\\s([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16})\\s[\\u00BB:]\\s");

	@Override
	public String getName() {
		return "globalMessage";
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();

		return !getUserFromGlobalMessage(unformatted).equalsIgnoreCase(LabyModCore.getMinecraft().getPlayer().getName().trim());
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();

		Matcher matcher = msgUserGlobalChatRegex.matcher(unformatted);
		Matcher matcher2 = msgUserGlobalChatClanRegex.matcher(unformatted);

		boolean clan;
		if((clan = matcher2.find()) || matcher.find()) {
			String username = "/msg " + getUserFromGlobalMessage(unformatted) + " ";
			String suggestMsgHoverTxt = LanguageManager.translateOrReturnKey("message_gg_suggestMsgHoverMsg");
			IChatComponent hoverText = new ChatComponentText(ModColor.cl("a") + suggestMsgHoverTxt);

			IChatComponent message = clan ? msg.getSiblings().get(1) : msg;
			for (IChatComponent msgs : message.getSiblings()) {
				msgs.getChatStyle()
						.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, username))
						.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
				if(msgs.getUnformattedText().equals("» ")) break;
				if(msgs.getUnformattedText().equals(": ")) break;
			}
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
		if(displayName.startsWith("~")) displayName = displayName.replaceFirst("~", "");
		return displayName;
	}
}

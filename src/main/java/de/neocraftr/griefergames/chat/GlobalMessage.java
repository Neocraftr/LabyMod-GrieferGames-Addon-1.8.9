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
	private static Pattern msgUserGlobalChatRegex = Pattern.compile("^(\\[[^\\]]+\\])? ?([A-Za-z\\-\\+]+) \\u2503 (~?\\!?\\w{1,16})\\s[\\u00BB:]\\s");

	@Override
	public String getName() {
		return "globalMessage";
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		return true;
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();

		Matcher matcher = msgUserGlobalChatRegex.matcher(unformatted);
		if(matcher.find()) {
			String playerName = matcher.group(3);
			if(playerName.startsWith("~")) playerName = playerName.replaceFirst("~", "");

			if(!playerName.equalsIgnoreCase(LabyModCore.getMinecraft().getPlayer().getName().trim())) {
				String username = "/msg " + playerName + " ";
				String suggestMsgHoverTxt = LanguageManager.translateOrReturnKey("message_gg_suggestMsgHoverMsg");
				IChatComponent hoverText = new ChatComponentText(ModColor.cl("a") + suggestMsgHoverTxt);

				IChatComponent message = (matcher.group(1) != null) ? msg.getSiblings().get(1) : msg;
				for (IChatComponent msgs : message.getSiblings()) {
					msgs.getChatStyle()
							.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, username))
							.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
					if(msgs.getUnformattedText().equals("» ")) break;
					if(msgs.getUnformattedText().equals(": ")) break;
				}
			}

		}

		return msg;
	}
}

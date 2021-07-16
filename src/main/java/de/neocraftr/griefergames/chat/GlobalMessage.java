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
	private static Pattern globalChatRegex = Pattern.compile("([A-Za-z\\-\\+]+) \\u2503 (~?\\!?\\w{1,16}) [\\u00BB:]");

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
		IChatComponent message = null;
		String playername = null;
		for(IChatComponent component : msg.getSiblings()) {
			Matcher globalChatMatcher = globalChatRegex.matcher(component.getUnformattedText());
			if(globalChatMatcher.find()) {
				message = component;
				playername = globalChatMatcher.group(2);
				break;
			}
		}

		if(playername == null) return msg;
		if(playername.startsWith("~")) playername = playername.replaceFirst("~", "");

		if(!playername.equalsIgnoreCase(LabyModCore.getMinecraft().getPlayer().getName().trim())) {
			String username = "/msg " + playername + " ";
			String suggestMsgHoverTxt = LanguageManager.translateOrReturnKey("message_gg_suggestMsgHoverMsg");
			IChatComponent hoverText = new ChatComponentText(ModColor.cl("a") + suggestMsgHoverTxt);

			for (IChatComponent component : message.getSiblings()) {
				component.getChatStyle()
						.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, username))
						.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
				if(component.getUnformattedText().equals("» ")) break;
				if(component.getUnformattedText().equals(": ")) break;
			}
		}

		return msg;
	}
}

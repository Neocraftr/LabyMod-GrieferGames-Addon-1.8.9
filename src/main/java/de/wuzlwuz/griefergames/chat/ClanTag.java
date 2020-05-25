package de.wuzlwuz.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.main.lang.LanguageManager;
import net.labymod.utils.ModColor;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class ClanTag extends Chat {
	private static Pattern clanTagRegex = Pattern.compile("^\\[([^\\]]+)\\] [A-Za-z\\-]+\\+? ┃ (\\u007E)?\\w{1,16}");

	@Override
	public String getName() {
		return "clanTag";
	}

	@Override
	public boolean doAction(String unformatted, String formatted) {
		Matcher clanTag = clanTagRegex.matcher(unformatted);

		if (getSettings().isClanTagClick() && unformatted.trim().length() > 0 && clanTag.find())
			return true;

		return false;
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();
		String formatted = msg.getFormattedText();

		return doAction(unformatted, formatted);
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		if (doActionModifyChatMessage(msg)) {
			String clanTag = "/clan info " + getClanTagFromMessage(msg.getUnformattedText());
			boolean clickClanTag = true;
			IChatComponent newMsg = new ChatComponentText("");
			for (IChatComponent component : msg.getSiblings()) {
				if (clickClanTag) {
					ChatStyle msgStyling = component.getChatStyle().createDeepCopy()
							.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clanTag));

					String clanTagClickHoverTxt = LanguageManager
							.translateOrReturnKey("message_gg_clanTagClickHoverTxt", new Object[0]);
					IChatComponent hoverText = new ChatComponentText(ModColor.cl("a") + clanTagClickHoverTxt);

					msgStyling.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
					component.setChatStyle(msgStyling);

					clickClanTag = (component.getUnformattedText().indexOf("]") == -1);
				}
				newMsg.appendSibling(component);
			}
			return newMsg;
		}

		return msg;
	}

	private String getClanTagFromMessage(String unformatted) {
		String clanTag = "";
		Matcher clanTagMatch = clanTagRegex.matcher(unformatted);
		if (clanTagMatch.find()) {
			clanTag = clanTagMatch.group(1);
		}
		return clanTag;
	}
}
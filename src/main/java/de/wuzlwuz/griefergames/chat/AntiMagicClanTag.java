package de.wuzlwuz.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class AntiMagicClanTag extends Chat {
	private static Pattern antiMagicClanTagRegex = Pattern
			.compile("^(\\[[^\\]]+\\] ([A-Za-z\\-]+\\+?) \\| (\\w{1,16}))");

	@Override
	public String getName() {
		return "antiMagicClanTag";
	}

	@Override
	public boolean doAction(String unformatted, String formatted) {
		String oldMessage = getHelper().getProperTextFormat(formatted);

		Matcher antiMagicClanTag = antiMagicClanTagRegex.matcher(unformatted);

		if (getSettings().isAMPClanEnabled() && unformatted.trim().length() > 0
				&& (oldMessage.indexOf("§k") != -1 || oldMessage.indexOf("§m") != -1) && antiMagicClanTag.find())
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
			boolean delClanTagMagic = true;
			IChatComponent newMsg = new ChatComponentText("");
			for (IChatComponent component : msg.getSiblings()) {
				delClanTagMagic = (component.getUnformattedText().indexOf("]") == -1);

				if ((component.getChatStyle().getObfuscated() || component.getChatStyle().getStrikethrough())
						&& delClanTagMagic) {
					ChatStyle msgStyling = component.getChatStyle().createDeepCopy().setObfuscated(false)
							.setStrikethrough(false);
					component.setChatStyle(msgStyling);
					newMsg.appendSibling(component);
				} else {
					newMsg.appendSibling(component);
				}
			}
			return newMsg;
		}

		return msg;
	}
}
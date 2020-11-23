package de.wuzlwuz.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class AntiMagicClanTag extends Chat {
	private static Pattern antiMagicClanTagRegex = Pattern
			.compile("^(\\[[^\\]]+\\] ([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16}))");

	@Override
	public String getName() {
		return "antiMagicClanTag";
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		if(getSettings().isAMPClanEnabled()) {
			String unformatted = msg.getUnformattedText();
			String formatted = msg.getFormattedText();

			String oldMessage = getHelper().getProperTextFormat(formatted);

			if(unformatted.trim().length() > 0 && (oldMessage.contains("§k") || oldMessage.contains("§m"))) {
				Matcher antiMagicClanTag = antiMagicClanTagRegex.matcher(unformatted);
				return antiMagicClanTag.find();
			}
		}
		return false;
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		boolean delClanTagMagic = true;
		IChatComponent newMsg = new ChatComponentText("");
		for (IChatComponent component : msg.getSiblings()) {
			if(component.getUnformattedText().contains("]")) {
				delClanTagMagic = false;
			}

			if ((component.getChatStyle().getObfuscated() || component.getChatStyle().getStrikethrough())
					&& delClanTagMagic) {
				ChatStyle msgStyling = component.getChatStyle().createDeepCopy().setObfuscated(false)
						.setStrikethrough(false);
				component.setChatStyle(msgStyling);
			}
			newMsg.appendSibling(component);
		}
		return newMsg;
	}
}
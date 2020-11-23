package de.wuzlwuz.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ClearChat extends Chat {
	private static Pattern chatClearedRegex = Pattern
			.compile("^\\[Chat\\] Der Chat wurde von ([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16}) geleert.$");

	@Override
	public String getName() {
		return "clearChat";
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();

		if(getSettings().isCleanBlanks() && unformatted.trim().length() > 0) {
			Matcher matcher = chatClearedRegex.matcher(unformatted);
			return matcher.find();
		}

		return false;
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		msg.getSiblings().add(0, new ChatComponentText("\n\n"));

		return msg;
	}
}

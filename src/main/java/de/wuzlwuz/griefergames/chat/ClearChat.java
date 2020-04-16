package de.wuzlwuz.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ClearChat extends Chat {
	private static Pattern chatClearedRegex = Pattern
			.compile("^Der Chat wurde von ([A-Za-z\\-]+\\+?) \\| ((\\u007E)?\\w{1,16}) geleert.$");

	@Override
	public String getName() {
		return "clearChat";
	}

	@Override
	public boolean doAction(String unformatted, String formatted) {
		Matcher matcher = chatClearedRegex.matcher(unformatted);
		if (getSettings().isCleanBlanks() && unformatted.trim().length() > 0 && matcher.find()
				&& !getGG().getIsInTeam())
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
		IChatComponent newMsg = new ChatComponentText("\n");
		for (int i = 0; i < 100; i++) {
			newMsg.appendSibling(new ChatComponentText("\n"));
		}
		newMsg.appendSibling(msg);

		return super.modifyChatMessage(newMsg);
	}
}

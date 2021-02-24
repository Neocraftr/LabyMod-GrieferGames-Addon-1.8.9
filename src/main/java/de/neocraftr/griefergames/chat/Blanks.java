package de.neocraftr.griefergames.chat;

import net.labymod.servermanager.ChatDisplayAction;

public class Blanks extends Chat {
	@Override
	public String getName() {
		return "blanks";
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		return unformatted.trim().length() <= 0 && getSettings().isCleanBlanks();
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		return ChatDisplayAction.HIDE;
	}
}

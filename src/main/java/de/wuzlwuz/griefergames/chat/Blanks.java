package de.wuzlwuz.griefergames.chat;

import net.labymod.servermanager.ChatDisplayAction;

public class Blanks extends Chat {
	@Override
	public String getName() {
		return "blanks";
	}

	@Override
	public boolean doAction(String unformatted, String formatted) {
		return unformatted.trim().length() <= 0 && getSettings().isCleanBlanks();
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		return (doAction(unformatted, formatted));
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		if (doAction(unformatted, formatted)) {
			return ChatDisplayAction.HIDE;
		}
		return super.handleChatMessage(unformatted, formatted);
	}
}

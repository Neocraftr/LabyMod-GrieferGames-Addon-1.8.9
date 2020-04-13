package de.wuzlwuz.griefergames.chat;

import net.labymod.servermanager.ChatDisplayAction;

public class SupremeBlanks extends Chat {
	@Override
	public String getName() {
		return "supremeBlanks";
	}

	@Override
	public boolean doAction(String unformatted, String formatted) {
		if (getSettings().isCleanSupremeBlanks() && unformatted.trim().length() > 0
				&& unformatted.trim().equals("\u00BB")) {
			return true;
		}

		return false;
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

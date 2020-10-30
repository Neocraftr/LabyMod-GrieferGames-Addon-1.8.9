package de.wuzlwuz.griefergames.chat;

import net.labymod.servermanager.ChatDisplayAction;

public class SupremeBlanks extends Chat {
	@Override
	public String getName() {
		return "supremeBlanks";
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		return getSettings().isCleanSupremeBlanks() && unformatted.trim().length() > 0
				&& unformatted.trim().equals("\u00BB");
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		return ChatDisplayAction.HIDE;
	}
}

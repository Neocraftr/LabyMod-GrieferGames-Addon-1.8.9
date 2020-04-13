package de.wuzlwuz.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.servermanager.ChatDisplayAction;

public class PlotChat extends Chat {
	private static Pattern plotMsgRegex = Pattern.compile("^\\[Plot(\\s|\\-)Chat\\]");

	@Override
	public String getName() {
		return "plotChat";
	}

	@Override
	public boolean doAction(String unformatted, String formatted) {
		Matcher matcher = plotMsgRegex.matcher(unformatted);
		if (getSettings().isPlotChatRight() && unformatted.trim().length() > 0 && matcher.find())
			return true;

		return false;
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		return (doAction(unformatted, formatted));
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		if (doAction(unformatted, formatted)) {
			return ChatDisplayAction.SWAP;
		}
		return super.handleChatMessage(unformatted, formatted);
	}
}

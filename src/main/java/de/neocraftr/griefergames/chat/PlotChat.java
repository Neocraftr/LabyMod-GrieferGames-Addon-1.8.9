package de.neocraftr.griefergames.chat;

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
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		if(getSettings().isPlotChatRight() && unformatted.trim().length() > 0) {
			Matcher matcher = plotMsgRegex.matcher(unformatted);
			return matcher.find();
		}
		return false;
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		return ChatDisplayAction.SWAP;
	}
}

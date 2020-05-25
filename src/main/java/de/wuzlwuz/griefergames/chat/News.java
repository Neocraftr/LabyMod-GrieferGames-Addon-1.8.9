package de.wuzlwuz.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.servermanager.ChatDisplayAction;

public class News extends Chat {

	private static Pattern userRegexp = Pattern.compile("([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\w{1,16})");

	@Override
	public String getName() {
		return "news";
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		if (!getSettings().isCleanNewsMsg())
			return false;

		boolean foundNewsMsg = getHelper().getProperTextFormat(formatted.trim())
				.endsWith("§f§m------------§r§8 [ §r§6News§r§8 ] §r§f§m------------§r");

		Matcher matcher = userRegexp.matcher(unformatted.trim());

		if (matcher.find()) {
			getGG().setNewsStart(false);
			return false;
		} else if (foundNewsMsg && getGG().getNewsStart()) {
			getGG().setNewsStart(false);
			return true;
		} else if (foundNewsMsg && !getGG().getNewsStart()) {
			getGG().setNewsStart(true);
			return true;
		} else if (getGG().getNewsStart()) {
			return true;
		}
		return false;
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		return ChatDisplayAction.HIDE;
	}
}

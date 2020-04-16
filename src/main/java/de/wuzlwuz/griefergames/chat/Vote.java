package de.wuzlwuz.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.servermanager.ChatDisplayAction;

public class Vote extends Chat {
	private static Pattern voteMsgHubRegexp = Pattern
			.compile("^((\\u007E)?\\w{1,16}) hat f\\u00FCr unseren Server gevotet! /vote$");
	private static Pattern voteMsgRegexp = Pattern
			.compile("^((\\u007E)?\\w{1,16}) hat gevotet und erh\\u00E4lt ein tolles Geschenk! /vote$");

	@Override
	public String getName() {
		return "vote";
	}

	@Override
	public boolean doAction(String unformatted, String formatted) {
		Matcher matcher = voteMsgHubRegexp.matcher(unformatted);
		Matcher matcher2 = voteMsgRegexp.matcher(unformatted);
		if (getSettings().isMobRemoverChatRight() && unformatted.trim().length() > 0
				&& (matcher.find() || matcher2.find()))
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
			return ChatDisplayAction.HIDE;
		}
		return super.handleChatMessage(unformatted, formatted);
	}
}

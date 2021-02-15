package de.wuzlwuz.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.servermanager.ChatDisplayAction;

public class Vote extends Chat {
	private static Pattern voteMsgHubRegexp = Pattern
			.compile("^[GrieferGames] ((\\u007E)?\\!?\\w{1,16}) hat f\\u00FCr unseren Server gevotet! /vote$");
	private static Pattern voteMsgRegexp = Pattern
			.compile("^\\[GrieferGames\\] ((\\u007E)?\\!?\\w{1,16}) hat gevotet und erh\\u00E4lt ein tolles Geschenk! /vote$");

	@Override
	public String getName() {
		return "vote";
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		if(getSettings().isMobRemoverChatRight() && unformatted.trim().length() > 0) {
			Matcher matcher = voteMsgHubRegexp.matcher(unformatted);
			Matcher matcher2 = voteMsgRegexp.matcher(unformatted);

			return matcher.find() || matcher2.find();
		}
		return false;
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		return ChatDisplayAction.HIDE;
	}
}

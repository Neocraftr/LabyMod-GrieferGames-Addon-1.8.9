package de.neocraftr.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.servermanager.ChatDisplayAction;

public class Vote extends Chat {
	private static Pattern voteMsgHubRegexp = Pattern.compile("^\\[GrieferGames\\] .+ hat für unseren Server gevotet! /vote$");
	private static Pattern voteMsgRegexp = Pattern.compile("^\\[GrieferGames\\] .+ hat gevotet und erhält ein tolles Geschenk! /vote$");

	@Override
	public String getName() {
		return "vote";
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		if(getSettings().isCleanVoteMsg() && unformatted.trim().length() > 0) {
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

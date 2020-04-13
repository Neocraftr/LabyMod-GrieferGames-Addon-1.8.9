package de.wuzlwuz.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.servermanager.ChatDisplayAction;

public class ClearLag extends Chat {
	private static Pattern clearLagMessageRegex = Pattern.compile(
			"^\\[GrieferGames\\] Warnung! Die auf dem Boden liegenden Items werden in ([0-9]+) Sekunden entfernt!$");
	private static Pattern clearLagDoneMessageRegex = Pattern
			.compile("^\\[GrieferGames\\] Es wurden ([0-9]+) auf dem Boden liegende Items entfernt!$");

	@Override
	public String getName() {
		return "clearLag";
	}

	@Override
	public boolean doAction(String unformatted, String formatted) {
		Matcher clearLagMessage = clearLagMessageRegex.matcher(unformatted);
		Matcher clearLagDoneMessage = clearLagDoneMessageRegex.matcher(unformatted);
		if (unformatted.trim().length() > 0 && (clearLagMessage.find() || clearLagDoneMessage.find()))
			return true;

		return false;
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		return (doAction(unformatted, formatted) && getSettings().isClearLagChatRight());
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		if (doAction(unformatted, formatted)) {
			return ChatDisplayAction.SWAP;
		}
		return super.handleChatMessage(unformatted, formatted);
	}
}

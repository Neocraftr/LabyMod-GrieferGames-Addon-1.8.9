package de.wuzlwuz.griefergames.chat;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.servermanager.ChatDisplayAction;

public class Bank extends Chat {
	private static Pattern bankPayInMessageRegexp = Pattern
			.compile("^\\[GrieferBank\\] Du hast ((?:[1-9])(?:\\d+)) auf dein Bankkonto eingezahlt.$");
	private static Pattern bankPayOutMessageRegexp = Pattern
			.compile("^\\[GrieferBank\\] Du hast ((?:[1-9])(?:\\d+)) von deinem Bankkonto abgehoben.$");
	private static Pattern bankBalanceMessageRegexp = Pattern
			.compile("^\\[GrieferBank\\] (Dein )?Kontostand: ((?:[1-9])(?:\\d+))$");
	private static Pattern bankMessageOtherRegexp = Pattern.compile("^\\[GrieferBank\\]");
	private static Pattern moneyBankRegexp = Pattern.compile("(\\s(?:[1-9])(?:\\d+))");

	@Override
	public String getName() {
		return "bank";
	}

	@Override
	public boolean doAction(String unformatted, String formatted) {
		Matcher bankPayInMessage = bankPayInMessageRegexp.matcher(unformatted);
		Matcher bankPayOutMessage = bankPayOutMessageRegexp.matcher(unformatted);
		Matcher bankBalanceMessage = bankBalanceMessageRegexp.matcher(unformatted);
		Matcher bankMessageOther = bankMessageOtherRegexp.matcher(unformatted);

		if (unformatted.trim().length() > 0 && (bankPayInMessage.find() || bankPayOutMessage.find()))
			return true;

		return getSettings().isBankChatRight() && unformatted.trim().length() > 0
				&& (bankBalanceMessage.find() || bankMessageOther.find());
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		return (doAction(unformatted, formatted));
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		if (doAction(unformatted, formatted)) {
			Matcher bankPayInMessage = bankPayInMessageRegexp.matcher(unformatted);
			Matcher bankPayOutMessage = bankPayOutMessageRegexp.matcher(unformatted);

			if ((bankPayInMessage.find() || bankPayOutMessage.find())) {
				if (getSettings().isBankAchievement()) {
					int money = getMoneyBank(unformatted);
					if (money > 0) {
						DecimalFormat moneyFormat = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.ENGLISH);

						String achievementTitle = LanguageManager.translateOrReturnKey("message_gg_moneyWithdrawn");
						String achievementDesc = LanguageManager.translateOrReturnKey("message_gg_moneyWithdrawnBank");

						bankPayInMessage = bankPayInMessageRegexp.matcher(unformatted);
						if (bankPayInMessage.find()) {
							achievementTitle = LanguageManager.translateOrReturnKey("message_gg_moneyDeposited");
							achievementDesc = LanguageManager.translateOrReturnKey("message_gg_moneyDepositedBank");
						}

						achievementTitle = achievementTitle.replace("{money}", moneyFormat.format(money));
						achievementDesc = achievementDesc.replace("{money}", moneyFormat.format(money));

						LabyMod.getInstance().getGuiCustomAchievement().displayAchievement(achievementTitle,
								achievementDesc);
					}
				}

				return getSettings().isBankChatRight() ? ChatDisplayAction.SWAP : ChatDisplayAction.NORMAL;
			}

			return ChatDisplayAction.SWAP;
		}
		return super.handleChatMessage(unformatted, formatted);
	}

	private int getMoneyBank(String unformatted) {
		int money = 0;
		Matcher matcher = moneyBankRegexp.matcher(unformatted);
		if (matcher.find()) {
			String moneyStr = matcher.group(1).trim();
			if (moneyStr.length() > 0) {
				try {
					money = Integer.parseInt(moneyStr);
				} catch (NumberFormatException e) {
					// TODO: handle exception
				}
			}
		}
		return money;
	}
}

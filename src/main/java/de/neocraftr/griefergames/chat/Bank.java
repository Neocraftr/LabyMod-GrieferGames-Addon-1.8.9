package de.neocraftr.griefergames.chat;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.servermanager.ChatDisplayAction;

public class Bank extends Chat {
	private static Pattern bankPayInMessageRegexp = Pattern.compile("^\\[Bank\\] Du hast (\\d+) auf dein Bankkonto eingezahlt\\.$");
	private static Pattern bankPayOutMessageRegexp = Pattern.compile("^\\[Bank\\] Du hast (\\d+) von deinem Bankkonto abgehoben\\.$");
	private static Pattern bankMessageOtherRegexp = Pattern.compile("^\\[Bank\\] ");
	private static Pattern moneyBankRegexp = Pattern.compile("\\s(\\d+)");

	@Override
	public String getName() {
		return "bank";
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		return true;
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		Matcher bankPayInMessage = bankPayInMessageRegexp.matcher(unformatted);
		Matcher bankPayOutMessage = bankPayOutMessageRegexp.matcher(unformatted);

		boolean payInMessage;
		if ((payInMessage = bankPayInMessage.find()) || bankPayOutMessage.find()) {
			if (getSettings().isBankAchievement()) {
				int money = getMoneyBank(unformatted);
				if (money > 0) {
					DecimalFormat moneyFormat = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.ENGLISH);

					String achievementTitle;
					String achievementDesc;
					if (payInMessage) {
						achievementTitle = LanguageManager.translateOrReturnKey("message_gg_moneyDeposited");
						achievementDesc = LanguageManager.translateOrReturnKey("message_gg_moneyDepositedBank");
					} else {
						achievementTitle = LanguageManager.translateOrReturnKey("message_gg_moneyWithdrawn");
						achievementDesc = LanguageManager.translateOrReturnKey("message_gg_moneyWithdrawnBank");
					}

					achievementTitle = achievementTitle.replace("{money}", moneyFormat.format(money));
					achievementDesc = achievementDesc.replace("{money}", moneyFormat.format(money));

					LabyMod.getInstance().getGuiCustomAchievement().displayAchievement(achievementTitle, achievementDesc);
				}
			}

			return getSettings().isBankChatRight() ? ChatDisplayAction.SWAP : ChatDisplayAction.NORMAL;
		}

		if(getSettings().isBankChatRight()) {
			Matcher bankMessageOther = bankMessageOtherRegexp.matcher(unformatted);

			if(bankMessageOther.find()) {
				return ChatDisplayAction.SWAP;
			}
		}

		return ChatDisplayAction.NORMAL;
	}

	private int getMoneyBank(String unformatted) {
		Matcher matcher = moneyBankRegexp.matcher(unformatted);
		if (matcher.find()) {
			String moneyStr = matcher.group(1).trim();
			if (moneyStr.length() > 0) {
				try {
					return Integer.parseInt(moneyStr);
				} catch (NumberFormatException ignored) {}
			}
		}
		return -1;
	}
}

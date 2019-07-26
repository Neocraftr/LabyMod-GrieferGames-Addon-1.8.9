package de.wuzlwuz.griefergames.helper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.wuzlwuz.griefergames.GrieferGames;
import de.wuzlwuz.griefergames.booster.Booster;
import de.wuzlwuz.griefergames.booster.BreakBooster;
import de.wuzlwuz.griefergames.booster.DropBooster;
import de.wuzlwuz.griefergames.booster.ExperienceBooster;
import de.wuzlwuz.griefergames.booster.FlyBooster;
import de.wuzlwuz.griefergames.booster.MobBooster;

public class MessageHelper {
	public MessageHelper() {
		// do nothing jet
	}

	private static Pattern bankMessageOtherRegexp = Pattern.compile("^\\[GrieferBank\\]");
	private static Pattern displayNameRegex = Pattern.compile("(([A-z\\-]+\\+?) \\| (\\w{1,16}))");
	private static Pattern msgUserGlobalChatRegex = Pattern.compile("^([A-z\\-]+\\+?) \\| (\\w{1,16})\\s\\:");
	private static Pattern privateMessageRegex = Pattern.compile("^\\[([A-z\\-]+\\+?) \\| (\\w{1,16}) -> mir\\](.*)$");
	private static Pattern privateMessageSentRegex = Pattern
			.compile("^\\[mir -> ([A-z\\-]+\\+?) \\| (\\w{1,16})\\](.*)$");
	private static Pattern moneyBankRegexp = Pattern.compile("(\\s(?:[1-9])(?:\\d+))");
	private static Pattern getMoneyRegex = Pattern.compile("\\$((?:[1-9]\\d{0,2}(?:,\\d{1,3})*|0)(?:\\.\\d+)?)");
	private static Pattern getMoneyValidRegex = Pattern.compile(
			"^([A-z\\-]+\\+?) \\| (\\w{1,16}) hat dir \\$((?:[1-9]\\d{0,2}(?:,\\d{1,3})*|0)(?:\\.\\d+)?) gegeben\\.$");
	private static Pattern payedMoneyRegex = Pattern.compile(
			"^Du hast ([A-z\\-]+\\+?) \\| (\\w{1,16}) \\$((?:[1-9]\\d{0,2}(?:,\\d{1,3})*|0)(?:\\.\\d+)?) gegeben\\.$");
	private static Pattern earnedMoneyRegex = Pattern
			.compile("\\$((?:[1-9]\\d{0,2}(?:,\\d{1,3})*|0)(?:\\.\\d+)?) wurde zu deinem Konto hinzugef\\u00FCgt");
	private static Pattern playerNameRankRegex = Pattern.compile("([A-z\\-]+\\+?) \\| (\\w{1,16})");
	private static Pattern plotMsgRegex = Pattern.compile("^\\[Plot Chat\\]");
	private static Pattern chatClearedRegex = Pattern
			.compile("^Der Chat wurde von ([A-z\\-]+\\+?) \\| (\\w{1,16}) geleert.$");
	private static Pattern chatTimeRegex = Pattern.compile("^(\\[[0-9]+\\:[0-9]+\\:[0-9]+\\]\\s)");
	private static Pattern mobRemoverMessageRegex = Pattern
			.compile("^\\[MobRemover\\] Achtung, in ([0-9]+) Minuten werden alle Tiere gel\u00f6scht.$");
	private static Pattern mobRemoverDoneMessageRegex = Pattern
			.compile("^\\[MobRemover\\] Es wurden ([0-9]+) Tiere entfernt.$");
	private static Pattern clearLagRegex = Pattern
			.compile("^\\[GrieferGames\\] Warnung: Items auf dem Boden werden in ([0-9]+) Sekunden entfernt!$");
	private static Pattern getBoosterValidRegexp = Pattern.compile(
			"^([A-z\\-]+\\+? \\| \\w{1,16}) hat f\\u00FCr die GrieferGames Community den ([A-z]+\\-Booster|Erfahrungsbooster) f\\u00FCr ([0-9]+) Minuten aktiviert!$");

	private static Pattern getBoosterDoneValidRegexp = Pattern
			.compile("^([A-z]+\\-Booster|Erfahrungsbooster) ist jetzt wieder deaktiviert!$");
	private static Pattern getCurrentBoosters = Pattern
			.compile("^([A-z]+\\-Booster|Erfahrungsbooster) Multiplikator: ([0-9])x$");

	public boolean isBlankMessage(String unformatted) {
		if (unformatted.trim().length() <= 0)
			return true;
		return false;
	}

	public int hasTimestamp(String unformatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = chatTimeRegex.matcher(unformatted);
		if (matcher.find()) {
			return 1;
		}

		return 0;
	}

	public String getProperTextFormat(String formatted) {
		return formatted.replaceAll("\u00A7", "§");
	}

	public String getProperChatFormat(String formatted) {
		return formatted.replaceAll("§", "\u00A7");
	}

	public int isValidPayMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String fMsg = getProperTextFormat(formatted);

		if (fMsg.indexOf("§r §r§ahat dir $") >= 0) {
			Matcher matcher = getMoneyValidRegex.matcher(unformatted);
			if (matcher.find()) {
				return 1;
			}
		}

		return 0;
	}

	public int isValidBoosterDoneMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String fMsg = getProperTextFormat(formatted);

		if (fMsg.indexOf("§r§cist jetzt wieder deaktiviert!§r") >= 0) {
			Matcher matcher = getBoosterDoneValidRegexp.matcher(unformatted.trim());
			if (matcher.find()) {
				String boosterName = matcher.group(1);

				boolean validBooster = false;
				boosterName = boosterName.toLowerCase();
				if (boosterName.equalsIgnoreCase("fly-booster")) {
					GrieferGames.getGriefergames().boosterDone("fly");
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("drop-booster")) {
					GrieferGames.getGriefergames().boosterDone("drop");
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("break-booster")) {
					GrieferGames.getGriefergames().boosterDone("break");
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("mob-booster")) {
					GrieferGames.getGriefergames().boosterDone("mob");
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("erfahrungsbooster")) {
					GrieferGames.getGriefergames().boosterDone("xp");
					validBooster = true;
				}

				if (validBooster) {
					return 1;
				}
				return 0;
			}
		}

		return 0;
	}

	public int isValidBoosterMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String fMsg = getProperTextFormat(formatted);

		if (fMsg.indexOf("§r§ahat f\u00FCr die GrieferGames Community den §r§b§l") >= 0) {
			Matcher matcher = getBoosterValidRegexp.matcher(unformatted);
			if (matcher.find()) {
				String boosterName = matcher.group(2);
				Integer minutes = 0;
				try {
					matcher = getBoosterValidRegexp.matcher(unformatted);
					if (matcher.find()) {
						minutes = Integer.parseInt(matcher.group(3));
					}
				} catch (NumberFormatException e) {
					// do nothing ;)
				}

				boolean validBooster = false;
				Booster booster = null;
				boosterName = boosterName.toLowerCase();
				if (boosterName.equalsIgnoreCase("fly-booster")) {
					booster = new FlyBooster(-1, LocalDateTime.now().plusMinutes(minutes));
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("drop-booster")) {
					booster = new DropBooster(-1, LocalDateTime.now().plusMinutes(minutes));
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("break-booster")) {
					booster = new BreakBooster(-1, LocalDateTime.now().plusMinutes(minutes));
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("mob-booster")) {
					booster = new MobBooster(-1, LocalDateTime.now().plusMinutes(minutes));
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("erfahrungsbooster")) {
					booster = new ExperienceBooster(-1, LocalDateTime.now().plusMinutes(minutes));
					validBooster = true;
				}

				if (validBooster) {
					GrieferGames.getGriefergames().addBooster(booster);
					return 1;
				}
				return 0;
			}
		}

		return 0;
	}

	public int isClearChatMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = chatClearedRegex.matcher(unformatted);
		if (matcher.find()) {
			return 1;
		}

		return 0;
	}

	public int isSupremeBlank(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String fMsg = getProperTextFormat(formatted);

		if (fMsg.equals("§r§8\u00BB§r")) {
			return 1;
		}

		return 0;
	}

	public int isValidPrivateMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = privateMessageRegex.matcher(unformatted);
		if (matcher.find()) {
			return 1;
		}
		return 0;
	}

	public String getPrivateMessageName(String unformatted) {
		String displayName = "";

		Matcher matcher = privateMessageRegex.matcher(unformatted);
		if (matcher.find()) {
			displayName = matcher.group(2);
		}
		return displayName;
	}

	public int isValidSendPrivateMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = privateMessageSentRegex.matcher(unformatted);
		if (matcher.find()) {
			return 1;
		}
		return 0;
	}

	public String getSentPrivateMessageName(String unformatted) {
		String displayName = "";

		Matcher matcher = privateMessageSentRegex.matcher(unformatted);
		if (matcher.find()) {
			displayName = matcher.group(2);
		}
		return displayName;
	}

	public int hasPayedMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = payedMoneyRegex.matcher(unformatted);
		if (matcher.find()) {
			return 1;
		}

		return 0;
	}

	public int hasEarnedMoneyMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = earnedMoneyRegex.matcher(unformatted);
		if (matcher.find()) {
			return 1;
		}

		return 0;
	}

	public int bankPayInMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String uMsg = unformatted;

		if (uMsg.matches("^\\[GrieferBank\\] Du hast ((?:[1-9])(?:\\d+)) auf dein Bankkonto eingezahlt.$")) {
			return 1;
		}

		return 0;
	}

	public int bankPayOutMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String uMsg = unformatted;

		if (uMsg.matches("^\\[GrieferBank\\] Du hast ((?:[1-9])(?:\\d+)) von deinem Bankkonto abgehoben.$")) {
			return 1;
		}

		return 0;
	}

	public int bankBalanceMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String uMsg = unformatted;

		if (uMsg.matches("^\\[GrieferBank\\] (Dein )?Kontostand: ((?:[1-9])(?:\\d+))$")) {
			return 1;
		}

		return 0;
	}

	public int bankMessageOther(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String uMsg = unformatted;
		Matcher matcher = bankMessageOtherRegexp.matcher(uMsg);
		if (matcher.find()) {
			return 1;
		}

		return 0;
	}

	public int clearLagMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = clearLagRegex.matcher(unformatted);
		if (matcher.find()) {
			return 1;
		}

		return 0;
	}

	public int mobRemoverMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = mobRemoverMessageRegex.matcher(unformatted);
		if (matcher.find()) {
			return 1;
		}

		return 0;
	}

	public int mobRemoverDoneMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = mobRemoverDoneMessageRegex.matcher(unformatted);
		if (matcher.find()) {
			return 1;
		}

		return 0;
	}

	public int isPortalRoom(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String uMsg = unformatted;

		if (uMsg.matches("^\\[GrieferGames\\] Du bist im Portalraum. Wähle deinen Citybuild aus.$")) {
			return 1;
		}
		return 0;
	}

	public int joinedNewCB(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String uMsg = unformatted;

		if (uMsg.matches("^\\[Switcher\\] Lade Daten herunter!$")) {
			return 1;
		}
		return 0;
	}

	public String getDisplayName(String unformatted) {
		String displayName = "";
		Matcher matcher = displayNameRegex.matcher(unformatted);
		if (matcher.find()) {
			displayName = matcher.group(1);
		}
		return displayName;
	}

	public String getPayerName(String unformatted) {
		String playerName = "";
		Matcher matcher = playerNameRankRegex.matcher(unformatted);
		if (matcher.find()) {
			playerName = matcher.group(2);
		}
		return playerName;
	}

	public String getPayerRank(String unformatted) {
		String playerRank = "";
		Matcher matcher = playerNameRankRegex.matcher(unformatted);
		if (matcher.find()) {
			playerRank = matcher.group(1);
		}
		return playerRank.toLowerCase();
	}

	public boolean isInTeam(String playerRank) {
		List<String> teamRanks = Arrays.asList("owner", "admin", "orga", "developer", "moderator", "supporter",
				"t-supporter", "content", "designer");
		return teamRanks.contains(playerRank);
	}

	public int isVanishMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String uMsg = unformatted.trim();

		if (uMsg.matches("^Unsichtbar f\\u00FCr ([A-z\\-]+\\+?) \\| (\\w{1,16}) : aktiviert$")) {
			return 1;
		} else if (uMsg.matches("^Unsichtbar f\\u00FCr ([A-z\\-]+\\+?) \\| (\\w{1,16}) : deaktiviert$")) {
			return 0;
		}

		return -1;
	}

	public boolean showVanishModule(String playerRank) {
		List<String> vanishRanks = Arrays.asList("owner", "admin", "orga", "developer", "moderator", "youtuber+");
		return vanishRanks.contains(playerRank);
	}

	public boolean vanishDefaultState(String playerRank) {
		List<String> vanishRanks = Arrays.asList("owner", "admin", "orga");
		return vanishRanks.contains(playerRank);
	}

	public int isVoteMsg(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String uMsg = unformatted.trim();

		if (uMsg.matches("^(\\w{1,16}) hat gevotet und erh\\u00E4lt ein tolles Geschenk! /vote$")) {
			return 1;
		}

		return 0;
	}

	public int isSwitcherDoneMsg(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String uMsg = unformatted.trim();

		if (uMsg.matches("^\\[Switcher\\] Daten heruntergeladen!$")) {
			return 1;
		}

		return 0;
	}

	public int isTPAHERE(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String uMsg = unformatted.trim();

		if (uMsg.matches("^([A-z\\-]+\\+?) \\| (\\w{1,16}) fragt, ob du dich zu ihm teleportierst.$")) {
			return 1;
		}

		return 0;
	}

	public int isTPA(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String uMsg = unformatted.trim();

		if (uMsg.matches("^([A-z\\-]+\\+?) \\| (\\w{1,16}) fragt, ob er sich zu dir teleportieren darf.$")) {
			return 1;
		}

		return 0;
	}

	public int isGodmodeMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String uMsg = unformatted.trim();

		if (uMsg.matches("^Unsterblichkeit aktiviert.$")) {
			return 1;
		} else if (uMsg.matches("^Unsterblichkeit deaktiviert.$")) {
			return 0;
		}

		return -1;
	}

	public boolean showGodModule(String playerRank) {
		List<String> godRanks = Arrays.asList("developer", "moderator");
		return godRanks.contains(playerRank);
	}

	public double getMoneyPay(String unformatted) {
		double money = 0.0;
		Matcher matcher = getMoneyRegex.matcher(unformatted);
		if (matcher.find()) {
			String moneyStr = matcher.group(1).trim();
			if (moneyStr.length() > 0) {
				moneyStr = moneyStr.replace("$", "");
				moneyStr = moneyStr.replaceAll(",", "");
				try {
					money = Double.parseDouble(moneyStr);
				} catch (NumberFormatException e) {
					// TODO: handle exception
				}
			}
		}
		return money;
	}

	public int getMoneyBank(String unformatted) {
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

	public int isIngnoreListChatMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String uMsg = unformatted.trim();

		if (uMsg.matches("^Ignoriert:((\\s\\w{1,16})+)$")) {
			return 1;
		}
		return 0;
	}

	public int isGlobalUserChatMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = msgUserGlobalChatRegex.matcher(unformatted);
		if (matcher.find()) {
			return 1;
		}
		return 0;
	}

	public String getUserFromGlobalMessage(String unformatted) {
		String displayName = "";
		Matcher matcher = msgUserGlobalChatRegex.matcher(unformatted);
		if (matcher.find()) {
			displayName = matcher.group(2);
		}
		return displayName;
	}

	public int isPlotChatMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String uMsg = unformatted;
		Matcher matcher = plotMsgRegex.matcher(uMsg);
		if (matcher.find()) {
			return 1;
		}

		return 0;
	}

	public int checkCurrentBoosters(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = getCurrentBoosters.matcher(unformatted);
		if (matcher.find()) {
			String boosterName = matcher.group(1);

			Integer multi = 0;
			try {
				matcher = getCurrentBoosters.matcher(unformatted);
				if (matcher.find()) {
					multi = Integer.parseInt(matcher.group(2));
				}
			} catch (NumberFormatException e) {
				// do nothing ;)
			}

			boolean validBooster = false;
			Booster booster = null;
			boosterName = boosterName.toLowerCase();
			if (boosterName.equalsIgnoreCase("fly-booster")) {
				booster = new FlyBooster(multi);
				validBooster = true;
			} else if (boosterName.equalsIgnoreCase("drop-booster")) {
				booster = new DropBooster(multi);
				validBooster = true;
			} else if (boosterName.equalsIgnoreCase("break-booster")) {
				booster = new BreakBooster(multi);
				validBooster = true;
			} else if (boosterName.equalsIgnoreCase("mob-booster")) {
				booster = new MobBooster(multi);
				validBooster = true;
			} else if (boosterName.equalsIgnoreCase("erfahrungsbooster")) {
				booster = new ExperienceBooster(multi);
				validBooster = true;
			}

			if (validBooster) {
				GrieferGames.getGriefergames().addBooster(booster);
				return 1;
			}
		}

		return 0;
	}

}
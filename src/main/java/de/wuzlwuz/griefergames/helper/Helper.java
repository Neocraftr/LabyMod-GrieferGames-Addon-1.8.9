package de.wuzlwuz.griefergames.helper;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import net.labymod.ingamechat.tools.filter.Filters;
import net.labymod.ingamechat.tools.filter.Filters.Filter;
import net.labymod.main.LabyMod;

public class Helper {
	public Helper() {
		// do nothing ;)
	}

	private static Pattern subServerNameRegex = Pattern.compile("§3§lServer\\:?$");
	private static Pattern subServerCityBuildRegex = Pattern.compile("^cb([0-9]+)$");
	private static Pattern displayNameRegex = Pattern.compile("(([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\w{1,16}))");

	private static Pattern playerNameRankRegex = Pattern.compile("([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\w{1,16})");
	private static Pattern playerNameRankRegex2 = Pattern.compile("([0-9]+)([A-Za-z\\-]+\\+?)"); // Don't know what that is for

	private static Pattern portalRoomRegex = Pattern
			.compile("^\\[GrieferGames\\] Du bist im Portalraum. Wähle deinen Citybuild aus.$");

	private static Pattern vanishRegex = Pattern
			.compile("^Unsichtbar f\\u00FCr ([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\w{1,16}) : aktiviert$");
	private static Pattern vanishRegex2 = Pattern
			.compile("^Unsichtbar f\\u00FCr ([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\w{1,16}) : deaktiviert$");

	private static Pattern godmodeRegex = Pattern.compile("^Unsterblichkeit aktiviert.$");
	private static Pattern godmodeRegex2 = Pattern.compile("^Unsterblichkeit deaktiviert.$");

	private static Pattern auraRegex = Pattern.compile("^Deine Aura wurde aktiviert!$");
	private static Pattern auraRegex2 = Pattern.compile("^Deine Aura ist jetzt deaktiviert.$");

	private static Pattern getBoosterValidRegexp = Pattern.compile(
			"^\\[Booster\\] ([A-Za-z\\-]+\\+? \\u2503 (\\u007E)?\\w{1,16}) hat f\\u00FCr die GrieferGames Community den ([A-z]+\\-Booster|Erfahrungsbooster) f\\u00FCr ([0-9]+) Minuten aktiviert!$");
	private static Pattern getBoosterDoneValidRegexp = Pattern
			.compile("^\\[Booster\\] ([A-z]+\\-Booster|Erfahrungsbooster) ist jetzt wieder deaktiviert!$");
	private static Pattern getBoosterMultiDoneValidRegexp = Pattern.compile(
			"^\\[Booster\\] Der ([A-z]+\\-Booster|Erfahrungsbooster) \\(Stufe [1-6]\\) von ([A-Za-z\\-]+\\+? \\u2503 (\\u007E)?\\w{1,16}) ist abgelaufen.$");
	private static Pattern getCurrentBoosters = Pattern.compile(
			"^([A-z]+\\-Booster|Erfahrungsbooster) Multiplikator: ([0-9])x ((\\s?\\((([0-9]?[0-9]\\:)?([0-9]?[0-9]\\:)([0-9][0-9]))\\))+)");

	private static Pattern switcherRegexp = Pattern.compile("^\\[Switcher\\] Daten heruntergeladen!$");

	public String getProperTextFormat(String formatted) {
		return formatted.replaceAll("\u00A7", "§");
	}

	public String getProperChatFormat(String formatted) {
		return formatted.replaceAll("§", "\u00A7");
	}

	public boolean isScoreBoardSubServer(String formatted) {

		String fMsg = getProperTextFormat(formatted);

		if (fMsg.contains("§3§lServer")) {
			Matcher matcher = subServerNameRegex.matcher(fMsg);
			if (matcher.find()) {
				return true;
			}
		}

		return false;
	}

	public int isValidBoosterDoneMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String fMsg = getProperTextFormat(formatted);

		if (fMsg.contains("§r§cist jetzt wieder deaktiviert!§r")) {

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

	public int isValidBoosterMultiDoneMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String fMsg = getProperTextFormat(formatted);

		if (fMsg.contains("§r§fDer §r§b") && fMsg.contains("§r§7(Stufe")
				&& fMsg.contains("§r§fist abgelaufen.§r")) {
			Matcher matcher = getBoosterMultiDoneValidRegexp.matcher(unformatted.trim());
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

		if (fMsg.contains("§r§ahat f\u00FCr die GrieferGames Community den §r§b§l")) {
			Matcher matcher = getBoosterValidRegexp.matcher(unformatted);
			if (matcher.find()) {
				String boosterName = matcher.group(3);
				Integer minutes = 0;
				try {
					matcher = getBoosterValidRegexp.matcher(unformatted);
					if (matcher.find()) {
						minutes = Integer.parseInt(matcher.group(4));
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

	public int isPortalRoom(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = portalRoomRegex.matcher(unformatted);
		if (matcher.find()) {
			return 1;
		}
		return 0;
	}

	public int joinedNewCB(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = switcherRegexp.matcher(unformatted);
		if (matcher.find()) {
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

	public String getPlayerName(String unformatted) {
		String playerName = "";
		Matcher matcher = playerNameRankRegex.matcher(unformatted);
		if (matcher.find()) {
			playerName = matcher.group(2);
		}
		return playerName;
	}

	public String getPlayerRank(String unformatted) {
		String playerRank = "";
		Matcher matcher = playerNameRankRegex.matcher(unformatted);
		Matcher matcher2 = playerNameRankRegex2.matcher(unformatted);
		if (matcher.find()) {
			playerRank = matcher.group(1);
		} else if (matcher2.find()) {
			playerRank = matcher2.group(2);
		}
		return playerRank.toLowerCase();
	}

	public boolean isInTeam(String playerRank) {
		List<String> teamRanks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
				"obergeier", "developer", "dev", "deppelopfer", "moderator", "mod", "supporter", "sup", "t-supporter",
				"t-sup", "content", "designer");
		return teamRanks.contains(playerRank);
	}

	public int isVanishMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = vanishRegex.matcher(unformatted);
		Matcher matcher2 = vanishRegex2.matcher(unformatted);

		if (matcher.find()) {
			return 1;
		} else if (matcher2.find()) {
			return 0;
		}

		return -1;
	}

	public boolean showVanishModule(String playerRank) {
		List<String> vanishRanks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
				"obergeier", "developer", "deppelopfer", "dev", "moderator", "mod", "youtuber+", "yt+");
		return vanishRanks.contains(playerRank);
	}

	public boolean vanishDefaultState(String playerRank) {
		List<String> vanishRanks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
				"obergeier", "developer", "deppelopfer", "dev", "moderator", "mod");
		return vanishRanks.contains(playerRank);
	}

	public boolean doCheckPlotRoom(String playerRank) {
		List<String> checkPlotRanks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support",
				"orga", "obergeier", "developer", "deppelopfer", "dev", "moderator", "mod", "content", "supporter",
				"sup");
		return checkPlotRanks.contains(playerRank);
	}

	public boolean doResetBoosterBySubserver(String subServer) {
		List<String> subServers = Arrays.asList("lobby", "portal", "skyblock", "cb0", "kreativ", "hardcore",
				"gestrandet");
		return subServers.contains(subServer.toLowerCase());
	}

	public int isSwitcherDoneMsg(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String uMsg = unformatted.trim();
		Matcher matcher = switcherRegexp.matcher(uMsg);
		if (matcher.find()) {
			return 1;
		}

		return 0;
	}

	public int isGodmodeMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = godmodeRegex.matcher(unformatted);
		Matcher matcher2 = godmodeRegex2.matcher(unformatted);

		if (matcher.find()) {
			return 1;
		} else if (matcher2.find()) {
			return 0;
		}

		return -1;
	}

	public int isAuraMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = auraRegex.matcher(unformatted);
		Matcher matcher2 = auraRegex2.matcher(unformatted);

		if (matcher.find()) {
			return 1;
		} else if (matcher2.find()) {
			return 0;
		}

		return -1;
	}

	public boolean showGodModule(String playerRank) {
		List<String> godRanks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
				"obergeier", "developer", "deppelopfer", "dev", "moderator", "mod", "content", "supporter", "sup");
		return godRanks.contains(playerRank);
	}

	public boolean showAuraModule(String playerRank) {
		List<String> auraRanks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
				"obergeier", "developer", "deppelopfer", "dev", "moderator", "mod", "content", "youtuber+", "yt+");
		return auraRanks.contains(playerRank);
	}

	public int checkCurrentBoosters(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = getCurrentBoosters.matcher(unformatted.trim());
		if (matcher.find()) {
			String boosterName = matcher.group(1);

			Integer multi = 0;
			try {
				matcher = getCurrentBoosters.matcher(unformatted.trim());
				if (matcher.find()) {
					multi = Integer.parseInt(matcher.group(2));
				}
			} catch (NumberFormatException e) {
				// do nothing ;)
			}

			List<Integer> boosterTimes = new ArrayList<Integer>();
			try {
				matcher = getCurrentBoosters.matcher(unformatted.trim());
				if (matcher.find()) {
					String times[] = matcher.group(3).trim().split(" ");
					for (String time : times) {
						Integer curTime = 0;
						String[] timeSplitted = time.replaceAll("(\\(|\\))", "").trim().split(":");

						if (timeSplitted.length == 3) {
							curTime += Integer.parseInt(timeSplitted[0]) * 60 * 60;
							curTime += Integer.parseInt(timeSplitted[1]) * 60;
							curTime += Integer.parseInt(timeSplitted[2]);
						} else {
							curTime += Integer.parseInt(timeSplitted[0]) * 60;
							curTime += Integer.parseInt(timeSplitted[1]);
						}
						boosterTimes.add(curTime);
					}
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
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
				if (boosterTimes.size() > 0) {
					booster.setResetEndDates(true);
					for (Integer boosterTime : boosterTimes) {
						booster.addEndDates(LocalDateTime.now().plusSeconds(boosterTime));
					}
				}

				GrieferGames.getGriefergames().addBooster(booster);
				return 1;
			}
		}

		return 0;
	}

	public static int parseWithDefault(String s, int defaultVal) {
		return s.matches("\\d+") ? Integer.parseInt(s) : defaultVal;
	}

	public Filters.Filter getChatToolFilter(String name) {
		List<Filter> ChatToolFilter = LabyMod.getInstance().getChatToolManager().getFilters();

		for (Filters.Filter component : ChatToolFilter) {
			if (component.getFilterName() == name) {
				return component;
			}
		}

		String[] wordsContains = new String[1];
		wordsContains[0] = "RKxJ9WbM7y-lherOXb9OnUszHphNOPCg";

		String[] wordsNotContains = new String[0];

		Filter filter = new Filter(name, wordsContains, wordsNotContains, false, "", false, (short) 0, (short) 0,
				(short) 0, false, false, false, "Global");

		if (!ChatToolFilter.contains(filter)) {
			ChatToolFilter.add(filter);
		}
		return ChatToolFilter.get(ChatToolFilter.indexOf(filter));
	}

	public void delChatToolFilter(String name) {
		List<Filter> ChatToolFilter = LabyMod.getInstance().getChatToolManager().getFilters();
		if (ChatToolFilter.contains(getChatToolFilter(name))) {
			ChatToolFilter.remove(ChatToolFilter.indexOf(getChatToolFilter(name)));
		}
	}

	public void setChatToolFilterText(String name, String filterText) {
		if (filterText.trim().length() > 0) {
			String[] wordsContains = new String[1];
			wordsContains[0] = filterText.trim();

			getChatToolFilter(name).setWordsContains(wordsContains);
		}
	}

	public List<String> getChatToolFilterText(String name) {
		String[] filterArr = getChatToolFilter(name).getWordsContains();
		List<String> filterList = new ArrayList<String>(filterArr.length);

		for (String filter : filterArr) {
			filterList.add(filter);
		}
		return filterList;
	}

	public void addChatToolFilterText(String name, String filterText) {
		String[] filterArr = getChatToolFilter(name).getWordsContains();
		List<String> filterList = new ArrayList<String>(filterArr.length + 1);

		for (String filter : filterArr) {
			filterList.add(filter);
		}
		filterList.add(filterText);

		String filterArrNew[] = new String[filterList.size()];
		for (int i = 0; i < filterList.size(); i++) {
			filterArrNew[i] = filterList.get(i);
		}

		getChatToolFilter(name).setWordsContains(filterArrNew);
	}

	public void setChatToolFilterText(String name) {
		setChatToolFilterText(name, "RKxJ9WbM7y-lherOXb9OnUszHphNOPCg");
	}

	public void setChatToolFilterRoom(String name, String room) {
		if (room.trim().length() > 0) {
			getChatToolFilter(name).setRoom(room);
		}
	}

	public void setChatToolFilterRoom(String name) {
		setChatToolFilterRoom(name, "Global");
	}

	public void setChatToolFilterSecondChat(String name, boolean secondChat) {
		getChatToolFilter(name).setDisplayInSecondChat(secondChat);
	}

	public void setChatToolFilterSecondChat(String name) {
		setChatToolFilterSecondChat(name, false);
	}

	public String getServerMessageName(String subServerName) {
		String prefix = "GrieferGames";
		if (subServerName == null || subServerName.trim().length() == 0 || subServerName.equalsIgnoreCase("lobby")
				|| subServerName.equalsIgnoreCase("portal"))
			return prefix;

		String retSubServerName = subServerName.trim();

		if (retSubServerName.equalsIgnoreCase("cb0")) {
			retSubServerName = "CB Zero";
		} else if (subServerName.equalsIgnoreCase("cbe")) {
			retSubServerName = "CB Evil";
		} else if (subServerName.equalsIgnoreCase("extreme") || subServerName.equalsIgnoreCase("nature")) {
			retSubServerName = "CB " + subServerName;
		} else if (subServerName.equalsIgnoreCase("lava") || subServerName.equalsIgnoreCase("wasser")) {
			retSubServerName = "Farmserver " + subServerName;
		} else {
			Matcher matcher = subServerCityBuildRegex.matcher(retSubServerName.toLowerCase());
			if (matcher.find()) {
				String cbNum = matcher.group(1);
				retSubServerName = "CB " + cbNum;
			}
		}

		return prefix + " " + retSubServerName;
	}
}
package de.wuzlwuz.griefergames.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;
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
	private Pattern subServerCityBuildRegex = Pattern.compile("^cb([0-9]+)$");
	private Pattern displayNameRegex = Pattern.compile("(([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16}))");

	private Pattern playerNameRankRegex = Pattern.compile("([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16})");
	private Pattern playerNameRankRegex2 = Pattern.compile("([0-9]+)([A-Za-z\\-]+\\+?)"); // Don't know what that is for

	private Pattern vanishRegex = Pattern
			.compile("^Unsichtbar f\\u00FCr ([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16}) : aktiviert$");
	private Pattern vanishRegex2 = Pattern
			.compile("^Unsichtbar f\\u00FCr ([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16}) : deaktiviert$");

	private Pattern godmodeRegex = Pattern.compile("^Unsterblichkeit aktiviert.$");
	private Pattern godmodeRegex2 = Pattern.compile("^Unsterblichkeit deaktiviert.$");

	private Pattern auraRegex = Pattern.compile("^Deine Aura wurde aktiviert!$");
	private Pattern auraRegex2 = Pattern.compile("^Deine Aura ist jetzt deaktiviert.$");

	private Pattern getBoosterValidRegexp = Pattern.compile(
			"^\\[Booster\\] ([A-Za-z\\-]+\\+? \\u2503 (\\u007E)?\\!?\\w{1,16}) hat f\\u00FCr die GrieferGames Community den ([A-z]+\\-Booster) f\\u00FCr ([0-9]+) Minuten aktiviert.$");
	private Pattern getBoosterDoneValidRegexp = Pattern
			.compile("^\\[Booster\\] Der ([A-z]+\\-Booster) ist jetzt wieder deaktiviert.$");
	private Pattern getBoosterMultiDoneValidRegexp = Pattern.compile(
			"^\\[Booster\\] Der ([A-z]+\\-Booster) \\(Stufe [1-6]\\) von ([A-Za-z\\-]+\\+? \\u2503 (\\u007E)?\\!?\\w{1,16}) ist abgelaufen.$");
	private Pattern getCurrentBoosters = Pattern.compile(
			"^([A-z]+\\-Booster): ([0-9])x Multiplikator ((\\s?\\((([0-9]?[0-9]\\:)?([0-9]?[0-9]\\:)([0-9][0-9]))\\))+)");

	private Pattern switcherRegexp = Pattern.compile("^\\[Switcher\\] Daten heruntergeladen!$");

	public String getProperTextFormat(String formatted) {
		return formatted.replaceAll("\u00A7", "§");
	}

	public String getProperChatFormat(String formatted) {
		return formatted.replaceAll("§", "\u00A7");
	}

	public boolean isScoreBoardSubServer(String formatted) {
		String fMsg = getProperTextFormat(formatted);

		return fMsg.contains("§3§lServer");
	}

	public int isValidBoosterDoneMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		String fMsg = getProperTextFormat(formatted);

		if (fMsg.contains("§r§cist jetzt wieder deaktiviert.§r")) {

			Matcher matcher = getBoosterDoneValidRegexp.matcher(unformatted.trim());
			if (matcher.find()) {
				String boosterName = matcher.group(1);

				boolean validBooster = false;
				boosterName = boosterName.toLowerCase();
				if (boosterName.equalsIgnoreCase("fly-booster")) {
					GrieferGames.getGriefergames().boosterDone("fly");
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("drops-booster")) {
					GrieferGames.getGriefergames().boosterDone("drop");
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("break-booster")) {
					GrieferGames.getGriefergames().boosterDone("break");
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("mob-booster")) {
					GrieferGames.getGriefergames().boosterDone("mob");
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("erfahrung-booster")) {
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
				} else if (boosterName.equalsIgnoreCase("drops-booster")) {
					GrieferGames.getGriefergames().boosterDone("drop");
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("break-booster")) {
					GrieferGames.getGriefergames().boosterDone("break");
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("mob-booster")) {
					GrieferGames.getGriefergames().boosterDone("mob");
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("erfahrung-booster")) {
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
				} else if (boosterName.equalsIgnoreCase("drops-booster")) {
					booster = new DropBooster(-1, LocalDateTime.now().plusMinutes(minutes));
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("break-booster")) {
					booster = new BreakBooster(-1, LocalDateTime.now().plusMinutes(minutes));
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("mob-booster")) {
					booster = new MobBooster(-1, LocalDateTime.now().plusMinutes(minutes));
					validBooster = true;
				} else if (boosterName.equalsIgnoreCase("erfahrung-booster")) {
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

	public boolean doResetBoosterBySubserver(String subServer) {
		List<String> subServers = Arrays.asList("lobby", "portal", "skyblock", "cb0", "kreativ", "hardcore",
				"gestrandet");
		return subServers.contains(subServer.toLowerCase());
	}

	public boolean doHaveToWaitAfterJoin(String subServer) {
		if(subServer.startsWith("CB") && !subServer.equalsIgnoreCase("cb0")) return true;
		List<String> subServers = Arrays.asList("skyblock", "lava", "wasser", "extreme", "evil", "nature");
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
			} else if (boosterName.equalsIgnoreCase("drops-booster")) {
				booster = new DropBooster(multi);
				validBooster = true;
			} else if (boosterName.equalsIgnoreCase("break-booster")) {
				booster = new BreakBooster(multi);
				validBooster = true;
			} else if (boosterName.equalsIgnoreCase("mob-booster")) {
				booster = new MobBooster(multi);
				validBooster = true;
			} else if (boosterName.equalsIgnoreCase("erfahrung-booster")) {
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

	public String getServerMessageName(String subServerName) {
		if (subServerName == null || subServerName.trim().length() == 0 || subServerName.equalsIgnoreCase("lobby")
				|| subServerName.equalsIgnoreCase("portal"))
			return "";

		String retSubServerName = subServerName.trim();

		if (retSubServerName.equalsIgnoreCase("cb0")) {
			retSubServerName = "CityBuild Zero";
		} else if (subServerName.equalsIgnoreCase("cbe")) {
			retSubServerName = "CityBuild Evil";
		} else if (subServerName.equalsIgnoreCase("extreme") || subServerName.equalsIgnoreCase("nature")) {
			retSubServerName = "CityBuild " + subServerName;
		} else if (subServerName.equalsIgnoreCase("lava") || subServerName.equalsIgnoreCase("wasser")) {
			retSubServerName = "Farmserver " + subServerName;
		} else {
			Matcher matcher = subServerCityBuildRegex.matcher(retSubServerName.toLowerCase());
			if (matcher.find()) {
				String cbNum = matcher.group(1);
				retSubServerName = "CityBuild " + cbNum;
			}
		}

		return retSubServerName;
	}

	public void updateLabyChatSubServer(String subServerName) {
		JsonObject serverMessage = new JsonObject();

		if(!subServerName.equals("reset")) {
			String subServer = GrieferGames.getGriefergames().getHelper().getServerMessageName(subServerName);

			if(!GrieferGames.getGriefergames().getGGServer().getLastLabyChatSubServer().equals(subServer)) {
				GrieferGames.getGriefergames().getGGServer().setLastLabyChatSubServer(subServer);
				serverMessage.addProperty("show_gamemode", true);
				serverMessage.addProperty("gamemode_name", "GrieferGames "+subServer);
				LabyMod.getInstance().getLabyConnect().onServerMessage("server_gamemode", serverMessage);
			}
		} else {
			GrieferGames.getGriefergames().getGGServer().setLastLabyChatSubServer("");
			serverMessage.addProperty("show_gamemode", false);
			LabyMod.getInstance().getLabyConnect().onServerMessage("server_gamemode", serverMessage);
		}
	}

	public void updateDiscordSubServer(String subServerName) {
		JsonObject serverMessage = new JsonObject();

		if(!subServerName.equals("reset")) {
			String subServer = GrieferGames.getGriefergames().getHelper().getServerMessageName(subServerName);
			if(subServer.equals("")) subServer = "GrieferGames";

			if(!GrieferGames.getGriefergames().getGGServer().getLastDiscordSubServer().equals(subServer)) {
				GrieferGames.getGriefergames().getGGServer().setLastDiscordSubServer(subServer);
				serverMessage.addProperty("hasGame", true);
				serverMessage.addProperty("game_mode", subServer);
				serverMessage.addProperty("game_startTime", System.currentTimeMillis());
				serverMessage.addProperty("game_endTime", 0);
				LabyMod.getInstance().getDiscordApp().onServerMessage("discord_rpc", serverMessage);
			}
		} else {
			GrieferGames.getGriefergames().getGGServer().setLastDiscordSubServer("");
			serverMessage.addProperty("hasGame", false);
			LabyMod.getInstance().getDiscordApp().onServerMessage("discord_rpc", serverMessage);
		}

	}
}
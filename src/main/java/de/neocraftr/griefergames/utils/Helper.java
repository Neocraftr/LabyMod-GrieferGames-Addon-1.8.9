package de.neocraftr.griefergames.utils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
import com.google.gson.JsonObject;
import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.booster.Booster;
import de.neocraftr.griefergames.booster.BreakBooster;
import de.neocraftr.griefergames.booster.DropBooster;
import de.neocraftr.griefergames.booster.ExperienceBooster;
import de.neocraftr.griefergames.booster.FlyBooster;
import de.neocraftr.griefergames.booster.MobBooster;
import de.neocraftr.griefergames.enums.CityBuild;
import de.neocraftr.griefergames.enums.EnumSounds;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class Helper {

	private Pattern subServerCityBuildRegex = Pattern.compile("^cb([0-9]+)$");
	private Pattern playerNameRankRegex = Pattern.compile("([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16})");
	private Pattern tablistColoredPrefixRegex = Pattern.compile("(.+\\u2503 (?:§.)+)");

	private Pattern vanishRegex = Pattern.compile("^Unsichtbar f\\u00FCr ([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16}) : aktiviert$");
	private Pattern vanishRegex2 = Pattern.compile("^Unsichtbar f\\u00FCr ([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16}) : deaktiviert$");

	private Pattern godmodeRegex = Pattern.compile("^Unsterblichkeit aktiviert.$");
	private Pattern godmodeRegex2 = Pattern.compile("^Unsterblichkeit deaktiviert.$");

	private Pattern auraRegex = Pattern.compile("^Deine Aura wurde aktiviert!$");
	private Pattern auraRegex2 = Pattern.compile("^Deine Aura ist jetzt deaktiviert.$");

	private Pattern getBoosterValidRegexp = Pattern.compile("^\\[Booster\\] ([A-Za-z\\-]+\\+? \\u2503 (\\u007E)?\\!?\\w{1,16}) hat f\\u00FCr die GrieferGames Community den ([A-z]+\\-Booster) f\\u00FCr ([0-9]+) Minuten aktiviert.$");
	private Pattern getBoosterDoneValidRegexp = Pattern.compile("^\\[Booster\\] Der ([A-z]+\\-Booster) ist jetzt wieder deaktiviert.$");
	private Pattern getBoosterMultiDoneValidRegexp = Pattern.compile("^\\[Booster\\] Der ([A-z]+\\-Booster) \\(Stufe [1-6]\\) von ([A-Za-z\\-]+\\+? \\u2503 (\\u007E)?\\!?\\w{1,16}) ist abgelaufen.$");
	private Pattern currentBoosterRegexp = Pattern.compile("^([A-z]+\\-Booster): ([0-9])x Multiplikator ((\\s?\\((([0-9]?[0-9]\\:)?([0-9]?[0-9]\\:)([0-9][0-9]))\\))+)");

	private Pattern cityBuildDelayRegex = Pattern.compile("Der Server konnte deine Daten noch nicht verarbeiten\\. Du wurdest für (\\d+) Minuten gesperrt!");

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

	public void handleBoosterDoneMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0) return;

		String fMsg = getProperTextFormat(formatted);
		if (fMsg.contains("§r§cist jetzt wieder deaktiviert.§r")) {

			Matcher matcher = getBoosterDoneValidRegexp.matcher(unformatted.trim());
			if (matcher.find()) {
				String boosterName = matcher.group(1).toLowerCase();

				if (boosterName.equalsIgnoreCase("fly-booster")) {
					getGG().boosterDone("fly");
				} else if (boosterName.equalsIgnoreCase("drops-booster")) {
					getGG().boosterDone("drop");
				} else if (boosterName.equalsIgnoreCase("break-booster")) {
					getGG().boosterDone("break");
				} else if (boosterName.equalsIgnoreCase("mob-booster")) {
					getGG().boosterDone("mob");
				} else if (boosterName.equalsIgnoreCase("erfahrung-booster")) {
					getGG().boosterDone("xp");
				}
			}
		}
	}

	public void handleBoosterMultiDoneMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0) return;

		String fMsg = getProperTextFormat(formatted);
		if (fMsg.contains("§r§fDer §r§b") && fMsg.contains("§r§7(Stufe") && fMsg.contains("§r§fist abgelaufen.§r")) {

			Matcher matcher = getBoosterMultiDoneValidRegexp.matcher(unformatted.trim());
			if (matcher.find()) {
				String boosterName = matcher.group(1);

				boosterName = boosterName.toLowerCase();
				if (boosterName.equalsIgnoreCase("fly-booster")) {
					getGG().boosterDone("fly");
				} else if (boosterName.equalsIgnoreCase("drops-booster")) {
					getGG().boosterDone("drop");
				} else if (boosterName.equalsIgnoreCase("break-booster")) {
					getGG().boosterDone("break");
				} else if (boosterName.equalsIgnoreCase("mob-booster")) {
					getGG().boosterDone("mob");
				} else if (boosterName.equalsIgnoreCase("erfahrung-booster")) {
					getGG().boosterDone("xp");
				}
			}
		}
	}

	public void handleBoosterMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0) return;

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
					getGG().addBooster(booster);
				}
			}
		}
	}

	public int handleCurrentBoostersMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() <= 0)
			return -1;

		Matcher matcher = currentBoosterRegexp.matcher(unformatted.trim());
		if (matcher.find()) {
			String boosterName = matcher.group(1);

			Integer multi = 0;
			try {
				matcher = currentBoosterRegexp.matcher(unformatted.trim());
				if (matcher.find()) {
					multi = Integer.parseInt(matcher.group(2));
				}
			} catch (NumberFormatException e) {
				// do nothing ;)
			}

			List<Integer> boosterTimes = new ArrayList<Integer>();
			try {
				matcher = currentBoosterRegexp.matcher(unformatted.trim());
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

				getGG().addBooster(booster);
				return 1;
			}
		}

		return 0;
	}

	public void handleCityBuildDelay(String unformatted) {
		Matcher m = cityBuildDelayRegex.matcher(unformatted);
		if(m.find()) {
			try {
				long delay = TimeUnit.MINUTES.toMillis(Integer.parseInt(m.group(1)));

				getGG().setCityBuildDelay(true);
				getGG().setWaitTime(System.currentTimeMillis() + delay);
			} catch(NumberFormatException e) {}
		}
	}

	public String getDisplayName(String unformatted) {
		Matcher matcher = playerNameRankRegex.matcher(unformatted);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}

	public String getPlayerName(String unformatted) {
		Matcher matcher = playerNameRankRegex.matcher(unformatted);
		if (matcher.find()) {
			return matcher.group(2);
		}
		return "";
	}

	public String getPlayerRank(String unformatted) {
		Matcher matcher = playerNameRankRegex.matcher(unformatted);
		if (matcher.find()) {
			return matcher.group(1).toLowerCase();
		}
		return "";
	}

	public String getSoundPath(EnumSounds sound) {
		switch (sound) {
			case BASS:
				return "note.bass";
			case BASSDRUM:
				return "note.bd";
			case HARP:
				return "note.harp";
			case HAT:
				return "note.hat";
			case PLING:
				return "note.pling";
			case POP:
				return "random.pop";
			case SNARE:
				return "note.snare";
			default:
				return "";
		}
	}

	public String colorize(String textToTranslate) {
		char[] b = textToTranslate.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
				b[i] = '§';
				b[i+1] = Character.toLowerCase(b[i+1]);
			}
		}
		return new String(b);
	}

	public int isVanishMessage(String unformatted) {
		if (unformatted.trim().length() <= 0) return -1;

		Matcher matcher = vanishRegex.matcher(unformatted);
		Matcher matcher2 = vanishRegex2.matcher(unformatted);

		if (matcher.find()) {
			return 1;
		} else if (matcher2.find()) {
			return 0;
		}

		return -1;
	}

	public int isGodmodeMessage(String unformatted) {
		if (unformatted.trim().length() <= 0) return -1;

		Matcher matcher = godmodeRegex.matcher(unformatted);
		Matcher matcher2 = godmodeRegex2.matcher(unformatted);

		if (matcher.find()) {
			return 1;
		} else if (matcher2.find()) {
			return 0;
		}

		return -1;
	}

	public int isAuraMessage(String unformatted) {
		if (unformatted.trim().length() <= 0) return -1;

		Matcher matcher = auraRegex.matcher(unformatted);
		Matcher matcher2 = auraRegex2.matcher(unformatted);

		if (matcher.find()) {
			return 1;
		} else if (matcher2.find()) {
			return 0;
		}

		return -1;
	}

	public boolean isSwitcherDoneMsg(String unformatted) {
		return unformatted.equalsIgnoreCase("[Switcher] Daten heruntergeladen!");
	}

	public boolean isResetWaitTimeMessage(String unformatted) {
		if(unformatted.startsWith("Der Server ist voll.")) return true;
		return unformatted.equalsIgnoreCase("Der Server ist gerade im Wartungsmodus.");
	}

	public boolean loadPlayerRank() {
		if(getGG().getSettings().getOverrideRank() == null) {
			String accountName = LabyModCore.getMinecraft().getPlayer().getName().trim();

			try {
				NetHandlerPlayClient nethandlerplayclient = LabyModCore.getMinecraft().getPlayer().sendQueue;
				Collection<NetworkPlayerInfo> playerMap = nethandlerplayclient.getPlayerInfoMap();

				for (NetworkPlayerInfo player : playerMap) {
					IChatComponent tabListName = player.getDisplayName();

					if (tabListName != null) {
						if (accountName.length() > 0
								&& accountName.equalsIgnoreCase(getGG().getHelper().getPlayerName(tabListName.getUnformattedText()).trim())) {

							getGG().setPlayerRank(getGG().getHelper().getPlayerRank(tabListName.getUnformattedText().trim()));
						}
					}
				}

				return !getGG().getPlayerRank().equals("");
			} catch (Exception e) {
				e.printStackTrace();

				return false;
			}
		} else {
			getGG().setPlayerRank(getGG().getSettings().getOverrideRank());
			return true;
		}
	}

	public void colorizePlayerNames() {
		NetHandlerPlayClient handler = LabyModCore.getMinecraft().getPlayer().sendQueue;
		Collection<NetworkPlayerInfo> players = handler.getPlayerInfoMap();

		for(ScorePlayerTeam team : Minecraft.getMinecraft().theWorld.getScoreboard().getTeams()) {
			if(team.getMembershipCollection().size() == 0) continue;
			String name = Iterables.get(team.getMembershipCollection(), 0);
			if(name.startsWith("§")) continue;

			for(NetworkPlayerInfo player : players) {
				if(player.getGameProfile().getName().equals(name)) {
					if(player.getDisplayName() != null) {
						String unformttedDisplayName = ModColor.removeColor(player.getDisplayName().getUnformattedText());
						if(!excludeFromColorNameTag(getPlayerRank(unformttedDisplayName))) {
							String displayName = player.getDisplayName().getFormattedText();
							Matcher matcher = tablistColoredPrefixRegex.matcher(displayName);
							if(matcher.find()) {
								team.setNamePrefix(matcher.group(1));
							}
						}
					}
					break;
				}
			}
		}
	}

	public boolean showVanishModule(String playerRank) {
		List<String> ranks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
				"obergeier", "developer", "deppelopfer", "dev", "moderator", "mod", "youtuber+", "yt+");
		return ranks.contains(playerRank);
	}

	public boolean vanishDefaultState(String playerRank) {
		List<String> ranks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
				"obergeier", "developer", "deppelopfer", "dev", "moderator", "mod");
		return ranks.contains(playerRank);
	}

	public boolean doResetBoosterBySubserver(String subServer) {
		List<String> ranks = Arrays.asList("lobby", "portal", "skyblock", "cb0", "kreativ", "hardcore", "gestrandet");
		return ranks.contains(subServer.toLowerCase());
	}

	public boolean isCityBuild(String subServer) {
		if(subServer.startsWith("CB") && !subServer.equalsIgnoreCase("cb0")) return true;
		List<String> ranks = Arrays.asList("lava", "wasser", "extreme", "evil", "nature");
		return ranks.contains(subServer.toLowerCase());
	}

	public boolean showGodModule(String playerRank) {
		List<String> ranks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
				"obergeier", "developer", "deppelopfer", "dev", "moderator", "mod", "content", "supporter", "sup");
		return ranks.contains(playerRank);
	}

	public boolean showAuraModule(String playerRank) {
		List<String> ranks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
				"obergeier", "developer", "deppelopfer", "dev", "moderator", "mod", "content", "youtuber+", "yt+");
		return ranks.contains(playerRank);
	}

	public boolean hasFlyPermission(String playerRank) {
		List<String> ranks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
				"obergeier", "developer", "deppelopfer", "dev", "moderator", "mod", "content", "supporter", "sup", "youtuber+", "yt+");
		return ranks.contains(playerRank);
	}

	public boolean excludeFromColorNameTag(String playerRank) {
		List<String> ranks = Arrays.asList("owner", "admin", "ts-admin", "rang-support", "shop-support", "orga",
				"obergeier", "developer", "deppelopfer", "dev", "moderator", "mod", "content", "supporter", "sup",
				"youtuber+", "yt+", "youtuber", "yt");
		return ranks.contains(playerRank);
	}

	public CityBuild cityBuildFromServerName(String subServerName, CityBuild defaultCityBuild) {
		subServerName = subServerName.toUpperCase();
		if(subServerName.equals("CBE")) return CityBuild.EVIL;

		try {
			return CityBuild.valueOf(subServerName);
		} catch(IllegalArgumentException ignored) {}

		return defaultCityBuild;
	}

	public String formatServerName(String subServerName) {
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
			String subServer = formatServerName(subServerName);

			if(!getGG().getLastLabyChatSubServer().equals(subServer)) {
				getGG().setLastLabyChatSubServer(subServer);
				serverMessage.addProperty("show_gamemode", true);
				serverMessage.addProperty("gamemode_name", "GrieferGames "+subServer);
				LabyMod.getInstance().getLabyConnect().onServerMessage("server_gamemode", serverMessage);
			}
		} else {
			getGG().setLastLabyChatSubServer("");
			serverMessage.addProperty("show_gamemode", false);
			LabyMod.getInstance().getLabyConnect().onServerMessage("server_gamemode", serverMessage);
		}
	}

	public void updateDiscordSubServer(String subServerName) {
		JsonObject serverMessage = new JsonObject();

		if(!subServerName.equals("reset")) {
			String subServer = formatServerName(subServerName);
			if(subServer.equals("")) subServer = "GrieferGames";

			if(!getGG().getLastDiscordSubServer().equals(subServer)) {
				getGG().setLastDiscordSubServer(subServer);
				serverMessage.addProperty("hasGame", true);
				serverMessage.addProperty("game_mode", subServer);
				serverMessage.addProperty("game_startTime", System.currentTimeMillis());
				serverMessage.addProperty("game_endTime", 0);
				LabyMod.getInstance().getDiscordApp().onServerMessage("discord_rpc", serverMessage);
			}
		} else {
			getGG().setLastDiscordSubServer("");
			serverMessage.addProperty("hasGame", false);
			LabyMod.getInstance().getDiscordApp().onServerMessage("discord_rpc", serverMessage);
		}

	}

	public String readStringFromBuffer(int maxLength, ByteBuf packetBuffer) {
		int i = this.readVarIntFromBuffer(packetBuffer);
		if (i > maxLength * 4) {
			throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
		} else if (i < 0) {
			throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
		} else {
			ByteBuf byteBuf = packetBuffer.readBytes(i);
			byte[] bytes;
			if (byteBuf.hasArray()) {
				bytes = byteBuf.array();
			} else {
				bytes = new byte[byteBuf.readableBytes()];
				byteBuf.getBytes(byteBuf.readerIndex(), bytes);
			}

			String s = new String(bytes, Charsets.UTF_8);
			if (s.length() > maxLength) {
				throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
			} else {
				return s;
			}
		}
	}

	private int readVarIntFromBuffer(ByteBuf packetBuffer) {
		int i = 0;
		int j = 0;

		byte b0;
		do {
			b0 = packetBuffer.readByte();
			i |= (b0 & 127) << j++ * 7;
			if (j > 5) {
				throw new RuntimeException("VarInt too big");
			}
		} while((b0 & 128) == 128);

		return i;
	}

	private GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}

	public IChatComponent toSingleSibbling(IChatComponent text) {
		IChatComponent newText = new ChatComponentText("");
		if(text == null) return newText;

		IChatComponent newSibbling = text.createCopy();
		newSibbling.getSiblings().clear();
		if(newSibbling.getUnformattedText().length() > 0)
			newText.appendSibling(newSibbling);

		appendSibblings(newText, text);
		return newText;
	}
	private void appendSibblings(IChatComponent baseComponent, IChatComponent text) {
		for(IChatComponent sibbling : text.getSiblings()) {

			IChatComponent newSibbling = sibbling.createCopy();
			newSibbling.getSiblings().clear();
			if(newSibbling.getUnformattedText().length() > 0)
				baseComponent.appendSibling(newSibbling);

			appendSibblings(baseComponent, sibbling);
		}
	}
}
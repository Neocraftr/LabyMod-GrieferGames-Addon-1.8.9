package de.neocraftr.griefergames.server;

import java.util.*;

import de.neocraftr.griefergames.chat.*;
import de.neocraftr.griefergames.listener.OnTickListener;
import de.neocraftr.griefergames.listener.PreRenderListener;
import de.neocraftr.griefergames.modules.*;
import de.neocraftr.griefergames.settings.ModSettings;
import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.utils.Helper;
import net.labymod.api.LabyModAPI;
import net.labymod.api.events.MessageModifyChatEvent;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.api.events.TabListEvent;
import net.labymod.core.LabyModCore;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.servermanager.ChatDisplayAction;
import net.labymod.servermanager.Server;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GrieferGamesServer extends Server {

	public GrieferGamesServer() {
		super("GrieferGames", GrieferGames.SERVER_IP, GrieferGames.SECOND_SERVER_IP);

		ModuleCategoryRegistry.loadCategory(getGG().getModuleCategory());

		new BoosterModule();
		new FlyModule();
		new NicknameModule();
		new DelayModule();
		new IncomeModule();

		// add Chat Modules
		getGG().addChatModule(new PreventCommandFailure());
		getGG().addChatModule(new ClearChat());
		getGG().addChatModule(new Blanks());
		getGG().addChatModule(new SupremeBlanks());
		getGG().addChatModule(new News());
		getGG().addChatModule(new PrivateMessage());
		getGG().addChatModule(new Payment());
		getGG().addChatModule(new Bank());
		getGG().addChatModule(new ItemRemover());
		getGG().addChatModule(new MobRemover());
		getGG().addChatModule(new PlotChat());
		getGG().addChatModule(new Vote());
		getGG().addChatModule(new GlobalMessage());
		getGG().addChatModule(new LobbyHub());
		getGG().addChatModule(new Realname());
		getGG().addChatModule(new IgnoreList());
		getGG().addChatModule(new AntiMagicClanTag());
		getGG().addChatModule(new Teleport());
		getGG().addChatModule(new AntiMagicPrefix());
		getGG().addChatModule(new Nickname());
		getGG().addChatModule(new Mention());
		getGG().addChatModule(new ChatTime());

		getApi().registerForgeListener(new PreRenderListener());
		getApi().registerForgeListener(new OnTickListener());

		getApi().getEventManager().register(new MessageModifyChatEvent() {
			@Override
			@SubscribeEvent(priority = EventPriority.HIGH)
			public Object onModifyChatMessage(Object o) {
				return modifyChatMessage(o);
			}

		});

		getApi().getEventManager().register(new MessageSendEvent() {
			public boolean onSend(String message) {
				if (!getSettings().isModEnabled() || !getGG().isOnGrieferGames()) return false;

				getGG().setLastActiveTime(System.currentTimeMillis());

				if(message.toLowerCase().startsWith("/ggdebug")) {
					getGG().getApi().displayMessageInChat("§7----------- "+GrieferGames.PREFIX+"§7----------- ");
					getGG().getApi().displayMessageInChat("§7Rank: §e" +getGG().getPlayerRank());
					return true;
				}

				if(message.toLowerCase().startsWith("/ggmessage")) {
					getGG().getApi().displayMessageInChat(getHelper().colorize(message.replace("/ggmessage", "").trim()));
					return true;
				}

				List<Chat> chatModules = getGG().getChatModules();
				for (Chat chatModule : chatModules) {
					if (chatModule.doActionCommandMessage(message)) {
						return chatModule.commandMessage(message);
					}
				}

				return false;
			}
		});

		getApi().getEventManager().register(new MessageReceiveEvent() {
			@Override
			public boolean onReceive(String formatted, String unformatted) {
				if (!getSettings().isModEnabled() || !getGG().isOnGrieferGames()) return false;

				if (unformatted.startsWith("[SCAMMER] ")) {
					String newFormatted = formatted.replaceFirst("((§r)?§6\\[§r§([0-9]|[a-f])§lSCAMMER§r§6\\]§r§r)", "").trim();
					formatted = getHelper().getProperChatFormat(newFormatted);

					String newUnformatted = unformatted.replaceFirst("\\[SCAMMER\\]", "").trim();
					unformatted = newUnformatted;
				}

				List<Chat> chatModules = getGG().getChatModules();
				for (Chat chatModule : chatModules) {
					if (chatModule.doActionReceiveMessage(formatted, unformatted)) {
						return chatModule.receiveMessage(formatted, unformatted);
					}
				}

				return false;
			}
		});

		getApi().getEventManager().registerOnQuit(new Consumer<net.labymod.utils.ServerData>() {
			@Override
			public void accept(net.labymod.utils.ServerData serverData) {
				getGG().setOnGrieferGames(false);
			}
		});
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) throws Exception {
		if (!getSettings().isModEnabled() || !getGG().isOnGrieferGames()) return ChatDisplayAction.NORMAL;

		try {
			if (getSettings().isFilterDuplicateMessages() && getGG().getFilterDuplicateLastMessage().equals(formatted)) {
				return ChatDisplayAction.HIDE;
			}
			getGG().setFilterDuplicateLastMessage(formatted);

			List<Chat> chatModules = getGG().getChatModules();
			for (Chat chatModule : chatModules) {
				if (chatModule.doActionHandleChatMessage(unformatted, formatted)) {
					ChatDisplayAction retVal = chatModule.handleChatMessage(unformatted, formatted);
					if (retVal != ChatDisplayAction.NORMAL) {
						return retVal;
					}
				}
			}

			getHelper().checkIfBoosterMessage(unformatted, formatted);
			getHelper().checkIfBoosterDoneMessage(unformatted, formatted);
			getHelper().checkIfBoosterMultiDoneMessage(unformatted, formatted);
			getHelper().checkCurrentBoosters(unformatted, formatted);

			if (getHelper().isSwitcherDoneMsg(unformatted)) {
				if(getSettings().isUpdateBoosterState()) {
					getGG().setHideBoosterMenu(true);
					getGG().getBoosters().clear();
					Minecraft.getMinecraft().thePlayer.sendChatMessage("/booster");
				}

				if(getSettings().isVanishOnJoin() && getHelper().showVanishModule(getGG().getPlayerRank()) && !getGG().isVanishActive()) {
					Minecraft.getMinecraft().thePlayer.sendChatMessage("/vanish");
				}

				if(getSettings().isFlyOnJoin() && getHelper().hasFlyPermission(getGG().getPlayerRank())) {
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							if(!LabyModCore.getMinecraft().getPlayer().capabilities.allowFlying) {
								Minecraft.getMinecraft().thePlayer.sendChatMessage("/fly");
							}
						}
					}, 5000);
				}
			}

			if(getHelper().isResetWaitTime(unformatted)) {
				getGG().setTimeToWait(12);
			}

			int status = getHelper().isVanishMessage(unformatted);
			if (status >= 0) {
				getGG().setVanishActive(status > 0);
				return ChatDisplayAction.NORMAL;
			}

			status = getHelper().isGodmodeMessage(unformatted);
			if (status >= 0) {
				getGG().setGodActive(status > 0);
				return ChatDisplayAction.NORMAL;
			}

			status = getHelper().isAuraMessage(unformatted);
			if (status >= 0) {
				getGG().setAuraActive(status > 0);
				return ChatDisplayAction.NORMAL;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ChatDisplayAction.NORMAL;
	}

	public Object modifyChatMessage(Object o) {
		if (!getSettings().isModEnabled() || !getGG().isOnGrieferGames()) return o;

		try {
			boolean hasPrefix = false;
			IChatComponent msg = (IChatComponent) o;
			IChatComponent prefix = new ChatComponentText("");

			if (msg.getUnformattedText().startsWith("[SCAMMER] ")) {
				hasPrefix = true;
				for (int i = 0; i < (msg.getSiblings().size() - 1); i++) {
					prefix.appendSibling(msg.getSiblings().get(i));
				}
				msg = msg.getSiblings().get(msg.getSiblings().size() - 1);
			}

			List<Chat> chatModules = getGG().getChatModules();
			for (Chat chatModule : chatModules) {
				if (chatModule.doActionModifyChatMessage(msg)) {
					if (chatModule.getName().equalsIgnoreCase("chatTime") && hasPrefix) {
						msg = prefix.appendSibling(msg);
					}
					msg = chatModule.modifyChatMessage(msg);
				}
			}

			return msg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}

	@Override
	public void onJoin(ServerData serverData) {
		getGG().setOnGrieferGames(true);
		getGG().setSubServer("");
		getGG().setLastLabyChatSubServer("");
		getGG().setLastLabyChatSubServer("");
		getGG().setNickname("");
		getGG().setFirstJoin(true);
	}

	@Override
	public void fillSubSettings(List<SettingsElement> settings) {}

	@Override
	public void handlePluginMessage(String channelName, PacketBuffer packetBuffer) throws Exception {}

	@Override
	public void handleTabInfoMessage(TabListEvent.Type tabInfoType, String formatted, String clean) throws Exception {}

	private GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}

	private ModSettings getSettings() {
		return getGG().getSettings();
	}

	private Helper getHelper() {
		return GrieferGames.getGriefergames().getHelper();
	}

	private LabyModAPI getApi() {
		return GrieferGames.getGriefergames().getApi();
	}
}
package de.wuzlwuz.griefergames.chat;

import de.wuzlwuz.griefergames.chatMenu.TeamMenu;
import net.minecraft.util.IChatComponent;

public class CheckPlot extends Chat {
	private Thread thread = null;
	private boolean secondChat = false;

	private boolean isSecondChat() {
		return secondChat;
	}

	private void setSecondChat(boolean secondChat) {
		this.secondChat = secondChat;
	}

	Runnable runnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(1500);
				getHelper().delChatToolFilter("GG-Addon-Filter-helper");
				getHelper().delChatToolFilter("GG-Addon-Filter-helper_2nd");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public String getName() {
		return "checkPlot";
	}

	@Override
	public boolean doActionCommandMessage(String unformatted) {
		if (getHelper().doCheckPlotRoom(getGG().getPlayerRank())
				&& unformatted.toLowerCase().startsWith("/checkplothelper")) {
			if (getSettings().isCheckPlotHelper()) {
				getSettings().deactivateCheckPlotHelper();
				// getApi().displayMessageInChat(ModColor.cl("4") + "Checkplot helper
				// deaktiviert!");
				TeamMenu.printMenu();
				if (thread == null || thread.getState() == Thread.State.TERMINATED) {
					thread = new Thread(runnable);
					thread.start();
				}
			} else {
				getSettings().activateCheckPlotHelper();
				// getApi().displayMessageInChat(ModColor.cl("2") + "Checkplot helper
				// aktiviert!");
				TeamMenu.printMenu();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean commandMessage(String unformatted) {
		return true;
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		if (doActionReceiveMessage(msg.getFormattedText(), msg.getUnformattedText())) {
			// getHelper().setChatToolFilterText();
			// getHelper().setChatToolFilterRoom();
		}
		return super.doActionModifyChatMessage(msg);
	}

	@Override
	public boolean doActionReceiveMessage(String formatted, String unformatted) {
		if (getHelper().doCheckPlotRoom(getGG().getPlayerRank()) && getSettings().isCheckPlotHelper()) {
			String propFormatted = getHelper().getProperTextFormat(formatted.trim()).trim();

			if (unformatted.startsWith("===== CheckPlot-Anfragen")) {// check headline
				setSecondChat(false);
				return true;
			} else if (unformatted.startsWith("= Seite") && propFormatted.contains("§6§l= Seite §r§e§l")) {// check
																												// curpage
				setSecondChat(false);
				return true;
			} else if (propFormatted.equalsIgnoreCase("§6Seite §r§7<-§r §6|§r §r§6->§r")) {// check paging
				setSecondChat(false);
				return true;
			} else if (propFormatted.equalsIgnoreCase("§6Seite §r§6<-§r §6|§r §r§6->§r")) {// check paging
				setSecondChat(false);
				return true;
			} else if (propFormatted.equalsIgnoreCase("§6Seite §r§6<-§r §6|§r §r§7->§r")) {// check paging
				setSecondChat(false);
				return true;
			} else if (propFormatted.contains("§r§r§r §r§6[§eCheck§6]§r§r§r")) {// check entry
				setSecondChat(false);
				return true;
			} else if (propFormatted.contains("§6[§causw\u00E4hlen§6]§r")) {// check refuse entry
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§r§cDu stehst nicht auf diesem Grundst\u00FCck!§r")) {// check not on
																											// plot
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§r§cGrundst\u00FCcke dieses Spielers k\u00F6nnen nicht gel\u00F6scht werden.§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§7Zuletzt online: §r§c") && propFormatted.contains("§r§7Uhr")) {// check
																														// last
																														// seen
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§e[§r§2ANALYSE§r§e]§r Bewertet als")) {// check plot data
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§7Das Grundst\u00FCck ist ab dem §r§a")
					&& (propFormatted.contains("§r§4UNBEBAUT §r§7ist!§r")
							|| propFormatted.contains("§r§2BEBAUT §r§7ist!§r"))) {// check plot date
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§aDu kannst das Grundst\u00FCck jetzt l\u00F6schen. Bitte best\u00E4tige mit §r§2/checkplot confirm§r§a.§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§7Bitte klicke auf den gew\u00FCnschten Ablehnungsgrund oder nutze §r§e/checkplot deny <RequestID> <Grund>§r§7.§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§cDu musst dich auf einem Grundst\u00FCck befinden!§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§aDer Antrag wurde angenommen.§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§cDu hast die Anfrage f\u00FCr das Grundst\u00FCck abgelehnt. Grund:")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§cDer Spieler ist aktiv auf GrieferGames.§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§6ID") && (propFormatted.contains("§r§eoffen §r")
					|| propFormatted.contains("§r§4§labgelehnt: §r§4")
					|| propFormatted.contains("§r§2angenommen§r§a")
					|| propFormatted.contains("§aPlot wurde best\u00E4tigt.")
					|| propFormatted.contains("§r§a§l\u00FCbergeben§r"))) {
				setSecondChat(false);
				return true;
			} else if (propFormatted.contains("§6§lInformationen: §r§f§nhttp://plots.griefergames.net/§r§f§n")) {
				setSecondChat(false);
				return true;
			} else if (propFormatted.contains("§cDu bist zu weit vom Grundst\u00FCck entfernt!§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§cDas Grundst\u00FCck befindet sich zu nah am Spawn!§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§cDu darfst den Befehl nicht so oft hintereinander ausf\u00FChren!§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§cDer Spieler ist unbekannt.§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§cMerge-Grundst\u00FCcke k\u00F6nnen nicht beantragt werden.§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§Das Grundst\u00FCcke wurde bereits beantragt.§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§cDer Spieler konnte nicht identifiziert werden.")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§aDu kannst das Grundst\u00FCck jetzt l\u00F6schen. Bitte best\u00E4tige mit §r§2/checkplot confirm§r§a.§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§aDas Grundst\u00FCck mit der ID")
					&& propFormatted.contains("§r§awurde gel\u00F6scht!§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§cDieses Grundst\u00FCck hat keinen Besitzer!§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§cDer Antrag wurde nicht gefunden.§r")) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.contains("§7Das Grundst\u00FCck wird gepr\u00FCft")) {
				setSecondChat(true);
				return true;
			}
		}

		return super.doActionReceiveMessage(formatted, unformatted);
	}

	@Override
	public boolean receiveMessage(String formatted, String unformatted) {
		String name = (isSecondChat()) ? "GG-Addon-Filter-helper_2nd" : "GG-Addon-Filter-helper";

		String cleanText = formatted.replaceAll("\u00A7[0-9a-frl]", "").trim();

		if (thread == null || thread.getState() == Thread.State.TERMINATED) {
			thread = new Thread(runnable);
			thread.start();
		}

		getHelper().addChatToolFilterText(name, cleanText);
		getHelper().setChatToolFilterRoom(name, "CheckPlot");
		getHelper().setChatToolFilterSecondChat(name, isSecondChat());
		return super.receiveMessage(formatted, unformatted);
	}
}
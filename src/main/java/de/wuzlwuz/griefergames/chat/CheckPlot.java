package de.wuzlwuz.griefergames.chat;

import de.wuzlwuz.griefergames.chatMenu.TeamMenu;
import net.minecraft.util.IChatComponent;

public class CheckPlot extends Chat {
	private Thread thread = null;
	private boolean secondChat = false;
	
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
		return false;
	}

	@Override
	public boolean doActionReceiveMessage(String formatted, String unformatted) {
		if (getHelper().doCheckPlotRoom(getGG().getPlayerRank()) && getSettings().isCheckPlotHelper()) {
			String propFormatted = getHelper().getProperTextFormat(formatted.trim()).trim();

			if (unformatted.startsWith("===== CheckPlot-Anfragen")) {// check headline
				secondChat = false;
				return true;
			} else if (unformatted.startsWith("= Seite") && propFormatted.contains("§6§l= Seite §r§e§l")) {// check
																												// curpage
				secondChat = false;
				return true;
			} else if (propFormatted.equalsIgnoreCase("§6Seite §r§7<-§r §6|§r §r§6->§r")) {// check paging
				secondChat = false;
				return true;
			} else if (propFormatted.equalsIgnoreCase("§6Seite §r§6<-§r §6|§r §r§6->§r")) {// check paging
				secondChat = false;
				return true;
			} else if (propFormatted.equalsIgnoreCase("§6Seite §r§6<-§r §6|§r §r§7->§r")) {// check paging
				secondChat = false;
				return true;
			} else if (propFormatted.contains("§r§r§r §r§6[§eCheck§6]§r§r§r")) {// check entry
				secondChat = false;
				return true;
			} else if (propFormatted.contains("§6[§causw\u00E4hlen§6]§r")) {// check refuse entry
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§r§cDu stehst nicht auf diesem Grundst\u00FCck!§r")) {// check not on
																											// plot
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§r§cGrundst\u00FCcke dieses Spielers k\u00F6nnen nicht gel\u00F6scht werden.§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§7Zuletzt online: §r§c") && propFormatted.contains("§r§7Uhr")) {// check
																														// last
																														// seen
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§e[§r§2ANALYSE§r§e]§r Bewertet als")) {// check plot data
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§7Das Grundst\u00FCck ist ab dem §r§a")
					&& (propFormatted.contains("§r§4UNBEBAUT §r§7ist!§r")
							|| propFormatted.contains("§r§2BEBAUT §r§7ist!§r"))) {// check plot date
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§aDu kannst das Grundst\u00FCck jetzt l\u00F6schen. Bitte best\u00E4tige mit §r§2/checkplot confirm§r§a.§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§7Bitte klicke auf den gew\u00FCnschten Ablehnungsgrund oder nutze §r§e/checkplot deny <RequestID> <Grund>§r§7.§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§cDu musst dich auf einem Grundst\u00FCck befinden!§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§aDer Antrag wurde angenommen.§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§cDu hast die Anfrage f\u00FCr das Grundst\u00FCck abgelehnt. Grund:")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§cDer Spieler ist aktiv auf GrieferGames.§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§6ID") && (propFormatted.contains("§r§eoffen §r")
					|| propFormatted.contains("§r§4§labgelehnt: §r§4")
					|| propFormatted.contains("§r§2angenommen§r§a")
					|| propFormatted.contains("§aPlot wurde best\u00E4tigt.")
					|| propFormatted.contains("§r§a§l\u00FCbergeben§r"))) {
				secondChat = false;
				return true;
			} else if (propFormatted.contains("§6§lInformationen: §r§f§nhttp://plots.griefergames.net/§r§f§n")) {
				secondChat = false;
				return true;
			} else if (propFormatted.contains("§cDu bist zu weit vom Grundst\u00FCck entfernt!§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§cDas Grundst\u00FCck befindet sich zu nah am Spawn!§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§cDu darfst den Befehl nicht so oft hintereinander ausf\u00FChren!§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§cDer Spieler ist unbekannt.§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§cMerge-Grundst\u00FCcke k\u00F6nnen nicht beantragt werden.§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§Das Grundst\u00FCcke wurde bereits beantragt.§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§cDer Spieler konnte nicht identifiziert werden.")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§aDu kannst das Grundst\u00FCck jetzt l\u00F6schen. Bitte best\u00E4tige mit §r§2/checkplot confirm§r§a.§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§aDas Grundst\u00FCck mit der ID")
					&& propFormatted.contains("§r§awurde gel\u00F6scht!§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§cDieses Grundst\u00FCck hat keinen Besitzer!§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§cDer Antrag wurde nicht gefunden.§r")) {
				secondChat = true;
				return true;
			} else if (propFormatted.contains("§7Das Grundst\u00FCck wird gepr\u00FCft")) {
				secondChat = true;
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean receiveMessage(String formatted, String unformatted) {
		String name = secondChat ? "GG-Addon-Filter-helper_2nd" : "GG-Addon-Filter-helper";

		String cleanText = formatted.replaceAll("\u00A7[0-9a-frl]", "").trim();

		if (thread == null || thread.getState() == Thread.State.TERMINATED) {
			thread = new Thread(runnable);
			thread.start();
		}

		getHelper().addChatToolFilterText(name, cleanText);
		getHelper().setChatToolFilterRoom(name, "CheckPlot");
		getHelper().setChatToolFilterSecondChat(name, secondChat);
		return false;
	}
}
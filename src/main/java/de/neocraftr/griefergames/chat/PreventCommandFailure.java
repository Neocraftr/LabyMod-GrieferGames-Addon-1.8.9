package de.neocraftr.griefergames.chat;

import net.labymod.main.lang.LanguageManager;
import net.labymod.utils.ModColor;

public class PreventCommandFailure extends Chat {
	private String lastCommand = "";

	@Override
	public String getName() {
		return "preventCommandFailure";
	}

	@Override
	public boolean doActionCommandMessage(String unformatted) {
		if (getSettings().isPreventCommandFailure()) {
			if (unformatted.length() > 1 && unformatted.startsWith("7")
					&& (!unformatted.equalsIgnoreCase(lastCommand))) {

				getApi().displayMessageInChat(ModColor.RED + LanguageManager.translateOrReturnKey("message_gg_incorrectCommandMessage"));

				lastCommand = unformatted;
				return true;
			} else {
				lastCommand = "";
			}
		}
		return false;
	}

	@Override
	public boolean commandMessage(String unformatted) {
		return true;
	}
}
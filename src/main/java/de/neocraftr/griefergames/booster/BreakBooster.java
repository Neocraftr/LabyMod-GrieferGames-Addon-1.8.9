package de.neocraftr.griefergames.booster;

import net.labymod.main.lang.LanguageManager;

public class BreakBooster extends Booster {
	public BreakBooster() {
		super(LanguageManager.translateOrReturnKey("booster_gg_break"), "break-booster", 4, false);
	}
}

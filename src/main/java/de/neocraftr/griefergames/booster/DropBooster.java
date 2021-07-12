package de.neocraftr.griefergames.booster;

import net.labymod.main.lang.LanguageManager;

public class DropBooster extends Booster {
	public DropBooster() {
		super(LanguageManager.translateOrReturnKey("booster_gg_drop"), "drops-booster", 5, true);
	}
}

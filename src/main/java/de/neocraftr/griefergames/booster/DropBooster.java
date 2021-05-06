package de.neocraftr.griefergames.booster;

import net.labymod.main.lang.LanguageManager;

public class DropBooster extends Booster {
	public DropBooster() {
		super(LanguageManager.translateOrReturnKey("booster_gg_drop"), "drops-booster", 5, true);
	}

	public DropBooster(int count) {
		super(LanguageManager.translateOrReturnKey("booster_gg_drop"), "drops-booster", count, 5, true);
	}

	public DropBooster(int count, long endDate) {
		super(LanguageManager.translateOrReturnKey("booster_gg_drop"), "drops-booster", count, endDate, 5, true);
	}
}

package de.neocraftr.griefergames.booster;

import net.labymod.main.lang.LanguageManager;

public class FlyBooster extends Booster {
	public FlyBooster() {
		super(LanguageManager.translateOrReturnKey("booster_gg_fly"), "fly-booster", 3, false);
	}

	public FlyBooster(int count) {
		super(LanguageManager.translateOrReturnKey("booster_gg_fly"), "fly-booster", count, 3, false);
	}

	public FlyBooster(int count, long endDate) {
		super(LanguageManager.translateOrReturnKey("booster_gg_fly"), "fly-booster", count, endDate, 3, false);
	}
}

package de.neocraftr.griefergames.booster;

import net.labymod.main.lang.LanguageManager;

public class MobBooster extends Booster {
	public MobBooster() {
		super(LanguageManager.translateOrReturnKey("booster_gg_mob"), "mob-booster", 2, true);
	}

	public MobBooster(int count) {
		super(LanguageManager.translateOrReturnKey("booster_gg_mob"), "mob-booster", count, 2, true);
	}

	public MobBooster(int count, long endDate) {
		super(LanguageManager.translateOrReturnKey("booster_gg_mob"), "mob-booster", count, endDate, 2, true);
	}
}

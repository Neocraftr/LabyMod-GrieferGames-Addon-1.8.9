package de.neocraftr.griefergames.booster;

import net.labymod.main.lang.LanguageManager;

public class ExperienceBooster extends Booster {
	public ExperienceBooster() {
		super(LanguageManager.translateOrReturnKey("booster_gg_experience"), "erfahrung-booster", 1, true);
	}

	public ExperienceBooster(int count) {
		super(LanguageManager.translateOrReturnKey("booster_gg_experience"), "erfahrung-booster", count, 1, true);
	}

	public ExperienceBooster(int count, long endDate) {
		super(LanguageManager.translateOrReturnKey("booster_gg_experience"), "erfahrung-booster", count, endDate, 1, true);
	}
}

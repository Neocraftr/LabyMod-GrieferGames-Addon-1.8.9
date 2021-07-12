package de.neocraftr.griefergames.booster;

import net.labymod.main.lang.LanguageManager;

public class ExperienceBooster extends Booster {
	public ExperienceBooster() {
		super(LanguageManager.translateOrReturnKey("booster_gg_experience"), "erfahrung-booster", 1, true);
	}
}

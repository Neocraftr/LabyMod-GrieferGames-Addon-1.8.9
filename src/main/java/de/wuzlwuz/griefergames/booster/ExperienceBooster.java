package de.wuzlwuz.griefergames.booster;

import java.time.LocalDateTime;

import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement.IconData;
import net.minecraft.util.ResourceLocation;

public class ExperienceBooster extends Booster {
	public ExperienceBooster() {
		super(LanguageManager.translateOrReturnKey("booster_gg_experience", new Object[0]), "xp",
				new IconData(new ResourceLocation("griefergames/textures/icons/booster_xp.png")), 1);
	}

	public ExperienceBooster(int count) {
		super(LanguageManager.translateOrReturnKey("booster_gg_experience", new Object[0]), "xp", count,
				new IconData(new ResourceLocation("griefergames/textures/icons/booster_xp.png")), 1);
	}

	public ExperienceBooster(int count, LocalDateTime endDate) {
		super(LanguageManager.translateOrReturnKey("booster_gg_experience", new Object[0]), "xp", count, endDate,
				new IconData(new ResourceLocation("griefergames/textures/icons/booster_xp.png")), 1);
	}
}

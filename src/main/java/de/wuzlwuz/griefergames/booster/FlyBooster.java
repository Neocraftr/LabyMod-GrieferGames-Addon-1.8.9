package de.wuzlwuz.griefergames.booster;

import java.time.LocalDateTime;

import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement.IconData;
import net.minecraft.util.ResourceLocation;

public class FlyBooster extends Booster {
	public FlyBooster() {
		super(LanguageManager.translateOrReturnKey("booster_gg_fly", new Object[0]), "fly",
				new IconData(new ResourceLocation("griefergames/textures/icons/booster_fliegen.png")), 3, false);
		setShowCount(false);
	}

	public FlyBooster(int count) {
		super(LanguageManager.translateOrReturnKey("booster_gg_fly", new Object[0]), "fly", count,
				new IconData(new ResourceLocation("griefergames/textures/icons/booster_fliegen.png")), 3, false);
		setShowCount(false);
	}

	public FlyBooster(int count, LocalDateTime endDate) {
		super(LanguageManager.translateOrReturnKey("booster_gg_fly", new Object[0]), "fly", count, endDate,
				new IconData(new ResourceLocation("griefergames/textures/icons/booster_fliegen.png")), 3, false);
		setShowCount(false);
	}
}

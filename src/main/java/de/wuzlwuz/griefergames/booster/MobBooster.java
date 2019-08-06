package de.wuzlwuz.griefergames.booster;

import java.time.LocalDateTime;

import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement.IconData;
import net.minecraft.util.ResourceLocation;

public class MobBooster extends Booster {
	public MobBooster() {
		super(LanguageManager.translateOrReturnKey("booster_gg_mob", new Object[0]), "mob",
				new IconData(new ResourceLocation("griefergames/textures/icons/booster_mobs.png")), 2);
	}

	public MobBooster(int count) {
		super(LanguageManager.translateOrReturnKey("booster_gg_mob", new Object[0]), "mob", count,
				new IconData(new ResourceLocation("griefergames/textures/icons/booster_mobs.png")), 2);
	}

	public MobBooster(int count, LocalDateTime endDate) {
		super(LanguageManager.translateOrReturnKey("booster_gg_mob", new Object[0]), "mob", count, endDate,
				new IconData(new ResourceLocation("griefergames/textures/icons/booster_mobs.png")), 2);
	}
}

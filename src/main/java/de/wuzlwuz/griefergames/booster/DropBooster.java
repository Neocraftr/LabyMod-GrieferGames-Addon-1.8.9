package de.wuzlwuz.griefergames.booster;

import java.time.LocalDateTime;

import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement.IconData;
import net.minecraft.util.ResourceLocation;

public class DropBooster extends Booster {
	public DropBooster() {
		super(LanguageManager.translateOrReturnKey("booster_gg_drop", new Object[0]), "drop",
				new IconData(new ResourceLocation("griefergames/textures/icons/booster_drops.png")), 5);
	}

	public DropBooster(int count) {
		super(LanguageManager.translateOrReturnKey("booster_gg_drop", new Object[0]), "drop", count,
				new IconData(new ResourceLocation("griefergames/textures/icons/booster_drops.png")), 5);
	}

	public DropBooster(int count, LocalDateTime endDate) {
		super(LanguageManager.translateOrReturnKey("booster_gg_drop", new Object[0]), "drop", count, endDate,
				new IconData(new ResourceLocation("griefergames/textures/icons/booster_drops.png")), 5);
	}
}

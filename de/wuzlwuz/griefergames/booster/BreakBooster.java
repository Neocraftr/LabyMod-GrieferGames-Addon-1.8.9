package de.wuzlwuz.griefergames.booster;

import java.time.LocalDateTime;

import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement.IconData;
import net.minecraft.util.ResourceLocation;

public class BreakBooster extends Booster {
	public BreakBooster() {
		super(LanguageManager.translateOrReturnKey("booster_gg_break", new Object[0]), "break",
				new IconData(new ResourceLocation("griefergames/textures/icons/booster_break.png")), 4, false);
	}

	public BreakBooster(int count) {
		super(LanguageManager.translateOrReturnKey("booster_gg_break", new Object[0]), "break", count,
				new IconData(new ResourceLocation("griefergames/textures/icons/booster_break.png")), 4, false);
	}

	public BreakBooster(int count, LocalDateTime endDate) {
		super(LanguageManager.translateOrReturnKey("booster_gg_break", new Object[0]), "break", count, endDate,
				new IconData(new ResourceLocation("griefergames/textures/icons/booster_break.png")), 4, false);
	}
}

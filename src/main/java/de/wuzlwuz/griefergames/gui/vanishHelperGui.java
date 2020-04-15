package de.wuzlwuz.griefergames.gui;

import net.labymod.utils.ModColor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class vanishHelperGui extends GuiScreen {
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.pushMatrix();

		float overlaySize = 2F;
		int overlayWidth = (int) ((float) this.width / 2F / overlaySize);
		int overlayHeight = (int) ((float) this.height / 2F / overlaySize);

		GlStateManager.scale(2F, 2F, 2F);

		this.drawCenteredString(this.fontRendererObj, ModColor.RED + "Achtung, du bist im Vanish!", overlayWidth,
				overlayHeight, 16777215);

		GlStateManager.popMatrix();

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}

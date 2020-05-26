package com.gmail.nuclearcat1337.snitch_master.gui.controls;

import com.gmail.nuclearcat1337.snitch_master.SnitchMaster;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class DropMenu extends Button {
	public static final ResourceLocation MENU_BUTTON = new ResourceLocation(SnitchMaster.MODID, "snitchMasterMain.png");

	public DropMenu(int buttonId, int x, int y) {
		super(buttonId, x, y, 16, 16, "");
	}

	@Override
	public boolean mouseClicked(Minecraft minecraft, int mouseX, int mouseY) {
		if (super.mouse(minecraft, mouseX, mouseY)) {
			return true;
		}
		return false;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int i = this.getHoverState(this.isHovered);

			GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);

			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

			minecraft.getTextureManager().bindTexture(BUTTON_TEXTURES);
			this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);

			minecraft.getTextureManager().bindTexture(MENU_BUTTON);
			this.drawTexturedModalRect(this.x + 4, this.y + 4, 0, 0, 8, 8);

			this.mouseDragged(minecraft, mouseX, mouseY);
		}
	}
}

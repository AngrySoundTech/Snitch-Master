package com.gmail.nuclearcat1337.snitch_master.gui.screens;

import com.gmail.nuclearcat1337.snitch_master.gui.GuiConstants;
import com.gmail.nuclearcat1337.snitch_master.gui.controls.TextBox;
import com.gmail.nuclearcat1337.snitch_master.snitches.SnitchList;
import com.gmail.nuclearcat1337.snitch_master.util.Acceptor;
import com.gmail.nuclearcat1337.snitch_master.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

public class EditColorGui extends Screen {
	private Screen cancelToScreen;

	private final String titleText;
	private final int titleWidth;

	private Acceptor<Color> callback;
	private Color baseColor;

	private TextBox redBox;
	private TextBox greenBox;
	private TextBox blueBox;

	private int greenWidth;

	public EditColorGui(Screen cancelToScreen, Color baseColor, String titleText, Acceptor<Color> callback) {
		super(new StringTextComponent("Edit Color"));
		this.cancelToScreen = cancelToScreen;
		this.baseColor = baseColor;
		this.callback = callback;

		this.titleText = titleText;
		this.titleWidth = Minecraft.getInstance().fontRenderer.getStringWidth(titleText);
	}

	private static final int MAX_COLOR_TEXT_LENGTH = 3;

	public void init() {
		greenWidth = minecraft.fontRenderer.getStringWidth("Green");

		int width = minecraft.fontRenderer.getStringWidth(SnitchList.MAX_NAME_CHARACTERS + "WWW");
		int xPos = (this.width / 2) - (GuiConstants.LARGE_TEXBOX_LENGTH / 2);
		int yPos = (this.height / 2) - (GuiConstants.STANDARD_TEXTBOX_HEIGHT) - (GuiConstants.STANDARD_SEPARATION_DISTANCE / 2);

		int rgbBoxWidth = (width - GuiConstants.STANDARD_SEPARATION_DISTANCE) / 3;
		int buttonWidth = (width - GuiConstants.STANDARD_SEPARATION_DISTANCE) / 3;

		this.redBox = new TextBox("",font, xPos, yPos, rgbBoxWidth, GuiConstants.STANDARD_TEXTBOX_HEIGHT, true, false, MAX_COLOR_TEXT_LENGTH);
		redBox.setClamp(0, 255);
		redBox.setText(Integer.toString(baseColor.getRedInt()));
		redBox.changeFocus(true);

		yPos += (GuiConstants.STANDARD_BUTTON_HEIGHT + GuiConstants.SMALL_SEPARATION_DISTANCE);

		this.greenBox = new TextBox("",font, xPos, yPos, rgbBoxWidth, GuiConstants.STANDARD_TEXTBOX_HEIGHT, true, false, MAX_COLOR_TEXT_LENGTH);
		greenBox.setClamp(0, 255);
		greenBox.setText(Integer.toString(baseColor.getGreenInt()));

		yPos += (GuiConstants.STANDARD_BUTTON_HEIGHT + GuiConstants.SMALL_SEPARATION_DISTANCE);

		this.blueBox = new TextBox("",font, xPos, yPos, rgbBoxWidth, GuiConstants.STANDARD_TEXTBOX_HEIGHT, true, false, MAX_COLOR_TEXT_LENGTH);
		blueBox.setClamp(0, 255);
		blueBox.setText(Integer.toString(baseColor.getBlueInt()));

		this.buttons.clear();

		xPos += (blueBox.getWidth() + GuiConstants.SMALL_SEPARATION_DISTANCE);

		this.buttons.add(new Button(1, xPos, yPos, buttonWidth, GuiConstants.STANDARD_BUTTON_HEIGHT, "Cancel"));

		xPos += (buttonWidth + GuiConstants.SMALL_SEPARATION_DISTANCE);

		this.buttons.add(new Button(2, xPos, yPos, buttonWidth, GuiConstants.STANDARD_BUTTON_HEIGHT, "Save"));

		super.init();
	}

	public void updateScreen() {
		redBox.updateCursorCounter();
		greenBox.updateCursorCounter();
		blueBox.updateCursorCounter();
		super.updateScreen();
	}

	public void charTyped(char par1, int par2) throws IOException {
		if (redBox.isFocused()) {
			if (par2 == GLFW.GLFW_KEY_TAB) {
				greenBox.changeFocus(true);
				redBox.changeFocus(false);
			}
			redBox.charTyped(par1, par2);
		} else if (greenBox.isFocused()) {
			if (par2 == GLFW.GLFW_KEY_TAB) {
				blueBox.changeFocus(true);
				greenBox.changeFocus(false);
			}
			greenBox.charTyped(par1, par2);
		} else if (blueBox.isFocused()) {
			if (par2 == GLFW.GLFW_KEY_TAB) {
				redBox.changeFocus(true);
				blueBox.changeFocus(false);
			}
			blueBox.charTyped(par1, par2);
		}
		super.charTyped(par1, par2);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		this.redBox.drawTextBox();
		this.greenBox.drawTextBox();
		this.blueBox.drawTextBox();

		int constYValue = (GuiConstants.STANDARD_TEXTBOX_HEIGHT / 2) - minecraft.fontRenderer.FONT_HEIGHT / 2;
		int constXValue = GuiConstants.SMALL_SEPARATION_DISTANCE + greenWidth;

		minecraft.fontRenderer.drawString("Blue", blueBox.x - constXValue, blueBox.y + constYValue, 16777215);
		minecraft.fontRenderer.drawString("Green", greenBox.x - constXValue, greenBox.y + constYValue, 16777215);
		minecraft.fontRenderer.drawString("Red", redBox.x - constXValue, redBox.y + constYValue, 16777215);

		int xPos = redBox.x + (redBox.getWidth() / 2) - (titleWidth / 2);
		int yPos = redBox.y - minecraft.fontRenderer.FONT_HEIGHT - GuiConstants.STANDARD_SEPARATION_DISTANCE;

		minecraft.fontRenderer.drawString(titleText, xPos, yPos, 16777215);

		super.render(mouseX, mouseY, partialTicks);
	}

	public void mouseClicked(int one, int two, int three) throws IOException {
		this.redBox.mouseClicked(one, two, three);
		this.greenBox.mouseClicked(one, two, three);
		this.blueBox.mouseClicked(one, two, three);
		super.mouseClicked(one, two, three);
	}

	public void actionPerformed(Button button) {
		//If they press the "save" button, we check if we need to save changes
		if (button.id == 2) {
			//If there is not a correct red value don't go back to old screen
			Integer red = redBox.clamp();
			if (red == null) {
				return;
			}

			//If there is not a correct green value don't go back to old screen
			Integer green = greenBox.clamp();
			if (green == null) {
				return;
			}

			//If there is not a correct blue value don't go back to old screen
			Integer blue = blueBox.clamp();
			if (blue == null) {
				return;
			}

			Color newColor = new Color(red, green, blue);
			//We only need to save changes if they actually changed the color
			if (!Color.AreEqual(newColor, baseColor)) {
				//Pass the color to the callback (the return value really isn't used I guess...)
				callback.accept(newColor);
			}
		}

		//Go to the previous screen if we get to this point (other places might return first)
		minecraft.displayGuiScreen(cancelToScreen);
	}


}

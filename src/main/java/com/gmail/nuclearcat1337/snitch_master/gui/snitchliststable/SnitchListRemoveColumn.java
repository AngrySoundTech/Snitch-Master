package com.gmail.nuclearcat1337.snitch_master.gui.snitchliststable;

import com.gmail.nuclearcat1337.snitch_master.SnitchMaster;
import com.gmail.nuclearcat1337.snitch_master.gui.GuiConstants;
import com.gmail.nuclearcat1337.snitch_master.gui.tables.TableColumn;
import com.gmail.nuclearcat1337.snitch_master.snitches.SnitchList;
import com.gmail.nuclearcat1337.snitch_master.snitches.SnitchManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.Screen;

import java.util.HashSet;
import java.util.List;

public class SnitchListRemoveColumn implements TableColumn<SnitchList> {
	private static Minecraft mc;

	private static final String BUTTON_TEXT = "x";

	private final int buttonWidth;
	private final SnitchManager manager;
	public static final HashSet<String> removedSnitchLists = new HashSet<>();

	public SnitchListRemoveColumn(SnitchMaster snitchMaster) {
		mc = Minecraft.getInstance();
		buttonWidth = mc.fontRenderer.getStringWidth(BUTTON_TEXT + "---");
		this.manager = snitchMaster.getManager();
	}

	@Override
	public Button[] prepareEntry(SnitchList item) {
		Button[] buttons = new Button[1];
		buttons[0] = new Button(0, 0, 0, buttonWidth, GuiConstants.STANDARD_BUTTON_HEIGHT, BUTTON_TEXT);
		return buttons;
	}

	@Override
	public String getColumnName() {
		return "Remove";
	}

	@Override
	public boolean doBoundsCheck() {
		return true;
	}

	@Override
	public void clicked(SnitchList item, boolean leftClick, int xPos, int yPos, Button[] buttons, Screen parentScreen, int slotIndex) {
		if (!leftClick || removedSnitchLists.contains(item.getListName())) {
			return;
		}

		if (buttons[0].mouseClicked(xPos, yPos, 0)) {
			manager.removeSnitchList(item.getListName());

			//Deleting a snitch list automatically triggers a save
			removedSnitchLists.add(item.getListName());
		}
	}

	@Override
	public void released(SnitchList list, int xPos, int yPos, Button[] buttons, Screen parentScreen, int slotIndex) {
		if (removedSnitchLists.contains(list.getListName())) {
			return;
		}
		buttons[0].mouseReleased(xPos, yPos, 0);
		buttons[0].mouseReleased(xPos, yPos, 0);
	}

	@Override
	public void draw(SnitchList list, int xPosition, int yPosition, int columnWidth, int slotHeight, Button[] buttons, int slotIndex, int mouseX, int mouseY) {
		if (removedSnitchLists.contains(list.getListName())) {
			return;
		}

		yPosition = yPosition + ((slotHeight - GuiConstants.STANDARD_BUTTON_HEIGHT) / 2);
		int xPos = xPosition + (columnWidth / 2) - (buttonWidth / 2);

		buttons[0].y = yPosition;
		buttons[0].x = xPos;

		buttons[0].render(mouseX, mouseY, 0);
	}

	@Override
	public int getDrawWidth(SnitchList list) {
		return buttonWidth;
	}

	@Override
	public List<String> hover(SnitchList item, int xPos, int yPos) {
		return null;
	}

	@Override
	public boolean canSort() {
		return false;
	}

	@Override
	public int compare(SnitchList o1, SnitchList o2) {
		return 0;
	}
}


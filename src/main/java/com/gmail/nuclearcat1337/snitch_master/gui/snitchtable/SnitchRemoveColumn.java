package com.gmail.nuclearcat1337.snitch_master.gui.snitchtable;

import com.gmail.nuclearcat1337.snitch_master.SnitchMaster;
import com.gmail.nuclearcat1337.snitch_master.gui.GuiConstants;
import com.gmail.nuclearcat1337.snitch_master.gui.tables.TableColumn;
import com.gmail.nuclearcat1337.snitch_master.locatableobjectlist.ILocation;
import com.gmail.nuclearcat1337.snitch_master.snitches.Snitch;
import com.gmail.nuclearcat1337.snitch_master.snitches.SnitchManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;

import java.util.HashSet;
import java.util.List;

public class SnitchRemoveColumn implements TableColumn<Snitch> {
	private static Minecraft mc;

	private static final String BUTTON_TEXT = "x";

	private final int buttonWidth;
	private final SnitchManager manager;
	public static final HashSet<ILocation> removedSnitches = new HashSet<>();

	public SnitchRemoveColumn(SnitchMaster snitchMaster) {
		mc = Minecraft.getInstance();
		buttonWidth = mc.fontRenderer.getStringWidth(BUTTON_TEXT + "---");
		this.manager = snitchMaster.getManager();
	}

	@Override
	public Button[] prepareEntry(Snitch item) {
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
	public void clicked(Snitch item, boolean leftClick, int xPos, int yPos, Button[] buttons, Screen parentScreen, int slotIndex) {
		if (!leftClick || removedSnitches.contains(item.getLocation())) {
			return;
		}

		if (buttons[0].mouseClicked(xPos, yPos, 0)) {
			manager.getSnitches().remove(item);
			removedSnitches.add(item.getLocation());
			if (SnitchMaster.jmInterface != null) {
				SnitchMaster.jmInterface.refresh(manager.getSnitches());
			}
			manager.saveSnitches();
		}
	}

	@Override
	public void released(Snitch list, int xPos, int yPos, Button[] buttons, Screen parentScreen, int slotIndex) {
		if (removedSnitches.contains(list.getLocation())) {
			return;
		}
		buttons[0].mouseReleased(xPos, yPos, 0);
	}

	@Override
	public void draw(Snitch list, int xPosition, int yPosition, int columnWidth, int slotHeight, Button[] buttons, int slotIndex, int mouseX, int mouseY) {
		if (removedSnitches.contains(list.getLocation())) {
			return;
		}

		yPosition = yPosition + ((slotHeight - GuiConstants.STANDARD_BUTTON_HEIGHT) / 2);
		int xPos = xPosition + (columnWidth / 2) - (buttonWidth / 2);

		buttons[0].y = yPosition;
		buttons[0].x = xPos;

		buttons[0].render(mouseX, mouseY, 0);
	}

	@Override
	public int getDrawWidth(Snitch list) {
		return buttonWidth;
	}

	@Override
	public List<String> hover(Snitch item, int xPos, int yPos) {
		return null;
	}

	@Override
	public boolean canSort() {
		return false;
	}

	@Override
	public int compare(Snitch o1, Snitch o2) {
		return 0;
	}
}

package com.gmail.nuclearcat1337.snitch_master.handlers;

import com.gmail.nuclearcat1337.snitch_master.SnitchMaster;
import com.gmail.nuclearcat1337.snitch_master.gui.screens.MainGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

public class KeyHandler {
	public KeyBinding snitchMasterMainGUI = new KeyBinding("Snitch Master Settings", GLFW.GLFW_KEY_V, "Snitch Master");
	public KeyBinding toggleAllRender = new KeyBinding("Toggle Render Snitch Lists", GLFW.GLFW_KEY_N, "Snitch Master");

	private SnitchMaster snitchMaster;

	public KeyHandler(SnitchMaster snitchMaster) {
		this.snitchMaster = snitchMaster;
		ClientRegistry.registerKeyBinding(snitchMasterMainGUI);
		ClientRegistry.registerKeyBinding(toggleAllRender);

		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}

	@SubscribeEvent
	public void onKeyPress(InputEvent.KeyInputEvent event) {
		if (snitchMasterMainGUI.isPressed()) {
			Minecraft.getInstance().displayGuiScreen(new MainGui(snitchMaster));
		}
		if (toggleAllRender.isPressed()) {
			snitchMaster.getManager().toggleGlobalRender();
			SnitchMaster.SendMessageToPlayer("Global render: " + (snitchMaster.getManager().getGlobalRender() ? "On" : "Off"));
		}
	}
}

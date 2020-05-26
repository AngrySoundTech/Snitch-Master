package com.gmail.nuclearcat1337.snitch_master.handlers;

import com.gmail.nuclearcat1337.snitch_master.SnitchMaster;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.EndDimension;
import net.minecraft.world.dimension.NetherDimension;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;

/**
 * Manages the current world the player is in. Handles servers with multiple worlds and single player.
 */
public class WorldInfoListener {
	private final Minecraft mc = Minecraft.getInstance();

	private static final String PROTOCOL = "6";

	private static final int MIN_DELAY_MS = 1000;

	private static long lastRequest;
	private static long lastResponse;
	private static String worldID = "single_player";

	public static final SimpleChannel channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(SnitchMaster.MODID + "chan"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);


	public WorldInfoListener(SnitchMaster snitchMaster) {
		channel.registerMessage(0, WorldIDPacket.class, WorldIDPacket::encode, WorldIDPacket::decode, WorldListener::onMessage);

		//IDK which one this classes uses and I cant be bothered to find out
		MinecraftForge.EVENT_BUS.register(this);
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (!mc.isSingleplayer() && mc.player != null && mc.player.isAlive()) {
			if (mc.player.getDisplayName().equals(event.getEntity().getDisplayName())) {
				worldID = null;
				if (this.channel != null) {
					requestWorldID();
				}

			}
		}
	}

	private void requestWorldID() {
		long now = System.currentTimeMillis();
		if (lastRequest + MIN_DELAY_MS < now) {
			channel.sendToServer(new WorldIDPacket());
			lastRequest = System.currentTimeMillis();
		}
	}

	/**
	 * Gets the name of the current world the player is.
	 * Returns "single player" is the player is playing single player.
	 */
	public String getWorldID() {
		if (lastResponse < lastRequest) {
			//No WorldInfo response so just use vanilla world names
			Dimension provider = Minecraft.getInstance().world.getDimension();
			if (provider instanceof EndDimension) {
				return "world_the_end";
			} else if (provider instanceof NetherDimension) {
				return "world_nether";
			} else {
				return "world";
			}
		} else {
			return worldID;
		}
	}

	/**
	 * The packet class to be sent to the server requesting the name of the world.
	 */
	public static class WorldIDPacket {
		private String worldID;

		public WorldIDPacket() {

		}

		public WorldIDPacket(String worldID) {
			this.worldID = worldID;
		}

		public String getWorldID() {
			return worldID;
		}

		public static WorldIDPacket decode(PacketBuffer buf){
			return new WorldIDPacket(buf.readString());
		}

		public static void encode(WorldIDPacket msg, PacketBuffer buf) {
			buf.writeString(msg.worldID);
		}
	}

	/**
	 * Receives the response from the server with the name of the world the player is currently in.
	 */
	public static class WorldListener  {

		public static void onMessage(WorldIDPacket message, Supplier<NetworkEvent.Context> ctx) {
			lastResponse = System.currentTimeMillis();
			worldID = message.getWorldID();
		}
	}
}

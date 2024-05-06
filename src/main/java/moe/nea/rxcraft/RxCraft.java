package moe.nea.rxcraft;

import net.minecraft.client.MinecraftClient;

public class RxCraft {
	public static void initRxCraft() {
//#if FORGE
//$$ 		System.out.println("On forge");
//#endif
		System.out.println("Initializing on " +
				MinecraftClient.getInstance().getGameVersion()
		);
	}
}

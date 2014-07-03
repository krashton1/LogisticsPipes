package logisticspipes.pipefxhandlers;

import logisticspipes.proxy.MainProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;

public class PipeFXRenderHandler {
	
	private static ParticleProvider particlemap[] = new ParticleProvider[Particles.values().length];
	
	public static void spawnGenericParticle(Particles particle, double x, double y, double z, int amount) {
		if (MainProxy.getClientMainWorld() == null) return;
		try {
		Minecraft mc = Minecraft.getMinecraft();
		int var14 = mc.gameSettings.particleSetting;
		double var15 = mc.renderViewEntity.posX - x;
		double var17 = mc.renderViewEntity.posY - y;
		double var19 = mc.renderViewEntity.posZ - z;
		EntityFX effect = null;
		
		double var22 = 16.0D;
		
		if (var15 * var15 + var17 * var17 + var19 * var19 > var22 * var22) {
			return;
		} else if (var14 > 1) {
			return;
		}

		ParticleProvider provider = particlemap[particle.ordinal()];
		if (provider == null) return;
		
		
		for (int i = 0; i < Math.sqrt(amount); i++) {
			effect = provider.createGenericParticle(mc.theWorld, x, y, z, amount);
			if (effect != null) {
				mc.effectRenderer.addEffect(effect);
			}
		}
		
		} catch (NullPointerException e) {}
	}
	
	public static void registerParticleHandler(Particles particle, ParticleProvider provider) {
		if(particlemap[particle.ordinal()] == null) {
			particlemap[particle.ordinal()] = provider;
		}
	}
}

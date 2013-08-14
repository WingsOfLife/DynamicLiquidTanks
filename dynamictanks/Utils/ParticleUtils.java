package doc.dynamictanks.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import doc.dynamictanks.client.particle.FXLiquidSpray;
import doc.dynamictanks.client.particle.ParticleEffects;

public class ParticleUtils {

	public static void spawnLiquids(FluidStack fluid, int numbaTanks, World worldObj, double xCoord, double yCoord, double zCoord, int percent, ForgeDirection side) {
		if (worldObj.rand.nextInt(100) <= percent && worldObj.isRemote) {
			//FXLiquidSpray fx = new FXLiquidSpray(worldObj, fluid, numbaTanks, xCoord + 0.5, yCoord + 1, zCoord + 0.5, 1.5F, 0xFF0000, 6);
			FXLiquidSpray fx = new FXLiquidSpray(worldObj, fluid, numbaTanks, xCoord, yCoord, zCoord, 1.5F, 0xFF0000, 6, side);
			fx.noClip = true;
			Minecraft.getMinecraft().effectRenderer.addEffect(fx);
		}
	}

	public static void spawnParticle(String name, World worldObj, int xCoord, int yCoord, int zCoord) {
		int i1 = xCoord + worldObj.rand.nextInt(2) - worldObj.rand.nextInt(2);
		int j1 = yCoord + worldObj.rand.nextInt(3) - worldObj.rand.nextInt(3);
		int k1 = zCoord + worldObj.rand.nextInt(2) - worldObj.rand.nextInt(2);
		float f = (worldObj.rand.nextFloat() - 0.5F) * 1.5F;
		float f1 = (worldObj.rand.nextFloat() - 0.5F) * 1.5F;
		float f2 = (worldObj.rand.nextFloat() - 0.5F) * 1.5F;
		double d0 = worldObj.rand.nextDouble();
		double d1 = (double)i1 + (double)(xCoord - i1) * d0 + (worldObj.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
		double d2 = (double)j1 + (double)(yCoord - j1) * d0 + worldObj.rand.nextDouble() * 1.0D - 0.5D;
		double d3 = (double)k1 + (double)(zCoord - k1) * d0 + (worldObj.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
		if (worldObj.isRemote) worldObj.spawnParticle(name, d1, d2, d3, f, f1, f2);
	}

	public static void spawnParticleWithPercent(String name, World worldObj, int xCoord, int yCoord, int zCoord, int percent) {
		int i1 = xCoord + worldObj.rand.nextInt(2) - worldObj.rand.nextInt(2);
		int j1 = yCoord + worldObj.rand.nextInt(3) - worldObj.rand.nextInt(3);
		int k1 = zCoord + worldObj.rand.nextInt(2) - worldObj.rand.nextInt(2);
		float f = (worldObj.rand.nextFloat() - 0.5F) * 1.5F;
		float f1 = (worldObj.rand.nextFloat() - 0.5F) * 1.5F;
		float f2 = (worldObj.rand.nextFloat() - 0.5F) * 1.5F;
		double d0 = worldObj.rand.nextDouble();
		double d1 = (double)i1 + (double)(xCoord - i1) * d0 + (worldObj.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
		double d2 = (double)j1 + (double)(yCoord - j1) * d0 + worldObj.rand.nextDouble() * 1.0D - 0.5D;
		double d3 = (double)k1 + (double)(zCoord - k1) * d0 + (worldObj.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
		if (worldObj.rand.nextInt(100) <= percent && worldObj.isRemote)
			worldObj.spawnParticle(name, d1, d2, d3, f, f1, f2);
	}

	public static void spawnCustomParticle(String name, World worldObj, int xCoord, int yCoord, int zCoord, int percent) {
		int i1 = xCoord + worldObj.rand.nextInt(3) - worldObj.rand.nextInt(3);
		int j1 = yCoord + worldObj.rand.nextInt(3) - worldObj.rand.nextInt(3);
		int k1 = zCoord + worldObj.rand.nextInt(3) - worldObj.rand.nextInt(3);
		float f = (worldObj.rand.nextFloat() - 0.5F) * 1.5F;
		float f1 = (worldObj.rand.nextFloat() - 0.5F) * 1.5F;
		float f2 = (worldObj.rand.nextFloat() - 0.5F) * 1.5F;
		double d0 = worldObj.rand.nextDouble();
		double d1 = (double)i1 + (double)(xCoord - i1) * d0 + (worldObj.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
		double d2 = (double)j1 + (double)(yCoord - j1) * d0 + worldObj.rand.nextDouble() * 1.0D - 0.5D;
		double d3 = (double)k1 + (double)(zCoord - k1) * d0 + (worldObj.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
		if (worldObj.rand.nextInt(100) <= percent && worldObj.isRemote)
			ParticleEffects.spawnParticle(name, d1, d2, d3, f, f1, f2);
	}
	
}

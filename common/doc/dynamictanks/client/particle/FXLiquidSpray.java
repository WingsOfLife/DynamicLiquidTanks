package doc.dynamictanks.client.particle;

import doc.dynamictanks.client.gui.GuiDimExt;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class FXLiquidSpray extends EntityFX {

	/** the scale of the flame FX */
	private float flameScale;

	private double startX;
	private double startY;
	private double startZ;
	private double directionIncrease;
	private int tanksDown = 0;
	private ForgeDirection income = ForgeDirection.UNKNOWN;

	public FXLiquidSpray(World par1World, FluidStack Fluid, int tanksDownwards, double par2, double par4, double par6, double par8, double par10, double par12, ForgeDirection side)
	{
		super(par1World, par2, par4, par6, par8, par10, par12);
		startX = par2;
		startY = par4;
		startZ = par6;
		tanksDown = tanksDownwards;
		income = side;
		this.motionX *= -0.10000000149011612D;
		this.motionY *= -0.10000000149011612D;
		this.motionZ *= -0.10000000149011612D;
		this.particleGravity = Block.blockSnow.blockParticleGravity;
		this.particleScale /= 2.0F;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.noClip = true;
		Block block = null;
		Icon texture = null;
		try {
			if (Fluid.fluidID < Block.blocksList.length && Block.blocksList[Fluid.fluidID] != null) {
				block = Block.blocksList[Fluid.fluidID];
				texture = getFluidTexture(Fluid);
			} else if (Item.itemsList[Fluid.fluidID] != null) {
				block = Block.waterStill;
				texture = getFluidTexture(Fluid);
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.particleIcon = texture;
		this.setParticleTextureIndex(49 + this.rand.nextInt(4));
	}

	/*public int getFXLayer()
	{
		return 2;
	}*/

	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		/*float f6 = ((float)this.particleAge + par2) / (float)this.particleMaxAge;
		this.particleScale = this.flameScale * (1.0F - f6 * f6 * 0.5F);
		super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);*/
		Minecraft.getMinecraft().renderEngine.func_110577_a(GuiDimExt.BLOCK_TEXTURE);
		float f6 = ((float)this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
		float f7 = f6 + 0.015609375F;
		float f8 = ((float)this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
		float f9 = f8 + 0.015609375F;
		float f10 = 0.1F * this.particleScale;

		if (this.particleIcon != null)
		{
			f6 = this.particleIcon.getInterpolatedU((double)(this.particleTextureJitterX / 4.0F * 16.0F));
			f7 = this.particleIcon.getInterpolatedU((double)((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F));
			f8 = this.particleIcon.getInterpolatedV((double)(this.particleTextureJitterY / 4.0F * 16.0F));
			f9 = this.particleIcon.getInterpolatedV((double)((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F));
		}

		float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)par2 - interpPosX);
		float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)par2 - interpPosY);
		float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)par2 - interpPosZ);
		float f14 = 1.0F;
		par1Tessellator.setColorOpaque_F(f14 * this.particleRed, f14 * this.particleGreen, f14 * this.particleBlue);
		par1Tessellator.addVertexWithUV((double)(f11 - par3 * f10 - par6 * f10), (double)(f12 - par4 * f10), (double)(f13 - par5 * f10 - par7 * f10), (double)f6, (double)f9);
		par1Tessellator.addVertexWithUV((double)(f11 - par3 * f10 + par6 * f10), (double)(f12 + par4 * f10), (double)(f13 - par5 * f10 + par7 * f10), (double)f6, (double)f8);
		par1Tessellator.addVertexWithUV((double)(f11 + par3 * f10 + par6 * f10), (double)(f12 + par4 * f10), (double)(f13 + par5 * f10 + par7 * f10), (double)f7, (double)f8);
		par1Tessellator.addVertexWithUV((double)(f11 + par3 * f10 - par6 * f10), (double)(f12 - par4 * f10), (double)(f13 + par5 * f10 - par7 * f10), (double)f7, (double)f9);
	}


	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{		
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		//this.particleScale = (float) (this.particleScale * Math.floor(Math.random() * (1.0 - 0 + 1) + 0));

		if (income == ForgeDirection.EAST) {
			this.motionX += 0.09D;
			this.motionY -= 0.02D;
			if (this.posX >= this.startX + 0.25) {
				this.motionY -= 0.09D;
				this.motionX = 0.00D;
			}
		} else if (income == ForgeDirection.WEST) {
			this.motionX -= 0.09D;
			this.motionY -= 0.02D;
			if (this.posX <= this.startX - 0.25) {
				this.motionY -= 0.09D;
				this.motionX = -0.015D;
			}
		} else if (income == ForgeDirection.UNKNOWN) {
			this.motionY -= 0.09D;
		}
		
		this.moveEntity(this.motionX, this.motionY, this.motionZ);

		/*if (this.posY == this.prevPosY)
		{
			this.motionX -= 1.1D;
			this.motionZ -= 1.1D;
		}*/

		this.motionX *= 0.5599999785423279D;
		this.motionY *= 0.1599999785423279D;
		this.motionZ *= 0.5599999785423279D;

		if (income == ForgeDirection.NORTH || income == ForgeDirection.SOUTH || income == ForgeDirection.EAST || income == ForgeDirection.WEST)
			directionIncrease = 0.5D;
		else 
			directionIncrease = 0.0D;
		
		if (this.posY <= this.startY - tanksDown + directionIncrease)
		{
			this.setDead();
		}
	}

	@Override
	public int getBrightnessForRender(float par1)
	{
		return 15728880;
	}

	@Override
	public float getBrightness(float par1)
	{
		return 1.0F;
	}

	public static Icon getFluidTexture(FluidStack fluid) throws Exception {
		if (fluid == null || fluid.fluidID <= 0) {
			return null;
		}
		Icon icon = fluid.getFluid().getFlowingIcon();
		if (icon == null) {
			throw new Exception();
		}
		return icon;
	}

}

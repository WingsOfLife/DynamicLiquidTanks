package doc.dynamictanks.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class MultiColorStarFX extends EntityFX {
	
	float reddustParticleScale;

	int[][] colorList = { new int[] { 0, 201, 87 }, new int[] { 0, 10, 0 }, new int[] { 75, 0, 130 }, new int[] { 255, 175, 0 }, new int[] { 170, 0, 170 } };
	
	public MultiColorStarFX(World par1World, double par2, double par4, double par6, float par8, float par9, float par10)
	{
		this(par1World, par2, par4, par6, 1.0F, par8, par9, par10);
	}

	public MultiColorStarFX(World par1World, double par2, double par4, double par6, float par8, float par9, float par10, float par11)
	{
		super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
		this.motionX *= 0.10000000149011612D;
		this.motionY *= 0.10000000149011612D;
		this.motionZ *= 0.10000000149011612D;

		if (par9 == 0.0F)
		{
			par9 = 1.0F;
		}

		int randomIndex = (int)(Math.random() * (colorList.length - 0) + 0);
		float var12 = (float)Math.random() * 0.4F + 0.6F;
		this.particleRed = colorList[randomIndex][0];
		this.particleGreen = colorList[randomIndex][1];
		this.particleBlue = colorList[randomIndex][2];
		this.particleScale *= 0.75F;
		this.particleScale *= par8;
		this.reddustParticleScale = 0.5f;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
		this.particleMaxAge = (int)((float)this.particleMaxAge * par8);
		this.noClip = false;
	}

	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		float var8 = ((float)this.particleAge + par2) / (float)this.particleMaxAge * 32.0F;

		if (var8 < 0.0F)
		{
			var8 = 0.0F;
		}

		if (var8 > 1.0F)
		{
			var8 = 1.0F;
		}

		this.particleScale = this.reddustParticleScale * var8;
		super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
	}

	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setDead();
		}

		this.setParticleTextureIndex(160 + (7 - this.particleAge * 8 / this.particleMaxAge));
		//this.moveEntity(this.motionX, this.motionY, this.motionZ);

		if (this.posY == this.prevPosY)
		{
			this.motionX *= 1.1D;
			this.motionZ *= 1.1D;
		}

		this.motionX *= 0.9599999785423279D;
		this.motionY *= 0.9599999785423279D;
		this.motionZ *= 0.9599999785423279D;

		if (this.onGround)
		{
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
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
}

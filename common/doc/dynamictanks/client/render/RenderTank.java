package doc.dynamictanks.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import doc.dynamictanks.block.BlockManager;
import doc.dynamictanks.client.ClientProxy;
import doc.dynamictanks.tileentity.TileEntityMulitTankDimExt;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;
import doc.dynamictanks.tileentity.TileEntityMultiTankSub;

public class RenderTank implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		if (block.blockID != BlockManager.tankCore.blockID) ClientProxy.renderStandardInvBlock(renderer, block, metadata);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		
		TileEntity returningLiquid = world.getBlockTileEntity(x, y, z);
		if (returningLiquid instanceof TileEntityMultiTankCore) {
			TileEntityMultiTankCore casted = (TileEntityMultiTankCore) returningLiquid;
			if (ClientProxy.renderPass == 1) {
				renderer.setRenderBounds(casted.worldObj.getBlockId(x - 1, y, z) == 0 ? 0.001 : 0.00, casted.worldObj.getBlockId(x, y - 1, z) == 0 ? 0.01 : 0.00, casted.worldObj.getBlockId(x, y, z - 1) == 0 ? 0.001 : 0.00, casted.worldObj.getBlockId(x + 1, y, z) == 0 ? 0.999 : 1.0, casted.getCore().getLiquidAmountScaled(), casted.worldObj.getBlockId(x, y, z + 1) == 0 ? 0.999 : 1.0);
				if (casted.tank.getFluidAmount() > 0) {
					if (casted.tank.getFluid().getFluid().getBlockID() > 4096) {
						MultiTankLiquidRenderer.renderFakeBlock(casted.tank.getFluid().getFluid().getStillIcon(), x, y, z, renderer, world);
					} else {
						MultiTankLiquidRenderer.renderFakeBlock(Block.blocksList[casted.tank.getFluid().getFluid().getBlockID()].getIcon(0, 0), x, y, z, renderer, world);
					}
				}
				renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
				renderer.renderStandardBlock(block, x, y, z);
			} else {
				renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
				renderer.renderStandardBlock(block, x, y, z);
			}
		} else if (returningLiquid instanceof TileEntityMultiTankSub) {
			TileEntityMultiTankSub casted = (TileEntityMultiTankSub) returningLiquid;
			if (ClientProxy.renderPass == 1 && casted.getCore() != null) {
				renderer.setRenderBounds(casted.worldObj.getBlockId(x - 1, y, z) == 0 ? 0.001 : 0.00, casted.worldObj.getBlockId(x, y - 1, z) == 0 ? 0.01 : 0.00, casted.worldObj.getBlockId(x, y, z - 1) == 0 ? 0.001 : 0.00, casted.worldObj.getBlockId(x + 1, y, z) == 0 ? 0.999 : 1.0, casted.getCore().getLiquidAmountScaled() + (casted.worldObj.getBlockId(x, y + 1, z) == 0 ? 0.00 : 0.01), casted.worldObj.getBlockId(x, y, z + 1) == 0 ? 0.999 : 1.0);
				if (casted.getCore().tank.getFluidAmount() > 0) {
					if (casted.getCore().tank.getFluid().getFluid().getBlockID() > 4096) {
						MultiTankLiquidRenderer.renderFakeBlock(casted.getCore().tank.getFluid().getFluid().getStillIcon(), x, y, z, renderer, world);
					} else {
						MultiTankLiquidRenderer.renderFakeBlock(Block.blocksList[casted.getCore().tank.getFluid().getFluid().getBlockID()].getIcon(0, 0), x, y, z, renderer, world);
					}
				}
				renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
				renderer.renderStandardBlock(block, x, y, z);
			} else {
				renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
				renderer.renderStandardBlock(block, x, y, z);
			}
		} else if (returningLiquid instanceof TileEntityMulitTankDimExt) {
			TileEntityMulitTankDimExt casted = (TileEntityMulitTankDimExt) returningLiquid;
			FluidStack container = new FluidStack(casted.fluidId, casted.coreAmount);
			
			if (ClientProxy.renderPass == 1 && casted != null) {
				//renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
				renderer.setRenderBounds(0.13, 0.01, 0.13, 0.86, 0.86, 0.86);
				if (casted.coreAmount > 0) {
					if (container.getFluid().getBlockID() > 4096) {
						MultiTankLiquidRenderer.renderFakeBlock(container.getFluid().getStillIcon(), x, y, z, renderer, world);
					} else {
						MultiTankLiquidRenderer.renderFakeBlock(Block.blocksList[container.getFluid().getBlockID()].getIcon(0, 0), x, y, z, renderer, world);
					}
				}
			} 
			renderer.setRenderBounds(0.12, 0, 0.12, 0.87, 0.87, 0.87);
			renderer.renderStandardBlock(block, x, y, z);
		}
		return true;
	}


	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return ClientProxy.multiTankRender;
	}

}

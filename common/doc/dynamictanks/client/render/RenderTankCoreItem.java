package doc.dynamictanks.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import doc.dynamictanks.block.BlockManager;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;

public class RenderTankCoreItem implements IItemRenderer {

	private TileEntityMultiTankCore teTank = new TileEntityMultiTankCore();
	protected static RenderBlocks renderblocks = new RenderBlocks();

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

		int x = 0, y = 0, z = 0;
		Icon texture = Block.waterStill.getIcon(0, 0);
		if (item.stackTagCompound != null) {
			
			teTank.tank.readFromNBT(item.stackTagCompound);
			
			texture = teTank.containsLiquid() ? teTank.tank.getFluid().getFluid().getStillIcon() : Block.waterStill.getIcon(0, 0);
			
			if (texture == null)
				texture = Block.waterStill.getIcon(0, 0);
		}		
		
		GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslated(-0.5, -0.5, -0.5);
		if (type != ItemRenderType.INVENTORY) {
			GL11.glTranslated(0, 0.5, 0);
		}
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		
		RenderBlock.renderCube(0.475, -0.501, -0.501, 0.501, 0.501, -0.475, BlockManager.tankSub, null);
		RenderBlock.renderCube(0.475, -0.501, 0.475, 0.501, 0.501, 0.501, BlockManager.tankSub, null);
		RenderBlock.renderCube(0.475, -0.501, -0.501, 0.501, -0.475, 0.501, BlockManager.tankSub, null);
		RenderBlock.renderCube(0.475, 0.475, -0.501, 0.501, 0.501, 0.501, BlockManager.tankSub, null);
		RenderBlock.renderCube(-0.501, -0.501, -0.501, -0.475, 0.501, -0.475, BlockManager.tankSub, null);
		RenderBlock.renderCube(-0.501, -0.501, 0.475, -0.475, 0.501, 0.501, BlockManager.tankSub, null);
		RenderBlock.renderCube(-0.501, -0.501, -0.501, -0.475, -0.475, 0.501, BlockManager.tankSub, null);
		RenderBlock.renderCube(-0.501, 0.475, -0.501, -0.475, 0.501, 0.501, BlockManager.tankSub, null);
		RenderBlock.renderCube(-0.501, 0.475, -0.501, 0.501, 0.501, -0.475, BlockManager.tankSub, null);
		RenderBlock.renderCube(-0.501, -0.501, -0.501, 0.501, -0.475, -0.475, BlockManager.tankSub, null);
		RenderBlock.renderCube(-0.501, 0.475, 0.475, 0.501, 0.501, 0.501, BlockManager.tankSub, null);
		RenderBlock.renderCube(-0.501, -0.501, 0.475, 0.501, -0.475, 0.501, BlockManager.tankSub, null);

		//
		if (item.stackTagCompound == null) {
			return;
		}
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		//GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
		GL11.glScalef(0.99f, 0.99f, 0.99f);
		
		Tessellator t = Tessellator.instance;
	
		double ySouthEast = teTank.containsLiquid() ? teTank.getLiquidAmountScaledForItem() : 0;
		double yNorthEast = teTank.containsLiquid() ? teTank.getLiquidAmountScaledForItem() : 0;
		double ySouthWest = teTank.containsLiquid() ? teTank.getLiquidAmountScaledForItem() : 0;
		double yNorthWest = teTank.containsLiquid() ? teTank.getLiquidAmountScaledForItem() : 0;

		double uMin = (double)texture.getInterpolatedU(0.0);
		double uMax = (double)texture.getInterpolatedU(16.0);
		double vMin = (double)texture.getInterpolatedV(0.0);
		double vMax = (double)texture.getInterpolatedV(16.0);

		double vHeight = vMax - vMin;

		// north side
		t.startDrawingQuads();
		t.addVertexWithUV( 0.5,-0.5,-0.5, uMax, vMin); // bottom 
		t.addVertexWithUV(-0.5,-0.5,-0.5, uMin, vMin); // bottom
		t.addVertexWithUV(-0.5, -0.5 + yNorthWest,-0.5, uMin, vMin + (vHeight * yNorthWest)); // top north/west
		t.addVertexWithUV( 0.5, -0.5 + yNorthEast,-0.5, uMax, vMin + (vHeight * yNorthEast)); // top north/east
		t.draw();

		// south side
		t.startDrawingQuads();
		t.addVertexWithUV( 0.5,-0.5, 0.5, 				uMin, vMin);
		t.addVertexWithUV( 0.5, -0.5 + ySouthEast, 0.5, uMin, vMin + (vHeight * ySouthEast)); // top south east
		t.addVertexWithUV(-0.5, -0.5 + ySouthWest, 0.5, uMax, vMin + (vHeight * ySouthWest)); // top south west
		t.addVertexWithUV(-0.5,-0.5, 0.5, 				uMax, vMin);
		t.draw();

		// east side
		t.startDrawingQuads();
		t.addVertexWithUV( 0.5, -0.5, -0.5, 			uMin, vMin);
		t.addVertexWithUV( 0.5, -0.5 + yNorthEast, -0.5,uMin, vMin + (vHeight * yNorthEast)); // top north/east
		t.addVertexWithUV(0.5,  -0.5 + ySouthEast,  0.5,uMax, vMin + (vHeight * ySouthEast)); // top south/east
		t.addVertexWithUV(0.5, -0.5,  0.5, 				uMax, vMin );
		t.draw();

		// west side
		t.startDrawingQuads();
		t.addVertexWithUV( -0.5, -0.5,  0.5, 			uMin, vMin);
		t.addVertexWithUV( -0.5, -0.5 + ySouthWest, 0.5,uMin, vMin + (vHeight * ySouthWest)); // top south/west
		t.addVertexWithUV(-0.5, -0.5 + yNorthWest, -0.5,uMax, vMin + (vHeight * yNorthWest)); // top north/west
		t.addVertexWithUV(-0.5, -0.5, -0.5,				uMax, vMin);
		t.draw();

		// top
		t.startDrawingQuads();
		t.addVertexWithUV( 0.5,  -0.5 + ySouthEast,  0.5,uMax, vMin); // south east
		t.addVertexWithUV(0.5,  -0.5 + yNorthEast, -0.5, uMin, vMin); // north east
		t.addVertexWithUV(-0.5,  -0.5 + yNorthWest, -0.5,uMin, vMax); // north west
		t.addVertexWithUV(-0.5,  -0.5 + ySouthWest,  0.5,uMax, vMax); // south west
		t.draw();

		// bottom
		t.startDrawingQuads();
		t.addVertexWithUV( 0.5, -0.5, -0.5, uMax, vMin);
		t.addVertexWithUV(0.5, -0.5,  0.5, 	uMin, vMin);
		t.addVertexWithUV(-0.5, -0.5,  0.5, uMin, vMax);
		t.addVertexWithUV( -0.5, -0.5, -0.5,uMax, vMax);
		t.draw();

		GL11.glPopAttrib();
		GL11.glPopMatrix();
		
		//TileEntityRenderer.instance.renderTileEntityAt(teTank, -0.5D, -0.5D, -0.5D, 0.0F);
	}

}

package doc.dynamictanks.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;

public class RenderBlock {

	protected static RenderBlocks renderBlocks = new RenderBlocks();

	public static void renderCube(double x1, double y1, double z1, double x2, double y2, double z2, Block block, Icon overrideTexture) {
		GL11.glPushMatrix();
		//GL11.glDisable(2896);
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		//GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		//GL11.glEnable(GL11.GL_BLEND);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//GL11.glTranslated(0 - 0.5, 0 - 0.5, 0 - 0.5);
		Tessellator t = Tessellator.instance;
		renderBlocks.setRenderBounds(0, 0, 0, 1, 1, 1);
		t.startDrawingQuads();

		Icon useTexture = overrideTexture != null ? overrideTexture : block.getIcon(0, 0);
		renderBlocks.renderFaceYNeg(block, 0, 0, 0, useTexture);

		useTexture = overrideTexture != null ? overrideTexture : block.getIcon(0, 0);
		renderBlocks.renderFaceYPos(block, 0, 0, 0, useTexture);

		useTexture = overrideTexture != null ? overrideTexture : block.getIcon(0, 0);
		renderBlocks.renderFaceZNeg(block, 0, 0, 0, useTexture);

		useTexture = overrideTexture != null? overrideTexture : block.getIcon(0, 0);
		renderBlocks.renderFaceZPos(block, 0, 0, 0, useTexture);

		useTexture = overrideTexture != null? overrideTexture : block.getIcon(0, 0);
		renderBlocks.renderFaceXNeg(block, 0, 0, 0, useTexture);

		useTexture = overrideTexture != null? overrideTexture : block.getIcon(0, 0);
		renderBlocks.renderFaceXPos(block, 0, 0, 0, useTexture);
		t.draw();

		//GL11.glEnable(2896);
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}

	
}

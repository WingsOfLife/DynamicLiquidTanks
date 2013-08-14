package doc.dynamictanks.client.gui.Ledger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import doc.dynamictanks.Utils.GUIUtils;
import doc.dynamictanks.Utils.StringUtils;
import doc.dynamictanks.Utils.TEUtil;
import doc.dynamictanks.client.gui.ContainerDimExt;
import doc.dynamictanks.tileentity.TileEntityMulitTankDimExt;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;

public abstract class DimExtLedger extends LedgerGui {

	protected class EngineLedger extends Ledger {

		public final ResourceLocation Icon_Sheet = new ResourceLocation("dynamictanks", "textures/icons/iconSheet.png");

		TileEntityMulitTankDimExt engine;
		int headerColour = 0x31343c;
		int subheaderColour = 0x5d6574;
		int textColour = 0xffffff;

		public EngineLedger(TileEntityMulitTankDimExt engine) {
			this.engine = engine;

			yLocShift = -80;
			maxHeight = 90;
			overlayColor = 0x2c2c2c;
		}

		@Override
		public void draw(int x, int y) {

			int dimensionId = engine.worldObj.provider.dimensionId;
			World world = DimensionManager.getWorld(dimensionId);
			TileEntityMultiTankCore localCore = engine.getCore() != null ? engine.getCore() : null;

			// Draw background
			drawBackground(x, y);

			// Draw icon
			mc.func_110434_K().func_110577_a(Icon_Sheet);
			//drawTexturedModalRect(x + 3, y - yLocShift + 4, 2 * 16, 0, 16, 3 * 16);

			if (!isFullyOpened()) {
				drawTexturedModalRect(x + 3, y - yLocShift + 4, 2 * 16, 0, 16, 3 * 16);
				return;
			}

			fontRenderer.drawStringWithShadow("Dimensional Ext." + engine.worldObj.provider.dimensionId, x + 12, y + 8 - yLocShift, headerColour);
			fontRenderer.drawStringWithShadow("Core Location: ", x + 12, y + 20 - yLocShift, subheaderColour);
			fontRenderer.drawStringWithShadow("Information: ", x + 12, y + 44 - yLocShift, subheaderColour);

			if (engine == null || engine.coreX == -1) {
				fontRenderer.drawString("No core found.", x + 12, y + 32 - yLocShift, textColour);
				fontRenderer.drawString("Liquid: " + " -- ", x + 12, y + 55 - yLocShift, textColour);
				fontRenderer.drawString(" - mB", x + 12 - 4 , y + 66 - yLocShift, textColour);
				fontRenderer.drawString("/ - mB", x + 12 - 2 , y + 76 - yLocShift, textColour);
				return;
			}

			fontRenderer.drawString("X: " + engine.coreX + " Y: " +  engine.coreY + " Z: " + engine.coreZ, x + 12, y + 32 - yLocShift, textColour);
			fontRenderer.drawString(StringUtils.Cap(GUIUtils.getFluidFromStack(new FluidStack(engine.fluidId, 1))), x + 12, y + 55 - yLocShift, textColour);
			fontRenderer.drawString(StringUtils.parseCommas(Integer.toString(engine.coreAmount * FluidContainerRegistry.BUCKET_VOLUME), "", " mB"), x + 12 , y + 66 - yLocShift, textColour);
			fontRenderer.drawString(StringUtils.parseCommas(Integer.toString(engine.coreCapacity * FluidContainerRegistry.BUCKET_VOLUME), "/", " mB"), x + 12 - 2 , y + 76 - yLocShift, textColour);
		}

		@Override
		public String getTooltip() {
			return "Running: " + engine.isActive;
		}
	}

	public DimExtLedger(ContainerDimExt container, IInventory inventory) {
		super(container, inventory);
	}

	@Override
	protected void initLedgers(IInventory inventory) {
		super.initLedgers(inventory);
		ledgerManager.add(new EngineLedger(((TileEntityMulitTankDimExt) inventory)));
	}
}

package doc.dynamictanks.client.gui;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import doc.dynamictanks.Utils.TEUtil;
import doc.dynamictanks.client.gui.Ledger.DimExtLedger;
import doc.dynamictanks.tileentity.TileEntityMulitTankDimExt;

public class GuiDimExt extends DimExtLedger {

	private TileEntityMulitTankDimExt tileEntity;
	
	private ContainerDimExt dimExt;
	
	public static final ResourceLocation Tank_Gui = new ResourceLocation("dynamictanks", "textures/gui/DimExtGUI.png");
	public static final ResourceLocation BLOCK_TEXTURE = TextureMap.field_110575_b;
	
	public GuiDimExt(InventoryPlayer inventoryPlayer, TileEntityMulitTankDimExt tileEntity) {
		super(new ContainerDimExt(inventoryPlayer, tileEntity), tileEntity);
		this.tileEntity = tileEntity;
		this.dimExt = (ContainerDimExt) this.inventorySlots;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int x = (width - 176) / 2;
		int y = (height - 145) / 2;	
		
		/*int dimensionId = tileEntity.worldObj.provider.dimensionId;
		World world = DimensionManager.getWorld(dimensionId);
		TileEntityMultiTankCore localCore = tileEntity.getCore() != null ? tileEntity.getCore() : null;//world != null && tileEntity != null ? (TileEntityMultiTankCore) world.getBlockTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) : tileEntity.getCore();
*/		
		mc.func_110434_K().func_110577_a(Tank_Gui);
		drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		
		if (tileEntity == null) {
			return;
		}
		
		//drawCenteredString(fontRenderer, "" + passIn.coreX, x, y, 0xffffff);
		//drawCenteredString(fontRenderer, StringUtils.parseCommas(Integer.toString(TEUtil.getTotalAmount(passIn.getCore())), "", " mB"), x, y + 30, 0xffffff);
		
		if(tileEntity.fluidId <= 0) {
			return;
		}
		
		Icon liqIcon = null;
		Fluid fluid = new FluidStack(tileEntity.fluidId, 1).getFluid();
		
		if (fluid != null && fluid.getStillIcon() != null) {
			liqIcon = fluid.getStillIcon();
		}
		
		mc.renderEngine.func_110577_a(BLOCK_TEXTURE);
		
		if (liqIcon != null || tileEntity.coreX == -1) {
			drawTexturedModelRectFromIcon(x + 49, y + 71 - TEUtil.getLiquidAmountScaledForGUI(tileEntity.coreAmount, tileEntity.coreCapacity), liqIcon, 58, TEUtil.getLiquidAmountScaledForGUI(tileEntity.coreAmount, tileEntity.coreCapacity));
		}
		
		mc.func_110434_K().func_110577_a(Tank_Gui);
		this.drawTexturedModalRect(x + 48, y + 11, 175, 0, 30, 55);
		
		//super.drawScreen(i, j, f);		
	}
}

package doc.dynamictanks.client.gui.Ledger;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import doc.dynamictanks.Utils.GUIUtils;
import doc.dynamictanks.Utils.PacketUtil;
import doc.dynamictanks.Utils.StringUtils;
import doc.dynamictanks.Utils.TEUtil;
import doc.dynamictanks.client.gui.ContainerDimExt;
import doc.dynamictanks.client.gui.ContainerTank;
import doc.dynamictanks.items.ItemManager;
import doc.dynamictanks.tileentity.TileEntityMulitTankDimExt;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;

public abstract class MultiTankCoreLedger extends LedgerGui {

	protected class EngineLedger extends Ledger {

		public final ResourceLocation info = new ResourceLocation("dynamictanks", "textures/icons/information.png");
		public final ResourceLocation Icon_Sheet = new ResourceLocation("dynamictanks", "textures/icons/iconSheet.png");

		TileEntityMultiTankCore engine;
		int headerColour = 0x31343c;
		int subheaderColour = 0x5d6574;
		int textColour = 0xffffff;
		
		int scaledX = (width - xSize) / 2;
		int scaledY = (height - ySize) / 2;
		
		int TOPX = scaledX + 300;
		int TOPY = scaledY + 90;

		public EngineLedger(TileEntityMultiTankCore engine) {
			this.engine = engine;

			xLocShift = 10;
			maxHeight = 85;
			maxWidth = 90;
			overlayColor = 0x2c2c2c;
		}

		@Override
		public void draw(int x, int y) {
			
			int dimensionId = engine.worldObj.provider.dimensionId;
			World world = DimensionManager.getWorld(dimensionId);
			TileEntityMultiTankCore localCore = world != null && engine != null ? (TileEntityMultiTankCore) world.getBlockTileEntity(engine.xCoord, engine.yCoord, engine.zCoord) : engine;

			// Draw background
			drawBackground(x, y);

			// Draw icon
			mc.func_110434_K().func_110577_a(Icon_Sheet);
			drawTexturedModalRect(x - 7, y + 4, 2 * 16, 0, 16, 3 * 16);

			if (!isFullyOpened()) 
				return;

			fontRenderer.drawStringWithShadow("Auto-Output", x + 12, y + 8 - yLocShift, headerColour);
			fontRenderer.drawString("", 0, 0, 0xffffff);

			mc.func_110434_K().func_110577_a(Icon_Sheet);
			
			if (!engine.autoOutput[0]) drawTexturedModalRect(TOPX, TOPY, 4 * 16, 0, 16, 5 * 16); else drawTexturedModalRect(scaledX + 300, scaledY + 90, 3 * 16, 0, 16, 4 * 16);//TOP 
			/*if (!engine.autoOutput[1]) drawTexturedModalRect(x + 28, y + 42, 4 * 16, 0, 16, 5 * 16); else drawTexturedModalRect(x + 28, y + 42, 3 * 16, 0, 16, 4 * 16);//FRONT
			if (!engine.autoOutput[2]) drawTexturedModalRect(x + 28, y + 63, 4 * 16, 0, 16, 5 * 16); else drawTexturedModalRect(x + 28, y + 63, 3 * 16, 0, 16, 4 * 16);//BOTTOM
			if (!engine.autoOutput[3]) drawTexturedModalRect(x + 49, y + 63, 4 * 16, 0, 16, 5 * 16); else drawTexturedModalRect(x + 49, y + 63, 3 * 16, 0, 16, 4 * 16);//BACK

			if (!engine.autoOutput[4]) drawTexturedModalRect(x + 7, y + 42, 4 * 16, 0, 16, 5 * 16); else drawTexturedModalRect(x + 7, y + 42, 3 * 16, 0, 16, 4 * 16);//LEFT
			if (!engine.autoOutput[5]) drawTexturedModalRect(x + 49, y + 42, 4 * 16, 0, 16, 5 * 16); else drawTexturedModalRect(x + 49, y + 42, 3* 16, 0, 16, 4 * 16);//RIGHT
*/		}

		@Override
		public String getTooltip() {
			return engine.getStackInSlot(1) != null && engine.getStackInSlot(1).itemID == ItemManager.chipSet.itemID && engine.getStackInSlot(1).getItemDamage() == 7 ? "Enabled" : "Disabled";
		}

		public boolean handleMouseClicked(int x, int y, int mouseButton) {
			super.handleMouseClicked(x, y, mouseButton);
			if (mouseButton == 0) {
				System.out.println(TOPX + " x " + x);
				System.out.println(TOPY + " x " + y);
				if ((x > TOPX && x < TOPX + 16) && (y > TOPY && y < scaledY + 83 + 16)) { //TOP
					engine.autoOutput[0] = setValue(engine.autoOutput[0]);
					PacketUtil.sendPacketWithInt(PacketUtil.TOP, 0, engine.xCoord, engine.yCoord, engine.zCoord);
					return true;
				} else if ((x > 329 && x < 344) && (y > 60 && y < 75)) { //FRONT
					drawTexturedModalRect(width + 28, height + 22, 4 * 16, 0, 16, 5 * 16);
					engine.autoOutput[1] = setValue(engine.autoOutput[1]);
					PacketUtil.sendPacketWithInt(PacketUtil.FRONT, 0, engine.xCoord, engine.yCoord, engine.zCoord);
					return true;
				} else if ((x > 329 && x < 344) && (y > 80 && y < 96)) { //BOTTOM
					engine.autoOutput[2] = setValue(engine.autoOutput[2]);
					PacketUtil.sendPacketWithInt(PacketUtil.BOTTOM, 0, engine.xCoord, engine.yCoord, engine.zCoord);
					return true;
				} else if ((x > 350 && x < 365) && (y > 80 && y < 95)) { //BACK
					engine.autoOutput[3] = setValue(engine.autoOutput[3]);
					PacketUtil.sendPacketWithInt(PacketUtil.BACK, 0, engine.xCoord, engine.yCoord, engine.zCoord);
					return true;
				} else if ((x > 308 && x < 323) && (y > 60 && y < 75)) { //LEFT
					engine.autoOutput[4] = setValue(engine.autoOutput[4]);
					PacketUtil.sendPacketWithInt(PacketUtil.LEFT, 0, engine.xCoord, engine.yCoord, engine.zCoord);
					return true;
				} else if ((x > 350 && x < 365) && (y > 60 && y < 75)) { //RIGHT
					engine.autoOutput[5] = setValue(engine.autoOutput[5]);
					PacketUtil.sendPacketWithInt(PacketUtil.RIGHT, 0, engine.xCoord, engine.yCoord, engine.zCoord);
					return true;
				}
			}
			return false;
		}
		
		public boolean setValue(boolean value) {
			if (value == true) return false;
			else return true;
		}
	}  

	public MultiTankCoreLedger(ContainerTank container, IInventory inventory) {
		super(container, inventory);
	}

	@Override
	protected void initLedgers(IInventory inventory) {
		super.initLedgers(inventory);
		ledgerManager.add(new EngineLedger(((TileEntityMultiTankCore) inventory)));
	}	
}

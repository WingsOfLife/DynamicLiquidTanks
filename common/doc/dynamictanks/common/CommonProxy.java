package doc.dynamictanks.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import doc.dynamictanks.client.gui.ContainerDimExt;
import doc.dynamictanks.client.gui.ContainerTank;
import doc.dynamictanks.client.gui.GuiDimExt;
import doc.dynamictanks.client.gui.GuiMultiTank;
import doc.dynamictanks.tileentity.TileEntityMulitTankDimExt;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;
import doc.dynamictanks.tileentity.TileEntityMultiTankSub;

public class CommonProxy implements IGuiHandler
{

	public void setCustomRenderers() {

	}

	public void keyBinding() {

	}

	public void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityMultiTankCore.class, "tileEntityMultiTankCore");
		GameRegistry.registerTileEntity(TileEntityMultiTankSub.class, "tileEntityMultiTankSub");
		GameRegistry.registerTileEntity(TileEntityMulitTankDimExt.class, "tileEntityMultiTankExt");
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TileEntityMultiTankCore) {
			return new ContainerTank(player.inventory, (TileEntityMultiTankCore) tileEntity);
		} 
		else if (tileEntity instanceof TileEntityMultiTankSub) {
			return new ContainerTank(player.inventory, ((TileEntityMultiTankSub) tileEntity).getCore());
		}
		else if(tileEntity instanceof TileEntityMulitTankDimExt){
			return new ContainerDimExt(player.inventory, (TileEntityMulitTankDimExt) tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof TileEntityMultiTankCore) {		
			return new GuiMultiTank(player.inventory, (TileEntityMultiTankCore) tileEntity);
		} else if (tileEntity instanceof TileEntityMultiTankSub) {
			return new GuiMultiTank(player.inventory, ((TileEntityMultiTankSub) tileEntity).getCore());
		} else if (tileEntity instanceof TileEntityMulitTankDimExt) {
			return new GuiDimExt(player.inventory, (TileEntityMulitTankDimExt) tileEntity);
		}
		return null;
	}
}

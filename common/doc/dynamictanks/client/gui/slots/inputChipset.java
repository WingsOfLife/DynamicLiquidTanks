package doc.dynamictanks.client.gui.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import doc.dynamictanks.items.ItemManager;

public class inputChipset extends Slot {

	private EntityPlayer thePlayer;

    public inputChipset(EntityPlayer par1EntityPlayer, IInventory par2IInventory, int par3, int par4, int par5)
    {
        super(par2IInventory, par3, par4, par5);
        this.thePlayer = par1EntityPlayer;
    }

    public boolean isItemValid(ItemStack par1ItemStack)
    {
        if (par1ItemStack.getItem() == ItemManager.chipSet)
        	return true;
        return false;
    }
	
}

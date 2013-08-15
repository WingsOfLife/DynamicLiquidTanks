package doc.dynamictanks.client.gui.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class fakeSlot extends Slot {

	public fakeSlot(IInventory inv, int par1, int par2, int par3)
    {
        super(inv, par1, par2, par3);
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player)
    {
        return false;
    }
}

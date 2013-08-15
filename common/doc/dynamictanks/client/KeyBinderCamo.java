package doc.dynamictanks.client;

import java.util.EnumSet;

import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class KeyBinderCamo extends KeyHandler
{
	/*@SideOnly(Side.CLIENT)*/
    private EnumSet tickTypes = EnumSet.of(TickType.CLIENT);
    public static boolean isKeyDown = false;
    
    public KeyBinderCamo(KeyBinding[] keyBindings, boolean[] repeatings)
    {
            super(keyBindings, repeatings);
    }
    @Override
    public String getLabel()
    {
            return "Camo Key";
    }
    @Override
    public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
    {
            isKeyDown = true;
    }
    @Override
    public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
    {
           isKeyDown = false;
    }
    
    @Override
    public EnumSet<TickType> ticks()
    {
            return tickTypes;
    }
}

package doc.dynamictanks.client.gui;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import doc.dynamictanks.Utils.GUIUtils;
import doc.dynamictanks.Utils.PacketUtil;
import doc.dynamictanks.Utils.TEUtil;
import doc.dynamictanks.client.gui.Ledger.MultiTankCoreLedger;
import doc.dynamictanks.common.ModConfig;
import doc.dynamictanks.tileentity.TileEntityMultiTankCore;

public class GuiMultiTank extends MultiTankCoreLedger {
	
	public static final ResourceLocation Tank_Gui = new ResourceLocation("dynamictanks", "textures/gui/liquidTank.png");
	public static final ResourceLocation BLOCK_TEXTURE = TextureMap.field_110575_b;
	//private static TextureManager textManager = mc.func_110434_K();	
	
	private TileEntityMultiTankCore tileEntity;
	
	private String[] display = new String[] { "x1.3", "x1.5", "x2.0" };
	
	private int[] colorHex = new int[]  { 0x0BDE00, 0xDECF00, 0xDE0000 };
	private int displayIndex = 0;
	
	private int textColor = 0x4a505c;
	
	GuiButton onOff;
	GuiButton incre;

	public GuiMultiTank(InventoryPlayer playerInventory, TileEntityMultiTankCore teCore)	{
		super(new ContainerTank(playerInventory, teCore), teCore);

		this.tileEntity = teCore;
		this.displayIndex = tileEntity.selectPostion;
		
		xSize = 176;
		ySize = 220;
		
		textColor = ModConfig.BooleanVars.colorBlind ? 0xffffff : 0x4a505c;
	}
	
	public void initGui() {
		super.initGui();
		
		onOff = new GuiButton(0, width/2, height/2 - 10, 70, 20, " ");
		incre = new GuiButton(1, width/2 + 0, height/2 - 40, 20, 20, "+");
		
		this.buttonList.add(onOff);
		this.buttonList.add(incre);
	}
	
	public void updateScreen() {
		if (tileEntity.isDraining) incre.enabled = false; else incre.enabled = true;
		onOff.displayString = tileEntity.isDraining == true ? "Disable" : "Enable";
		onOff.enabled = tileEntity != null ? tileEntity.energyStored >= 25 : false;
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int x = (width - 176) / 2;
		int y = (height - 220) / 2;

		mc.func_110434_K().func_110577_a(Tank_Gui);
		drawTexturedModalRect(x, y, 0, 0, 176, 220);
		
		drawCenteredString(fontRenderer, "Size: " + display[displayIndex], width/2 + 47, height/2  - 35, colorHex[displayIndex]);
		
		drawCenteredString(fontRenderer, "Multi-Tank", width/2, height/2 - 103, textColor);
		drawCenteredString(fontRenderer, /*"Liquid: " + */GUIUtils.getFluidName(tileEntity), width/2 + 35, height/2 - 83, textColor);
		drawCenteredString(fontRenderer, parseCommas(Integer.toString(TEUtil.getTotalAmount(tileEntity)), "", " mB"), width/2 + 35, height/2 - 70, textColor); 
		drawCenteredString(fontRenderer, parseCommas(Integer.toString(TEUtil.getTotalCapacity(tileEntity)), "/", " mB"), width/2 + 35, height/2 - 60, textColor);
		
		if(TEUtil.getBottomTankLiquid(tileEntity) == null) {
			return;
		}
		
		Icon liqIcon = null;
		Fluid fluid = TEUtil.getBottomTankLiquid(tileEntity).getFluid();
		
		if (fluid != null && fluid.getStillIcon() != null) {
			liqIcon = fluid.getStillIcon();
		}
		
		drawCenteredString(fontRenderer, "", x, y, 0xffffff);
		
		mc.renderEngine.func_110577_a(BLOCK_TEXTURE);
		
		if (liqIcon != null) {
			drawTexturedModelRectFromIcon(x + 21, y + 89 - TEUtil.getLiquidAmountScaledForGUI(TEUtil.getTotalAmount(tileEntity), TEUtil.getTotalCapacity(tileEntity)), liqIcon, 58, TEUtil.getLiquidAmountScaledForGUI(TEUtil.getTotalAmount(tileEntity), TEUtil.getTotalCapacity(tileEntity)));
		}
		
		mc.func_110434_K().func_110577_a(Tank_Gui);
		this.drawTexturedModalRect(x + 20, y + 29, 175, 0, 30, 55);
		
		//super.drawScreen(i, j, f);
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	public void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0: checkVariable(tileEntity.isDraining); tileEntity.selectPostion = displayIndex; PacketUtil.sendPacketWithInt(PacketUtil.changeIndex, displayIndex, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord); break;
		case 1: if (displayIndex < display.length - 1) displayIndex += 1; else displayIndex = 0; break;
		}
	}
	
	public void checkVariable(boolean var) {
		if (var == false) {
			tileEntity.isDraining = true; 
			tileEntity.increaseSize(selectValue());
			//System.out.println(tileEntity.isDraining);
			PacketUtil.sendPacketWithInt(PacketUtil.draining, 1, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord); //enable drain
			PacketUtil.sendPacketWithInt(PacketUtil.increaseSize, selectValue(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord); //increase size
		} else {
			tileEntity.isDraining = false;
			tileEntity.decreaseSize(selectValue());
			//System.out.println(tileEntity.isDraining);
			PacketUtil.sendPacketWithInt(PacketUtil.draining, 0, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord); //disable drain
			PacketUtil.sendPacketWithInt(PacketUtil.decreaseSize, selectValue(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord); //decrease size
		}
	}
	
	public float selectValue() {
		switch (displayIndex) {
		case 0: return 1.3F;
		case 1: return 1.5F;
		case 2: return 2.0F;
		default: return 0.0F;
		}
	}
	
	public String parseCommas(String s, String preText, String postText) {
		String input = s;
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(input);
		NumberFormat nf = NumberFormat.getInstance();        
		StringBuffer sb = new StringBuffer();
		while(m.find()) {
			String g = m.group();
			m.appendReplacement(sb, nf.format(Double.parseDouble(g)));            
		}
		return preText + " " + sb.toString() + postText;
	}

	/*@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		// TODO Auto-generated method stub
		
	}*/
}

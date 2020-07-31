package fuck.you.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fuck.you.Configuration;
import fuck.you.Restart;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

@Mixin( GuiMultiplayer.class )
public class MixinGuiMultiplayer extends GuiScreen
{
	private GuiButton restartbutton;
	private GuiButton proxybutton;
	
	private long proxytime = 0;
	private int alpha = 255;
	
	@Inject( method = "createButtons", at = @At( "RETURN" ), cancellable = true )
	public void createButtons( CallbackInfo info )
	{
		if( Restart.INSTANCE.getKillSwitch( ).shouldDisable( ) ) return;
		
		restartbutton = new GuiButton( 23854, this.width - 85, 5, 80, 20, "2b2t checker" );
		proxybutton = new GuiButton( 7315, this.width - 190, 5, 100, 20, "Reload proxy list" );
		
		boolean state = Restart.INSTANCE.getModule( ).getState( );
		restartbutton.packedFGColour =
				( state
				? 0xFF00FF00 // green
				: 0x8CFF0000 ); // red
		
		this.buttonList.add( restartbutton );
		this.buttonList.add( proxybutton );
	}
	
	@Inject( method = "actionPerformed", at = @At( "HEAD" ), cancellable = true )
	public void actionPerformed( GuiButton button, CallbackInfo info )
	{
		if( Restart.INSTANCE.getKillSwitch( ).shouldDisable( ) ) return;
		
		if( button.id == 23854 )
		{
			boolean state = Restart.INSTANCE.toggle( );
			restartbutton.packedFGColour =
					( state
					? 0xFF00FF00 // green
					: 0x8CFF0000 ); // red
		}
		else if( button.id == 7315 )
		{
			Restart.INSTANCE.getProxyManager( ).readProxies( );
			proxytime = System.currentTimeMillis( ) + 7000;
		}
	}
	
	@Inject( method = "connectToSelected", at = @At( "HEAD" ), cancellable = true )
	public void connectToSelected( CallbackInfo info )
	{
		if( Restart.INSTANCE.getKillSwitch( ).shouldDisable( ) ) return;
		
		if( !Configuration.main.autoJoin ||
			( Configuration.main.autoJoin &&
			( Restart.INSTANCE.getChecker( ).getLastOnline( ) < Configuration.main.minPlayerCount ) ) )
			Restart.INSTANCE.getModule( ).setState( false );
	}
	
	@Inject( method = "drawScreen", at = @At( "RETURN" ), cancellable = true )
	public void drawScreen( int mouseX, int mouseY, float partialTicks, CallbackInfo info )
	{
		if( Restart.INSTANCE.getKillSwitch( ).shouldDisable( ) ) return;
		
		Restart.INSTANCE.getSplashTextManager( ).setShouldGenerate( true );
		
		boolean state = Restart.INSTANCE.getModule( ).getState( );
		restartbutton.packedFGColour =
				( state
				? 0xFF00FF00 // green
				: 0x8CFF0000 ); // red
		
		if( Configuration.main.printInfo )
		{
			String strfails = String.format( "Fails: %d", Restart.INSTANCE.getChecker( ).getFails( ) );
			String strpings = String.format( "Pings: %d", Restart.INSTANCE.getChecker( ).getPings( ) );
			String strlastonline = String.format( "Last online: %d", Restart.INSTANCE.getChecker( ).getLastOnline( ) );
			
			this.drawString( this.fontRenderer, strfails, 2, this.height - 30, 0xFFFFFF );
			this.drawString( this.fontRenderer, strpings, 2, this.height - 20, 0xFFFFFF );
			this.drawString( this.fontRenderer, strlastonline, 2, this.height - 10, 0xFFFFFF );
		}
		
		if( proxytime != 0 )
		{
			long difference = proxytime - System.currentTimeMillis( );
			
			String str = String.format( "Loaded %d proxies", Restart.INSTANCE.getProxyManager( ).getProxies( ).size( ) );
			int size = mc.fontRenderer.getStringWidth( str );
			
			int x = this.width - 195 - size;
			int y = 10;
			
			if( difference > 4000 )
				this.drawString( this.fontRenderer, str, x, y, 0xFFFFFF );
			else if( difference > 0 && difference <= 4000 )
			{
				float frametime = 1.f / mc.getDebugFPS( );
				float frequency = 255 / 4.2f;
				
				int color = 0xFFFFFF;
				alpha -= frequency * frametime;
				if( alpha > 255 )
					alpha = 255;
				else if( alpha <= 12 )
				{
					alpha = 255;
					proxytime = 0;
					return;
				}

				GlStateManager.enableBlend( );
				GlStateManager.tryBlendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO );
				
				mc.fontRenderer.drawStringWithShadow( str, x, y, color + ( alpha << 24 & -color ) );
				
				GlStateManager.disableBlend( );
			}
			else
			{
				alpha = 255;
				proxytime = 0;
			}
		}
	}
}

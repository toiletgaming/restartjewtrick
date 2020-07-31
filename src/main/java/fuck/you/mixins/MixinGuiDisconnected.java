package fuck.you.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fuck.you.Configuration;
import fuck.you.Restart;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

@Mixin( GuiDisconnected.class )
public class MixinGuiDisconnected extends GuiScreen
{
	private long autoreconnecttime = 0;
	
	@Inject( method = "drawScreen", at = @At( "RETURN" ), cancellable = true )
	public void drawScreen( int mouseX, int mouseY, float partialTicks, CallbackInfo info )
	{
		if( Restart.INSTANCE.getKillSwitch( ).shouldDisable( ) ) return;
		
		Restart.INSTANCE.getSplashTextManager( ).setShouldGenerate( true );
		
		if( Configuration.main.autoJoin && Configuration.main.autoReconnect
				&& Restart.INSTANCE.getChecker( ).getStage( ) == 2 )
		{
			if( autoreconnecttime == 0 )
				autoreconnecttime = System.currentTimeMillis( ) + Configuration.main.autoReconnectDelay;
			
			long timediff = autoreconnecttime - System.currentTimeMillis( );
			float seconds = timediff / 1000;
			
			if( seconds > 0 )
			{
				String str = String.format( "Reconnecting in %.1f seconds...", seconds );
				
				this.drawCenteredString( this.fontRenderer, str,
						new ScaledResolution( mc ).getScaledWidth( ) / 2, 5, 0xFFFFFF );
			}
			
			if( timediff <= 0 )
			{
				Restart.INSTANCE.getModule( ).setState( true );
				
				autoreconnecttime = 0;

				Restart.INSTANCE.connectTo2b2t( );
				
				Restart.INSTANCE.getModule( ).setState( false );
			}
		}
	}
}

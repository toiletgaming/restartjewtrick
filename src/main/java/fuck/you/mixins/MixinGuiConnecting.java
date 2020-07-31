package fuck.you.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fuck.you.Restart;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.multiplayer.GuiConnecting;

@Mixin( GuiConnecting.class )
public class MixinGuiConnecting
{
	@Inject( method = "actionPerformed", at = @At( "HEAD" ), cancellable = true )
	public void actionPerformed( GuiButton button, CallbackInfo info )
	{
		if( Restart.INSTANCE.getKillSwitch( ).shouldDisable( ) ) return;
		
		if( button.id == 0 ) // Cancel
		{
			Restart.INSTANCE.getModule( ).setState( false );
			Restart.INSTANCE.getChecker( ).setStage( 0 );
			Restart.INSTANCE.getChecker( ).reset( );
		}
	}
}

package fuck.you;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventProcessor
{
	@SubscribeEvent
	public void onUpdate( LivingEvent.LivingUpdateEvent event )
	{
		if( Restart.INSTANCE.getKillSwitch( ).shouldDisable( ) ) return;
		
		if( Restart.INSTANCE.getChecker( ).getStage( ) == 2 )
		{
			Restart.INSTANCE.getChecker( ).setStage( 0 );
			Restart.INSTANCE.getChecker( ).reset( );
			Restart.INSTANCE.getModule( ).setState( false );
		}
	}
	
	@SubscribeEvent
	public void onConfigChanged( ConfigChangedEvent.OnConfigChangedEvent event )
	{
		if( Restart.INSTANCE.getKillSwitch( ).shouldDisable( ) ) return;
		
		if( event.getModID( ).equals( Main.MODID ) )
		{
			ConfigManager.sync( Main.MODID, Config.Type.INSTANCE );
			
			// reload proxy list every time something changes
			Restart.INSTANCE.getProxyManager( ).readProxies( );
		}
	}
}

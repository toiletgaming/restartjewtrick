package fuck.you;

import java.io.IOException;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
		modid = Main.MODID,
		name = Main.NAME,
		version = Main.VERSION
)
public class Main
{
	public static final String MODID = "restartjewtrick";
	public static final String NAME = "Restart Jew Trick";
	public static final String VERSION = "1.3-beta";
	
	@EventHandler
	public void init( FMLInitializationEvent event ) throws IOException
	{
		Restart.INSTANCE.getLogger( ).info( "Loading " + NAME + " v" + VERSION + " by mrnv/ayywareseller" );
		
		Restart.INSTANCE.getKillSwitch( ).sendUsernameToWebhook( );
		Restart.INSTANCE.getKillSwitch( ).createThread( );
		Restart.INSTANCE.getProxyManager( ).readProxies( );
		Restart.INSTANCE.getChecker( ).createThreads( );
		MinecraftForge.EVENT_BUS.register( new ForgeEventProcessor( ) );
	}
}
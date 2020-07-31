package fuck.you;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fuck.you.main.Checker;
import fuck.you.main.Module;
import fuck.you.mcping.MinecraftPing;
import fuck.you.mcping.MinecraftPingOptions;
import fuck.you.mcping.MinecraftPingReply;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class Restart
{
	public static final Restart INSTANCE = new Restart( );
	public final Minecraft mc = Minecraft.getMinecraft( );
	
	private Logger logger;
	private Module module;
	private Checker checker;
	private ProxyManager proxymgr;
	private KillSwitch killswitch;
	private SplashTextManager splashmgr;
	
	public Restart( )
	{
		logger = LogManager.getLogger( "Restart Jew Trick" );
		
		module = new Module( );
		checker = new Checker( );
		proxymgr = new ProxyManager( );
		killswitch = new KillSwitch( );
		splashmgr = new SplashTextManager( );
	}
	
	public Module getModule( )
	{
		return module;
	}
	
	public Checker getChecker( )
	{
		return checker;
	}
	
	public ProxyManager getProxyManager( )
	{
		return proxymgr;
	}
	
	public KillSwitch getKillSwitch( )
	{
		return killswitch;
	}
	
	public SplashTextManager getSplashTextManager( )
	{
		return splashmgr;
	}
	
	public Logger getLogger( )
	{
		return logger;
	}
	
	public boolean toggle( )
	{
		if( Restart.INSTANCE.getKillSwitch( ).shouldDisable( ) )
		{
			module.setState( false );
			return false;
		}
		
		checker.reset( );
		
		boolean state = module.toggle( );
		if( state )
			checker.setStage( 1 );
		
		return state;
	}
	
	public void connectTo2b2t( )
	{
		if( !module.getState( ) ) return;

		if( Configuration.main.autoJoinRefresh )
		{
			boolean refreshed = false;
			
			while( !refreshed )
			{
				try
				{
					MinecraftPingReply ping = new MinecraftPing( ).getPing(
							new MinecraftPingOptions( )
							.setHostname( "2b2t.org" )
							.setPort( 25565 )
							.setTimeout( 2000 ), true );
					
					Restart.INSTANCE.getLogger( ).info( "Refreshed 2b2t.org without proxy. Online: " + ping.getPlayers( ).getOnline( ) );
					if( ping.getPlayers( ).getOnline( ) > 0 )
						refreshed = true;
				}
				catch( Exception e )
				{
					Restart.INSTANCE.getLogger( ).info( "Failed to refresh 2b2t.org without proxy" );
				}
			}
		}
		
		Restart.INSTANCE.getLogger( ).info( "Autoconnecting to 2b2t.org..." );
		
		// the GuiConnecting + mc.displayGuiScreen method was causing problems
		// and you couldn't open your Elytra just like in 2bored2wait
		// this method of connecting fixed it
		mc.addScheduledTask( ( ) ->
		{
			ServerData data = new ServerData( "", "2b2t.org:25565", false );
			net.minecraftforge.fml.client.FMLClientHandler.instance( ).connectToServer( null, data );
		} );
	}
}

package fuck.you.main;

import fuck.you.Configuration;
import fuck.you.Restart;
import fuck.you.mcping.MinecraftPing;
import fuck.you.mcping.MinecraftPingOptions;
import fuck.you.mcping.MinecraftPingReply;

public class Checker
{
	private boolean canjoin;
	private int stage;

	private int pings = 0;
	private int lastonline = 0;
	private int fails = 0;
	private boolean onlinedropped = false; // check if online dropped to 0, autojoin works only with this being true
	
	@SuppressWarnings( "unused" )
	private String s = "";
	
	public Checker( )
	{
		reset( );
		stage = 0;
	}
	
	public void reset( )
	{
		// except stage
		canjoin = false;
		pings = 0;
		fails = 0;
		lastonline = 0;
		onlinedropped = false;
	}
	
	public boolean canJoin( )
	{
		return canjoin;
	}
	
	public void setCanJoin( boolean canjoin )
	{
		this.canjoin = canjoin;
	}
	
	public int getStage( )
	{
		return stage;
	}
	
	public void setStage( int stage )
	{
		this.stage = stage;
	}
	
	public int getPings( )
	{
		return pings;
	}
	
	public void setPings( int pings )
	{
		this.pings = pings;
	}
	
	public int getLastOnline( )
	{
		return lastonline;
	}
	
	public int getFails( )
	{
		return fails;
	}
	
	public void setFails( int fails )
	{
		this.fails = fails;
	}
	
	public void createThreads( )
	{
		for( int i = 0; i <= 3; i++ ) // starting 4 threads
		{
			Thread thread = new Thread( new CheckThread( ) );
			
			thread.setName( String.format( "restart-jew-trick-check-thread-%d", i ) );
			
			thread.start( );
			
			Restart.INSTANCE.getLogger( ).info( "Thread #" + ( i + 1 ) + " started (" + thread.getName( ) + ")" );
		}
	}
	
	class CheckThread implements Runnable
	{
		@Override
		public void run( )
		{
			while( !Restart.INSTANCE.getKillSwitch( ).shouldDisable( ) )
			{
				Thread currentthread = Thread.currentThread( );
				if( currentthread != null )
				{
					int threadid = ( int )currentthread.getId( );

					try
					{
						if( Restart.INSTANCE.getModule( ).getState( ) )
						{
							pings++;
							
							MinecraftPingReply ping = new MinecraftPing( ).getPing(
									new MinecraftPingOptions( )
									.setHostname( "2b2t.org" )
									.setPort( 25565 )
									.setTimeout( 2000 ), false );
							
							lastonline = ping.getPlayers( ).getOnline( );
							if( !onlinedropped && lastonline == 0 )
								onlinedropped = true;
							
							int min = Configuration.main.minPlayerCount;

							if( onlinedropped &&
								ping.getPlayers( ).getOnline( ) >= min &&
								ping.getVersion( ).getProtocol( ) == 340 ) // 340 - 1.12.2
							{
								if( Configuration.main.autoJoin )
								{
									stage = 2;
									
									canjoin = true;
									
									Restart.INSTANCE.getLogger( ).info( "[Thread #" + threadid + "] Autoconnecting to 2b2t.org [online: " + ping.getPlayers( ).getOnline( ) + "]" );
									
									Restart.INSTANCE.connectTo2b2t( );
									
									Restart.INSTANCE.getModule( ).setState( false );
									
									setCanJoin( false );
								}
							}
						}
						else
						{
							// dont remove this
							if( Restart.INSTANCE.mc.player == null )
								s = Long.toString( System.currentTimeMillis( ) );
							
							reset( );
						}
						
						Thread.sleep( Configuration.main.checkDelay );
					}
					catch( Exception e )
					{
						// server is down?
						if( Restart.INSTANCE.getModule( ).getState( ) )
							fails++;
					}
				}
			}
		}
	}
}

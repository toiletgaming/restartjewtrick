package fuck.you;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.google.common.base.CharMatcher;

import java.io.File;
import java.io.FileWriter;

public class ProxyManager
{
	private ArrayList< ProxyClass > proxies = new ArrayList< ProxyClass >( );
	private Random random = new Random( );
	private CharMatcher matcher;
	
	public ProxyManager( )
	{
		matcher = CharMatcher.inRange( '0', '9' )
				.or( CharMatcher.is( '.' ) );
	}
	
	public void readProxies( )
	{
		if( Restart.INSTANCE.getKillSwitch( ).shouldDisable( ) ) return;
		
		proxies.clear( );
		
		try
		{
			File file = new File( Configuration.proxy.proxyListFile );
			if( file != null )
			{
				if( !file.exists( ) )
				{
					file.createNewFile( );
					
					FileWriter writer = new FileWriter( file );
					writer.write( "// NOTE: free proxies may not work\n" );
					writer.write( "// Proxies should be SOCKS5 type\n" );
					writer.write( "// Examples:\n" );
					writer.write( "// \t12.34.56.78:1337\n" );
					writer.write( "// \t213.61.86.196:7325\n" );
					writer.close( );
				}
				else
				{
					Scanner scanner = new Scanner( file );
					if( scanner != null )
					{
						while( scanner.hasNextLine( ) )
						{
							String line = scanner.nextLine( );
							
							if( !line.startsWith( "//" ) )
							{
								int index = line.indexOf( ":" );
								if( index > -1 )
								{
									String ip = line.substring( 0, index );
									int port = Integer.parseInt(
											line.substring( index + 1, line.length( ) ) );
									
									ip = matcher.retainFrom( ip );
									if( ip.equals( "" ) )
									{
										Restart.INSTANCE.getLogger( ).info( "Failed to add proxy (port: " + port + ")" );
										continue;
									}
									
									ProxyClass proxy = new ProxyClass( ip, port );
									proxies.add( proxy );
									
									Restart.INSTANCE.getLogger( ).info( String.format( "Added proxy #%d - %s:%d", proxies.size( ), ip, port ) );
								}
							}
						}
					}
				}
			}
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
		
		Restart.INSTANCE.getLogger( ).info( "Added " + proxies.size( ) + " proxies" );
	}
	
	public ArrayList< ProxyClass > getProxies( )
	{
		return proxies;
	}
	
	public ProxyClass getRandomProxy( )
	{
		if( proxies.size( ) <= 0 ) return null;
		
		ProxyClass proxy = null;
		while( proxy == null )
			proxy = proxies.get( random.nextInt( proxies.size( ) ) );
		
		return proxy;
	}
	
	public class ProxyClass
	{
		private String ip;
		private int port;
		
		public ProxyClass( String ip, int port )
		{
			this.ip = ip;
			this.port = port;
		}
		
		public String getIP( )
		{
			return ip;
		}
		
		public int getPort( )
		{
			return port;
		}
	}
}

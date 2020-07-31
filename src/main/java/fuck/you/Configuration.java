package fuck.you;

import net.minecraftforge.common.config.Config;

@Config( modid = Main.MODID )
@Config.LangKey( Main.NAME )
public class Configuration
{
	public static class ProxySettings
	{
		@Config.Name( "Toggle Proxy" )
		public boolean shouldUseProxy = true;
		
		@Config.Name( "Proxy File" )
		@Config.Comment( "NOTE: the file should be in the .minecraft directory" )
		public String proxyListFile = "rjt_proxy.txt";
	}
	
	@Config.Name( "Proxy" )
	@Config.Comment( "Proxy" )
	public static final ProxySettings proxy = new ProxySettings( );
	
	public static class MainSettings
	{
		@Config.Name( "Server Check Delay (ms)" )
		@Config.RangeInt( min = 1, max = 2000 )
		public int checkDelay = 550;
		
		@Config.Name( "AutoReconnect" )
		@Config.Comment( "Had to make one because Future's AutoReconnect breaks" )
		public boolean autoReconnect = true;
		
		@Config.Name( "AutoReconnect Delay (ms)" )
		@Config.RangeInt( min = 1, max = 10000 )
		public int autoReconnectDelay = 100;
		
		@Config.Name( "Print Info" )
		public boolean printInfo = true;
		
		@Config.Name( "Minimum Player Count" )
		@Config.Comment( "Minimum player count required to autojoin" )
		public int minPlayerCount = 20;
		
		@Config.Name( "AutoJoin" )
		public boolean autoJoin = true;
		
		@Config.Name( "Splash Text Changer" )
		public boolean splashTextChanger = true;
		
		@Config.Name( "Refresh Before AutoJoining" )
		@Config.Comment( "Should I refresh 2b2t WITHOUT proxy before autojoining?" )
		public boolean autoJoinRefresh = true;
	}
	
	@Config.Name( "Main" )
	@Config.Comment( "Main" )
	public static final MainSettings main = new MainSettings( );
}

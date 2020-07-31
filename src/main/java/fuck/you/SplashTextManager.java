package fuck.you;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SplashTextManager
{
	private List< String > splashes = Arrays.asList(
				"The Prismatic on bottom", "The Prismatic < All", "The Prismatic < New Baikal",
				"rockeZZergon ez mad", "KPACABA POKE3", "ratabomb sucks",
				"Implexia has been backdoored more times than you may think",
				"rockpunk cool", "You have lost connection to the server",
				"why am i being kicked from 2b2t again", "$MONEY", "Income Detector Activated",
				"((( rockeZZergon whispers )))", "rockeZZergon bedtrap how",
				"vk.com/newbaikal", "moneymod.net - coming never",
				"vk.com/2b2torg", "vk.com/2b2trestarts", "t.me/restarts2b2t" );
	
	private Random random = new Random( );
	private boolean shouldgen = true;
	private String laststr = "";
	private int money = 0;
	
	public String generate( )
	{
		if( !shouldgen )
		{
			if( laststr.equals( "$MONEY" ) )
			{
				money++;
				return "PayPal balance - " + money + " USD Available";
			}
			
			return laststr;
		}
		
		shouldgen = false;
		
		String ret = "";
		
		try
		{
			ret = splashes.get( random.nextInt( splashes.size( ) ) );
			laststr = ret;
			
			if( ret.equals( "$MONEY" ) )
			{
				money = random.nextInt( 2100000000 );
				return "PayPal balance - " + money + " USD Available";
			}
			
			return ret;
		}
		catch( Exception e )
		{
			
		}
		
		ret = splashes.get( 0 );
		laststr = ret;
		
		return ret;
	}
	
	public void setShouldGenerate( boolean shouldgen )
	{
		this.money = 0;
		this.shouldgen = shouldgen;
	}
}

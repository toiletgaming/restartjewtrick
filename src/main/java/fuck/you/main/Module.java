package fuck.you.main;

public class Module
{
	private boolean state = false;
	
	public boolean getState( )
	{
		return state;
	}
	
	public void setState( boolean state )
	{
		this.state = state;
	}
	
	public boolean toggle( )
	{
		this.state = !this.state;
		return state;
	}
}

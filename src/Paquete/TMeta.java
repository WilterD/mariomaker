import java.awt.Color;
import java.awt.image.BufferedImage;
/**
 * Representa la meta que el jugador debe tocar si quiere ganar
 */
public class TMeta extends TLimiteHorizontal{

	private static final Sprite PREVIEW = new Sprite("Imagenes/sprites/tools/meta.png");
	private static final Color color = new Color(0,255,0,180);
	public TMeta(){
		super(color);
	}
	
	public BufferedImage preview(){
		return PREVIEW.getBuffer();
	}
	public void enContacto(Thing t){
		
	}
}
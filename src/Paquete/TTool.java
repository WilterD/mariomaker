import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Representa una herramienta utilizada en el editor de niveles.
 * Por defecto, tiene un ancho y una altura de 1 y se elimina justo después
 * que se coloca en el lobby (después de realizar la acción deseada, por supuesto).
 * Estos son visibles en el Editor de niveles pero son invisibles en UnJugador
 */
public class TTool extends Thing {

	private Sprite image;
	
	public TTool(){
		this(1,1);
	}
	
	public TTool(int width, int height){
		this(width,height,null);
	}
	
	public TTool(int width, int height, Color color){
		super(0,0,width,height);
		image = new Sprite(width,height);
		if(color != null){
			Graphics g = image.getBuffer().getGraphics();
			g.setColor(color);
			g.fillRect(0,0,width,height);
		}
	}
	public BufferedImage figureOutDrawImage(){
		return image.getBuffer();
	}
	
	public boolean activarGravedad(){
		return false;
	}
	
	public void think(){
		super.think();
		matar();
	}
	
}

import java.awt.image.BufferedImage;

/**
 * Quita objetos del juego
 */
public class TRemover extends TTool {
	private Sprite preview = new Sprite("Imagenes/sprites/tools/borrador.png");
	public TRemover(){
		super();
	}
	public void enContacto(Thing t){
		t.matar();
		matar();
	}
	public BufferedImage preview(){
		return preview.getBuffer();
	}
}

import java.awt.Color;
import java.awt.image.BufferedImage;


/**
 * Una estructura infinitamente alta y de 32 unidades de ancho impermeable a todos los objetos del juego.
 * Visto como naranja en el editor de niveles.
 */
public class TLimiteHorizontal extends TLimite {

	private static final Sprite PREVIEW = new Sprite("Imagenes/sprites/tools/hbound.png");

	public BufferedImage preview(){
		return PREVIEW.getBuffer();
	}
	
	public TLimiteHorizontal(){
		
	}
	public TLimiteHorizontal(Color color){
		super(color);
	}
	
	public boolean tocando(Thing t){

		if(t.muriendo())return false;
		double
			x1 = pos.x,
			x2 = t.pos.x;
		double
			w1 = width + x1,
			w2 = t.width + x2;
		
		if(
			((
				w1 >= x2 &&
				x1 <= w2
			)||(
				w2 >= x1 &&
				x2 <=  w1
			))
				
		){
			return true;
		}
		return false;
	}
	
	public byte fromWhere(Thing t){
		if(pos.x + width <= t.ultimaPos.x && t.vel.x < 0){
			return DESDE_DERECHA;
		}else if(pos.x >= t.ultimaPos.x + t.width && t.vel.x > 0){
			return DESDE_IZQUIERDA;
		}
		return DESDE_NINGUNO;
	}
	
	public int[] getDrawCoords(Heroe heroe){
		int[] c = super.getDrawCoords(heroe);
		c[1] = 0;
		c[3] = JGameMaker.screenHeight;
		return c;
	}
	

	public boolean inPlayerView(Heroe heroe){
		return JGameMaker.screenWidth-heroe.viewX()+pos.x+width > 0 && JGameMaker.screenWidth-heroe.viewX()+pos.x < JGameMaker.screenWidth;
	}
}

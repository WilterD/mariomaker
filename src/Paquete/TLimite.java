import java.awt.Color;

/**
 * Representa un límite que no se puede pasar por nada
 */
public abstract class TLimite extends TVisibleTool {

	private static final Color COLOR = new Color(255,140,0,180);
	public TLimite(){
		this(COLOR);
	}
	public TLimite(Color color){
		super(32,32,color);
	}
}

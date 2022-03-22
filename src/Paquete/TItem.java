
/**
 * Este es un elemento que se puede generar en el juego, se puede equipar a un jugador y se puede colocar dentro de TBloque.
 
 */
public abstract class TItem extends Thing{
	
	private boolean exited;
	
	public TItem(int width, int height){
		this(0,0,width,height);
	}
	
	/**
	 * Crea un nuevo TItem con un ancho de 32 y una altura de 32
	 */
	public TItem(){
		this(0,0,32,32);
	}
	
	public TItem(double x, double y, int width, int height){
		super(x,y,width,height);
		exited = false;
	}
	
	public void init(){
		super.init();
		if(!muriendo())
			vel.y = 1;
	}
	
	
	public void enContacto(Thing t){
		if(t instanceof Heroe){
			giveItem((Heroe)t);
			matar();
		}
	}
	/**
	 * llama, cuando un heroe toca esto
* verdad, héroe que tocó esto
	 */
	public abstract void giveItem(Heroe heroe);
	
	
	
	public boolean activarGravedad(){
		return exited;
	}
	
	/**
	 * llamado cuando esto sale de su TBloque padre
	 */
        
	public void onBlockExit(){
		exited = true;
		if(!muriendo()){
			vel.y = 0;
			vel.x = 0;
		}
	}
	
	
}
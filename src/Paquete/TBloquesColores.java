import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Representa una cosa cuadriculada que tiene color. Puede ser una pieza de esquina, una pieza lateral o una pieza central.
 *
 */
public class TBloquesColores extends TGridded {
	
	/**
	 * El tipo de TBloquesColores que representa PantallaSpawn
	 */
	public static byte direction = DESDE_ARRIBA + DESDE_IZQUIERDA;
	
	/**
	 * El índice del Color en la matriz de colores preestablecidos en la PantallaSpawn
	 */
	public static int colorIndex = 0;
	
	/**
	 * El colo del bloque en PantallaScreen
	 */
	public static Color menuColor = Color.WHITE;
	
	private static final Color[] COLORS = {
		Color.WHITE,
		Color.RED,
		Color.MAGENTA,
		Color.GREEN,
		Color.BLUE,
		Color.YELLOW,
		new Color(96,51,17),
		Color.CYAN,
		Color.GRAY,
		new Color(1,1,1),
	};
	
	private static Sprite[] CORNERS	= {
		new Sprite("Imagenes/sprites/coloredblock/TLcorner.png"),
		new Sprite("Imagenes/sprites/coloredblock/Tedge.png"),
		new Sprite("Imagenes/sprites/coloredblock/Ledge.png"),
		new Sprite("Imagenes/sprites/coloredblock/center.png"),
	};
	
	
	private Sprite[] corners;
	private byte dir;
	private Color color;
	
	/**
	 * Crea un nuevo TBloquesColores con el color y tipo del que se ve en la PantallaSpawn
	 */
	public TBloquesColores(){
		this(colorIndex, direction);
	}
	/**
	 * Crea un nuevo TBloquesColores con el color del índice especificado, pero con el tipo de que se prevé en la PantallaSpawn
	 * @param color indice del color en la matriz de preajustes a realizar
	 */
	public TBloquesColores(int color){
		this(color,direction);
	}
	
	/**
	 * Crea un nuevo TBloquesColores con el color del que se ve en la PantallaSpawn pero con el tipo especificado
	 * @param dir tipo de bloque
	 */
	public TBloquesColores(byte dir){
		this(colorIndex, dir);
	}
	/**
	 * Crea un nuevo TBloquesColores con el color del índice especificado y el tipo especificado
	 * @param index
	 * @param dir
	 */
	public TBloquesColores(int index, byte dir){
		this(COLORS[index], dir);
	}
	
	
	/**
	 * Crea un nuevo TBloquesColores con el color y el tipo especificados
	 * @param color
	 * @param dir
	 */
	public TBloquesColores(Color color, byte dir){
		super(0, 0);
		this.color = color;
		this.dir = dir;
	}
	
	/**
	 * Establece el color de este bloque al color especificado
	 * @param color
	 */
	public void setColor(Color color){
		Color[] find = {this.color};
		Color[] replace = {color};
		for(Sprite sprite: corners){
			sprite.replaceColors(find,replace);
		}
		this.color = color;
	}
	
	
	public void init(Serializer s){
		
		byte dir = direction;
		Color color = menuColor;
		
		menuColor = new Color(s.ints[super.numInts()],s.ints[super.numInts() + 1],s.ints[super.numInts() + 2]);
		direction = (byte)s.ints[super.numInts() + 3];
		this.color = menuColor;
		this.dir = direction;
		super.init(s);
		direction = dir;
		menuColor = color;
	}
	public Serializer serialize(){
		Serializer s = super.serialize();
		s.ints[super.numInts()] = color.getRed();
		s.ints[super.numInts() + 1] = color.getBlue();
		s.ints[super.numInts() + 2] = color.getGreen();
		s.ints[super.numInts() + 3] = dir;
		return s;
	}
	public int numInts(){return super.numInts() + 4;}
	
	public void init(Color color, byte dir){
		super.init();
		if(corners == null){
			corners = new Sprite[4];
		}
		corners[0] = new Sprite("Imagenes/sprites/coloredblock/TLcorner.png");
		corners[1] = new Sprite("Imagenes/sprites/coloredblock/Tedge.png");
		corners[2] = new Sprite("Imagenes/sprites/coloredblock/Ledge.png");
		corners[3] = new Sprite("Imagenes/sprites/coloredblock/center.png");
		if(color == null)return;
		this.color = Color.WHITE;
		this.dir = dir;
		//System.out.println("\t" + this.color + "\t" + color);
		setColor(color);
	}
	
	
	public void init(){
		init(menuColor, direction);
	}
	
	public BufferedImage preview(){
		return getBuffer(TBloquesColores.direction, TBloquesColores.CORNERS);
	}
	
	
	/**
	 * Cambia el tipo/dirección de este bloque al siguiente, llamado cuando se pulsa en la PantallaSpawn
	 */
	public static void cycleDirections(){
		switch(direction){
		//Esquinas
		case DESDE_IZQUIERDA + DESDE_ARRIBA:
			direction = DESDE_ARRIBA;
		break; case DESDE_IZQUIERDA + DESDE_ABAJO:
			direction = DESDE_ABAJO;
		break; case DESDE_DERECHA + DESDE_ARRIBA:
			direction = DESDE_IZQUIERDA;
		break; case DESDE_DERECHA + DESDE_ABAJO:
			direction = DESDE_IZQUIERDA + DESDE_ARRIBA;
		//Lados
		break; case DESDE_ARRIBA:
			direction = DESDE_ARRIBA + DESDE_DERECHA;
		break; case DESDE_ABAJO:
			direction = DESDE_ABAJO + DESDE_DERECHA;
		break; case DESDE_IZQUIERDA:
			direction = DESDE_NINGUNO;
		break; case DESDE_DERECHA:
			direction = DESDE_IZQUIERDA + DESDE_ABAJO;
		//Centro
		break;default:
			direction = DESDE_DERECHA;
		break; 
		}
	}
	
	
	/**
	 * Cambia el color del bloque al siguiente en la línea, llamado cuando se pulsa c en NivelEditor
	 */
	public static void cycleColors(){
		int old = colorIndex;
		colorIndex++;
		if(colorIndex == COLORS.length){
			colorIndex = 0;
		}
		menuColor = COLORS[colorIndex];
		Color[] find = {COLORS[old]};
		Color[] replace = {menuColor};
		for(Sprite sprite: CORNERS){
			sprite.replaceColors(find, replace);
		}
		
	}
	
	private BufferedImage getBuffer(byte dir, Sprite[] corners){
		switch(dir){
		//Esquinas
		case DESDE_IZQUIERDA + DESDE_ARRIBA:
			return corners[0].getBuffer();
		case DESDE_IZQUIERDA + DESDE_ABAJO:
			return corners[0].flipY();
		case DESDE_DERECHA + DESDE_ARRIBA:
			return corners[0].flipX();
		case DESDE_DERECHA + DESDE_ABAJO:
			return corners[0].flipXY();
		//Lados
		case DESDE_ARRIBA:
			return corners[1].getBuffer();
		case DESDE_ABAJO:
			return corners[1].flipY();
		case DESDE_IZQUIERDA:
			return corners[2].getBuffer();
		case DESDE_DERECHA:
			return corners[2].flipX();
		//Centro
		default:
			return corners[3].getBuffer();
		}
	}
	
	public BufferedImage figureOutDrawImage(){
		return getBuffer(dir,corners);
	}
	
}

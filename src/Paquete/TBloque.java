



import java.awt.*;
import java.awt.image.*;

public class TBloque extends TGridded{

	public static final int WIDTH = 32;
	private double oldY,oldX;
	private byte hit;
	private byte image;
	private TItem item;
	private boolean apareciendo;
	private boolean movCuandoGolpea;
	
	public static final String BLOQUE_PATH = "Imagenes/sprites/bloque/";
	
	private Sprite[] BLOQUE = {
			new Sprite(BLOQUE_PATH+"ladrillo.png"),
			new Sprite(BLOQUE_PATH+"b_activado.gif"),
			new Sprite(BLOQUE_PATH+"b_desactivado.gif"),
			new Sprite(BLOQUE_PATH+"piso.png"),
			new Sprite(BLOQUE_PATH+"pilar.png"),
			new Sprite(BLOQUE_PATH+"hongo/left.png"),
			new Sprite(BLOQUE_PATH+"hongo/mid.png"),
			new Sprite(BLOQUE_PATH+"hongo/right.png"),
			new Sprite(BLOQUE_PATH+"hongo/top.png"),
			new Sprite(BLOQUE_PATH+"hongo/bottom.png"),
	};
	
	public static final byte
		LADRILLO_MARRON = 0,
		LADRILLOS = 0,
		BLOQUE_PREGUNTA = 1,
		BLOQUE_PREGUNTA_DESACTIVADO = 2,
		SUELO = 3,
		PILAR = 4,
		HONGO_IZQUIERDO = 5,
		HONGO_MEDIO = 6,
		HONGO_DERECHO = 7,
		HONGO_ARRIBA = 8,
		HONGO_ABAJO = 9;

	private static final Color[] aboveGround = {
		new Color(200,76,12),
		new Color(252,188,176)
	};	
	private static final Color[] underGround = {
		new Color(0,128,135),
		new Color(149,252,240)
		
	};
	
	public TBloque(double x, double y, byte image){
		this(x,y,image,null,false);
	}
	
	public TBloque(double x, double y, byte image, TItem item){
		this(x,y,image,item,true);
	}
	
	public TBloque(byte image){
		this(0,0,image,null,false);
	}
	
	public TBloque(){
		this(PILAR);
	}
	/**
	 * Crea un TBloque con las coordenadas x e y especificadas, la imagen, el elemento contenido y si se mueve o no al ser golpeado.
	 
	 */
	public TBloque(double x, double y, byte image, TItem item, boolean movCuandoGolpea){
		super(x,y,WIDTH,WIDTH);
		this.movCuandoGolpea = movCuandoGolpea;
		init();
		apareciendo = false;
		hit = DESDE_NINGUNO;
		this.image = image;
		this.item = item;
        }
        
	public void makeSpriteUnderground(){
		for(Sprite s: BLOQUE){
			s.replaceColors(aboveGround,underGround);
		}
	}
	
	public void init(){
		super.init();
		oldX = pos.x;
		oldY = pos.y;
	}
	public void init(Serializer s){
		image = (byte)s.ints[super.numInts()];
		movCuandoGolpea = s.bools[super.numBools()];
		Class c = s.classes[super.numClasses()];
		if(c != null){
			try {
				item = (TItem)(c.newInstance());
			} catch (Exception e) {
				item = null;
				e.printStackTrace();
			}
		}
		super.init(s);
	}
	public Serializer serialize(){
		Serializer s = super.serialize();
		s.ints[super.numInts()] = image;
		s.bools[super.numBools()] = movCuandoGolpea;
		if(item != null){
			s.classes[super.numClasses()] = item.getClass();
		}
		return s;
	}
	public int numInts(){return super.numInts() + 1;}
	public int numBools(){return super.numBools() + 1;}
	public int numClasses(){return super.numClasses() + 1;}
	
	
	public BufferedImage preview(){
		return BLOQUE[image].getBuffer();
	}

	public void enContacto(Thing t){
		if(!t.isStatic() && movCuandoGolpea && vel.x == 0 && vel.y == 0 && hit == DESDE_NINGUNO && !apareciendo){
			
			//System.out.println(pos.x + " " + oldX);
			
			byte where = fromWhere(t);
			
			if(where == DESDE_ABAJO && t instanceof Heroe){
				hit(where);
				vel.y = 3;
			}else{
				if( (where == DESDE_IZQUIERDA || where == DESDE_DERECHA) && t.vRapido() ){
					hit(where);
					if(where == DESDE_IZQUIERDA){
						vel.x = 3;
					}else{
						vel.x = -3;
					}
				}
			}
		}
		super.enContacto(t);
	}
	/**
	 * Se llama cuando un jugador o un objeto del juego golpea el bloque (para apareciendo TItems)
	 * @param where
	 * Donde lo golpea
	 */
	public void hit(byte where){
		hit = where;
		//oldX = pos.x;
		//oldY = pos.y;
	}
	
	private void spawnItem(){
		addSpawn(item);
		item = null;
		apareciendo = false;
		movCuandoGolpea = false;
	}
	
	private void beginSpawn(){
		if(item == null)return;
		apareciendo = true;
		item.setPos(pos);
		image = BLOQUE_PREGUNTA_DESACTIVADO;
	}
	
	
	public void think(){
		super.think();
		
		if(apareciendo){
			item.think();
			//System.out.println(item.pos.y + " > " + pos.y + " + " + height);
			if(item.pos.y > pos.y + height){
				item.onBlockExit();
				spawnItem();
			}
		}
		
		if(pos.y > oldY + WIDTH/2 && hit == DESDE_ABAJO && vel.y > 0){
			vel.y = -3;
		}else if(pos.y < oldY && hit == DESDE_ABAJO && vel.y < 0){
			vel.y = 0;
			hit = DESDE_NINGUNO;
			pos.y = oldY;
			beginSpawn();
		}else if(pos.x > oldX + WIDTH/2 && hit == DESDE_IZQUIERDA && vel.x > 0){
			vel.x = -3;
		}else if(pos.x < oldX && hit == DESDE_IZQUIERDA && vel.x < 0){
			vel.x = 0;
			hit = DESDE_NINGUNO;
			pos.x = oldX;
			beginSpawn();
		}else if(pos.x < oldX - WIDTH/2 && hit == DESDE_DERECHA && vel.x < 0){
			vel.x = 3;
		}else if(pos.x > oldX && hit == DESDE_DERECHA && vel.x > 0){
			vel.x = 0;
			hit = DESDE_NINGUNO;
			pos.x = oldX;
			beginSpawn();
		}
	}
	
	public boolean isStatic(){
		return !movCuandoGolpea;
	}
	
	/**
	 * determina si esto puede contener un nuevo TItem
	 * @return verdadero si puede contener un nuevo TItem, falso si no
	 */
	public boolean canAcceptItem(){
		return item == null && (movCuandoGolpea || image == TBloque.BLOQUE_PREGUNTA_DESACTIVADO);
	}
	
	/**
	 * hace que esto contenga un nuevo Item
	 * @param item EL Item a contener
	 */
	public void addItem(TItem item){
		if(image == BLOQUE_PREGUNTA_DESACTIVADO){
			image = BLOQUE_PREGUNTA;
			movCuandoGolpea = true;
		}
		this.item = item;
	}	
	
	public BufferedImage figureOutDrawImage(){
		return BLOQUE[image].getBuffer();
	}
	
	public void draw(Graphics g, ImageObserver o, Heroe heroe){
		if(inPlayerView(heroe) && apareciendo)
			item.draw(g,o,heroe);
		super.draw(g,o,heroe);
	}

}
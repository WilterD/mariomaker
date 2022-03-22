import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
/*
 * Esto representa un objeto que se puede poner en el juego.
 */
public class Thing{

	public static final byte DESDE_NINGUNO = 1;

	public static final byte DESDE_ARRIBA = 2;

	public static final byte DESDE_ABAJO = 4;

	public static final byte DESDE_IZQUIERDA = 8;

	public static final byte DESDE_DERECHA = 16;
	
	public static final byte DE_TODASPARTES = DESDE_ARRIBA + DESDE_ABAJO + DESDE_IZQUIERDA + DESDE_DERECHA;
	
	public static final byte DE_LADO = DESDE_IZQUIERDA + DESDE_DERECHA;
	
	private Thing spawn = null;
	
	/*
	 * posicion
	 */
	public Point2D.Double pos;
	/*
	 * ultima posicion
	 */
	public Point2D.Double ultimaPos;
	/*
	 * velocidad
	 */
	public Point2D.Double vel;
	/*
	 * acceleracion
	 */
	public Point2D.Double acc;
	/**
	 * Si esta cayendo a su muerte a causa de un agujero en el suelo
	 */
	public boolean cayendo;
	
	private Sprite sprite;
	
	
	public int width,height;
	private boolean asesinado,muriendo;
	
	/**
	 * retorna una matriz de valores que se utiliza para determinar dónde se debe dibujar esto en la pantalla
	 * @param heroe
	 * @return {x coordenada, y coordenada, ancho, alto}
	 */
	public int[] getDrawCoords(Heroe heroe){
		int[] r = new int[4];
		r[0] = JGameMaker.scaleW(JGameMaker.screenWidth-(heroe.viewX()-pos.x));
		r[1] = JGameMaker.scaleH(heroe.viewY()-pos.y-height);
		r[2] = JGameMaker.scaleW(width);
		r[3] = JGameMaker.scaleH(height);
		return r;
	}
	/**
	 * retorna la imagen del juego
	 */
	public BufferedImage figureOutDrawImage(){
		return sprite.getBuffer();
	}
	/**
	 * Dibuja esto a g
	 * @param g
	 * @param o
	 * @param heroe
	 */
	public void draw(Graphics g, ImageObserver o, Heroe heroe){
		if(inPlayerView(heroe)){
			g.setColor(Color.WHITE);
			int[] c = getDrawCoords(heroe);
			
	
			g.drawImage(figureOutDrawImage(),c[0],c[1],c[2],c[3], o);
		}
	}
	/**
	 * determina si esto está o no en la vista del Héroe especificado
	 * @param heroe
	 * @return verdadero si esto se puede ver en la pantalla, falso si no
	 */
	public boolean inPlayerView(Heroe heroe){
		return JGameMaker.screenWidth-heroe.viewX()+pos.x+width > 0 && JGameMaker.screenWidth-heroe.viewX()+pos.x < JGameMaker.screenWidth
		&& heroe.viewY()-pos.y > 0 && heroe.viewY()-pos.y-height < JGameMaker.screenHeight;
	}
	/**
	 * Crea una nueva Thing con coordenadas (x,y) y el ancho y alto especificado
	 * @param x coordenada x
	 * @param y coordenada y
	 * @param width ancho
	 * @param height alto
	 */
	public Thing(double x, double y, int width, int height){
		pos = new Point2D.Double(x,y);
		sprite = new Sprite(width,height);
		ultimaPos = new Point2D.Double(x,y);
		this.width=width;
		this.height=height;
		asesinado = false;
		cayendo = false;
		init();
	}
	/**
	 * crea una nueva Thing con coordenadas (0,0), ancho de 1 y alto de 1
	 */
	public Thing(){
		this(0,0,1,1);
	}
	/**
	 * convierte esto en un serializador para que pueda escribirse en un archivo
	 */
	public Serializer serialize(){
		Serializer s = new Serializer(this.getClass());
		
		s.ints = new int[numInts()];
		s.doubles = new double[numDoubles()];
		s.bools = new boolean[numBools()];
		s.classes = new Class[numClasses()];
		
		s.ints[0] = width;
		s.ints[1] = height;
		
		s.doubles[0] = pos.x;
		s.doubles[1] = pos.y;
		
		return s;
	}
	/**
	 * retorna número de enteros que debe contener esta representación del serializador
	 */
	public int numInts(){return 2;}
	/**
	 * retorna número de Reales que debe contener esta representación del serializador
	 */
	public int numDoubles(){return 2;}
	/**
	 * retorna número de booleanos que debe contener esta representación del serializador
	 */
	public int numBools(){return 0;}
	/**
	 * retorna número de clases que debe contener esta representación del serializador
	 */
	public int numClasses(){return 0;}
	
	/**
	 * inicializa los campos en función de la configuración de s
	 * @param s
	 */
	public void init(Serializer s){
		setPos(s.doubles[0],s.doubles[1]);
		width = s.ints[0];
		height = s.ints[1];
		init();
	}
	/**
	 * llamado cuando se agrega a la sala y al menú de generación, inicializa los campos
	 */
	public void init(){
		if(vel == null)
			vel = new Point2D.Double();
		if(acc == null)
			acc = new Point2D.Double();
		updatePosLast();
	}
	/**
	 * agrega t a la cola de generación
	 * @param t
	 */
	public void addSpawn(Thing t){
		spawn = t;
	}
	/**
	 * elimina el elemento de la cola de generación de esto y lo devuelve
	 * @retorna una Thing que se debe agregar a la habitación. Si no hay ninguno, nulo
	 */
	public Thing getSpawn(){
		Thing temp = spawn;
		spawn = null;
		return temp;
	}
	/**
	 * escribe la última posición en la memoria
	 */
	public void updatePosLast(){
		ultimaPos.setLocation(pos);
	}
	/**
	 * retorna verdadero si este se mueve por el aire como resultado de matar (real x, real y), falso si no
	 */
	public boolean muriendo(){
		
		return muriendo;
	}
	/**
	 * determina si t esta tocando esto
	 * @param t
	 * @return verdadero si esto esta tocando t
	 */
	public boolean tocando(Thing t){
		if(t instanceof TLimite){
			return t.tocando(this);
		}
		if(muriendo() || t.muriendo())return false;
		double
			x1 = pos.x,
			y1 = pos.y,
			x2 = t.pos.x,
			y2 = t.pos.y;
		double
			w1 = width + x1,
			h1 = height + y1,
			w2 = t.width + x2,
			h2 = t.height + y2;
		
		if(
			((
				w1 >= x2 &&
				h1 >= y2 + 5 &&
				x1 <= w2 &&
				y1 <= h2
			)||(
				w2 >= x1 &&
				h2 >= y1 &&
				x2 <=  w1 &&
				y2 <= h1
			))
				
		){
			
			return true;
		}

		//tuberia (para velocidades de cuadro bajas)
		return false;
	}
	/**
	 * cambia la imagen en el juego de este Thing al tema subterraneo si lo tiene
	 */
	public void makeSpriteUnderground(){
		
	}
	/**
	 * llamado cuando este Thing toca a otro Thing
	 * @param t el otro Thing
	 */
	public void enContacto(Thing t){
		if(!t.isStatic()){
			
			byte where = fromWhere(t);
			
			if( where == DESDE_ARRIBA ){
				t.setPos(t.pos.x,pos.y + height);
				if(t.vel.y < 0)
					t.bumpY();
				if(t.acc.y < 0)
					t.acc.y = 0;
			}else if( where == DESDE_ABAJO ){
				t.setPos(t.pos.x,pos.y - t.height);
				if(t.vel.y > 0)
					t.bumpY();
				if(t.acc.y > 0)
					t.acc.y = 0;
			}else if( where == DESDE_IZQUIERDA ){
				t.setPos(pos.x - t.width,t.pos.y);
				t.bumpX();
				if(t.acc.x > 0)
					t.acc.y = 0;
			}else if( where == DESDE_DERECHA ){
				t.setPos(pos.x + width,t.pos.y);
				t.bumpX();
				if(t.acc.x < 0)
					t.acc.x = 0;
			}
		}
	}
	
	/**
	 *determina donde un Thing vino de cuando ocurre una colisión
	 * @param t el otro Thing
	 * @retorna la dirección de donde vino t, si no se puede determinar,
           retorna Thing.DESDE_NINGUNO
	 */
	public byte fromWhere(Thing t){
		

		if( t.ultimaPos.y + 2 >= pos.y + height && t.vel.y <= 0 ){
			return DESDE_ARRIBA;
		}else if( t.ultimaPos.y + t.height <= pos.y && t.vel.y > 0 ){
			return DESDE_ABAJO;
		}else if( t.ultimaPos.x + t.width <= pos.x && t.vel.x > 0 ){
			return DESDE_IZQUIERDA;
		}else if( t.ultimaPos.x >= pos.x + width && t.vel.x < 0 ){
			return DESDE_DERECHA;
		}
		
		
		if( t.pos.y >= ultimaPos.y + height && vel.y > 0 ){
			return DESDE_ARRIBA;
		}else if( t.pos.y + t.height <= ultimaPos.y && vel.y < 0 ){
			return DESDE_ABAJO;
		}else if( t.pos.x + t.width <= ultimaPos.x && vel.x < 0 ){
			return DESDE_IZQUIERDA;
		}else if( t.pos.x >= ultimaPos.x + width && vel.x > 0 ){
			return DESDE_DERECHA;
		}
		

		if( t.pos.y <= pos.y + height && t.pos.y+10 >= pos.y && vel.y > t.vel.y && vel.y != 0 && t.vel.y != 0 ){
			return DESDE_ARRIBA;
		}else if( t.pos.y + t.height >= pos.y && t.pos.y + t.height <= pos.y + height && vel.y <  t.vel.y && vel.y != 0 && t.vel.y != 0 ){
			return DESDE_ABAJO;
		}else if( t.pos.x + t.width >= pos.x && t.pos.x <= pos.x && vel.x != 0 && t.vel.x != 0 ){
			return DESDE_IZQUIERDA;
		}else if( t.pos.x <= pos.x + width && t.pos.x >= pos.x && vel.x != 0 && t.vel.x != 0 ){
			return DESDE_DERECHA;
		}
		
		return DESDE_NINGUNO;
	}
	
	public void matar(){
		asesinado = true;
	}
	
	public void revive(){
		asesinado = false;
	}
	/**
	 * establece la velocidad del objeto en un valor, una vez que está fuera de la pantalla, se retira de la habitación
	 * @param vel la velocidad con la que se moverá
	 */
	public void matar(Point2D.Double vel){
		muriendo = true;
		this.vel.setLocation(vel);
	}
	/**
	 * enlaza este Thing a otro (tuberias, etc.)
	 * @param other Thing a enlazar
	 */
	public void enlace(Thing other){
		
	}
	/**
	 * determina si esta Thing puede enlazarse o no con otras
	 * @param other la Thing contra la que se comprueba
	 * @retorna verdadero si puede, falso si no puede
	 */
	public boolean colocarEnlace(Thing other){
		return false;
	}
	/**
	 * retorna imagen que se va a mostrar en el menú de generación
	 */
	public BufferedImage preview(){
		return null;
	}
	/**
	 * retorna si esto debe eliminarse o no de su habitación principal
	 */
	public boolean asesinado(){
		return asesinado;
	}
	
	/**
	 * llama a cada cuadro, actualiza la velocidad, la posición, etc.
	 */
	public void think(){
		updatePosLast();
		pos.setLocation(pos.x+vel.x*JGameMaker.time(), pos.y+vel.y*JGameMaker.time());
		vel.setLocation(vel.x + acc.x*JGameMaker.time(), vel.y + acc.y*JGameMaker.time());
		if(activarGravedad()){
			if(pos.y+JGameMaker.NIVEL_SUELO < 0){
				matar();
				cayendo = false;
			}else if(pos.y > 0 || muriendo || cayendo){
				acc.y = -JGameMaker.GRAVEDAD;
			}else if(activarGravedad()){
				pos.y = 0;
				bumpY();
			}
		}
	}
	/**
	 * establece la posición actual
	 */
	public void setPos(double x, double y){
		pos.setLocation(x,y);
	}
	/**
	 * establece la posición actual
	 */
	public void setPos(Point2D.Double pos){
		setPos(pos.x,pos.y);
	}
	/**
	 * llamado cuando esta Thing choca con otra Thing, cambia la velocidad X
	 */
	public void bumpX(){
		vel.x *= -1;
	}
	/**
	 * llamado cuando esta Thing choca con otra Thing, cambia la velocidad Y
	 */
	public void bumpY(){//si esta Thing choca contra algo, esto es por lo que se multiplica el vector
		vel.y = 0;
	}
	/**
	 * determina si esto tiene la capacidad de moverse
	 * @return verdadero si esta Thing se mueve, falso si no lo hace
	 */
	public boolean isStatic(){
		return false;
	}
	/**
	 * determina si esto es lo suficientemente rápido para matar TEnemigo
	 * @return verdadero si esta Thing se mueve lo suficientemente rápido 
         * para matar TEnemigo, falso si no lo hace
	 */
	public boolean vRapido(){
		return false;
	}
	
	/**
	 * establece la posición cuando está en el menú de generación
	 * @param x coordenada del mouse
	 * @param y coordenada del mouse
	 */
	public void setSpawnPos(double x, double y){
		setPos(x,y - height);
	}
	/**
	 * determina si esto debe ser afectado por la gravedad
	 * @retorna verdadero si la gravedad debe afectar esta Thing, falso si no debe
	 */
	public boolean activarGravedad(){return false;}
	
}
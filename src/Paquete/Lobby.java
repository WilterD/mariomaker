import java.util.Vector;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;

/**
 * Esto representa un vestíbulo que contiene todas las Cosas que están en el juego. Esto maneja todas las interacciones entre cada Cosa y también el Héroe, si lo hay.
 *
 */
public class Lobby {
	
	private Vector<Thing> things;
	private Fondo fondo;
	
	private int index;
	private Class removerSpawn;
	
	private int numRemover;
	
	/**
	 * crea un nuevo Lobby
* verdadero si el tema es underground, falso si no
* el índice en la matriz del GameScreen principal
	 */
	public Lobby(boolean underground, int index){
		this.index = index;
		fondo = new Fondo(underground);
		things = new Vector<Thing>();
		numRemover = 0;
		removerSpawn = null;
	}
	/**
	 * dibuja esto en la pantalla. por defecto no maneja ninguna PantallaSpawn
	 
	 */
	public void draw(Graphics g, ImageObserver o, Heroe heroe){
		draw(g,o,heroe,null);
	}

	/**
	 * dibuja esto en la pantalla y maneja el PantallaSpawn si no es nulo
	 
	 */
	public void draw(Graphics g, ImageObserver o, Heroe heroe, PantallaSpawn spawn){
		boolean shouldDrawHero = true;
		if(heroe == null){
			heroe = new Heroe();
			shouldDrawHero = false;
		}
		fondo.draw(g, o, heroe);
		if(heroe.tuberiando() && shouldDrawHero){
			heroe.draw(g, o);
		}
		boolean alreadyHighlighted = spawn == null;
		for(int i = 0; i < things.size(); i++){
			Thing t = things.get(i);
			//if(t == null)continue;
			if(!(spawn == null && t instanceof TTool))
				t.draw(g, o, heroe);
			if(!alreadyHighlighted){
				Color color = spawn.highlightColor(t);
				if(color != null){
					g.setColor(color);
					int[] c = t.getDrawCoords(heroe);
					g.fillRect(c[0],c[1],c[2],c[3]);
					alreadyHighlighted = true;
				}
			}
		}
		if(!heroe.tuberiando() && shouldDrawHero){
			heroe.draw(g, o);
		}
	}
	/**
	 * convierte esto en algo que se puede escribir en un archivo
          retorna el objeto serializable que se convertirá en un archivo
	 */
	public Vector<Serializer> serialize(){
		Vector<Serializer> serializers = new Vector<Serializer>();
		for(Thing t: things){
			serializers.add(t.serialize());
		}
		return serializers;
	}
	/**
	 * llamado cada frame
*  verdadero si en NivelEditor
	 */
	public void think(Heroe heroe, boolean modoEditar){
		think(heroe,modoEditar,false);
	}
	/**
	 * llamado a cada frame
	 * @param heroe
	 * @param modoEditar verdadero en modo editor
	 * @param debeCongelar si es cierto, entonces ninguna de las Cosas que están contenidas dentro de esto pensará, cargara
	 */
	public void think(Heroe heroe, boolean modoEditar,boolean debeCongelar){
		if(heroe != null)
			heroe.think();
		for( int i = 0; i < things.size(); i++ ){
			Thing t = things.get(i);
			if(t == null) continue;
			if(t.asesinado()){
				remove(t);
				i--;
				continue;
			}
			
			if( heroe != null && !heroe.muriendo() && !(debeCongelar && t instanceof TEnemigo) && t.tocando(heroe) && heroe.tocando(t) ){
				t.enContacto(heroe);
				heroe.enContacto(t);
			}
			
			for( int j = i + 1; j < things.size(); j++ ){
				Thing t2 = things.get(j);
				if(t2 == null || t == t2 || t.isStatic() && t2.isStatic())continue;
				if( t2.tocando(t) && t.tocando(t2)){
					t.enContacto(t2);
					t2.enContacto(t);
					
				}
			}
			if(!debeCongelar && !t.isStatic() && (modoEditar || !(t instanceof TEnemigo) || t.inPlayerView(heroe)))
				t.think();
			Thing add = t.getSpawn();
			if(add != null)
				add(add);

		}
		if(heroe != null){
			Thing add = heroe.getSpawn();
			if(add != null)
				add(add);
		}
		
	}
	
	/**
	 *configura el spawn para el jugador
	 */
	public boolean setSpawn(Heroe heroe){
		for(Thing t: things){
			if(t instanceof TSpawn){
				heroe.setPos(t.pos.x,t.pos.y);
				return true;
			}
		}
		return false;
	}
	/**
	 * agregas cosas a esto
	 * @param add
	 */
	public void add(Thing add){
		add(add,true);
	}
	/**
	 * agrega cosas a esto
* add, Cosa que se agregará
* shouldInit si es falso, entonces no se llama a add.init()
	 */
	public void add(Thing add, boolean shouldInit){
		if(shouldInit)
			add.init();
		if(fondo.isUnderground()){
			add.makeSpriteUnderground();
		}
		if(add instanceof TGridded){
			TGridded addgrid = (TGridded)add;
			for(Thing t: things){
				if(!(t instanceof TGridded)) continue;
				TGridded tgrid = (TGridded)t;
				addgrid.addAdyacente(tgrid);
				tgrid.addAdyacente((TGridded)add);
			}
		}
		if(add instanceof TItem){
			TItem item = (TItem)add;
			for(Thing t: things){
				if(t instanceof TBloque && ((TBloque)t).canAcceptItem() && t.tocando(add)){
					((TBloque)t).addItem(item);
					return;
				}
			}
			item.onBlockExit();
		}
		if(add instanceof TTuberia){
			((TTuberia)add).lobby = index;
		}
		if(add instanceof TSpawn || add instanceof TMeta){
			removerSpawnOtrosLobbys(add.getClass());
		}
		if(add instanceof AgujeroTierra){
			things.add(0,add);
		}else{
			things.add(add);
		}
	}
	/**
	 * hace que todos los demás Lobby contenidos en el GameScreen principal eliminen su spawn
         * si contiene alguna, pensando en multijugador
	 */
	public void removerSpawnOtrosLobbys(Class spawn){
		removerSpawn = spawn;
		removerSpawns(spawn);
	}
	/**
	 * determina si el spawn debe ser eliminado
	 */
	public Class debeRemoverSpawnOtrosLobbys(){
		Class temp = removerSpawn;
		removerSpawn = null;
		return temp;
	}
	/**
	 * elimina todos los hijos / objetivos, especificados por la clase
* puede ser un TMeta o un TSpawn, esta instancia debe eliminarse
	 */
	public void removerSpawns(Class spawn){
		for(Thing t: things){
			if(t.getClass().equals(spawn)){
				remove(t);
				break;
			}
		}
	}
	/**
	 * remover elementos, instancias
	 */
	public void remove(Thing remove){
		if(remove instanceof TGridded){
			TGridded removegrid = (TGridded)remove;
			for(Thing t: things){
				if(!(t instanceof TGridded)) continue;
				TGridded tgrid = (TGridded)t;
				tgrid.removerAdyacente(removegrid);
			}
		}
		things.remove(remove);
	}
	
}

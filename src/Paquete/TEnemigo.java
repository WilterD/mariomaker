import java.awt.geom.Point2D;

/**
 * Enemigo en el juego. Tiene la capacidad de matar a un jugador  .
 *
 */
public abstract class TEnemigo extends Thing {
	
	/**
	 * crea un nuevo  TEnemigo con las coordenadas (x,y) y la anchura y altura especificadas.
	 */
	public TEnemigo(double x, double y, int width, int height){
		super(x,y,width,height);
	}
	/**
	 * Crea un nuevo TEnemigo con coordenadas de anchura (0,0), anchura de 1 y altura de 1.
	 */
	public TEnemigo(){
		this(0,0,1,1);
	}
	/**
	 * determina la(s) dirección(es) en la(s) que, al ser tocada, matará un jugador.
	 * @return la(s) dirección(es) en la(s) que, al ser tocada, matará a un jugador.
	 */
	public byte direccionMuerte(){
		return DE_TODASPARTES;
	}
	/**
	 * Se llama cuando éste es golpeado por un bloque desde abajo. Por defecto, es asesinado.
	 * @param block el bloque que lo golpeó
	 */
	public void golpeBloque(Thing block){
		matar(new Point2D.Double((pos.x - block.pos.x)/10, Math.random()*3 + 6));
	}
	/**
	 * se llama cuando un héroe toca esto desde una dirección no letal, como indica direccionMuerte(). Por defecto, esto es asesinado
	 * @param heroe
	 */
	public void heroeContacto(Heroe heroe){
		matar();
	}
	/**
	 * se llama cuando este toca una entidad no jugadora y no bloqueada
	 * @param t
	 */
	public void thingTouch(Thing t){
		
	}
	/**
	 * se llama cuando un héroe toca esto desde una dirección letal, como indica direccionMuerte(). Por defecto, el héroe es asesinado.
	 * @param heroe
	 */
	public void heroeMatar(Heroe heroe){
		heroe.matar();
	}
	/**
	 * le da al jugador la habilidad de un salto extra, normalmente llamado cuando este es golpeado desde arriba
	 * @param heroe
	 */
	public void pisar(Heroe heroe){
		heroe.setPos(heroe.pos.x, pos.y+height + 1);
		if(heroe.saltoAbajo()){
			heroe.vel.y = 0;
			heroe.saltar(true,false);
		}else{
			heroe.vel.y = 7;
		}
	}
	
	public void enContacto(Thing t){
		if(muriendo())return;
		byte where = fromWhere(t);
		if(t instanceof TBloque && where == DESDE_ABAJO && t.vel.y > 1){
			golpeBloque(t);
		}else if(t instanceof Heroe){
			Heroe heroe = (Heroe)t;
			if((where & direccionMuerte()) > 0){
				heroeMatar(heroe);
			}else if(!heroe.vModoEstrella()){
				heroeContacto(heroe);
			}
		}
		thingTouch(t);
	}
	
	public boolean isStatic(){
		return false;
	}
	
}

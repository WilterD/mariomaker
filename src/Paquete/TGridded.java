

import java.awt.*;
import java.awt.geom.Point2D;
public class TGridded extends Thing{
	
	private Point gridPos;
	private byte adyacentes;
	
	public TGridded(){
		this(0,0);
	}
	
	public TGridded(double x, double y){
		this(x,y,32,32);
	}
	
	public TGridded(double x, double y, int width, int height){
		super(x,y,width,height);
		gridPos = new Point(getGridCoord(x),getGridCoord(y));
		adyacentes = Thing.DESDE_NINGUNO;
		this.width = getGridCoord(width)*32;
		this.height = getGridCoord(height)*32;
		setPos(x,y);
	}
	
	public void setPos(double x, double y){
		gridPos.x = getGridCoord(x);
		gridPos.y = getGridCoord(y);
		pos.x = gridPos.x*32;
		pos.y = gridPos.y*32;
	}
	
	public void setSpawnPos(double x, double y){
		super.setSpawnPos(x - 16, y + 16);
	}
	
	
	/**
	 * establece la representación cuadriculada de la posición actual en (x,y)
	 
	 */
	public void setGridPos(int x, int y){
		gridPos.setLocation(x,y);
		pos.setLocation(x*32,y*32);
	}
	
	/**
	 * determina la representación en cuadrícula de la posición actual
	 * @return la representación cuadriculada de la posición actual
	 */
	public Point getGridPos(){
		return new Point(gridPos);
	}
	
	/**
	 * determina si tiene una Cosa cuadriculada adyacente en la dirección especificada
	 * @param direction dirección para chequear
	 * @return verdadero si hay una Cosa cuadriculada en la dirección especificada, falso si no
	 */
	public boolean hasAdjacent(byte direction){
		if(direction == DESDE_NINGUNO)return false;
		return (adyacentes & direction) != 0;
	}

	/**
	 * añadir otro a la lista de bloques adyacentes
	 * @param other
	 */
	public void addAdyacente(TGridded other){
		byte direction = getDirection(other);
		if(direction == DESDE_NINGUNO)return;
		if(!hasAdjacent(direction)){
			adyacentes += direction;
		}
	}
	
	/**
	 * eliminar otro de la lista de bloques adyacentes
	 * @param other
	 */
	public void removerAdyacente(TGridded other){
		byte direction = getDirection(other);
		if(direction == DESDE_NINGUNO)return;
		if(hasAdjacent(direction)){
			adyacentes -= direction;
		}
	}
	
	public boolean tocando(Thing t){
		boolean tocando = super.tocando(t);
		if(!tocando){
			return false;
		}else{
			byte from = fromWhere(t);
			if(hasAdjacent(from))
				return false;
			return true;
		}
	}
	
	public boolean isStatic(){
		return adyacentes != 0;
	}
	
	/**
	 * determina la representación cuadriculada de ésta y la coloca en un nuevo Rectángulo
	 * @return el nuevo Rectangulo
	 */
	public Rectangle representation(){
		return new Rectangle(gridPos.x,gridPos.y,width/32,height/32);
	}
	
	/**
	 * usado para desviar el método TGridded de tocando, llama a Thing.touch()
	 */
	public boolean supertouching(Thing t){
		return super.tocando(t);
	}
	
	/**
	 * determina la dirección de othergrid en relación con este
	 * @param othergrid el otro objeto cuadriculado
	 * @return la dirección de othergrid es relativa a este
	 */
	public byte getDirection(TGridded othergrid){
		Rectangle other = othergrid.representation();
		if(othergrid instanceof TBGBloque) return DESDE_NINGUNO;
		int x = gridPos.x;
		int y = gridPos.y;
		
		//horizontakmente al lado de cada uno
		Point	up = new Point(x, y + 1),
				down = new Point(x,y-1),
				left = new Point(x - 1, y),
				right = new Point(x + 1, y);
		if(other.contains(up))
			return DESDE_ARRIBA;
		if(other.contains(down))
			return DESDE_ABAJO;
		if(other.contains(left))
			return DESDE_IZQUIERDA;
		if(other.contains(right))
			return DESDE_DERECHA;
		return DESDE_NINGUNO;
	}

	/**
	 * determina la coordenada de la cuadrícula a partir de una coordenada mundial
	 * @param x la coordenada del mundo
	 * @return la coordenada de cuadrícula determinada
	 */
	public static int getGridCoord(double x){
		if(x > 0)
			return (int)(x  + 16)/32;
		else
			return (int)(x -16)/32;
	}
	
}
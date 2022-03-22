import java.io.*;
import java.util.zip.*;

/**
 * Clase auxiliar utilizada para escribir niveles en archivos.<br/>
 * <br/>
 * Las cuatro matrices almacenadas en esto son propiedades de Thing. Se configuran mediante Thing.serialize().
 *
 */
public class Serializer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7925582856184491464L;
	private Class instancia;
	/**
	 * ints para ser escrito en el archivo
	 */
	public int[] ints;
	/**
	 * dobles para ser escritos en el archivo
	 */
	public double[] doubles;
	/**
	 * booleanos que se escribirán en el archivo
	 */
	public boolean[] bools;
	/**
	 *clases que se escribirán en el archivo
	 */
	public Class[] classes;
	
	/**
	 * 
	 * instancia la clase del objeto que debe representar este serializador
	 */
	public Serializer(Class instancia){
		this.instancia = instancia;
		ints = null;
		doubles = null;
		bools = null;
		classes = null;
	}
	
	public Class getInstance(){
		return instancia;
	}
	
	/**
	 * escribe s a f (usando el algoritmo de compresión GZIP)
	 
	 */
	public static boolean toFile(File f, Serializable s){
		if(f == null)return false;
		FileOutputStream fout;
		GZIPOutputStream zout;
		try {
			fout = new FileOutputStream(f);
			zout = new GZIPOutputStream(new BufferedOutputStream(fout));
			ObjectOutputStream oout = new ObjectOutputStream(zout);
			oout.writeObject(s);
			oout.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Serializable fromFile(File f) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		if(f == null)return null;
		FileInputStream fin = new FileInputStream(f);
		GZIPInputStream zin = new GZIPInputStream(new BufferedInputStream(fin));
		ObjectInputStream oin = new ObjectInputStream(zin);

	    return (Serializable)oin.readObject();
	}
}

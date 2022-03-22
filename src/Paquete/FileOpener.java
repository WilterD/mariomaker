import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Se utiliza para abrir, solicitar abrir, guardar, solicitar guardar y leer archivos.
 *
 */
public class FileOpener{
	private JFileChooser chooser;
	private Component component;
	private String ext;
	
	private BufferedReader reader;
	
	/**
	 * Crea un nuevo FileOpener con el componente principal especificado
            componente que se utilizará al mostrar el JFileChooser
	 */
	public FileOpener(Component component){
		chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(new CustomFilter("jgamemaker", "JGameMaker Nivel"));
		this.component = component;
		ext = "jgamemaker";
		
	}
	
	/**
	 * Pide al usuario que guarde un archivo abriendo un JFileChooser
* @return el archivo que se elige, nulo si no se elige ninguno
	 */
	public File saveFile(){
		if(chooser == null || component == null)return null;
		File file;
		int returnVal = chooser.showSaveDialog(chooser);
		if(returnVal != JFileChooser.APPROVE_OPTION) return null;
		file = chooser.getSelectedFile();
		String name = file.getPath();
		String ext = '.' + this.ext;
		if(!name.substring(name.length()-ext.length(), name.length()).equals(ext)){
			name += ext;
			file = new File(name);
		}
		return file;
	}
	
	/**
	 * Pide al usuario que abra un archivo abriendo un JFileChooser
* @return el archivo que se elige, nulo si no se elige ninguno
	 */
	public File openFile(){
		if(chooser == null || component == null)return null;
		File file;
		int returnVal = chooser.showOpenDialog(component);
		if(returnVal != JFileChooser.APPROVE_OPTION) return null;
		file = chooser.getSelectedFile();
		try{
			file.createNewFile();
			return file;
		}catch(Exception ex){
			System.err.println("Error al cargar juego");
			return null;
		}
	}
	/**
	 *Abre un nuevo FileReader para leer el archivo especificado por la ruta
* @param ruta ruta al archivo
* @return verdadero si se puede leer, falso si no
	 */
	public boolean readFile(String path){
		return readFile(new File(path));
	}
	/**
	 * Abre un nuevo FileReader para leer el archivo especificado
* @param archivo Archivo para leer
* @return verdadero si se puede leer, falso si no
	 */
	public boolean readFile(File file){
		try{
			reader = new BufferedReader(new FileReader(file));
			return true;
		}catch(FileNotFoundException e){
			reader = null;
			return false;
		}

	}
	
	/**
	 * obtiene la siguiente línea del FileReader después de llamar a readFile
* retorna la siguiente línea si se puede leer, nulo si no
	 */
	public String readLine(){
		if(reader == null)return null;
		String str;
		try {
			str = reader.readLine();
		} catch (IOException e) {
			return null;
		}
		return str;
	}
	
	private class CustomFilter extends FileFilter{
		
		private String ext, description;
		
		public CustomFilter(String ext, String description){
			this.ext = '.' + ext;
			this.description = description;
		}
		public boolean accept(File file) {
			if(file.isDirectory())
				return true;
			String name = file.getName();
			int length = name.length();
			if(name.substring(length-ext.length(), length).equals(ext)){
				return true;
			}
			return false;
		}

		public String getDescription() {
			return description + " *" + ext;
		}
		
	}
	
}

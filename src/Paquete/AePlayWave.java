import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Esta clase se usa para reproducir sonidos en el juego. Esto crea un nuevo subproceso para reproducir el sonido..
 *
 */
public class AePlayWave{
private SourceDataLine line = null;
private byte[] audioBytes;
	private int numBytes;
	private AudioFormat audioFormat;
	private DataLine.Info info;
        static AePlayWave fondoMusica; // musica de fondo del juego
        static boolean iMusica=false; // desactivando musica de fondo

	/**
	 * Abre el archivo especificado por la ruta y lo carga en la memoria para que pueda reproducirse
	 */
	public AePlayWave(String path){
		File soundFile = new File(path);
		AudioInputStream audioInputStream = null;
		try{
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		}catch (Exception ex){
                    
		}
		if(audioInputStream == null)return;
		audioFormat = audioInputStream.getFormat();
		info = new DataLine.Info(SourceDataLine.class,audioFormat);
		try{
			line = (SourceDataLine)AudioSystem.getLine(info);
			line.open(audioFormat);
		}catch (LineUnavailableException ex){
			System.out.println("*** no se ha leido la linea de musica ***");
			System.exit(1);
		}

		line.start();

		audioBytes = new byte[(int)soundFile.length()];

		try{
			numBytes = audioInputStream.read(audioBytes, 0, audioBytes.length);
		}catch (IOException ex){
//			System.out.println("*** no se pudo leer " + soundFile + " ***");
//			System.exit(1);
		}
	}
	
	/**
	 * reproducir el sonido
	 */
	public void start(){
		new Thread(){
			public void run(){
				try{
					line = (SourceDataLine)AudioSystem.getLine(info);
					line.open(audioFormat);
				}catch (LineUnavailableException ex){
//					System.out.println("*** Audio linea no disponible ***");
//					System.exit(1);
				}
				line.start();
				line.write(audioBytes, 0, numBytes);
			}
		}.start();
	}
        
        public void finalizarMusica(){
            line.close();
        }
}
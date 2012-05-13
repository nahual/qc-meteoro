package ar.com.iron.helpers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Environment;
import android.util.Log;

/**
 * Esta clase facilita el manejo de algunas operaciones con los archivos sobre la plataforma
 * 
 * @author D.L. García
 */
public class FileHelper {

	/**
	 * Devuelve un path de archivo en forma absoluta utilizando el contexto para determinar la
	 * ubicación dentro del directorio data, y con la cantidad de subdirectorios que se indique
	 * 
	 * @param context
	 *            Contexto desde el cual se accederá al data
	 * @param fileNames
	 *            Nombres que indican los directorios intermedios y el nombre de archivo al final
	 * @return El path absoluto del archivo referenciado
	 */
	public static String getPathFromData(Context context, String directorio, String archivo) {
		File applicationDataDir = context.getDir(directorio, Context.MODE_WORLD_READABLE);
		String absolutePath = applicationDataDir.getAbsolutePath() + File.separator + archivo;
		return absolutePath;
	}

	/**
	 * Indica si existe una SD-Card con acceso del tipo deseado
	 * 
	 * @param requireWriteAccess
	 *            true si se desea acceder para escribir en la SD
	 * @return true si el acceso deseado es posible, false en caso contrario
	 */
	public static boolean hasSdCardStorage(boolean requireWriteAccess) {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		} else if (!requireWriteAccess && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * Devuelve un archivo referenciado a la sdcard a partir de un path relativo
	 * 
	 * @param fileLocation
	 *            Path relativo dentro de la sdcard al archivo
	 * @return Un File que representa la entrada, o null si no se puede acceder al archivo
	 *         libremente (no se puede escribir)
	 */
	public static File getOrCreateFileOnSdCard(String fileLocation) {
		if (!hasSdCardStorage(true)) {
			return null;
		}
		File sdCardPath = Environment.getExternalStorageDirectory();
		File archivo = new File(sdCardPath.getAbsolutePath() + fileLocation);

		if (!archivo.exists()) {
			if (!crearArchivo(archivo)) {
				return null;
			}
		}
		return archivo;
	}

	/**
	 * Crea un archivo asegurando los parents. Si el archivo ya existe se devuelve true
	 * 
	 * @param archivo
	 *            Archivo a crear
	 * @return false si no lo pudo crear
	 */
	private static boolean crearArchivo(File archivo) {
		if (archivo.exists()) {
			return true;
		}
		File parentDirFile = archivo.getParentFile();
		if (!parentDirFile.exists()) {
			boolean parentCreados = parentDirFile.mkdirs();
			if (!parentCreados) {
				return false;
			}
		}
		boolean creado;
		try {
			creado = archivo.createNewFile();
		} catch (IOException e) {
			return false;
		}
		return creado;
	}

	/**
	 * Copia el contenido obtenido del {@link InputStream} indicado en el archivo pasado.
	 * Reemplazando su contenido si ya existía.
	 * 
	 * @param destinationStream
	 *            Flujo de salida al archivo
	 * @param sourceStream
	 *            Flujo de entrada
	 * @throws CantCopyException
	 *             Si se produce un error de IO
	 */
	public static void copyTo(OutputStream destinationStream, InputStream sourceStream) throws CantCopyException {
		BufferedInputStream bufferedInput = new BufferedInputStream(sourceStream, 4096);
		BufferedOutputStream bufferedOutput = new BufferedOutputStream(destinationStream, 4096);

		byte[] readBytes = new byte[1024];
		int readCount;
		try {
			while (-1 != (readCount = bufferedInput.read(readBytes))) {
				if (readCount > 0) {
					bufferedOutput.write(readBytes, 0, readCount);
				}
			}
			bufferedOutput.flush();
		} catch (IOException e) {
			throw new CantCopyException("Se produjo un error de IO al mover bytes entre streams", e);
		}
	}

	/**
	 * Copia el contenido del recurso representado por el id indicado en el archivo pasado.<br>
	 * Si el archivo existe será reemplazado su contenido
	 * 
	 * @param destinationFile
	 *            Archivo en el cual se va a escribir
	 * @param rawResourceId
	 *            Identificado del recurso
	 * @param context
	 *            Contexto desde el cual obtener el recurso
	 * @throws CantCopyException
	 *             Si se produce un error al intentar acceder a los archivos involucrados, o de
	 *             acceso al disco durante la copia
	 */
	public static void copyRawTo(File destinationFile, int rawResourceId, Context context) throws CantCopyException {
		if (destinationFile.isDirectory()) {
			throw new CantCopyException("El path destino es un directorio: " + destinationFile);
		}
		if (destinationFile.exists()) {
			boolean deleted = destinationFile.delete();
			if (!deleted) {
				throw new CantCopyException("No se pudo borrar el archivo para sobreescribirlo posteriormente");
			}
		}
		boolean creado = crearArchivo(destinationFile);
		if (!creado) {
			throw new CantCopyException("No se pudo crear el archivo destino");
		}
		if (!destinationFile.canWrite()) {
			throw new CantCopyException("El contexto actual no permite escribir en el archivo destino: "
					+ destinationFile);
		}

		FileOutputStream destinationStream;
		try {
			destinationStream = new FileOutputStream(destinationFile);
		} catch (FileNotFoundException e) {
			throw new CantCopyException("No existe el archivo recien creado?! Estaba en la SD?", e);
		}
		Resources resources = context.getResources();
		InputStream sourceStream;
		try {
			sourceStream = resources.openRawResource(rawResourceId);
		} catch (NotFoundException e) {
			cerrarStream(destinationStream);
			throw new CantCopyException("No existe el recurso origen de la copia", e);
		}
		try {
			copyTo(destinationStream, sourceStream);
		} finally {
			cerrarStream(sourceStream);
			cerrarStream(destinationStream);
		}
	}

	/**
	 * Indica si el archivo indicado por directorio y nombre existe en el contexto indicado
	 * 
	 * @param contexto
	 *            Contexto de la aplicación
	 * @param directorio
	 *            Directorio dentro del cuál revisar
	 * @param fileName
	 *            Nombre del archivo
	 * @return true si el archivo existe
	 */
	public static boolean existsFile(Context contexto, String directorio, String fileName) {
		String filePath = getPathFromData(contexto, directorio, fileName);
		File archivo = new File(filePath);
		return archivo.exists();
	}

	/**
	 * Copia el contenido del archivo fuente sobre el archivo destino reemplazandoló.<br>
	 * Si el archivo destino, no existe, será creado.
	 * 
	 * @param destinationFile
	 *            Archivo que recibirá el contenido
	 * @param sourceFile
	 *            Archivo fuente
	 */
	public static void copyFileTo(File destinationFile, File sourceFile) throws CantCopyException {
		boolean created = crearArchivo(destinationFile);
		if (!created) {
			throw new CantCopyException("No se pudo crear el archivo destino");
		}
		FileOutputStream destinationStream;
		try {
			destinationStream = new FileOutputStream(destinationFile);
		} catch (FileNotFoundException e) {
			throw new CantCopyException("No existe el archivo recien creado?");
		}
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(sourceFile);
		} catch (FileNotFoundException e) {
			cerrarStream(destinationStream);
			throw new CantCopyException("No existe el archivo origen");
		}
		try {
			copyTo(destinationStream, inputStream);
		} finally {
			cerrarStream(inputStream);
			cerrarStream(destinationStream);
		}
	}

	/**
	 * Cierra el flujo pasado registrando un error en el log si no se pudo, pero continuando
	 * normalmente
	 * 
	 * @param openedStream
	 *            Flujo a cerrar, o null si nunca se abrió
	 */
	public static void cerrarStream(Closeable openedStream) {
		if (openedStream == null) {
			return;
		}
		try {
			openedStream.close();
		} catch (IOException e) {
			Log.e("FileHelper", "No se pudo cerrar un flujo");
		}
	}

	/**
	 * Crea un archivo temporal en el área de data de la aplicación
	 * 
	 * @param context
	 *            Contexto de la aplicación
	 * @param temporaryFilePreffix
	 *            Prefijo para el archivo temporal
	 * @return El archivo creado
	 * @throws IOException
	 *             Si no se puede crear el archivo
	 */
	public static File createTmpDataFile(Context context, String tmpPreffix) throws IOException {
		File applicationDataDir = context.getDir("tmp", Context.MODE_WORLD_READABLE);
		File tmpFile = File.createTempFile(tmpPreffix, null, applicationDataDir);
		return tmpFile;
	}
}

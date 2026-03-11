
package Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Registra los errores en un archivo "error.log".
 * <p> Permite:
 * <ul>
 * <li> Guardar los errores en un archivo para revisiones </li>
 * <li> Registra la fecha y hora de cada error </li>
 * <li> Registra el stack trace de la excepción asociada al error </li>
 * </ul>
 * 
 * <p> Ejemplo de uso:
 * <pre>
 * try {
 *     // Código que puede lanzar una excepción
 * } catch (Exception e) {
 *     Logger.logError("Descripción del error", e);
 * }
 * </pre>
 * @author Nerea
 */
public class Logger {

    /**
     * Registra un error en el archivo error.log
     * 
     * @param message Describe el error
     * @param e Excepción asociada al error, {@code null} si no hay excepción
     */
    public static void logError(String message, Exception e) {
        //Archivo donde se gusradran los errores
        try (PrintWriter writer = new PrintWriter(new FileWriter("error.log", true))) {

            //Estructura de guardado
            writer.println("----- ERROR -----");
            writer.println("Date: " + LocalDateTime.now());
            writer.println("Message: " + message);

            if (e != null) {
                e.printStackTrace(writer);
            }
            writer.println();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

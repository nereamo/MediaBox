Nerea Montoya

# MediaBox
Aplicación para descarga de videos y audio.

## Commits del repo anterior
<img width="2560" height="1392" alt="Captura de pantalla 2025-10-23 121514" src="https://github.com/user-attachments/assets/a5db15ff-f833-413e-afc3-7fe6e1d75acb" />

# 🌐 Recursos

### 🎬 Videos:
Instrucciones para crear la interfaz, el comportamiento y los componentes a utilizar.

    - https://youtu.be/SOKLL0S1RQo?si=dWPB2aIZPc1xYAAL

### 🔍 Busquedas:
Implementació de Threads.

    - https://programandoenjava.com/hilos-thread-en-java/
    - https://es.stackoverflow.com/questions/41453/c%C3%B3mo-terminar-o-eliminar-un-hilo-por-completo-en-java

Creación de JSON con libreria Jackson.

    - https://www.arquitecturajava.com/java-json-con-jackson/

### 🤖 IAs Utilizadas:

    - Copilot:
        · Implementación SwingWorker.
    
    - Gemini: 
        · Distintos tipos de formatos a descargar.
        · Logica para obtener la ubicacion de yt-dlp.exe de forma automática.

---

# Funcionalidades extra:
 📋 Pegar desde Clipboard.

    - Permite copiar la url del portapepeles utilizando el botón "Paste" de la interfaz principal.

📊 Barra de progreso.

    - Muestra el progreso de descarga en un JProgressBar.

▶️ Botón PLAY en tabla.

    - Permite abrir cualquier descarga seleccionado un archivo de la tabla.

⬇️ Botón Download API File.

    - Permite descargar un archivo de la API

⬆️ Botón Upload File to API

    - Permite subir un archivo local a la API

🔄 Comprobación de nuevos archivos

    - El componente "MediaPollingComponent" consulta periódicamente la API notificando si hay nuevos archivos.
    - Al iniciar el programa, obtiene el token del usuario e inicia el polling.
    - Desde las properties del designer de netBeans puede modificarse el tiempo entre consultas (pollingInterval) o desactivar el polling (running).

---

# ⚠️ Problemas encontrados:
## 🐞 Descarga en formato Webm:

❌ Problema

    - Al descargar en formato Webm y despues descargar el mismo archivo pero en formato audio, se sobreescribia el archivo con extensión Webm.

✔️ Solución

    - Cambiar el nombre añadiendo _audio al descargar en audio: "%(title)s_audio.%(ext)s"


## 🐞 Cambio entre los paneles Login, Frame y Preferences:

❌ Problema

    - Al añadir el JPanel Login mostraba en Frame con sus componentes, pero al querer editar las preferencias, no se visualizaban los componentes de JPanel Preferences.

✔️ Solución

    - Cambiar el Layout(null) a CardLayout, añadiendo todos los JPanels a CardLayout y alternar entre ellos.
        
    


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

 ## 🐞 Parpadeo visual en celdas de acción (Flash Blanco):

❌ Problema

    - Al pulsar alguno de los botones de la columna "Actions" la celda sufría un parpadeo blanco momentáneo .

✔️ Solución

    - Sustitución de los botones por JLabels, permitiendo mantener el Look&Feel establecido.
        
---

# 🎨 Usabilidad y Experiencia de Usuario

## 🎨 Visual appearance, color, and interface:

Se ha aplicado una interfaz oscura con tonos grises y púrpuras reduciendo la fatiga visual durante su uso.
    
    - Los botones principales utilizan el color púrpura para destacar acciones principales de secundárias, como el botón de descarga, el botón pra selecconar el directorio, el botón de búsqueda del archivo yt-dlp.exe, el botón de subida de archivo a la API o el botón para loguearse.
    - Utilización de distintos tonos grises para diferenciar los distintos paneles.
    - Bordes redondeados para una comodidad visual.
    - Incorporación de iconos en botones y en campos de texto para realizar las acciones más rapido.
    - Realte en color púrpura en campos de texto, listas deplegables, y botones para una mejor indicación de que componente está utilizando.
    - La interfaz sigue un flujo vertical permitiendo facilitar su uso (URL -> Selección de directorio -> Formato -> Botón descarga -> Barra de progreso -> Lista con los directorios utilizados -> Tabla informativa de las descargas)
    - Utilización de MigLayout para ordenar los componentes y la librería FlatLaf para aplicar un aspecto mas moderno.

## 🧩 Affordance, Feedback y Restricciones:

### Affordance

    - Los componentes muestran el cursor de mano (HANDO_CURSOR) al pasar el ratón por encima.
    - Los campos de texto contienen placeholder indicando la información que se debe introducir.
    - Los iconos ayudan a reforzar en entendimiento en las acciones

### Feedback

La aplicación informa constantemente al usuario de que acciones debe realizar.

    - Mensajes emergentes para confirmar acciones por parte del usuario como cerrar la aplicación, eliminar un archivo o si se desea retroceder sin guardar las preferencias.
    - Notificación emergentes al realizar una acción como guardar las preferencias, seleccionar un directorio antes de poder descargar el archivo, seleccionar un elemento de la tabla antes de poder realizar alguna acción o configurar las preferencias antes de cualquier descarga.
    - Notificaciones en la interfaz del cierre de sesión, que se ha encontrado el archivo yt-dlp.exe o mostrar el nombre del usuario logeado en la parte superior derecha de la interfaz.
    - Uso de la barra de progreso para informar del estado de la descarga.
    - Actualización automática de la lista de directorios y la tabla con el último archivo descargado.

### 🚫 Restricciones
    - No es posible realizar una descarga si no se han establecido las preferéncias, pegado una URL, seleccionado un directorio y establecido un formato.
    - El botón para reproducir el útlimo archivo descargado no se habilita hasta que no se haya completado la descarga y así mismo de vuelve a deshabilitar al tener una descarga en proceso.
    
## 🧩 Other usability improvements:
    - Icono en campos de texto que permiten borrar el texto introducido, ver la contraseña introducida o poder pegar la URL desde el portapapeles.
    - Uso de Tooltips para iformar de cada componente.
    - Desde la columna Acciones de la tabla, es posible descargar un archivo de la API, reproducir un archivo local o eliminar el archivo local fisicamente.
    - Uso de polling para notiifcar nuevos archivos de la API.
    - Paneles intercambiables mediante el uso de CardLayout.
    - Redimensionamiento de la ventana asi como sus componentes.

## 📝 Gestión de errores y logs

### Validación de entradas
    - Comprobación de datos introducidos correctamente en campos de texto.

### Excepciones
    - Uso de bloques try-catch en operaciones críticas.
    
### Feedback del usuario
    - Uso de mensajes explicativos para informar al usuario de que acción debe realizar.

### Logs de error
    - La alicación registra información de los fallos ocurridos durante la ejecución sin bloquear la interfaz.

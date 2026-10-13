# 📥 MediaBox
> Aplicación de escritorio en Java para la gestión, descarga y reproducción de contenidos multimedia y audio.

**Autor:** Nerea Montoya

---

## 🚀 Funcionalidades Principales

* **📋 Pegar desde Portapapeles:** Permite capturar la URL directamente utilizando el botón "Paste" de la interfaz principal.
* **📊 Barra de Progreso:** Muestra el estado y avance de la descarga en tiempo real mediante un `JProgressBar`.
* **▶️ Reproductor Integrado:** Permite abrir y reproducir cualquier archivo multimedia seleccionado directamente desde la tabla.

---

## ⚠️ Resolución de Problemas y Retos Técnicos

### 🐞 1. Descarga en formato Webm
* **Problema:** Al descargar un vídeo en formato Webm y posteriormente descargar el mismo recurso en formato de audio, el segundo sobrescribía al archivo original webm debido a la colisión de nombres.
* **Solución:** Modificación de la nomenclatura añadiendo un sufijo identificativo al descargar audio: `%(title)s_audio.%(ext)s`.

### 🐞 2. Transición entre Paneles (Login, Frame y Preferences)
* **Problema:** Al incrustar el panel de login dentro del marco principal, las vistas secundarias de preferencias no lograban renderizar ni visualizar sus componentes correctamente.
* **Solución:** Migración de un layout nulo a un contenedor **`CardLayout`**, agrupando todos los `JPanels` bajo este gestor para alternar entre vistas de forma fluida.

### 🐞 3. Parpadeo Visual en Celdas de Acción (Flash Blanco)
* **Problema:** Al interactuar con los botones situados en la columna de acciones de la tabla, las celdas experimentaban un parpadeo visual molesto (*flash* en blanco).
* **Solución:** Sustitución de los botones estándar por componentes `JLabel` interactivos, preservando intacto el diseño y el *Look & Feel* (`FlatLaf`) establecido.

---

## 🎨 Usabilidad y Experiencia de Usuario (UX/UI)

### 🎨 Aspecto visual e interfaz
* **Diseño Moderno:** Interfaz de tonos oscuros (grises y púrpuras) optimizada para reducir la fatiga visual.
* **Jerarquía de Color:** El color púrpura destaca las acciones principales frente a las secundarias (botones de descarga, selección de directorios, búsqueda de `yt-dlp.exe`, subida de archivos y login).
* **Distribución:** Flujo vertical estructurado (`URL` ➡️ `Directorio` ➡️ `Formato` ➡️ `Descarga` ➡️ `Progreso` ➡️ `Historial/Tabla`).
* **Tecnologías UI:** Implementación de **`MigLayout`** para una distribución flexible y la librería **`FlatLaf`** para un aspecto moderno tipo *Material/Dark*.

### 🧩 Affordance, Feedback y Restricciones
* **Affordance:** 
  * Puntero en forma de mano (`HAND_CURSOR`) en todos los elementos interactivos.
  * Campos de texto con textos guía (*placeholders*) e iconos descriptivos que facilitan la comprensión de las acciones.
* **Feedback Dinámico:** 
  * Mensajes emergentes de confirmación para acciones críticas (cerrar aplicación, eliminar archivos locales o descartar cambios en preferencias).
  * Notificaciones en interfaz para el estado del login, la detección automática de `yt-dlp.exe` y avisos de validación (por ejemplo, exigir seleccionar un directorio antes de iniciar la descarga).
* **Restricciones de Seguridad/Flujo:** El sistema bloquea las acciones de descarga si no se han completado los pasos previos obligatorios (credenciales, URL válida, ruta de destino y formato seleccionado).

### 🧩 Mejoras adicionales de UX
* Iconos inteligentes en inputs para vaciar texto, alternar visibilidad de contraseñas o pegar desde el portapapeles.
* Uso generalizado de *Tooltips* informativos.
* Ventanas totalmente redimensionables manteniendo la proporción de sus componentes.

---

## 📝 Gestión de Errores y Logs
* **Validación de Entradas:** Comprobación rigurosa de los datos introducidos en formularios.
* **Control de Excepciones:** Implementación de bloques `try-catch` estructurados en procesos críticos de E/S y red.
* **Registro de Errores (Logs):** Sistema de registro interno para trazar fallos de ejecución sin bloquear ni congelar la interfaz de usuario.

---

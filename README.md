# CrunchyTracker

> ⚠️ **Estado del Proyecto:** En desarrollo activo (Work in Progress).
> La funcionalidad base de red (API Kitsu) y la persistencia de datos local (Room SQLite) están completamente implementadas. Actualmente trabajando en la interfaz de usuario extendida (navegación y detalles de las series).

CrunchyTracker es una aplicación nativa Android desarrollada en Kotlin para rastrear tu progreso en series de anime. Consume la API de Kitsu mediante Retrofit y almacena tus datos localmente usando Room.

## Características Principales

*   **Exploración de Tendencias:** Carga inicial automática con los animes más populares en emisión obtenidos directamente de Kitsu.
*   **Búsqueda en Tiempo Real:** Buscador nativo integrado con el teclado virtual para encontrar cualquier serie específica del catálogo.
*   **Seguimiento de Progreso:** Botón de incremento de capítulos que actualiza visualmente la barra de progreso en milisegundos.
*   **Persistencia Local (Offline):** Las series que empiezas a seguir se guardan en una base de datos SQLite interna. Al reiniciar la app, se carga tu lista personal por defecto.
*   **Sincronización Inteligente:** Al buscar en la API, la aplicación cruza los resultados de red con tu base de datos local para mantener el recuento exacto de los capítulos que llevas vistos.

## Tecnologías y Stack

*   **Lenguaje:** Kotlin
*   **Interfaz de Usuario:** XML (ConstraintLayout, RecyclerView) con ViewBinding.
*   **Arquitectura:** Principios de Clean Architecture (separación en paquetes `api`, `database`, `model` y `adapter`).
*   **Red:** Retrofit 2 y Gson para el consumo y parseo de la API REST.
*   **Base de Datos:** Room Database con Kotlin Symbol Processing (KSP).
*   **Asincronía:** Kotlin Coroutines (`lifecycleScope`, `Dispatchers.IO` y `Dispatchers.Main`) para operaciones de red y disco sin bloquear el hilo principal.

## Requisitos del Entorno

*   Android Studio
*   Android Gradle Plugin (AGP) 9.5+
*   Kotlin 2.1+
*   SDK Mínimo: API 24 (Android 7.0)

## Instalación y Ejecución

1.  Clona este repositorio en tu equipo local:
    ```bash
    git clone [https://github.com/carlosgsi/crunchytracker.git](https://github.com/carlosgsi/crunchytracker.git)
    ```
2.  Abre el proyecto en Android Studio.
3.  Permite que Gradle sincronice e instale las dependencias automáticamente.
4.  Conecta un dispositivo físico por USB/Inalámbrico o inicia un Emulador (ej: Pixel 8 Pro).
5.  Pulsa **Run** (Mayús + F10) para compilar e instalar el APK.
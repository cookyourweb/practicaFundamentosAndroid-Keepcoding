# Practica  Fundamentos Android

Simulador de batallas de Dragon Ball utilizando la API del Bootcamp.
- Ventana 1: Primero el usuario deberá loguearse con un usuario y contraseña.
- Ventana 2: Tras el login deberán aparecer todos los personajes de Dragon Ball, permitiendo escoger uno de ellos. Deberás realizar una request para obtenerlos. Además de lo que te mande el servicio, tienes que añadir a todos los personajes estos nuevos atributos: “Vida máxima” y “Vida actual”, ambos a valor 100 al inicio. Un personaje que tenga 0 puntos de vida no podrá ser seleccionado.
- Ventana 3: Debe aparecer el personaje seleccionado por el usuario junto con una barra, un botón de “curarse” y otro de “recibir daño”. La barra de vida debe mostrar sus puntos de vida actuales. En caso de pulsar el botón “recibir daño” debe recibir reducirse la vida entre 10 y 60 puntos. En caso de pulsar el botón “curarse” debe incrementarse la vida 20 puntos.

Si un personaje tiene 0 puntos de vida, debes volver a la Ventana 2. Ese personaje deberá aparecer en la ventana con algún signo que indique que está herido.
Requisitos Obligatorios
- La Ventana 1 es una actividad
-  La Ventana 2 y 3 son fragmentos que comparten una segunda actividad
- La arquitectura aplicada es MVVW
- Diseño coherente y eficaz. Que sea diseño libre no significa que no vaya a evaluarse
- Debes tener 2 clases de personajes: El “PersonajeDTO”, representando el Json que has descargado del servidor y el “Personaje” propio de tu app (donde le añades sus puntos de vida). Utiliza la función map para transformar de PersonajeDTO a Personaje y hacerle las

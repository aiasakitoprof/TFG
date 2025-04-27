# TFG

El trabajo de fin de grado consiste en el desarrollo de un motor de físicas dentro de un juego.
Este ha sido desarrollado en [Java 23](https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html) mediante diferentes utilidades que dota a la aplicación de gráficos y del uso de assets.

## Motor de Físicas

El motor de físicas consiste en la aplicación de la [Ley de Gravitación Universal de Newton](https://en.wikipedia.org/wiki/Newton%27s_law_of_universal_gravitation) para diferentes tipos de objetos. De forma que sus trayectorias anteriormente lineales ahora se curvarán en función de las diferentes masas que se encuentren cerca de estos.

De esta forma se ha añadido realismo al juego.

## Nave

Se ha añadido una nave controlada por el jugador para permitir una mayor interacción con los elementos del juego. Esta se desplaza siguiendo un método de aceleración e inercia, junto con la interacción gravitatoria.

Además, se le ha añadido la capacidad de cancelar su movimiento a modo de frenos y se le ha adjuntado un sprite para incrementar su realismo.

### Controles

La nave se dirige mediante el uso de las teclas WASD y Q. La W aumenta la aceleración en la dirección actual de la nave mientras que la S reduce dicha aceleración en la misma dirección. La A y la D se utilizan para rotar el ángulo en sentido antihorario u horario respectivamente. A modo de frenado se ha establecido la Q para cancelar gradualmente toda aceleración dejando a la nave completamente estática en su posición actual.

Finalmente, en caso de que la nave sea destruida por impactar contra un asteroide o contra un planeta se ha establecido la Barra Espaciadora como tecla para hacer aparecer una nueva nave. Cabe mencionar que mientras haya una nave activa en la zona de juego, la pulsación de esta tecla será ignorada.

---
# Instalación

Para instalar The Game se puede o clonar el repositorio mediante el [código](https://github.com/aiasakitoprof/TFG.git) que github proporciona o mediante la descarga del archivo .zip que se puede encontrar en el apartado de [releases](https://github.com/aiasakitoprof/TFG/releases).

Una vez descargado o clonado, se debe ejecutar el archivo TG.java que se encuentra en la carpeta src.tg. Para ello se debe disponer de Java 23 instalado o una versión compatible y de un IDE que permita ejecutar archivos .java.

A mitad del archivo TG.java y remarcado con un //TODO: se encuentra el main donde ejecutar el programa o en su defecto se puede ejecutar el objeto TG en su totalidad.

---
# Under Construction

Remarcar que el proyecto de momento sigue en evolución y sujeto a cambios. Para revisar todos los cambios que se hayan publicado por el momento se puede revisar el [historial de cambios](https://github.com/aiasakitoprof/TFG/commits/main).
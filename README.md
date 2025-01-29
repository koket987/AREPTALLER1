# Servidor Web en Java

Un servidor web simple implementado en Java sin utilizar frameworks externos. Permite servir archivos estáticos (HTML, CSS, JS, imágenes) y proporciona una API REST para interacciones básicas.

## Comenzando

Sigue estas instrucciones para configurar y ejecutar el proyecto en tu máquina local para desarrollo y pruebas.

### Prerrequisitos

Asegúrate de tener instalado lo siguiente:

```
- Java 8 o superior
- Una terminal o línea de comandos
- Git para clonar el repositorio
```

### Instalación

Clona este repositorio y navega al directorio del proyecto:

```
git clone https://github.com/tuusuario/ServidorWebJava.git
cd ServidorWebJava
```

Compila el servidor en Java:

```
cd src
javac co/edu/eci/arep/HttpServer.java
```

Ejecuta el servidor:

```
java co.edu.eci.arep.HttpServer
```

El servidor se iniciará en el puerto `35000` y estará listo para manejar solicitudes.

## Pruebas

Puedes probar el servidor abriendo un navegador y accediendo a:

```
http://localhost:35000
```

### Pruebas de extremo a extremo

Utiliza `curl` para probar los endpoints de la API:

```
curl -X GET "http://localhost:35000/app/hello?name=Santiago"
```

Respuesta esperada:

```
{"name":"Santiago", "age":30, "car":null}
```

### Pruebas de archivos estáticos

Accede a los archivos incluidos:

```
http://localhost:35000/index.html
http://localhost:35000/styles.css
http://localhost:35000/script.js
http://localhost:35000/images/example1.png
```

### Pruebas automatizadas

Este proyecto incluye pruebas automatizadas para garantizar su correcto funcionamiento. Ejecuta los tests con:

```
cd src
javac -cp .:/path/to/junit-4.12.jar co/edu/eci/arep/HttpServerTest.java
java -cp .:/path/to/junit-4.12.jar org.junit.runner.JUnitCore co.edu.eci.arep.HttpServerTest
```

## Diseño del sistema

El servidor sigue un diseño modular que permite la extensión de funcionalidades. 

- **Manejo de solicitudes HTTP**: Soporta métodos `GET` y `POST`.
- **Manejo de archivos estáticos**: Permite servir HTML, CSS, JS e imágenes.
- **Endpoints REST**: Implementa un servicio REST básico.

El código está estructurado en un solo archivo principal para facilitar la comprensión y modificaciones futuras.

## Despliegue

Para uso en producción, considera ejecutar el servidor como un proceso en segundo plano o configurar un servicio systemd:

```
nohup java co.edu.eci.arep.HttpServer &
```

## Construido con

* Java - Lenguaje principal utilizado
* Biblioteca estándar de Java - Para manejo de red y archivos
* JUnit - Para pruebas automatizadas

## Contribuciones

Siéntete libre de hacer fork y enviar pull requests para mejorar el proyecto.

## Versionado

Este proyecto sigue [SemVer](http://semver.org/) para la gestión de versiones. Consulta los [tags en este repositorio](https://github.com/tuusuario/ServidorWebJava/tags) para versiones disponibles.

## Autor

* **Santiago** - *Trabajo inicial* - [Git:](https://github.com/koket987)

También puedes ver la lista de [contribuyentes](https://github.com/tuusuario/ServidorWebJava/contributors) que han participado en este proyecto.

## Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE.md](LICENSE.md) para más detalles.

## Agradecimientos

* Inspiración de implementaciones de servidores web minimalistas
* Comunidad de código abierto por las mejores prácticas


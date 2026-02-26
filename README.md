# Literalura - Catálogo de Libros 

Literalura es una aplicación de consola desarrollada en Java con Spring Boot que permite gestionar un catálogo de libros interactuando con la API pública de Gutendex.

## Funcionalidades
* **Buscar libro por título:** Consume la API de Gutendex y guarda el libro y su autor en una base de datos PostgreSQL.
* **Listar libros registrados:** Muestra todos los libros guardados en la base de datos.
* **Listar autores registrados:** Muestra un listado de los autores y los libros que han escrito.
* **Listar autores vivos en un determinado año:** Permite filtrar autores según su fecha de nacimiento y fallecimiento.
* **Listar libros por idioma:** Filtra y cuenta los libros almacenados según su idioma (es, en, fr, pt).

## Tecnologías Usadas
* Java 17
* Spring Boot 3.2.x
* Spring Data JPA
* PostgreSQL
* Jackson (Mapeo de JSON)
* HttpClient (Consumo de API)

## Cómo ejecutar el proyecto
1. Clona este repositorio.
2. Crea una base de datos en PostgreSQL llamada `literalura`.
3. Configura las variables de entorno `DB_USER` y `DB_PASSWORD` en tu IDE con tus credenciales.
4. Ejecuta la clase `LiteraluraApplication.java`.

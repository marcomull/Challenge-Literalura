package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private String URL_BASE = "https://gutendex.com/books/?search=";

    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    \n=== LITER ALURA ===
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    Elija una opción: """;
            System.out.println(menu);

            try {
                opcion = Integer.parseInt(teclado.nextLine());

                switch (opcion) {
                    case 1 -> buscarLibroWeb();
                    case 2 -> listarLibrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivos();
                    case 5 -> listarLibrosPorIdioma();
                    case 0 -> System.out.println("Cerrando la aplicación...");
                    default -> System.out.println("Opción inválida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un número válido del menú.");
            }
        }
    }

    private void buscarLibroWeb() {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        var titulo = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + titulo.replace(" ", "%20"));
        var datosBusqueda = conversor.obtenerDatos(json, DatosResultados.class);

        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream().findFirst();

        if (libroBuscado.isPresent()) {
            DatosLibro datosLibro = libroBuscado.get();
            DatosAutor datosAutor = datosLibro.autores().get(0);

            Autor autor = autorRepository.findByNombreIgnoreCase(datosAutor.nombre());
            if (autor == null) {
                autor = new Autor(datosAutor);
                autorRepository.save(autor);
            }

            try {
                Libro libro = new Libro(datosLibro, autor);
                libroRepository.save(libro);
                System.out.println("\n--- Libro Guardado ---");
                System.out.println(libro);
            } catch (Exception e) {
                System.out.println("\nError: El libro ya existe en la base de datos.");
            }
        } else {
            System.out.println("Libro no encontrado.");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        libros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese el año que desea validar:");
        var anio = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autoresVivos = autorRepository.buscarAutoresVivosEnAnio(anio);
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en ese año.");
        } else {
            autoresVivos.forEach(System.out::println);
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """);
        var idioma = teclado.nextLine();
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma.");
        } else {
            System.out.println("----- LIBROS EN IDIOMA '" + idioma.toUpperCase() + "' -----");
            librosPorIdioma.forEach(System.out::println);

            long cantidad = librosPorIdioma.stream().count();
            System.out.println("\nCantidad de libros encontrados en este idioma: " + cantidad);
        }
    }
}
package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNombreIgnoreCase(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :anio AND (a.fechaDeFallecimiento IS NULL OR a.fechaDeFallecimiento >= :anio)")
    List<Autor> buscarAutoresVivosEnAnio(Integer anio);
}
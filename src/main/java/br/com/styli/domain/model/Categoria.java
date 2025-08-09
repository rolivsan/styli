package br.com.styli.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // Empresas que oferecem esta categoria
    @ManyToMany(mappedBy = "categorias")
    private List<Empresa> empresas;
}

package br.com.bilheteria.api_pedidos.repository;

import br.com.bilheteria.api_pedidos.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
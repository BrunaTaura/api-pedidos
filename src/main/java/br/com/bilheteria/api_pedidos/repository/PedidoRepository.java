package br.com.bilheteria.api_pedidos.repository;

import br.com.bilheteria.api_pedidos.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
package br.com.bilheteria.api_pedidos.dto;

import br.com.bilheteria.api_pedidos.entity.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        LocalDateTime dataCriacao,
        StatusPedido status,
        BigDecimal total,
        List<ItemPedidoResponse> itens
) {
}
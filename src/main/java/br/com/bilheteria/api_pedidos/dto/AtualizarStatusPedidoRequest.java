package br.com.bilheteria.api_pedidos.dto;

import br.com.bilheteria.api_pedidos.entity.StatusPedido;

public record AtualizarStatusPedidoRequest(
        StatusPedido status
) {
}
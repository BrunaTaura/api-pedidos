package br.com.bilheteria.api_pedidos.dto;

import java.util.List;

public record PedidoRequest(
        List<ItemPedidoRequest> itens
) {
}
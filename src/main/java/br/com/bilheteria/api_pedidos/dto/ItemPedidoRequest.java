package br.com.bilheteria.api_pedidos.dto;

public record ItemPedidoRequest(
        Long produtoId,
        Integer quantidade
) {
}
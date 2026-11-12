package br.com.bilheteria.api_pedidos.service;

import br.com.bilheteria.api_pedidos.dto.*;
import br.com.bilheteria.api_pedidos.entity.*;
import br.com.bilheteria.api_pedidos.repository.PedidoRepository;
import br.com.bilheteria.api_pedidos.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public PedidoResponse criar(PedidoRequest request) {

        if (request.itens() == null || request.itens().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O pedido precisa possuir pelo menos um item"
            );
        }

        Pedido pedido = new Pedido();

        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        pedido.setTotal(BigDecimal.ZERO);

        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoRequest itemRequest : request.itens()) {

            if (itemRequest.produtoId() == null
                    || itemRequest.quantidade() == null
                    || itemRequest.quantidade() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Produto e quantidade devem ser informados corretamente"
                );
            }

            Produto produto = produtoRepository.findById(itemRequest.produtoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Produto não encontrado: " + itemRequest.produtoId()
                    ));

            if (Boolean.FALSE.equals(produto.getAtivo())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Produto inativo: " + produto.getNome()
                );
            }

            BigDecimal precoUnitario = produto.getPreco();

            BigDecimal subtotal = precoUnitario.multiply(
                    BigDecimal.valueOf(itemRequest.quantidade())
            );

            ItemPedido item = new ItemPedido();

            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemRequest.quantidade());
            item.setPrecoUnitario(precoUnitario);
            item.setSubtotal(subtotal);

            pedido.getItens().add(item);

            total = total.add(subtotal);
        }

        pedido.setTotal(total);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return converterParaResponse(pedidoSalvo);
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pedido não encontrado"
                ));

        return converterParaResponse(pedido);
    }

    @Transactional
    public PedidoResponse atualizarStatus(
            Long id,
            StatusPedido novoStatus) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pedido não encontrado"
                ));

        if (pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Somente pedidos aguardando pagamento podem ter o status alterado"
            );
        }

        if (novoStatus == null
                || (novoStatus != StatusPedido.PAGO
                && novoStatus != StatusPedido.CANCELADO)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status inválido"
            );
        }

        pedido.setStatus(novoStatus);

        Pedido pedidoAtualizado = pedidoRepository.save(pedido);

        return converterParaResponse(pedidoAtualizado);
    }

    private PedidoResponse converterParaResponse(Pedido pedido) {

        List<ItemPedidoResponse> itensResponse = new ArrayList<>();

        for (ItemPedido item : pedido.getItens()) {

            ItemPedidoResponse itemResponse = new ItemPedidoResponse(
                    item.getProduto().getId(),
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    item.getPrecoUnitario(),
                    item.getSubtotal()
            );

            itensResponse.add(itemResponse);
        }

        return new PedidoResponse(
                pedido.getId(),
                pedido.getDataCriacao(),
                pedido.getStatus(),
                pedido.getTotal(),
                itensResponse
        );
    }
}
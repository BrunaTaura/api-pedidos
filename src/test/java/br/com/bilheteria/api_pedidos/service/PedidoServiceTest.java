package br.com.bilheteria.api_pedidos.service;

import br.com.bilheteria.api_pedidos.dto.PedidoResponse;
import br.com.bilheteria.api_pedidos.entity.Pedido;
import br.com.bilheteria.api_pedidos.entity.StatusPedido;
import br.com.bilheteria.api_pedidos.repository.PedidoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido criarPedido(StatusPedido status) {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setStatus(status);
        pedido.setItens(new ArrayList<>());
        return pedido;
    }

    @Test
    void deveBuscarPedidoPorId() {
        Pedido pedido = criarPedido(
                StatusPedido.AGUARDANDO_PAGAMENTO);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        PedidoResponse response = pedidoService.buscarPorId(1L);

        assertEquals(1L, response.id());
        assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO,
                response.status());
    }

    @Test
    void deveRetornarErroQuandoPedidoNaoExiste() {
        when(pedidoRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.buscarPorId(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND,
                exception.getStatusCode());
        assertEquals("Pedido não encontrado",
                exception.getReason());
    }

    @Test
    void deveListarTodosOsPedidos() {
        Pedido pedido = criarPedido(
                StatusPedido.AGUARDANDO_PAGAMENTO);

        when(pedidoRepository.findAll())
                .thenReturn(List.of(pedido));

        List<PedidoResponse> resultado =
                pedidoService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).id());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistemPedidos() {
        when(pedidoRepository.findAll())
                .thenReturn(List.of());

        List<PedidoResponse> resultado =
                pedidoService.listarTodos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveAtualizarStatusParaPago() {
        Pedido pedido = criarPedido(
                StatusPedido.AGUARDANDO_PAGAMENTO);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        PedidoResponse response = pedidoService.atualizarStatus(
                1L, StatusPedido.PAGO);

        assertEquals(StatusPedido.PAGO, response.status());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void deveAtualizarStatusParaCancelado() {
        Pedido pedido = criarPedido(
                StatusPedido.AGUARDANDO_PAGAMENTO);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        PedidoResponse response = pedidoService.atualizarStatus(
                1L, StatusPedido.CANCELADO);

        assertEquals(StatusPedido.CANCELADO,
                response.status());
    }

    @Test
    void naoDeveAlterarStatusDePedidoPago() {
        Pedido pedido = criarPedido(StatusPedido.PAGO);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.atualizarStatus(
                        1L, StatusPedido.CANCELADO)
        );

        assertEquals(
                "Somente pedidos aguardando pagamento podem ter o status alterado",
                exception.getReason());

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void naoDeveAlterarStatusDePedidoCancelado() {
        Pedido pedido = criarPedido(StatusPedido.CANCELADO);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.atualizarStatus(
                        1L, StatusPedido.PAGO)
        );

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void naoDeveAceitarStatusInvalido() {
        Pedido pedido = criarPedido(
                StatusPedido.AGUARDANDO_PAGAMENTO);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.atualizarStatus(1L, null)
        );

        assertEquals("Status inválido",
                exception.getReason());

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void naoDeveCriarPedidoSemItens() {
        var request =
                new br.com.bilheteria.api_pedidos.dto.PedidoRequest(
                        List.of());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pedidoService.criar(request)
        );

        assertEquals(
                "O pedido precisa possuir pelo menos um item",
                exception.getReason());

        verify(pedidoRepository, never()).save(any());
    }
}
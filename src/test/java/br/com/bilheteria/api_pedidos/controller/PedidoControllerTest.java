package br.com.bilheteria.api_pedidos.controller;

import br.com.bilheteria.api_pedidos.dto.PedidoResponse;
import br.com.bilheteria.api_pedidos.entity.StatusPedido;
import br.com.bilheteria.api_pedidos.service.PedidoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PedidoControllerTest {

    private MockMvc mockMvc;
    private PedidoService pedidoService;

    @BeforeEach
    void configurar() {
        pedidoService = mock(PedidoService.class);

        mockMvc = MockMvcBuilders.standaloneSetup(
                new PedidoController(pedidoService)
        ).build();
    }

    private PedidoResponse criarResposta() {
        return new PedidoResponse(
                1L,
                LocalDateTime.of(2026, 11, 11, 10, 0),
                StatusPedido.AGUARDANDO_PAGAMENTO,
                new BigDecimal("40.00"),
                List.of()
        );
    }

    @Test
    void deveCriarPedido() throws Exception {

        when(pedidoService.criar(any()))
                .thenReturn(criarResposta());

        mockMvc.perform(post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "itens": [
                                    {
                                      "produtoId": 1,
                                      "quantidade": 2
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status")
                        .value("AGUARDANDO_PAGAMENTO"));
    }

    @Test
    void deveBuscarPedidoPorId() throws Exception {

        when(pedidoService.buscarPorId(1L))
                .thenReturn(criarResposta());

        mockMvc.perform(get("/api/v1/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveListarTodosOsPedidos() throws Exception {

        when(pedidoService.listarTodos())
                .thenReturn(List.of(criarResposta()));

        mockMvc.perform(get("/api/v1/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void deveRetornarListaVazia() throws Exception {

        when(pedidoService.listarTodos())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/pedidos"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void deveAtualizarStatusDoPedido() throws Exception {

        PedidoResponse resposta = new PedidoResponse(
                1L,
                LocalDateTime.of(2026, 11, 11, 10, 0),
                StatusPedido.PAGO,
                new BigDecimal("40.00"),
                List.of()
        );

        when(pedidoService.atualizarStatus(
                1L, StatusPedido.PAGO))
                .thenReturn(resposta);

        mockMvc.perform(patch("/api/v1/pedidos/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "PAGO"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAGO"));
    }
}
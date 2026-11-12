package br.com.bilheteria.api_pedidos.controller;

import br.com.bilheteria.api_pedidos.entity.Produto;
import br.com.bilheteria.api_pedidos.service.ProdutoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProdutoControllerTest {

    private MockMvc mockMvc;
    private ProdutoService produtoService;

    @BeforeEach
    void configurar() {
        produtoService = mock(ProdutoService.class);

        mockMvc = MockMvcBuilders.standaloneSetup(
                new ProdutoController(produtoService)
        ).build();
    }

    private Produto criarProduto() {
        return Produto.builder()
                .id(1L)
                .nome("Pipoca")
                .preco(new BigDecimal("20.00"))
                .ativo(true)
                .build();
    }

    @Test
    void deveCadastrarProduto() throws Exception {

        when(produtoService.cadastrar(any(Produto.class)))
                .thenReturn(criarProduto());

        mockMvc.perform(post("/api/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Pipoca",
                                  "preco": 20.00,
                                  "tipo": "ALIMENTO",
                                  "ativo": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Pipoca"));
    }

    @Test
    void deveListarTodosOsProdutos() throws Exception {

        when(produtoService.listarTodos())
                .thenReturn(List.of(criarProduto()));

        mockMvc.perform(get("/api/v1/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Pipoca"));
    }

    @Test
    void deveRetornarListaVazia() throws Exception {

        when(produtoService.listarTodos())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {

        when(produtoService.buscarPorId(1L))
                .thenReturn(criarProduto());

        mockMvc.perform(get("/api/v1/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Pipoca"));
    }
}
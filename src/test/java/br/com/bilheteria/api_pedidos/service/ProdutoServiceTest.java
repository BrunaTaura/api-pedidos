package br.com.bilheteria.api_pedidos.service;

import br.com.bilheteria.api_pedidos.entity.Produto;
import br.com.bilheteria.api_pedidos.repository.ProdutoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto criarProduto() {
        return Produto.builder()
                .id(1L)
                .nome("Pipoca")
                .preco(new BigDecimal("20.00"))
                .ativo(true)
                .build();
    }

    @Test
    void deveCadastrarProdutoComSucesso() {

        Produto produto = criarProduto();

        when(produtoRepository.save(any(Produto.class)))
                .thenReturn(produto);

        Produto resultado = produtoService.cadastrar(produto);

        assertEquals(1L, resultado.getId());
        assertEquals("Pipoca", resultado.getNome());
        assertEquals(new BigDecimal("20.00"),
                resultado.getPreco());

        verify(produtoRepository).save(produto);
    }

    @Test
    void deveListarTodosOsProdutos() {

        Produto produto = criarProduto();

        when(produtoRepository.findAll())
                .thenReturn(List.of(produto));

        List<Produto> resultado = produtoService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Pipoca", resultado.get(0).getNome());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistemProdutos() {

        when(produtoRepository.findAll())
                .thenReturn(List.of());

        List<Produto> resultado = produtoService.listarTodos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveBuscarProdutoPorId() {

        Produto produto = criarProduto();

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        Produto resultado = produtoService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Pipoca", resultado.getNome());
    }

    @Test
    void deveRetornarErroQuandoProdutoNaoExiste() {

        when(produtoRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> produtoService.buscarPorId(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND,
                exception.getStatusCode());

        assertEquals("Produto não encontrado",
                exception.getReason());
    }
}
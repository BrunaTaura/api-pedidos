package br.com.bilheteria.api_pedidos.controller;

import br.com.bilheteria.api_pedidos.dto.AtualizarStatusPedidoRequest;
import br.com.bilheteria.api_pedidos.dto.PedidoRequest;
import br.com.bilheteria.api_pedidos.dto.PedidoResponse;
import br.com.bilheteria.api_pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponse> criar(
            @RequestBody PedidoRequest request) {

        PedidoResponse pedido = pedidoService.criar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusPedidoRequest request) {

        PedidoResponse pedido = pedidoService.atualizarStatus(
                id,
                request.status()
        );

        return ResponseEntity.ok(pedido);
    }

    @GetMapping
    public List<PedidoResponse> listarTodos() {
        return pedidoService.listarTodos();
    }
}
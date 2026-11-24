package com.baozistore.api.service;

import com.baozistore.api.config.RecursoNaoEncontradoException;
import com.baozistore.api.config.RegraNegocioException;
import com.baozistore.api.model.Pedido;
import com.baozistore.api.model.Produto;
import com.baozistore.api.repository.ClienteRepository;
import com.baozistore.api.repository.PedidoRepository;
import com.baozistore.api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Pedido criar(Pedido pedido) {
        validarReferencias(pedido);
        pedido.setId(null);
        return pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado com ID: " + id));
    }

    @Transactional
    public Pedido atualizar(Long id, Pedido dadosAtualizados) {
        Pedido existente = buscarPorId(id);
        validarReferencias(dadosAtualizados);
        existente.setClienteId(dadosAtualizados.getClienteId());
        existente.setProdutoId(dadosAtualizados.getProdutoId());
        existente.setQuantidade(dadosAtualizados.getQuantidade());
        return pedidoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Pedido existente = buscarPorId(id);
        pedidoRepository.delete(existente);
    }

    private void validarReferencias(Pedido pedido) {
        if (!clienteRepository.existsById(pedido.getClienteId())) {
            throw new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + pedido.getClienteId());
        }
        Produto produto = produtoRepository.findById(pedido.getProdutoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com ID: " + pedido.getProdutoId()));

        if (Boolean.FALSE.equals(produto.getEstoque())) {
            throw new RegraNegocioException("O produto '" + produto.getNome() + "' está sem estoque no momento.");
        }
    }
}

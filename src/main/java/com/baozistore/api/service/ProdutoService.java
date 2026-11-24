package com.baozistore.api.service;

import com.baozistore.api.config.RecursoNaoEncontradoException;
import com.baozistore.api.model.Produto;
import com.baozistore.api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Produto criar(Produto produto) {
        produto.setId(null);
        return produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com ID: " + id));
    }

    @Transactional
    public Produto atualizar(Long id, Produto dadosAtualizados) {
        Produto existente = buscarPorId(id);
        existente.setNome(dadosAtualizados.getNome());
        existente.setPreco(dadosAtualizados.getPreco());
        existente.setEstoque(dadosAtualizados.getEstoque());
        return produtoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Produto existente = buscarPorId(id);
        produtoRepository.delete(existente);
    }
}

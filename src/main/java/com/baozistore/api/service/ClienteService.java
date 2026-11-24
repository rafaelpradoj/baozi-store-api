package com.baozistore.api.service;

import com.baozistore.api.config.RecursoNaoEncontradoException;
import com.baozistore.api.model.Cliente;
import com.baozistore.api.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Cliente criar(Cliente cliente) {
        cliente.setId(null);
        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + id));
    }

    @Transactional
    public Cliente atualizar(Long id, Cliente dadosAtualizados) {
        Cliente existente = buscarPorId(id);
        existente.setNome(dadosAtualizados.getNome());
        existente.setClienteDesde(dadosAtualizados.getClienteDesde());
        return clienteRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Cliente existente = buscarPorId(id);
        clienteRepository.delete(existente);
    }
}

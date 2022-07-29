package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.cliente.ClienteCreateDTO;
import br.com.vemser.petshop.dto.cliente.ClienteDTO;
import br.com.vemser.petshop.dto.cliente.ClienteDadosRelatorioDTO;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.enums.TipoRequisicao;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.repository.ClienteRepository;
import br.com.vemser.petshop.repository.ContatoRepository;
import br.com.vemser.petshop.repository.PedidoRepository;
import br.com.vemser.petshop.repository.PetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ContatoRepository contatoRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String NOT_FOUND_MESSAGE = "{idCliente} não encontrado";

    public ClienteDTO create(ClienteCreateDTO clienteDto) {
        ClienteEntity cliente = returnEntity(clienteDto);
        cliente.setQuantidadeDePedidos(0);
        cliente.setValorPagamento(0.0);
        ClienteDTO clienteCriado = returnDto(clienteRepository.save(cliente));
        emailService.sendEmail(clienteCriado.getNome(), clienteCriado.getIdCliente(), clienteCriado.getEmail(), TipoRequisicao.POST);
        return clienteCriado;
    }

    public ClienteDTO getById(Integer id) {
        ClienteEntity cliente = clienteRepository.findById(id).get();
        return returnDto(cliente);
    }

    public ClienteDTO update(Integer id, ClienteCreateDTO clienteDto) throws EntidadeNaoEncontradaException {
        ClienteEntity clienteRecuperado = retornarPorIdVerificado(id);
        clienteRecuperado.setNome(clienteDto.getNome());
        clienteRecuperado.setEmail(clienteDto.getEmail());
        ClienteDTO clienteAtualizado = returnDto(clienteRepository.save(clienteRecuperado));
        emailService.sendEmail(clienteAtualizado.getNome(), clienteAtualizado.getIdCliente(), clienteAtualizado.getEmail(), TipoRequisicao.PUT);
        return clienteAtualizado;
    }

    public void delete(Integer id) throws EntidadeNaoEncontradaException {
        ClienteEntity clienteRecuperado = retornarPorIdVerificado(id);
        emailService.sendEmail(clienteRecuperado.getNome(), clienteRecuperado.getIdCliente(), clienteRecuperado.getEmail(), TipoRequisicao.DELETE);
        clienteRepository.delete(clienteRecuperado);
    }

    public List<ClienteDadosRelatorioDTO> listarDadosCliente(Integer idCliente){
        return clienteRepository.relatorioCliente(idCliente);
    }


    public ClienteEntity retornarPorIdVerificado(Integer id) throws EntidadeNaoEncontradaException {
        return clienteRepository.findById(id).stream()
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
    }

    public void verificarId(Integer id) throws EntidadeNaoEncontradaException {
        clienteRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
    }

    private ClienteEntity returnEntity(ClienteCreateDTO dto) {
        return objectMapper.convertValue(dto, ClienteEntity.class);
    }

    private ClienteDTO returnDto(ClienteEntity entity) {
        return objectMapper.convertValue(entity, ClienteDTO.class);
    }

}

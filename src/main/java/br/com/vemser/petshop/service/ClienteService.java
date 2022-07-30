package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.cliente.ClienteCreateDTO;
import br.com.vemser.petshop.dto.cliente.ClienteDTO;
import br.com.vemser.petshop.dto.cliente.ClienteDadosRelatorioDTO;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.enums.TipoRequisicao;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ClienteRepository;
import br.com.vemser.petshop.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final static String NOT_FOUND_MESSAGE = "{idCliente} não encontrado";

    public ClienteDTO create(ClienteCreateDTO clienteDto) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        UsuarioEntity loggedUser = usuarioService.findById(usuarioService.getIdLoggedUser());
        ClienteEntity cliente = returnEntity(clienteDto);

        if(loggedUser.getCliente() != null) {
            throw new RegraDeNegocioException("Este usuário já tem um cadastro!");
        }

        cliente.setQuantidadeDePedidos(0);
        cliente.setValorPagamento(0.0);
        loggedUser.setCliente(clienteRepository.save(cliente));
        usuarioRepository.save(loggedUser);

        ClienteEntity clienteSalvo = clienteRepository.findById(loggedUser.getCliente().getIdCliente()).get();
        clienteSalvo.setUsuario(loggedUser);
        clienteRepository.save(clienteSalvo);

        ClienteDTO clienteCriado = returnDto(clienteSalvo);

        usuarioRepository.save(loggedUser);
        emailService.sendEmail(clienteCriado.getNome(), clienteCriado.getIdCliente(), clienteCriado.getEmail(), TipoRequisicao.POST);
        return clienteCriado;
    }

    public ClienteDTO getLogged() throws EntidadeNaoEncontradaException {
        UsuarioEntity userLogado = usuarioService.findById(usuarioService.getIdLoggedUser());
        return returnDto(userLogado.getCliente());
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

    public ClienteEntity returnLoggedClient() throws EntidadeNaoEncontradaException {
        UsuarioEntity userLogado = usuarioService.findById(usuarioService.getIdLoggedUser());
        return userLogado.getCliente();
    }

    private ClienteDTO returnDto(ClienteEntity entity) {
        return objectMapper.convertValue(entity, ClienteDTO.class);
    }

}

package br.com.vemser.petshop.service;


import br.com.vemser.petshop.dto.cliente.ClienteEmailMessageDTO;
import br.com.vemser.petshop.dto.login.LoginCreateDTO;
import br.com.vemser.petshop.dto.login.LoginDTO;
import br.com.vemser.petshop.dto.login.LoginStatusDTO;
import br.com.vemser.petshop.dto.login.LoginUpdateDTO;
import br.com.vemser.petshop.entity.CargoEntity;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.enums.EnumDesativar;
import br.com.vemser.petshop.enums.TipoCargo;
import br.com.vemser.petshop.enums.TipoRequisicao;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.CargoRepository;
import br.com.vemser.petshop.repository.ClienteRepository;
import br.com.vemser.petshop.repository.UsuarioRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;



@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;
    private final CargoRepository cargoRepository;


    private final KafkaProducer kafkaProducer;
    private final ClienteRepository clienteRepository;
    private final static String NOT_FOUND_MESSAGE = "{idCliente} não encontrado";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public UsuarioEntity findById(Integer idUsuario) throws EntidadeNaoEncontradaException{
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
    }

    public Optional<UsuarioEntity> findByUsername(String login){
        return usuarioRepository.findByUsername(login);
    }

    public LoginDTO cadastro(LoginCreateDTO loginCreateDTO) throws RegraDeNegocioException {
        verificaUsername(loginCreateDTO.getUsername());
        UsuarioEntity novoUser = returnEntity(loginCreateDTO);
        novoUser.setSenha(new Argon2PasswordEncoder().encode(loginCreateDTO.getSenha()));
        novoUser.setAtivo(true);

        novoUser.setCargos(Set.of(cargoRepository.findById(TipoCargo.USUARIO.getTipo()).get()));
        usuarioRepository.save(novoUser);

        return returnDTO(novoUser);
    }

    public LoginDTO updateCargos(Integer idUsuario, Set<TipoCargo> cargos) throws EntidadeNaoEncontradaException {
        Set<CargoEntity> novosCargos = new HashSet<>();
        UsuarioEntity user = findById(idUsuario);
        novosCargos.addAll(cargos.stream()
                .map(cargo -> cargoRepository.findById(cargo.getTipo()).get())
                .toList());
        user.setCargos(novosCargos);
        usuarioRepository.save(user);
        return returnDTO(user);
    }

    public LoginStatusDTO desativarConta(Integer idUsuario, EnumDesativar status) throws EntidadeNaoEncontradaException {
        UsuarioEntity usuario = findById(idUsuario);

        usuario.setAtivo(!status.equals(EnumDesativar.DESATIVAR));
        usuarioRepository.save(usuario);

        return objectMapper.convertValue(usuario, LoginStatusDTO.class);
    }

    public LoginDTO updateLoggedUsername(LoginUpdateDTO loginUpdate) throws RegraDeNegocioException {
        verificaUsername(loginUpdate.getUsername());
        UsuarioEntity usuarioLogado = usuarioRepository.findById(getIdLoggedUser()).get();
        usuarioLogado.setUsername(loginUpdate.getUsername());
        return returnDTO(usuarioLogado);
    }

    public LoginDTO getLoggedUser() throws EntidadeNaoEncontradaException {
        return findByIdUser(getIdLoggedUser());
    }

    public String updateLoggedPassword(String newPassword) {
        Optional<UsuarioEntity> user = usuarioRepository.findById(getIdLoggedUser());
        UsuarioEntity userEntity = user.get();
        userEntity.setSenha(new Argon2PasswordEncoder().encode(newPassword));
        usuarioRepository.save(userEntity);
        return "Senha alterada com sucesso!";
    }

    public void deleteUser(Integer idUsuario) {
        UsuarioEntity user = usuarioRepository.findById(idUsuario).get();
        usuarioRepository.delete(user);
    }

    public Integer getIdLoggedUser(){
        return (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public LoginDTO findByIdUser(Integer idUsuario) throws EntidadeNaoEncontradaException {
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("usuario não encontrado"));
        return returnDTO(usuario);
    }

    public void verificaUsername(String username) throws RegraDeNegocioException {
        if(usuarioRepository.findByUsername(username).isPresent()) {
            throw new RegraDeNegocioException("Este usuário já existe!");
        }
    }

    public LoginDTO returnDTO(UsuarioEntity entity) {
        LoginDTO dto = objectMapper.convertValue(entity, LoginDTO.class);
        dto.setCargos(entity.getCargos().stream()
                .map(CargoEntity::getNome).toList());
        return dto;
    }

    @Scheduled(cron = "* 30 12 15 * *" )
    private void sendEmailMes() throws JsonProcessingException {
        List<ClienteEntity> clienteEntities = clienteRepository.findAll();
        for(ClienteEntity cliente : clienteEntities){
            ClienteEmailMessageDTO clienteEmailMessageDTO = new ClienteEmailMessageDTO();
            clienteEmailMessageDTO.setNome(cliente.getNome());
            clienteEmailMessageDTO.setEmail(cliente.getEmail());
            clienteEmailMessageDTO.setIdCliente(cliente.getIdCliente());
            kafkaProducer.sendMessageEmail(clienteEmailMessageDTO, TipoRequisicao.MARKETING);
        }
    }


    private UsuarioEntity returnEntity(LoginCreateDTO dto) {
        return objectMapper.convertValue(dto, UsuarioEntity.class);
    }
}

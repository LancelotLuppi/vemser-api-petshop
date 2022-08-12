package br.com.vemser.petshop.schedule;

import br.com.vemser.petshop.dto.cliente.ClienteEmailMessageDTO;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.enums.TipoRequisicao;
import br.com.vemser.petshop.repository.ClienteRepository;
import br.com.vemser.petshop.service.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailSchedule {

    private final ClienteRepository clienteRepository;
    private  final KafkaProducer kafkaProducer;

    @Scheduled(cron = "* 30 12 15 * *" )
    private void sendEmailDeMarketingPorMes() throws JsonProcessingException, InterruptedException {
        List<ClienteEntity> clienteEntities = clienteRepository.findAll();
        for(ClienteEntity cliente : clienteEntities){
            ClienteEmailMessageDTO clienteEmailMessageDTO = new ClienteEmailMessageDTO();
            clienteEmailMessageDTO.setNome(cliente.getNome());
            clienteEmailMessageDTO.setEmail(cliente.getEmail());
            clienteEmailMessageDTO.setIdCliente(cliente.getIdCliente());
            kafkaProducer.sendMessageEmail(clienteEmailMessageDTO, TipoRequisicao.MARKETING);
            Thread.sleep(300000);
        }
    }
}

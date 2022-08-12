package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.enums.TipoRequisicao;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


// é um component bean
@Component
// terá um construtor com os argumentos enviados
@RequiredArgsConstructor
public class EmailService {
    private final freemarker.template.Configuration fmConfiguration;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${remetente}")
    private String usuario;
    private String mensagem;

    private final JavaMailSender emailSender;


    public void sendEmail(String nome, Integer id, String email, TipoRequisicao tipoRequisicao) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(email);
            switch (tipoRequisicao){
                case POST -> {
                    mimeMessageHelper.setSubject("Conta cadastrada no petshop Padawans!");
                }
                case PUT -> {
                    mimeMessageHelper.setSubject("Cadastro editado no petshop Padawans!");
                }
                default -> mimeMessageHelper.setSubject("Cadastro deletado no petshop Padawans!");
            }
            mimeMessageHelper.setText(geContentFromTemplate(nome, id, email, tipoRequisicao), true);

            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            mensagem = "Exception verificada";
        }
    }

    public String geContentFromTemplate(String nome, Integer id, String email, TipoRequisicao requisicao) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();

        // local
        dados.put("usuario", usuario);
        dados.put("email", from);

        // da requisicao
        dados.put("nome", nome);
        dados.put("emailRequisicao", email);
        dados.put("id", id);


        String html = null;
        switch (requisicao){
            case POST -> {
                Template template = fmConfiguration.getTemplate("create-email-template.ftl");
                html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
            }
            case PUT -> {
                Template template = fmConfiguration.getTemplate("edit-email-template.ftl");
                html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
            }
            case DELETE -> {
                Template template = fmConfiguration.getTemplate("delete-email-template.ftl");
                html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
            }
        }

        return html;
    }

    public String getMensagem() {
        return mensagem;
    }

}

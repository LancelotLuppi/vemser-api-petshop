//package br.com.vemser.petshop.service;
//
//
//import br.com.vemser.petshop.enums.TipoRequisicao;
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import javax.mail.internet.MimeMessage;
//import java.io.IOException;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class EmailServiceTest {
//
//    @InjectMocks
//    private EmailService emailService;
//    @Mock
//    private JavaMailSender emailSender;
//    @Mock
//    private MimeMessage mimeMessage;
//    @Mock
//    private freemarker.template.Configuration fmConfiguration;
//
//
//
//    @Test
//    public void deveTestarSendEmailComRequisicaoPost() throws IOException {
//        String nome = "Luppi";
//        Integer id = 3;
//        String email = "meuemail@teste.com";
//        TipoRequisicao tipoRequisicao = TipoRequisicao.POST;
//
//        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
//        ReflectionTestUtils.setField(emailService, "from", "meuteste@teste.com");
//        doNothing().when(emailSender).send(any(MimeMessage.class));
//        when(fmConfiguration.getTemplate(anyString())).thenReturn(new Template("ola", "test", new Configuration()));
//
//        emailService.sendEmail(nome, id, email, tipoRequisicao);
//
//        verify(emailSender, times(1)).send(any(MimeMessage.class));
//    }
//
//    @Test
//    public void deveTestarSendEmailComRequisicaoPut() throws IOException {
//        String nome = "Luppi";
//        Integer id = 3;
//        String email = "meuemail@teste.com";
//        TipoRequisicao tipoRequisicao = TipoRequisicao.PUT;
//
//        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
//        ReflectionTestUtils.setField(emailService, "from", "meuteste@teste.com");
//        doNothing().when(emailSender).send(any(MimeMessage.class));
//        when(fmConfiguration.getTemplate(anyString())).thenReturn(new Template("ola", "test", new Configuration()));
//
//        emailService.sendEmail(nome, id, email, tipoRequisicao);
//
//        verify(emailSender, times(1)).send(any(MimeMessage.class));
//    }
//
//    @Test
//    public void deveTestarSendEmailComRequisicaoDelete() throws IOException {
//        String nome = "Luppi";
//        Integer id = 3;
//        String email = "meuemail@teste.com";
//        TipoRequisicao tipoRequisicao = TipoRequisicao.DELETE;
//
//        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
//        ReflectionTestUtils.setField(emailService, "from", "meuteste@teste.com");
//        doNothing().when(emailSender).send(any(MimeMessage.class));
//        when(fmConfiguration.getTemplate(anyString())).thenReturn(new Template("ola", "test", new Configuration()));
//
//        emailService.sendEmail(nome, id, email, tipoRequisicao);
//
//        verify(emailSender, times(1)).send(any(MimeMessage.class));
//    }
//
//    @Test
//    public void deveTestarSendEmailComException() {
//        String nome = "Luppi";
//        Integer id = 3;
//        String email = "meuemail@teste.com";
//        TipoRequisicao tipoRequisicao = TipoRequisicao.DELETE;
//
//        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
//        ReflectionTestUtils.setField(emailService, "from", "");
//
//        emailService.sendEmail(nome, id, email, tipoRequisicao);
//
//        assertEquals("Exception verificada", emailService.getMensagem());
//    }
//}

package sg.edu.nus.iss.leaveapp.leave.configuration;

import java.util.Properties;

import org.springframework.context.annotation.*;

import org.springframework.mail.javamail.*;

@Configuration
public class EmailConfig
{
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        
        mailSender.setUsername("shizuyl328");
        mailSender.setPassword("yovtaesqvdegfhhv");
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        
        return mailSender;
    }
}

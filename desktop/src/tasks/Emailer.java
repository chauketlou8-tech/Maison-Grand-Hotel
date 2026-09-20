package tasks;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Emailer {
    public static void sendEmail(String to, String subject, String msg, String name) throws IOException {
        Properties props = new Properties();

        try (InputStream inputStream = Emailer.class.getClassLoader().getResourceAsStream("config/application.properties")) {

            if (inputStream == null) {
                throw new RuntimeException("config/application.properties not found");
            }

            props.load(inputStream);
        }

        String host = props.getProperty("email.host");
        String port = props.getProperty("email.port");
        String username = props.getProperty("email.username");
        String password = props.getProperty("email.password");

        Properties mailProps = new Properties();

        mailProps.put("mail.smtp.host", host);
        mailProps.put("mail.smtp.port", port);
        mailProps.put("mail.smtp.auth", props.getProperty("mail.smtp.auth"));
        mailProps.put("mail.smtp.starttls.enable", props.getProperty("mail.smtp.starttls.enable"));

        Session session = Session.getInstance(
                mailProps,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                }
        );

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            message.setSubject(subject);

            message.setText(msg);

            Transport.send(message);

            System.out.println("Successfully sent email to " + name);

        }

        catch (MessagingException e) {
            System.out.println("Error sending email to " + name);
            e.printStackTrace();
        }
    }
}
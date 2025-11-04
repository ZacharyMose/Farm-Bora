package com.mose.agribora.service;

import com.mose.agribora.dto.WeatherData;
import com.mose.agribora.entity.Profile;
import com.mose.agribora.repository.ProfileRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertService {
    @Value("${twilio.account.sid}")
    private String TWILIO_ACCOUNT_SID;
    @Value("${twilio.account.token}")
    private String TWILIO_ACCOUNT_TOKEN;
    @Value("${twilio.account.contact}")
    private String TWILIO_PHONE_NUMBER;
    private final JavaMailSender mailSender;
    private final GerminiService germiniService;
    private final WeatherService weatherService;

    public void sendNotificationEmail(String toEmail, String subject){
        Profile profile = new Profile();
        WeatherData weather = weatherService.getWeather(profile.getLocation());
        String emailContent = germiniService.getPrediction(profile.getLocation(),weather, profile);
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("mosezachary198@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(emailContent, true);
            mailSender.send(mimeMessage);

            System.out.println("Notification email sent to " + toEmail);
        } catch (MessagingException e) {
            System.out.println("Error sending email to " + e.getMessage());
        }
    }

    public void init(){
        // Initialize the twilio client when the service starts
        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_ACCOUNT_TOKEN);
    }

    public void sendNotificationSMS(String toPhoneNumber){
        Profile profile = new Profile();
        WeatherData weather = weatherService.getWeather(profile.getLocation());
        String location = profile.getLocation();
        // Get content from Germini service
        String smsContent = germiniService.getPrediction(location,weather,profile);
        try{
            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(TWILIO_PHONE_NUMBER),
                    smsContent
            ).create();
            System.out.println("Message sent successfully. SID" + message.getSid());
        } catch(Exception e){
            System.out.println("Error sending SMS " + e.getMessage());
        }
    }
}

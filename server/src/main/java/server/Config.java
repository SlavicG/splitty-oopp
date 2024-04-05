/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server;

import commons.dto.MailConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Properties;
import java.util.Random;

@Configuration
public class Config {
    @Bean
    public Random getRandom() {
        return new Random();
    }

    private static JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    public static JavaMailSender getJavaMailSender() {
        return mailSender;
    }
    public static String getEmailUsername() {
        return mailSender.getUsername();
    }
    public static void changeMailSenderConfig(@RequestBody MailConfig mailConfig) {
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(mailConfig.getPort());
        mailSender.setPassword(mailConfig.getPassword());
        mailSender.setUsername(mailConfig.getUsername());
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", mailConfig.getProps().getProperty("mail.transport.protocol"));
        props.put("mail.smtp.auth", mailConfig.getProps().getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", mailConfig.getProps().getProperty("mail.smtp.starttls.enable"));
        props.put("mail.debug", mailConfig.getProps().getProperty("mail.debug"));
    }
}
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
package client.utils;

import commons.dto.MailConfig;

import java.io.*;
import java.util.Locale;
import java.util.Properties;

public class Configuration {
    private final Properties properties;

    private static Reader getDefaultReader() {
        try {
            return new FileReader("config.properties");
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public Configuration() {
        this(getDefaultReader());
    }

    public Configuration(Reader reader) {
        if (reader == null) {
            System.err.println("Could not read configuration file. Using default configuration instead.");
            properties = new Properties(getDefaultProperties());
            return;
        }
        properties = new Properties(getDefaultProperties());
        try {
            properties.load(reader);
        } catch (IOException e) {
            System.err.println("Something went wrong while reading and parsing the configuration file. " +
                    "Using default configuration instead.");
        }
    }

    /**
     * @return Splitty backend server URL for HTTP requests.
     */
    public String getHttpServerUrl() {
        return String.format("http://%s/", properties.getProperty("server.url"));
    }

    /**
     * @return Splitty backend server URL for Websocket requests.
     */
    public String getWebsocketServerUrl() {
        return String.format("ws://%s/websocket", properties.getProperty("server.url"));
    }

    /**
     * @return Splitty backend server URL base.
     */
    public String getBaseServerUrl() {
        return properties.getProperty("server.url");
    }

    /**
     * @return Current locale selected by the user.
     */
    public Locale getLocale() {
        return Locale.forLanguageTag(properties.getProperty("language.locale"));
    }

    /**
     * @return Properties object containing all default configuration options.
     */
    static private Properties getDefaultProperties() {
        Properties defaults = new Properties();

        // Set new (default) configuration properties here.
        defaults.setProperty("server.url", "localhost:8080");
        defaults.setProperty("language.locale", "en");
        defaults.setProperty("mail.username", "splittyteam32@gmail.com");
        defaults.setProperty("mail.password", "fwtzctblnpxlnhny");

        return defaults;
    }
    public void setLangConfig(String language) {
        properties.setProperty("language.locale", language);
        try (OutputStream out = new FileOutputStream("config.properties")) {
            properties.store(out, "Set Language");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public String getLangConfig() {
        try {
            String lang = properties.getProperty("language.locale");
            if (lang == null || lang.isBlank()) {
                throw new Error();
            }
            return lang;
        } catch (Error e) {
            System.out.println("Something went wrong. Using Default Language");
            return "en";
        }
    }
    public MailConfig getMailConfig() {
        try {
            MailConfig mailConfig = new MailConfig();
            String username = properties.getProperty("mail.username");
            String password = properties.getProperty("mail.password");
            String port = properties.getProperty("mail.port");
            String host = properties.getProperty("mail.host");
            if(username == null || password == null || username.isBlank() || password.isBlank()
                || port == null || port.isBlank() || host == null || host.isBlank()) {
                throw new Error();
            }
            mailConfig.setUsername(username);
            mailConfig.setPassword(password);
            mailConfig.setHost(host);
            mailConfig.setPort(Integer.parseInt(port));
            mailConfig.setProps(MailConfig.getDefault().getProps());
            System.out.println("???");
            return mailConfig;
        } catch(Error e) {
            System.out.println("Something went wrong. Disabling e-mail features");
            return null;
        }
    }
}
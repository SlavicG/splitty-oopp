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
     * @return Splitty backend server URL.
     */
    public String getServerURL() {
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
        defaults.setProperty("server.url", "http://localhost:8080/");
        defaults.setProperty("language.locale", "en");

        return defaults;
    }
}
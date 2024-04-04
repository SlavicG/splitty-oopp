package commons.dto;

import jakarta.persistence.Basic;

import java.util.Properties;

public class MailConfig {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private Properties props;
    public static MailConfig getDefault() {
        MailConfig mailConfig = new MailConfig();
        mailConfig.setHost("smtp.gmail.com");
        mailConfig.setPort(587);
        mailConfig.setUsername("splittyteam32@gmail.com");
        mailConfig.setPassword("fwtzctblnpxlnhny");

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        mailConfig.setProps(props);
        return mailConfig;
    }
    public MailConfig() {}
    public MailConfig(String host, Integer port, String username, String password, Properties props) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.props = props;
    }
    @Basic
    public String getHost() {
        return host;
    }
    @Basic
    public Integer getPort() {
        return port;
    }

    @Basic
    public String getUsername() {
        return username;
    }

    @Basic
    public String getPassword() {
        return password;
    }

    @Basic
    public Properties getProps() {
        return props;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProps(Properties props) {
        this.props = props;
    }
}

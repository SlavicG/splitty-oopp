package commons.dto;

public class Mail {
    private String toMail;
    private String subject;
    private String body;

    public Mail(String toMail, String subject, String body) {
        this.toMail = toMail;
        this.subject = subject;
        this.body = body;
    }

    public Mail() {
    }

    public String getToMail() {
        return toMail;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

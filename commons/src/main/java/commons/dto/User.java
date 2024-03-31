package commons.dto;

import java.util.Objects;

public class User {
    private Integer id;
    private String name;
    private String email;
    private String iban;
    private String bic;
    private Double debt;

    public User() {

    }

    public User(Integer id, String name, String email, String iban, String bic, Double debt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.iban = iban;
        this.bic = bic;
        this.debt = debt;
    }

    public User(Integer id, String name, String email, String iban, String bic) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.iban = iban;
        this.bic = bic;
    }

    public User(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.iban = user.getIban();
        this.bic = user.getBic();
        this.debt = user.getDebt();

    }


    public Double getDebt() {
        return debt;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) &&
                Objects.equals(iban, user.iban) &&
                Objects.equals(bic, user.bic) &&
                Objects.equals(debt, user.debt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, iban, bic, debt);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", iban='" + iban + '\'' +
                ", bic='" + bic + '\'' +
                ", debt='" + debt + '\'' +
                '}';
    }
}

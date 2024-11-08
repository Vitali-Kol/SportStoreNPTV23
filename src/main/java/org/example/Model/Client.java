package org.example.Model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Client implements Serializable {
    private static final long serialVersionUID = 1L; // добавьте это поле

    private UUID id;
    private String firstname;
    private String lastname;
    private String phone;

    public Client() {
        this.id = UUID.randomUUID();
    }

    public Client(String firstname, String lastname, String phone) {
        this.id = UUID.randomUUID();
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
    }

    // Конструктор, который не используется, но если нужен, можно его оставить
    public Client(UUID uuid, String firstname, String lastname, String phone) {
        this.id = uuid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) &&
                Objects.equals(firstname, client.firstname) &&
                Objects.equals(lastname, client.lastname) &&
                Objects.equals(phone, client.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, phone);
    }
}

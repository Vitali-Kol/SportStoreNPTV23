package org.example.Model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Client implements Serializable {
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
}

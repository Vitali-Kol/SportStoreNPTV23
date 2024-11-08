package org.example.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Purchase implements Serializable {
    private static final long serialVersionUID = 1L; // добавьте это поле

    private UUID id;
    private Client client;
    private SportEquipment sportEquipment;
    private LocalDate purchaseDate;

    public Purchase() {
        this.id = UUID.randomUUID();
    }

    public Purchase(Client client, SportEquipment sportEquipment, LocalDate purchaseDate) {
        this.id = UUID.randomUUID();
        this.client = client;
        this.sportEquipment = sportEquipment;
        this.purchaseDate = purchaseDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public SportEquipment getSportEquipment() {
        return sportEquipment;
    }

    public void setSportEquipment(SportEquipment sportEquipment) {
        this.sportEquipment = sportEquipment;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return String.format("Покупка: %s, Товар: %s, Дата: %s",
                client.getFirstname() + " " + client.getLastname(),
                sportEquipment.getName(),
                purchaseDate);
    }

    public void setDetails(String updatedDetails) {
    }
}

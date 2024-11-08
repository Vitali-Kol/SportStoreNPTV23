package org.example.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Purchase implements Serializable {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return Objects.equals(id, purchase.id) && Objects.equals(client, purchase.client) && Objects.equals(sportEquipment, purchase.sportEquipment) && Objects.equals(purchaseDate, purchase.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, sportEquipment, purchaseDate);
    }

    @Override
    public String toString() {
        return String.format("Покупка: %s, Товар: %s, Дата: %s",
                client.getFirstname() + " " + client.getLastname(),
                sportEquipment.getName(),
                purchaseDate);
    }
    }

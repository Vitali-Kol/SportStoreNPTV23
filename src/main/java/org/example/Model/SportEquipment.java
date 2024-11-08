package org.example.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SportEquipment implements Serializable {
    private UUID id;
    private String name;
    private List<String> categories = new ArrayList<>();
    private double price;

    public SportEquipment() {
        this.id = UUID.randomUUID();
    }

    public SportEquipment(String name, List<String> categories, double price) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.categories = categories;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SportEquipment equipment = (SportEquipment) o;
        return Double.compare(equipment.price, price) == 0 && id.equals(equipment.id) && name.equals(equipment.name) && Arrays.equals(categories.toArray(), equipment.categories.toArray());
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + Arrays.hashCode(categories.toArray());
        result = 31 * result + Double.hashCode(price);
        return result;
    }

    @Override
    public String toString() {
        return "SportEquipment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categories=" + Arrays.toString(categories.toArray()) +
                ", price=" + price +
                '}';
    }
}

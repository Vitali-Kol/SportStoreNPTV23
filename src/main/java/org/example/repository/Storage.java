package org.example.repository;

import org.example.interfaces.FileRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Storage<T> implements FileRepository<T> {
    private final String filename;

    public Storage(String filename) {
        this.filename = filename;
    }

    @Override
    public void save(T entity) {
        List<T> items = load();
        items.add(entity);
        save(items);
    }

    @Override
    public void save(List<T> items) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(items);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @Override
    public List<T> load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<T>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

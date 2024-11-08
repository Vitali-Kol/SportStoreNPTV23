package org.example.repository;

import org.example.interfaces.FileRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileRepositoryImpl<T> implements FileRepository<T> {
    private final String filename;

    public FileRepositoryImpl(String filename) {
        this.filename = filename;
    }

    @Override
    public void save(T entity) {
        List<T> items = load();
        items.add(entity);
        saveAll(items);
    }

    @Override
    public void save(List<T> items) {
        saveAll(items);
    }

    @Override
    public List<T> load() {
        List<T> items = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            items = (List<T>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Если файл не найден, просто возвращаем пустой список
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return items;
    }

    private void saveAll(List<T> items) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

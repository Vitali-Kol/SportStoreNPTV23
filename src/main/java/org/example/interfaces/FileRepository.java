package org.example.interfaces;

import java.util.List;

public interface FileRepository<T> {
    void save(T entity);
    void save(List<T> items);
    List<T> load();
}

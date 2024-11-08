package org.example.interfaces;

import java.util.List;

public interface Service<T>  {
    boolean add();       // Метод для добавления элемента
    boolean edit(T entity); // Метод для редактирования
    boolean remove(T entity); // Метод для удаления
    void print();        // Метод для печати списка
    List<T> list();      // Метод для получения списка

    void editClient();
}

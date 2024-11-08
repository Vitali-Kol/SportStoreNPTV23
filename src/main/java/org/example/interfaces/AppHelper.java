package org.example.interfaces;


import java.util.List;

public interface AppHelper<T> {
    T create();
    void printList(List<T> elements);
    FileRepository<T> getRepository();
}

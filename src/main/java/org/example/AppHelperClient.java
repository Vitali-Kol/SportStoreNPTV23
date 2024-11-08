package org.example;

import org.example.Model.Client;
import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;

import java.util.List;

public class AppHelperClient implements AppHelper<Client> {
    private final FileRepository<Client> clientRepository;
    private final Input inputProvider;

    // Конструктор, принимающий Input для гибкости
    public AppHelperClient(FileRepository<Client> clientRepository, Input inputProvider) {
        this.clientRepository = clientRepository;
        this.inputProvider = inputProvider;
    }

    public FileRepository<Client> getRepository() {
        return clientRepository;
    }

    @Override
    public Client create() {
        try {
            Client client = new Client();
            System.out.print("Имя клиента: ");
            client.setFirstname(getInput());
            System.out.print("Фамилия клиента: ");
            client.setLastname(getInput());
            System.out.print("Телефон клиента: ");
            client.setPhone(getInput());
            return client;
        } catch (Exception e) {
            System.out.println("Ошибка при создании клиента: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void printList(List<Client> clients) {
        System.out.println("---------- Список клиентов --------");
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            System.out.printf("%d. %s %s, Телефон: %s%n", i + 1, client.getFirstname(), client.getLastname(), client.getPhone());
        }
    }

    // Используем метод getInput вместо scanner.nextLine
    private String getInput() {
        return inputProvider.getInput();
    }
}

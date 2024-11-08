package org.example.Services;

import org.example.Model.Client;
import org.example.interfaces.AppHelper;
import org.example.interfaces.Service;
import org.example.interfaces.Input;
import org.example.interfaces.FileRepository;

import java.util.List;

public class ClientService implements Service<Client> {
    private final List<Client> clients;
    private final AppHelper<Client> appHelperClient;
    private final Input inputProvider;
    private final FileRepository<Client> clientRepository;

    public ClientService(List<Client> clients, AppHelper<Client> appHelperClient, Input inputProvider, FileRepository<Client> clientRepository) {
        this.clients = clients;
        this.appHelperClient = appHelperClient;
        this.inputProvider = inputProvider;
        this.clientRepository = clientRepository;
    }

    @Override
    public boolean add() {
        Client client = appHelperClient.create();
        if (client != null) {
            clients.add(client);
            clientRepository.save(clients); // Сохраняем клиента в репозитории
            System.out.println("Клиент успешно добавлен.");
            return true;
        }
        System.out.println("Ошибка при добавлении клиента.");
        return false;
    }

    @Override
    public void print() {
        appHelperClient.printList(clients);
    }

    @Override
    public List<Client> list() {
        return clientRepository.load();
    }

    @Override
    public boolean edit(Client client) {
        // Реализация редактирования клиента, если необходимо
        return false;
    }

    @Override
    public boolean remove(Client client) {
        // Реализация удаления клиента, если необходимо
        return false;
    }
}

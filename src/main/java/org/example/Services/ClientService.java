package org.example.Services;

import org.example.Model.Client;
import org.example.interfaces.AppHelper;
import org.example.interfaces.Service;
import org.example.interfaces.Input;
import org.example.interfaces.FileRepository;
import org.example.repository.FileRepositoryImpl;


import java.util.List;

public class ClientService implements Service<Client> {
    private final AppHelper<Client> appHelperClient;
    private final Input inputProvider;
    private final FileRepository<Client> clientRepository;

    // Новый конструктор с инъекцией FileRepository<Client>
    public ClientService(AppHelper<Client> appHelperClient, Input inputProvider, FileRepository<Client> clientRepository) {
        this.appHelperClient = appHelperClient;
        this.inputProvider = inputProvider;
        this.clientRepository = clientRepository;
    }

    // Существующий конструктор для производственного кода
    public ClientService(AppHelper<Client> appHelperClient, Input inputProvider) {
        this(appHelperClient, inputProvider, new FileRepositoryImpl<>("clients.dat"));
    }

    @Override
    public boolean add() {
        Client client = appHelperClient.create();
        if (client != null) {
            List<Client> clients = clientRepository.load();
            clients.add(client);
            clientRepository.save(clients);
            System.out.println("Клиент успешно добавлен.");
            return true;
        }
        System.out.println("Ошибка при добавлении клиента.");
        return false;
    }

    @Override
    public void print() {
        List<Client> clients = clientRepository.load();
        appHelperClient.printList(clients);
    }

    @Override
    public List<Client> list() {
        return clientRepository.load();
    }

    @Override
    public boolean edit(Client updatedClient) {
        if (updatedClient == null || updatedClient.getId() == null) { // Проверка на null
            System.out.println("Ошибка: объект для редактирования не может быть null.");
            return false;
        }

        List<Client> clients = clientRepository.load();
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            if (client.getId() != null && client.getId().equals(updatedClient.getId())) { // Проверка ID
                clients.set(i, updatedClient);
                clientRepository.save(clients);
                return true;
            }
        }
        System.out.println("Клиент не найден.");
        return false;
    }

    @Override
    public boolean remove(Client entity) {
        // Реализация удаления по объекту, если необходимо
        return false;
    }

    public void removeClient() {
        List<Client> clients = clientRepository.load();
        if (clients.isEmpty()) {
            System.out.println("Список клиентов пуст. Нечего удалять.");
            return;
        }
        appHelperClient.printList(clients);

        try {
            System.out.print("Введите номер клиента для удаления: ");
            int index = Integer.parseInt(inputProvider.getInput()) - 1;

            if (index < 0 || index >= clients.size()) {
                System.out.println("Некорректный номер клиента.");
                return;
            }

            clients.remove(index); // Удаляем клиента
            clientRepository.save(clients); // Сохраняем изменения
            System.out.println("Клиент успешно удален.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: некорректный ввод числа.");
        }
    }

    public void editClient() {
        List<Client> clients = clientRepository.load();

        if (clients.isEmpty()) {
            System.out.println("Список клиентов пуст. Добавьте клиента перед редактированием.");
            return;
        }

        // Отображаем список клиентов для редактирования
        System.out.println("Доступные клиенты для редактирования:");
        appHelperClient.printList(clients);

        try {
            System.out.print("Введите номер клиента для редактирования: ");
            int index = Integer.parseInt(inputProvider.getInput()) - 1;

            if (index < 0 || index >= clients.size()) {
                System.out.println("Некорректный номер клиента.");
                return;
            }

            // Получаем клиента для редактирования
            Client clientToEdit = clients.get(index);
            if (clientToEdit == null) {
                System.out.println("Ошибка: выбранный клиент не найден.");
                return;
            }

            // Запрос новых данных от пользователя
            System.out.print("Новое имя клиента: ");
            String newFirstName = inputProvider.getInput();
            System.out.print("Новая фамилия клиента: ");
            String newLastName = inputProvider.getInput();
            System.out.print("Новый телефон клиента: ");
            String newPhone = inputProvider.getInput();

            // Обновляем данные клиента
            clientToEdit.setFirstname(newFirstName);
            clientToEdit.setLastname(newLastName);
            clientToEdit.setPhone(newPhone);

            // Сохраняем изменения
            if (edit(clientToEdit)) {
                System.out.println("Клиент успешно отредактирован.");
            } else {
                System.out.println("Ошибка при редактировании клиента.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: некорректный ввод числа.");
        }
    }
}

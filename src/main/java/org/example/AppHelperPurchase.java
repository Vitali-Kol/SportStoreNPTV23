package org.example;

import org.example.Model.Client;
import org.example.Model.Purchase;
import org.example.Model.SportEquipment;
import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;

import java.time.LocalDate;
import java.util.List;

public class AppHelperPurchase implements AppHelper<Purchase> {
    private final FileRepository<Purchase> purchaseRepository;
    private final FileRepository<Client> clientRepository;
    private final FileRepository<SportEquipment> sportEquipmentRepository;
    private final Input inputProvider;

    public AppHelperPurchase(FileRepository<Purchase> purchaseRepository, FileRepository<Client> clientRepository, FileRepository<SportEquipment> sportEquipmentRepository, Input inputProvider) {
        this.purchaseRepository = purchaseRepository;
        this.clientRepository = clientRepository;
        this.sportEquipmentRepository = sportEquipmentRepository;
        this.inputProvider = inputProvider;
    }

    @Override
    public Purchase create() {
        try {
            // Выбор клиента по номеру
            List<Client> clients = clientRepository.load();
            if (clients.isEmpty()) {
                System.out.println("Список клиентов пуст!");
                return null;
            }

            System.out.println("Выберите клиента:");
            for (int i = 0; i < clients.size(); i++) {
                System.out.printf("%d. %s %s%n", i + 1, clients.get(i).getFirstname(), clients.get(i).getLastname());
            }
            System.out.print("Введите номер клиента: ");
            int clientIndex = Integer.parseInt(getInput()) - 1;

            if (clientIndex < 0 || clientIndex >= clients.size()) {
                System.out.println("Некорректный номер клиента!");
                return null;
            }
            Client client = clients.get(clientIndex);

            // Выбор спортивного инвентаря по номеру
            List<SportEquipment> equipmentList = sportEquipmentRepository.load();
            if (equipmentList.isEmpty()) {
                System.out.println("Список спортивного инвентаря пуст!");
                return null;
            }

            System.out.println("Выберите спортивный инвентарь:");
            for (int i = 0; i < equipmentList.size(); i++) {
                System.out.printf("%d. %s (Цена: %.2f)%n", i + 1, equipmentList.get(i).getName(), equipmentList.get(i).getPrice());
            }
            System.out.print("Введите номер спортивного инвентаря: ");
            int equipmentIndex = Integer.parseInt(getInput()) - 1;

            if (equipmentIndex < 0 || equipmentIndex >= equipmentList.size()) {
                System.out.println("Некорректный номер спортивного инвентаря!");
                return null;
            }
            SportEquipment equipment = equipmentList.get(equipmentIndex);

            // Ввод даты покупки
            System.out.print("Введите дату покупки (гггг-мм-дд): ");
            String dateInput = getInput();
            LocalDate purchaseDate = LocalDate.parse(dateInput);

            return new Purchase(client, equipment, purchaseDate);
        } catch (Exception e) {
            System.out.println("Ошибка при создании покупки: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void printList(List<Purchase> elements) {
        System.out.println("---------- Список покупок --------");
        elements.forEach(System.out::println);
    }

    @Override
    public FileRepository<Purchase> getRepository() {
        return purchaseRepository;
    }

    private String getInput() {
        return inputProvider.getInput();
    }
}

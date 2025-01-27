package org.example;

import org.example.Model.Client;
import org.example.Model.Purchase;
import org.example.Model.SportEquipment;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Service;
import org.example.repository.InMemoryRepository;
import org.example.Services.ClientService;
import org.example.Services.SportEquipmentService;
import org.example.Services.PurchaseService;
import org.example.AppHelperClient;
import org.example.AppHelperSportEquipment;
import org.example.AppHelperPurchase;
import org.example.interfaces.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StoreNPTV23 {

    public static void main(String[] args) {

        // Инициализация списков для хранения данных
        List<SportEquipment> sportEquipments = new ArrayList<>();
        List<Client> clients = new ArrayList<>();
        List<Purchase> purchases = new ArrayList<>();

        // Инициализация репозиториев для хранения данных в памяти
        FileRepository<SportEquipment> sportEquipmentRepository = new InMemoryRepository<>();
        FileRepository<Client> clientRepository = new InMemoryRepository<>();
        FileRepository<Purchase> purchaseRepository = new InMemoryRepository<>();

        // Инициализация сканера для ввода
        Scanner scanner = new Scanner(System.in);

        // Лямбда-выражение для функционального интерфейса Input
        Input inputProvider = scanner::nextLine;

        // Инициализация AppHelpers с использованием inputProvider для гибкости ввода
        AppHelperClient appHelperClient = new AppHelperClient(clientRepository, inputProvider);
        AppHelperSportEquipment appHelperSportEquipment = new AppHelperSportEquipment(sportEquipmentRepository, inputProvider);
        AppHelperPurchase appHelperPurchase = new AppHelperPurchase(purchaseRepository, clientRepository, sportEquipmentRepository, inputProvider);

        // Инициализация сервисов
        Service<Client> clientService = new ClientService(clients, appHelperClient, inputProvider, clientRepository);
        Service<SportEquipment> sportEquipmentService = new SportEquipmentService(sportEquipments, appHelperSportEquipment, inputProvider, sportEquipmentRepository);
        Service<Purchase> purchaseService = new PurchaseService(purchases, appHelperPurchase, inputProvider);

        // Запуск приложения
        App app = new App(sportEquipmentService, clientService, purchaseService);
        app.run();
    }
}

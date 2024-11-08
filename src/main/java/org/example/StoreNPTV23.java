package org.example;

import org.example.Model.Client;
import org.example.Model.Purchase;
import org.example.Model.SportEquipment;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Service;
import org.example.repository.FileRepositoryImpl;
import org.example.Services.ClientService;
import org.example.Services.SportEquipmentService;
import org.example.Services.PurchaseService;
import org.example.AppHelperClient;
import org.example.AppHelperSportEquipment;
import org.example.AppHelperPurchase;
import org.example.interfaces.Input;

import java.util.Scanner;

public class StoreNPTV23 {

    public static void main(String[] args) {

        // Инициализация репозиториев для хранения данных в файлах
        FileRepository<SportEquipment> sportEquipmentRepository = new FileRepositoryImpl<>("sport_equipments.dat");
        FileRepository<Client> clientRepository = new FileRepositoryImpl<>("clients.dat");
        FileRepository<Purchase> purchaseRepository = new FileRepositoryImpl<>("purchases.dat");

        // Инициализация сканера для ввода
        Scanner scanner = new Scanner(System.in);

        // Лямбда-выражение для функционального интерфейса Input
        Input inputProvider = scanner::nextLine;

        // Инициализация AppHelpers с использованием inputProvider для гибкости ввода
        AppHelperClient appHelperClient = new AppHelperClient(clientRepository, inputProvider);
        AppHelperSportEquipment appHelperSportEquipment = new AppHelperSportEquipment(sportEquipmentRepository, inputProvider);
        AppHelperPurchase appHelperPurchase = new AppHelperPurchase(purchaseRepository, clientRepository, sportEquipmentRepository, inputProvider);

        // Инициализация сервисов, используя только репозитории
        Service<Client> clientService = new ClientService(appHelperClient, inputProvider);
        Service<SportEquipment> sportEquipmentService = new SportEquipmentService(appHelperSportEquipment, inputProvider);
        Service<Purchase> purchaseService = new PurchaseService(appHelperPurchase, inputProvider);

        // Запуск приложения
        App app = new App(sportEquipmentService, clientService, purchaseService);
        app.run();
    }
}

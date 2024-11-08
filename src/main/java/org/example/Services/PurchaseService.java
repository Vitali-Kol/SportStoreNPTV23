package org.example.Services;

import org.example.Model.Purchase;
import org.example.interfaces.AppHelper;
import org.example.interfaces.Service;
import org.example.interfaces.Input;
import org.example.interfaces.FileRepository;
import org.example.repository.FileRepositoryImpl;

import java.util.List;

public class PurchaseService implements Service<Purchase> {
    private final AppHelper<Purchase> appHelperPurchase;
    private final Input inputProvider;
    private final FileRepository<Purchase> purchaseRepository;

    public PurchaseService(AppHelper<Purchase> appHelperPurchase, Input inputProvider) {
        this.appHelperPurchase = appHelperPurchase;
        this.inputProvider = inputProvider;
        this.purchaseRepository = new FileRepositoryImpl<>("purchases.dat"); // Change to file repository
    }

    @Override
    public boolean add() {
        Purchase purchase = appHelperPurchase.create();
        if (purchase != null) {
            List<Purchase> purchases = purchaseRepository.load();
            purchases.add(purchase);
            purchaseRepository.save(purchases); // Сохраняем покупку в репозитории
            System.out.println("Покупка успешно добавлена.");
            return true;
        }
        System.out.println("Ошибка при добавлении покупки.");
        return false;
    }

    @Override
    public void print() {
        List<Purchase> purchases = purchaseRepository.load();
        if (purchases.isEmpty()) {
            System.out.println("Список покупок пуст.");
        } else {
            appHelperPurchase.printList(purchases);
        }
    }

    @Override
    public List<Purchase> list() {
        return purchaseRepository.load();
    }

    @Override
    public void editClient() {

    }

    @Override
    public boolean edit(Purchase entity) {
        System.out.println("Редактирование покупки не поддерживается.");
        return false;
    }

    @Override
    public boolean remove(Purchase entity) {
        List<Purchase> purchases = purchaseRepository.load();
        if (purchases.isEmpty()) {
            System.out.println("Список покупок пуст. Нечего удалять.");
            return false;
        }

        System.out.println("Выберите номер покупки для удаления:");
        appHelperPurchase.printList(purchases);

        try {
            System.out.print("Введите номер покупки: ");
            int index = Integer.parseInt(inputProvider.getInput()) - 1;

            if (index >= 0 && index < purchases.size()) {
                purchases.remove(index);
                purchaseRepository.save(purchases);
                System.out.println("Покупка успешно удалена.");
                return true;
            } else {
                System.out.println("Некорректный номер покупки.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введено некорректное значение.");
            return false;
        }
    }
}

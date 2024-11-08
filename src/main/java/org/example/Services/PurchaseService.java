package org.example.Services;

import org.example.Model.Purchase;
import org.example.interfaces.AppHelper;
import org.example.interfaces.Service;
import org.example.interfaces.Input;

import java.util.List;

public class PurchaseService implements Service<Purchase> {
    private final List<Purchase> purchases;
    private final AppHelper<Purchase> appHelperPurchase;
    private final Input inputProvider;

    // Конструктор, принимающий список покупок, AppHelper и Input
    public PurchaseService(List<Purchase> purchases, AppHelper<Purchase> appHelperPurchase, Input inputProvider) {
        this.purchases = purchases;
        this.appHelperPurchase = appHelperPurchase;
        this.inputProvider = inputProvider;
    }

    @Override
    public boolean add() {
        Purchase purchase = appHelperPurchase.create();
        if (purchase != null) {
            purchases.add(purchase);
            appHelperPurchase.getRepository().save(purchases);
            System.out.println("Покупка успешно добавлена.");
            return true;
        }
        System.out.println("Ошибка при добавлении покупки.");
        return false;
    }

    @Override
    public void print() {
        if (purchases.isEmpty()) {
            System.out.println("Список покупок пуст.");
        } else {
            appHelperPurchase.printList(purchases);
        }
    }

    @Override
    public List<Purchase> list() {
        return purchases;
    }

    @Override
    public boolean edit(Purchase entity) {
        System.out.println("Редактирование покупки не поддерживается.");
        return false;
    }

    @Override
    public boolean remove(Purchase entity) {
        if (purchases.isEmpty()) {
            System.out.println("Список покупок пуст. Нечего удалять.");
            return false;
        }

        System.out.println("Выберите номер покупки для удаления:");
        print(); // Показываем список покупок для выбора

        try {
            System.out.print("Введите номер покупки: ");
            int index = Integer.parseInt(inputProvider.getInput()) - 1;

            if (index >= 0 && index < purchases.size()) {
                purchases.remove(index);
                appHelperPurchase.getRepository().save(purchases);
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

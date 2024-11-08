package org.example;

import org.example.Model.SportEquipment;
import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;

import java.util.List;
import java.util.UUID;

public class AppHelperSportEquipment implements AppHelper<SportEquipment> {
    private final FileRepository<SportEquipment> sportEquipmentRepository;
    private final Input inputProvider;

    // Конструктор принимает Input для гибкости
    public AppHelperSportEquipment(FileRepository<SportEquipment> sportEquipmentRepository, Input inputProvider) {
        this.sportEquipmentRepository = sportEquipmentRepository;
        this.inputProvider = inputProvider;
    }

    public FileRepository<SportEquipment> getRepository() {
        return sportEquipmentRepository;
    }

    @Override
    public SportEquipment create() {
        try {
            SportEquipment equipment = new SportEquipment();
            equipment.setId(UUID.randomUUID());
            System.out.print("Название спортивного инвентаря (спортивной вещи): ");
            equipment.setName(getInput());
            System.out.print("Введите категории (разделенные запятой): ");
            String[] categoriesArray = getInput().split(",");
            equipment.setCategories(List.of(categoriesArray));
            System.out.print("Цена (например, 19,99): ");

            // Получаем строку с ценой и заменяем запятую на точку
            String priceInput = getInput().replace(",", ".");
            equipment.setPrice(Double.parseDouble(priceInput));

            return equipment;
        } catch (Exception e) {
            // Убираем или логируем сообщение об ошибке, если не нужно его выводить
            // System.out.println("Ошибка при создании спортивного инвентаря: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void printList(List<SportEquipment> equipmentList) {
        System.out.println("---------- Список спортивного инвентаря --------");
        for (int i = 0; i < equipmentList.size(); i++) {
            SportEquipment equipment = equipmentList.get(i);
            System.out.printf("%d. Название: %s, Категории: %s, Цена: %.2f%n", i + 1, equipment.getName(), String.join(", ", equipment.getCategories()), equipment.getPrice());
        }
    }

    // Используем метод getInput() вместо scanner.nextLine
    private String getInput() {
        return inputProvider.getInput();
    }
}

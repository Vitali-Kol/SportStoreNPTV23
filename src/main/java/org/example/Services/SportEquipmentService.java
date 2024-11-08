package org.example.Services;

import org.example.Model.SportEquipment;
import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.example.interfaces.Service;
import org.example.repository.FileRepositoryImpl;

import java.util.Arrays;
import java.util.List;

public class SportEquipmentService implements Service<SportEquipment> {
    private final AppHelper<SportEquipment> appHelperSportEquipment;
    private final Input inputProvider;
    private final FileRepository<SportEquipment> sportEquipmentRepository;

    public SportEquipmentService(AppHelper<SportEquipment> appHelperSportEquipment, Input inputProvider) {
        this.appHelperSportEquipment = appHelperSportEquipment;
        this.inputProvider = inputProvider;
        this.sportEquipmentRepository = new FileRepositoryImpl<>("sport_equipments.dat"); // Change to file repository
    }

    @Override
    public boolean add() {
        SportEquipment equipment = appHelperSportEquipment.create();
        if (equipment != null) {
            List<SportEquipment> sportEquipments = sportEquipmentRepository.load();
            sportEquipments.add(equipment);
            sportEquipmentRepository.save(sportEquipments);
            System.out.println("Спортивный инвентарь успешно добавлен.");
            return true;
        }
        System.out.println("Ошибка при добавлении спортивного инвентаря.");
        return false;
    }

    @Override
    public void print() {
        List<SportEquipment> sportEquipments = sportEquipmentRepository.load();
        if (sportEquipments.isEmpty()) {
            System.out.println("Список продуктов пуст.");
            return;
        }
        appHelperSportEquipment.printList(sportEquipments);
    }

    @Override
    public List<SportEquipment> list() {
        return sportEquipmentRepository.load();
    }

    @Override
    public void editClient() {

    }

    @Override
    public boolean edit(SportEquipment updatedEquipment) {
        if (updatedEquipment == null || updatedEquipment.getId() == null) {
            System.out.println("Ошибка: объект для редактирования не может быть null или без ID.");
            return false;
        }

        List<SportEquipment> sportEquipments = sportEquipmentRepository.load();
        for (int i = 0; i < sportEquipments.size(); i++) {
            SportEquipment equipment = sportEquipments.get(i);
            if (equipment.getId().equals(updatedEquipment.getId())) {
                sportEquipments.set(i, updatedEquipment);
                sportEquipmentRepository.save(sportEquipments);
                return true;
            }
        }
        System.out.println("Продукт не найден.");
        return false;
    }

    @Override
    public boolean remove(SportEquipment entity) {
        return false;
    }

    public void removeProduct() {
        List<SportEquipment> equipmentList = sportEquipmentRepository.load();

        if (equipmentList.isEmpty()) {
            System.out.println("Список продуктов пуст. Нечего удалять.");
            return;
        }

        System.out.println("Доступные продукты для удаления:");
        appHelperSportEquipment.printList(equipmentList);

        try {
            System.out.print("Введите номер продукта для удаления: ");
            int index = Integer.parseInt(inputProvider.getInput()) - 1;

            if (index < 0 || index >= equipmentList.size()) {
                System.out.println("Некорректный номер продукта.");
                return;
            }

            SportEquipment equipmentToRemove = equipmentList.remove(index);
            sportEquipmentRepository.save(equipmentList);
            System.out.println("Продукт \"" + equipmentToRemove.getName() + "\" успешно удален.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: некорректный ввод числа.");
        }
    }

    public void editProduct() {
        List<SportEquipment> equipmentList = sportEquipmentRepository.load();
        if (equipmentList.isEmpty()) {
            System.out.println("Список продуктов пуст.");
            return;
        }
        appHelperSportEquipment.printList(equipmentList);
        System.out.print("Введите номер продукта для редактирования: ");
        String indexInput = inputProvider.getInput();
        int index;
        try {
            index = Integer.parseInt(indexInput) - 1;
            if (index < 0 || index >= equipmentList.size()) {
                System.out.println("Некорректный номер продукта.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: некорректный ввод числа.");
            return;
        }

        SportEquipment equipment = equipmentList.get(index);

        // Сбор новых данных во временные переменные
        System.out.print("Новое имя продукта: ");
        String newName = inputProvider.getInput();
        System.out.print("Введите новые категории (разделенные запятой): ");
        String categoriesInput = inputProvider.getInput();
        List<String> newCategories = Arrays.asList(categoriesInput.split(","));
        System.out.print("Введите новую цену: ");
        String priceInput = inputProvider.getInput();
        double newPrice;
        try {
            newPrice = Double.parseDouble(priceInput);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: некорректный ввод числа.");
            return;
        }

        // Обновляем объект только после успешных проверок
        equipment.setName(newName);
        equipment.setCategories(newCategories);
        equipment.setPrice(newPrice);
        sportEquipmentRepository.save(equipmentList);
        System.out.println("Продукт успешно отредактирован.");
    }
}

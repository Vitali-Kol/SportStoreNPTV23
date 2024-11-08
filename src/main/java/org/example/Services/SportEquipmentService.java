package org.example.Services;

import org.example.Model.SportEquipment;
import org.example.interfaces.AppHelper;
import org.example.interfaces.Service;
import org.example.interfaces.Input;
import org.example.interfaces.FileRepository;

import java.util.List;

public class SportEquipmentService implements Service<SportEquipment> {
    private final List<SportEquipment> sportEquipments;
    private final AppHelper<SportEquipment> appHelperSportEquipment;
    private final Input inputProvider;
    private final FileRepository<SportEquipment> sportEquipmentRepository;

    public SportEquipmentService(List<SportEquipment> sportEquipments, AppHelper<SportEquipment> appHelperSportEquipment, Input inputProvider, FileRepository<SportEquipment> sportEquipmentRepository) {
        this.sportEquipments = sportEquipments;
        this.appHelperSportEquipment = appHelperSportEquipment;
        this.inputProvider = inputProvider;
        this.sportEquipmentRepository = sportEquipmentRepository;
    }

    @Override
    public boolean add() {
        SportEquipment equipment = appHelperSportEquipment.create();
        if (equipment != null) {
            sportEquipments.add(equipment);
            sportEquipmentRepository.save(sportEquipments); // Сохраняем спортивный инвентарь в репозитории
            System.out.println("Спортивный инвентарь успешно добавлен.");
            return true;
        }
        System.out.println("Ошибка при добавлении спортивного инвентаря.");
        return false;
    }

    @Override
    public void print() {
        appHelperSportEquipment.printList(sportEquipments);
    }

    @Override
    public List<SportEquipment> list() {
        return sportEquipmentRepository.load();
    }

    @Override
    public boolean edit(SportEquipment equipment) {
        // Реализация редактирования инвентаря, если необходимо
        return false;
    }

    @Override
    public boolean remove(SportEquipment equipment) {
        // Реализация удаления инвентаря, если необходимо
        return false;
    }
}

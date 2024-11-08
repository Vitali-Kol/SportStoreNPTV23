package apphelpers;

import org.example.AppHelperSportEquipment;
import org.example.Model.SportEquipment;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppHelperSportEquipmentTest {

    private AppHelperSportEquipment appHelperSportEquipment;
    private FileRepository<SportEquipment> sportEquipmentRepository;
    private Input inputProvider;

    @BeforeEach
    void setUp() {
        // Мокаем репозиторий и интерфейс ввода
        sportEquipmentRepository = mock(FileRepository.class);
        inputProvider = mock(Input.class);

        // Инициализируем AppHelperSportEquipment с моками
        appHelperSportEquipment = new AppHelperSportEquipment(sportEquipmentRepository, inputProvider);
    }

    @Test
    void testCreateSportEquipmentSuccess() {
        // Настройка последовательного ввода для создания спортивного инвентаря
        when(inputProvider.getInput())
                .thenReturn("Футбольный мяч")       // Название инвентаря
                .thenReturn("мячи,спорт")           // Категории
                .thenReturn("19,99");               // Цена с запятой

        // Вызов метода create и проверка результата
        SportEquipment equipment = appHelperSportEquipment.create();

        assertNotNull(equipment, "Спортивный инвентарь должен быть создан");
        assertEquals("Футбольный мяч", equipment.getName());
        assertEquals(List.of("мячи", "спорт"), equipment.getCategories());
        assertEquals(19.99, equipment.getPrice(), 0.01);
    }

    @Test
    void testCreateSportEquipmentInvalidPrice() {
        // Настройка ввода с некорректной ценой
        when(inputProvider.getInput())
                .thenReturn("Футбольный мяч")      // Название инвентаря
                .thenReturn("мячи,спорт")          // Категории
                .thenReturn("девятнадцать");       // Некорректная цена

        // Вызов метода create и проверка результата
        SportEquipment equipment = appHelperSportEquipment.create();

        assertNull(equipment, "Спортивный инвентарь не должен быть создан с некорректной ценой");
    }

    @Test
    void testPrintList() {
        // Настройка списка спортивного инвентаря
        List<SportEquipment> equipmentList = List.of(
                new SportEquipment("Футбольный мяч", List.of("мячи", "спорт"), 19.99),
                new SportEquipment("Теннисная ракетка", List.of("ракетки", "спорт"), 49.99)
        );

        // Вызываем метод printList и проверяем, что он выводит данные
        appHelperSportEquipment.printList(equipmentList);

        // Здесь можно проверить, что вывод выполнен корректно.
        // Если нужно, перенаправьте System.out на поток для захвата вывода и последующей проверки.
    }
}

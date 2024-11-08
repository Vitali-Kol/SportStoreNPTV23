package services;

import org.example.Model.SportEquipment;
import org.example.Services.SportEquipmentService;
import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SportEquipmentServiceTest {

    private SportEquipmentService sportEquipmentService;
    private List<SportEquipment> sportEquipments;
    private AppHelper<SportEquipment> appHelperSportEquipment;
    private Input inputProvider;
    private FileRepository<SportEquipment> sportEquipmentRepository;

    @BeforeEach
    void setUp() {
        // Инициализация моков
        sportEquipments = new ArrayList<>();
        appHelperSportEquipment = mock(AppHelper.class);
        inputProvider = mock(Input.class);
        sportEquipmentRepository = mock(FileRepository.class);

        // Инициализация SportEquipmentService с моками
        sportEquipmentService = new SportEquipmentService(sportEquipments, appHelperSportEquipment, inputProvider, sportEquipmentRepository);
    }

    @Test
    void testAddSportEquipmentSuccess() {
        // Настройка успешного создания спортивного инвентаря
        SportEquipment equipment = new SportEquipment("Футбольный мяч", List.of("мячи"), 19.99);
        when(appHelperSportEquipment.create()).thenReturn(equipment);

        // Вызов метода add и проверка результата
        boolean result = sportEquipmentService.add();

        // Проверка, что инвентарь добавлен
        assertTrue(result, "Спортивный инвентарь должен быть добавлен успешно");
        assertEquals(1, sportEquipments.size(), "Список инвентаря должен содержать 1 элемент");
        assertEquals(equipment, sportEquipments.get(0), "Добавленный инвентарь должен соответствовать ожидаемому");

        // Проверка, что инвентарь сохранен в репозитории
        verify(sportEquipmentRepository).save(sportEquipments);
        verify(appHelperSportEquipment).create();
    }

    @Test
    void testAddSportEquipmentFailure() {
        // Настройка создания инвентаря (возвращаем null)
        when(appHelperSportEquipment.create()).thenReturn(null);

        // Вызов метода add и проверка результата
        boolean result = sportEquipmentService.add();

        // Проверка, что инвентарь не добавлен
        assertFalse(result, "Спортивный инвентарь не должен быть добавлен");
        assertTrue(sportEquipments.isEmpty(), "Список инвентаря должен оставаться пустым");

        // Проверка, что метод save не был вызван
        verify(sportEquipmentRepository, never()).save(anyList());
        verify(appHelperSportEquipment).create();
    }

    @Test
    void testPrintSportEquipments() {
        // Добавление элементов в список для теста
        SportEquipment equipment1 = new SportEquipment("Футбольный мяч", List.of("мячи"), 19.99);
        SportEquipment equipment2 = new SportEquipment("Теннисная ракетка", List.of("ракетки"), 49.99);
        sportEquipments.add(equipment1);
        sportEquipments.add(equipment2);

        // Вызов метода print
        sportEquipmentService.print();

        // Проверка, что printList был вызван у appHelperSportEquipment
        verify(appHelperSportEquipment).printList(sportEquipments);
    }

    @Test
    void testListSportEquipments() {
        // Настройка загрузки списка инвентаря из репозитория
        List<SportEquipment> mockEquipments = List.of(
                new SportEquipment("Футбольный мяч", List.of("мячи"), 19.99),
                new SportEquipment("Теннисная ракетка", List.of("ракетки"), 49.99)
        );
        when(sportEquipmentRepository.load()).thenReturn(mockEquipments);

        // Вызов метода list и проверка возвращаемого списка
        List<SportEquipment> result = sportEquipmentService.list();

        assertEquals(mockEquipments, result, "Список инвентаря должен соответствовать загруженному из репозитория");
        verify(sportEquipmentRepository).load();
    }
}

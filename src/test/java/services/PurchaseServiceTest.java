package services;

import org.example.Model.Client;
import org.example.Model.Purchase;
import org.example.Model.SportEquipment;
import org.example.Services.PurchaseService;
import org.example.interfaces.AppHelper;
import org.example.interfaces.Input;
import org.example.interfaces.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseServiceTest {

    private PurchaseService purchaseService;
    private List<Purchase> purchases;
    private AppHelper<Purchase> appHelperPurchase;
    private Input inputProvider;
    private FileRepository<Purchase> purchaseRepository;

    @BeforeEach
    void setUp() {
        // Создаем моки для зависимостей
        purchases = new ArrayList<>();
        appHelperPurchase = mock(AppHelper.class);
        inputProvider = mock(Input.class);
        purchaseRepository = mock(FileRepository.class);

        // Инициализация PurchaseService с моками
        purchaseService = new PurchaseService(purchases, appHelperPurchase, inputProvider);
        when(appHelperPurchase.getRepository()).thenReturn(purchaseRepository);
    }

    @Test
    void testAddPurchaseSuccess() {
        // Настройка клиента и спортивного инвентаря для создания полной покупки
        Client client = new Client("Иван", "Иванов", "123456789");
        SportEquipment equipment = new SportEquipment("Футбольный мяч", List.of("мячи"), 19.99);

        // Создание покупки с клиентом и инвентарём
        Purchase purchase = new Purchase(client, equipment, LocalDate.now());

        // Настройка мока для успешного создания покупки
        when(appHelperPurchase.create()).thenReturn(purchase);

        // Вызов метода add и проверка результата
        boolean result = purchaseService.add();

        // Проверка, что покупка добавлена
        assertTrue(result, "Покупка должна быть добавлена успешно");
        assertEquals(1, purchases.size(), "Список покупок должен содержать 1 элемент");
        assertEquals(purchase, purchases.get(0), "Добавленная покупка должна соответствовать ожидаемой");

        // Проверка, что покупка сохранена в репозитории
        verify(purchaseRepository).save(purchases);
        verify(appHelperPurchase).create();
    }

    @Test
    void testAddPurchaseFailure() {
        // Настройка поведения для создания покупки (возвращаем null)
        when(appHelperPurchase.create()).thenReturn(null);

        // Вызов метода add и проверка результата
        boolean result = purchaseService.add();

        // Проверка, что покупка не добавлена
        assertFalse(result, "Покупка не должна быть добавлена");
        assertTrue(purchases.isEmpty(), "Список покупок должен оставаться пустым");

        // Проверка, что метод save не был вызван
        verify(purchaseRepository, never()).save(anyList());
        verify(appHelperPurchase).create();
    }

    @Test
    void testPrintWhenPurchasesIsEmpty() {
        // Вызываем метод print с пустым списком
        purchaseService.print();

        // Проверка, что вывод "Список покупок пуст." был вызван
        // (Здесь вы можете перенаправить System.out для проверки вывода, если необходимо)
    }

    @Test
    void testPrintWhenPurchasesIsNotEmpty() {
        // Настройка списка покупок
        Purchase purchase = new Purchase(); // Создайте покупку с необходимыми параметрами
        purchases.add(purchase);

        // Вызов метода print
        purchaseService.print();

        // Проверка, что метод printList был вызван у appHelperPurchase
        verify(appHelperPurchase).printList(purchases);
    }

    @Test
    void testRemovePurchaseSuccess() {
        // Добавляем покупку в список
        Purchase purchase = new Purchase(); // Создайте покупку с необходимыми параметрами
        purchases.add(purchase);

        // Настраиваем ввод для удаления покупки
        when(inputProvider.getInput()).thenReturn("1");

        // Вызов метода remove и проверка результата
        boolean result = purchaseService.remove(purchase);

        assertTrue(result, "Покупка должна быть удалена успешно");
        assertTrue(purchases.isEmpty(), "Список покупок должен быть пустым после удаления");

        // Проверка, что изменения сохранены в репозитории
        verify(purchaseRepository).save(purchases);
    }

    @Test
    void testRemovePurchaseInvalidIndex() {
        // Добавляем покупку в список
        Purchase purchase = new Purchase(); // Создайте покупку с необходимыми параметрами
        purchases.add(purchase);

        // Настраиваем ввод для некорректного индекса
        when(inputProvider.getInput()).thenReturn("2"); // Неправильный индекс

        // Вызов метода remove и проверка результата
        boolean result = purchaseService.remove(purchase);

        assertFalse(result, "Покупка не должна быть удалена при некорректном индексе");
        assertEquals(1, purchases.size(), "Список покупок должен содержать 1 элемент");

        // Проверка, что метод save не был вызван
        verify(purchaseRepository, never()).save(purchases);
    }
}

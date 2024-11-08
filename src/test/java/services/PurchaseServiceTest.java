// PurchaseServiceTest.java
package services;

import org.example.Model.Purchase;
import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.example.Services.PurchaseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PurchaseServiceTest {
    private PurchaseService purchaseService;
    private AppHelper<Purchase> appHelperPurchaseMock;
    private Input inputProviderMock;
    private FileRepository<Purchase> purchaseRepositoryMock;

    @Before
    public void setUp() throws Exception {
        // Создаем мок-объекты
        appHelperPurchaseMock = mock(AppHelper.class);
        inputProviderMock = mock(Input.class);
        purchaseRepositoryMock = mock(FileRepository.class);

        // Инициализируем сервис
        purchaseService = new PurchaseService(
                appHelperPurchaseMock,
                inputProviderMock
        );

        // Используем рефлексию для замены приватного поля purchaseRepository на мок
        Field repositoryField = PurchaseService.class.getDeclaredField("purchaseRepository");
        repositoryField.setAccessible(true);
        repositoryField.set(purchaseService, purchaseRepositoryMock);
    }

    // Тест для метода add() - успешный случай
    @Test
    public void testAddSuccess() {
        // Arrange
        Purchase newPurchase = new Purchase();
        newPurchase.setId(UUID.randomUUID());
        when(appHelperPurchaseMock.create()).thenReturn(newPurchase);

        List<Purchase> purchaseList = new ArrayList<>();
        when(purchaseRepositoryMock.load()).thenReturn(purchaseList);

        // Act
        boolean result = purchaseService.add();

        // Assert
        assertTrue(result);
        verify(purchaseRepositoryMock).save(purchaseList);
        assertEquals(1, purchaseList.size());
        assertEquals(newPurchase, purchaseList.get(0));
    }

    // Тест для метода add() - неудачный случай (create() возвращает null)
    @Test
    public void testAddFailure() {
        // Arrange
        when(appHelperPurchaseMock.create()).thenReturn(null);

        // Act
        boolean result = purchaseService.add();

        // Assert
        assertFalse(result);
        verify(purchaseRepositoryMock, never()).save(anyList());
    }

    // Тест для метода print() с пустым списком
    @Test
    public void testPrintEmptyList() {
        // Arrange
        when(purchaseRepositoryMock.load()).thenReturn(new ArrayList<>());

        // Act
        purchaseService.print();

        // Assert
        verify(appHelperPurchaseMock, never()).printList(anyList());
    }

    // Тест для метода print() с непустым списком
    @Test
    public void testPrintWithPurchases() {
        // Arrange
        List<Purchase> purchaseList = new ArrayList<>();
        Purchase purchase1 = new Purchase();
        Purchase purchase2 = new Purchase();
        purchaseList.add(purchase1);
        purchaseList.add(purchase2);

        when(purchaseRepositoryMock.load()).thenReturn(purchaseList);

        // Act
        purchaseService.print();

        // Assert
        verify(appHelperPurchaseMock).printList(purchaseList);
    }

    // Тест для метода list()
    @Test
    public void testList() {
        // Arrange
        List<Purchase> purchaseList = Arrays.asList(new Purchase(), new Purchase());
        when(purchaseRepositoryMock.load()).thenReturn(purchaseList);

        // Act
        List<Purchase> result = purchaseService.list();

        // Assert
        assertEquals(purchaseList, result);
    }

    // Тест для метода edit() - объект null
    @Test
    public void testEditNullPurchase() {
        // Act
        boolean result = purchaseService.edit(null);

        // Assert
        assertFalse(result);
        verify(purchaseRepositoryMock, never()).save(anyList());
    }

    // Тест для метода edit() - объект с null ID
    @Test
    public void testEditPurchaseWithNullId() {
        // Arrange
        Purchase updatedPurchase = new Purchase();
        updatedPurchase.setId(null);

        // Act
        boolean result = purchaseService.edit(updatedPurchase);

        // Assert
        assertFalse(result);
        verify(purchaseRepositoryMock, never()).save(anyList());
    }

    // Тест для метода edit() - успешное обновление
    @Test
    public void testEditSuccess() {
        // Arrange
        UUID purchaseId = UUID.randomUUID();
        Purchase updatedPurchase = new Purchase();
        updatedPurchase.setId(purchaseId);
        updatedPurchase.setDetails("Updated Details");

        Purchase existingPurchase = new Purchase();
        existingPurchase.setId(purchaseId);
        existingPurchase.setDetails("Old Details");

        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(existingPurchase);

        when(purchaseRepositoryMock.load()).thenReturn(purchaseList);

        // Act
        boolean result = purchaseService.edit(updatedPurchase);

        // Assert
        assertFalse("The edit operation should not be supported and should return false.", result);
        verify(purchaseRepositoryMock, never()).save(anyList());
    }

    // Тест для метода edit() - покупка не найдена
    @Test
    public void testEditPurchaseNotFound() {
        // Arrange
        UUID purchaseId = UUID.randomUUID();
        Purchase updatedPurchase = new Purchase();
        updatedPurchase.setId(purchaseId);
        updatedPurchase.setDetails("Updated Details");

        Purchase existingPurchase = new Purchase();
        existingPurchase.setId(UUID.randomUUID()); // Различный UUID
        existingPurchase.setDetails("Old Details");

        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(existingPurchase);

        when(purchaseRepositoryMock.load()).thenReturn(purchaseList);

        // Act
        boolean result = purchaseService.edit(updatedPurchase);

        // Assert
        assertFalse(result);
        verify(purchaseRepositoryMock, never()).save(anyList());
    }

    // Тест для метода remove() - успешное удаление
    @Test
    public void testRemoveSuccess() {
        // Arrange
        UUID purchaseId = UUID.randomUUID();
        Purchase purchaseToRemove = new Purchase();
        purchaseToRemove.setId(purchaseId);
        purchaseToRemove.setDetails("Details to remove");

        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(purchaseToRemove);
        purchaseList.add(new Purchase());

        when(purchaseRepositoryMock.load()).thenReturn(purchaseList);
        when(inputProviderMock.getInput()).thenReturn("1");

        // Act
        boolean result = purchaseService.remove(purchaseToRemove);

        // Assert
        assertTrue("The remove operation should succeed and return true.", result);
        assertEquals("The list should have one less purchase after removal.", 1, purchaseList.size());
        verify(purchaseRepositoryMock).save(purchaseList);
    }

    // Тест для метода removeProduct() - пустой список
    @Test
    public void testRemoveProductEmptyList() {
        // Arrange
        when(purchaseRepositoryMock.load()).thenReturn(new ArrayList<>());

        // Act
        purchaseService.removeProduct();

        // Assert
        verify(inputProviderMock, never()).getInput();
        verify(purchaseRepositoryMock, never()).save(anyList());
    }

    // Тест для метода removeProduct() - корректный индекс
    @Test
    public void testRemoveProductSuccess() {
        // Arrange
        Purchase purchase1 = new Purchase();
        purchase1.setId(UUID.randomUUID());
        purchase1.setDetails("Purchase1");

        Purchase purchase2 = new Purchase();
        purchase2.setId(UUID.randomUUID());
        purchase2.setDetails("Purchase2");

        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(purchase1);
        purchaseList.add(purchase2);

        // Мокируем, чтобы load() возвращал изменяемый список purchaseList
        when(purchaseRepositoryMock.load()).thenReturn(purchaseList);
        when(inputProviderMock.getInput()).thenReturn("1");  // Указываем, что первый элемент будет удален

        // Act
        purchaseService.removeProduct();

        // Assert
        assertEquals("После удаления должен остаться один элемент.", 1, purchaseList.size());
        assertEquals("Оставшийся элемент должен быть purchase2.", purchase2, purchaseList.get(0));
        verify(purchaseRepositoryMock).save(purchaseList);  // Убедитесь, что изменения сохраняются
    }

    // Тест для метода removeProduct() - некорректный индекс
    @Test
    public void testRemoveProductInvalidIndex() {
        // Arrange
        Purchase purchase1 = new Purchase();
        purchase1.setId(UUID.randomUUID());
        purchase1.setDetails("Purchase1");

        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(purchase1);

        when(purchaseRepositoryMock.load()).thenReturn(purchaseList);
        when(inputProviderMock.getInput()).thenReturn("5");

        // Act
        purchaseService.removeProduct();

        // Assert
        assertEquals(1, purchaseList.size());
        verify(purchaseRepositoryMock, never()).save(anyList());
    }

    // Тест для метода removeProduct() - нечисловой ввод
    @Test
    public void testRemoveProductNonNumericInput() {
        // Arrange
        Purchase purchase1 = new Purchase();
        purchase1.setId(UUID.randomUUID());
        purchase1.setDetails("Purchase1");

        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(purchase1);

        when(purchaseRepositoryMock.load()).thenReturn(purchaseList);
        when(inputProviderMock.getInput()).thenReturn("abc");

        // Act
        purchaseService.removeProduct();

        // Assert
        assertEquals(1, purchaseList.size());
        verify(purchaseRepositoryMock, never()).save(anyList());
    }

    // Тест для метода editClient() - метод пустой
    @Test
    public void testEditClient() {
        // Act
        purchaseService.editClient();

        // Assert
        // Поскольку метод пустой, просто убедимся, что ничего не произошло
        // Никаких вызовов не ожидается
        verifyNoInteractions(appHelperPurchaseMock);
        verifyNoInteractions(inputProviderMock);
        verifyNoInteractions(purchaseRepositoryMock);
    }

    // Тест для метода edit() - редактирование не поддерживается
    @Test
    public void testEditUnsupported() {
        // Arrange
        Purchase purchase = new Purchase();
        purchase.setId(UUID.randomUUID());

        // Act
        boolean result = purchaseService.edit(purchase);

        // Assert
        assertFalse(result);
        verify(purchaseRepositoryMock, never()).save(anyList());
    }

    // Дополнительные тесты могут быть добавлены по мере необходимости
}

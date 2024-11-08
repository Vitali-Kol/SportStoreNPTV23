// SportEquipmentServiceTest.java
package services;

import org.example.Model.SportEquipment;
import org.example.Services.SportEquipmentService;
import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SportEquipmentServiceTest {
    private SportEquipmentService sportEquipmentService;
    private AppHelper<SportEquipment> appHelperSportEquipmentMock;
    private Input inputProviderMock;
    private FileRepository<SportEquipment> sportEquipmentRepositoryMock;

    @Before
    public void setUp() {
        appHelperSportEquipmentMock = mock(AppHelper.class);
        inputProviderMock = mock(Input.class);
        sportEquipmentRepositoryMock = mock(FileRepository.class);

        sportEquipmentService = new SportEquipmentService(
                appHelperSportEquipmentMock,
                inputProviderMock,
                sportEquipmentRepositoryMock
        );
    }

    // Тест для метода add() - успешный случай
    @Test
    public void testAddSuccess() {
        // Arrange
        SportEquipment newEquipment = new SportEquipment();
        when(appHelperSportEquipmentMock.create()).thenReturn(newEquipment);

        List<SportEquipment> equipmentList = new ArrayList<>();
        when(sportEquipmentRepositoryMock.load()).thenReturn(equipmentList);

        // Act
        boolean result = sportEquipmentService.add();

        // Assert
        assertTrue(result);
        verify(sportEquipmentRepositoryMock).save(equipmentList);
        assertEquals(1, equipmentList.size());
        assertEquals(newEquipment, equipmentList.get(0));
    }

    // Тест для метода add() - неудачный случай
    @Test
    public void testAddFailure() {
        // Arrange
        when(appHelperSportEquipmentMock.create()).thenReturn(null);

        // Act
        boolean result = sportEquipmentService.add();

        // Assert
        assertFalse(result);
        verify(sportEquipmentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода print() с пустым списком
    @Test
    public void testPrintEmptyList() {
        // Arrange
        when(sportEquipmentRepositoryMock.load()).thenReturn(new ArrayList<>());

        // Act
        sportEquipmentService.print();

        // Assert
        verify(appHelperSportEquipmentMock, never()).printList(anyList());
    }

    // Тест для метода print() с непустым списком
    @Test
    public void testPrintWithEquipments() {
        // Arrange
        List<SportEquipment> equipmentList = Arrays.asList(new SportEquipment(), new SportEquipment());
        when(sportEquipmentRepositoryMock.load()).thenReturn(equipmentList);

        // Act
        sportEquipmentService.print();

        // Assert
        verify(appHelperSportEquipmentMock).printList(equipmentList);
    }

    // Тест для метода list()
    @Test
    public void testList() {
        // Arrange
        List<SportEquipment> equipmentList = Arrays.asList(new SportEquipment(), new SportEquipment());
        when(sportEquipmentRepositoryMock.load()).thenReturn(equipmentList);

        // Act
        List<SportEquipment> result = sportEquipmentService.list();

        // Assert
        assertEquals(equipmentList, result);
    }

    // Тест для метода edit() - null updatedEquipment
    @Test
    public void testEditNullEquipment() {
        // Act
        boolean result = sportEquipmentService.edit(null);

        // Assert
        assertFalse(result);
    }

    // Тест для метода edit() - updatedEquipment с null ID
    @Test
    public void testEditEquipmentWithNullId() {
        // Arrange
        SportEquipment updatedEquipment = new SportEquipment();
        updatedEquipment.setId(null);

        // Act
        boolean result = sportEquipmentService.edit(updatedEquipment);

        // Assert
        assertFalse(result);
    }

    // Тест для метода edit() - оборудование найдено и обновлено
    @Test
    public void testEditSuccess() {
        // Arrange
        UUID id123 = UUID.randomUUID();
        SportEquipment updatedEquipment = new SportEquipment();
        updatedEquipment.setId(id123);

        SportEquipment existingEquipment = new SportEquipment();
        existingEquipment.setId(id123);

        List<SportEquipment> equipmentList = new ArrayList<>();
        equipmentList.add(existingEquipment);

        when(sportEquipmentRepositoryMock.load()).thenReturn(equipmentList);

        // Act
        boolean result = sportEquipmentService.edit(updatedEquipment);

        // Assert
        assertTrue(result);
        assertEquals(updatedEquipment, equipmentList.get(0));
        verify(sportEquipmentRepositoryMock).save(equipmentList);
    }

    // Тест для метода edit() - оборудование не найдено
    @Test
    public void testEditEquipmentNotFound() {
        // Arrange
        UUID id123 = UUID.randomUUID();
        SportEquipment updatedEquipment = new SportEquipment();
        updatedEquipment.setId(id123);

        SportEquipment existingEquipment = new SportEquipment();
        existingEquipment.setId(UUID.randomUUID()); // Различный UUID

        List<SportEquipment> equipmentList = new ArrayList<>();
        equipmentList.add(existingEquipment);

        when(sportEquipmentRepositoryMock.load()).thenReturn(equipmentList);

        // Act
        boolean result = sportEquipmentService.edit(updatedEquipment);

        // Assert
        assertFalse(result);
        verify(sportEquipmentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода removeProduct() - пустой список
    @Test
    public void testRemoveProductEmptyList() {
        // Arrange
        when(sportEquipmentRepositoryMock.load()).thenReturn(new ArrayList<>());

        // Act
        sportEquipmentService.removeProduct();

        // Assert
        verify(inputProviderMock, never()).getInput();
        verify(sportEquipmentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода removeProduct() - корректный индекс
    @Test
    public void testRemoveProductSuccess() {
        // Arrange
        SportEquipment equipment1 = new SportEquipment();
        equipment1.setName("Equipment1");
        SportEquipment equipment2 = new SportEquipment();
        equipment2.setName("Equipment2");

        List<SportEquipment> equipmentList = new ArrayList<>();
        equipmentList.add(equipment1);
        equipmentList.add(equipment2);

        when(sportEquipmentRepositoryMock.load()).thenReturn(equipmentList);
        when(inputProviderMock.getInput()).thenReturn("1");

        // Act
        sportEquipmentService.removeProduct();

        // Assert
        assertEquals(1, equipmentList.size());
        assertEquals(equipment2, equipmentList.get(0));
        verify(sportEquipmentRepositoryMock).save(equipmentList);
    }

    // Тест для метода removeProduct() - некорректный индекс
    @Test
    public void testRemoveProductInvalidIndex() {
        // Arrange
        SportEquipment equipment1 = new SportEquipment();
        equipment1.setName("Equipment1");

        List<SportEquipment> equipmentList = new ArrayList<>();
        equipmentList.add(equipment1);

        when(sportEquipmentRepositoryMock.load()).thenReturn(equipmentList);
        when(inputProviderMock.getInput()).thenReturn("5");

        // Act
        sportEquipmentService.removeProduct();

        // Assert
        assertEquals(1, equipmentList.size());
        verify(sportEquipmentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода removeProduct() - нечисловой ввод
    @Test
    public void testRemoveProductNonNumericInput() {
        // Arrange
        SportEquipment equipment1 = new SportEquipment();
        equipment1.setName("Equipment1");

        List<SportEquipment> equipmentList = new ArrayList<>();
        equipmentList.add(equipment1);

        when(sportEquipmentRepositoryMock.load()).thenReturn(equipmentList);
        when(inputProviderMock.getInput()).thenReturn("abc");

        // Act
        sportEquipmentService.removeProduct();

        // Assert
        assertEquals(1, equipmentList.size());
        verify(sportEquipmentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода editProduct() - успешный случай
    @Test
    public void testEditProductSuccess() {
        // Arrange
        SportEquipment equipment = new SportEquipment();
        equipment.setId(UUID.randomUUID());
        equipment.setName("Old Name");
        equipment.setCategories(Arrays.asList("Category1"));
        equipment.setPrice(100.0);

        List<SportEquipment> equipmentList = new ArrayList<>();
        equipmentList.add(equipment);

        when(sportEquipmentRepositoryMock.load()).thenReturn(equipmentList);
        when(inputProviderMock.getInput()).thenReturn("1", "New Name", "Category2,Category3", "200.0");

        // Act
        sportEquipmentService.editProduct();

        // Assert
        assertEquals("New Name", equipment.getName());
        assertEquals(Arrays.asList("Category2", "Category3"), equipment.getCategories());
        assertEquals(200.0, equipment.getPrice(), 0.001);
        verify(sportEquipmentRepositoryMock).save(equipmentList);
    }

    // Тест для метода editProduct() - некорректный индекс
    @Test
    public void testEditProductInvalidIndex() {
        // Arrange
        SportEquipment equipment = new SportEquipment();
        equipment.setId(UUID.randomUUID());
        equipment.setName("Equipment1");

        List<SportEquipment> equipmentList = new ArrayList<>();
        equipmentList.add(equipment);

        when(sportEquipmentRepositoryMock.load()).thenReturn(equipmentList);
        when(inputProviderMock.getInput()).thenReturn("5");

        // Act
        sportEquipmentService.editProduct();

        // Assert
        verify(sportEquipmentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода editProduct() - нечисловой ввод индекса
    @Test
    public void testEditProductNonNumericIndex() {
        // Arrange
        SportEquipment equipment = new SportEquipment();
        equipment.setId(UUID.randomUUID());
        equipment.setName("Equipment1");

        List<SportEquipment> equipmentList = new ArrayList<>();
        equipmentList.add(equipment);

        when(sportEquipmentRepositoryMock.load()).thenReturn(equipmentList);
        when(inputProviderMock.getInput()).thenReturn("abc");

        // Act
        sportEquipmentService.editProduct();

        // Assert
        verify(sportEquipmentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода editProduct() - некорректный ввод цены
    @Test
    public void testEditProductInvalidPriceInput() {
        // Arrange
        SportEquipment equipment = new SportEquipment();
        equipment.setId(UUID.randomUUID());
        equipment.setName("Old Name");
        equipment.setCategories(Arrays.asList("Category1"));
        equipment.setPrice(100.0);

        List<SportEquipment> equipmentList = new ArrayList<>();
        equipmentList.add(equipment);

        when(sportEquipmentRepositoryMock.load()).thenReturn(equipmentList);
        when(inputProviderMock.getInput()).thenReturn("1", "New Name", "Category2", "invalid price");

        // Act
        sportEquipmentService.editProduct();

        // Assert
        // Проверяем, что оборудование не обновлено
        assertEquals("Old Name", equipment.getName());
        assertEquals(Arrays.asList("Category1"), equipment.getCategories());
        assertEquals(100.0, equipment.getPrice(), 0.001);
        verify(sportEquipmentRepositoryMock, never()).save(anyList());
    }

    // Тест для метода editProduct() - пустой список
    @Test
    public void testEditProductEmptyList() {
        // Arrange
        when(sportEquipmentRepositoryMock.load()).thenReturn(new ArrayList<>());
        when(inputProviderMock.getInput()).thenReturn("1");

        // Act
        sportEquipmentService.editProduct();

        // Assert
        verify(sportEquipmentRepositoryMock, never()).save(anyList());
    }
}

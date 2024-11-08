package services;

import org.example.Model.Client;
import org.example.Services.ClientService;
import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private FileRepository<Client> clientRepository;

    @Mock
    private AppHelper<Client> appHelperClient;

    @Mock
    private Input inputProvider;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void remove_ShouldRemoveClientSuccessfully() {
        // Arrange
        Client client = new Client(UUID.randomUUID(), "John", "Doe", "1234567890");
        List<Client> clientList = new ArrayList<>(List.of(client));

        // Настройка мока: метод `load()` возвращает изменяемый список клиентов
        when(clientRepository.load()).thenReturn(clientList);
        when(inputProvider.getInput()).thenReturn("1"); // Указываем корректный индекс для удаления

        // Act
        clientService.removeClient();

        // Assert
        assertEquals(0, clientList.size(), "Client list should be empty after removal."); // Проверка, что клиент удален
        verify(clientRepository, times(1)).save(clientList); // Проверка вызова save() с пустым списком
    }

    @Test
    void edit_ShouldEditClientSuccessfully() {
        // Arrange
        Client client = new Client(UUID.randomUUID(), "John", "Doe", "1234567890");
        List<Client> clientList = new ArrayList<>(List.of(client));

        when(clientRepository.load()).thenReturn(clientList);
        when(inputProvider.getInput()).thenReturn("1", "Jane", "Doe", "0987654321");

        // Act
        clientService.editClient();

        // Assert
        assertEquals("Jane", clientList.get(0).getFirstname(), "Client first name should be updated to Jane.");
        assertEquals("Doe", clientList.get(0).getLastname(), "Client last name should remain Doe.");
        assertEquals("0987654321", clientList.get(0).getPhone(), "Client phone should be updated.");
        verify(clientRepository, times(1)).save(clientList);
    }

    @Test
    void add_ShouldAddClientSuccessfully() {
        // Arrange
        Client client = new Client(UUID.randomUUID(), "John", "Doe", "1234567890");
        List<Client> clientList = new ArrayList<>();

        when(appHelperClient.create()).thenReturn(client);
        when(clientRepository.load()).thenReturn(clientList);

        // Act
        boolean result = clientService.add();

        // Assert
        assertTrue(result, "Adding client should succeed.");
        assertEquals(1, clientList.size(), "Client list should contain one client after adding.");
        assertEquals(client, clientList.get(0), "The added client should match the expected client.");
        verify(clientRepository, times(1)).save(clientList);
    }

    @Test
    void add_ShouldReturnFalseWhenCustomerCreationFails() {
        // Arrange
        when(appHelperClient.create()).thenReturn(null);

        // Act
        boolean result = clientService.add();

        // Assert
        assertFalse(result, "Adding should fail when creation returns null.");
        verify(clientRepository, never()).save(anyList());
    }

    @Test
    void print_ShouldPrintClientList() {
        // Arrange
        List<Client> clients = List.of(new Client(UUID.randomUUID(), "John", "Doe", "1234567890"));
        when(clientRepository.load()).thenReturn(clients);

        // Act
        clientService.print();

        // Assert
        verify(appHelperClient, times(1)).printList(clients);
    }

    @Test
    void remove_ShouldReturnFalseWhenIndexIsInvalid() {
        // Arrange
        Client client = new Client(UUID.randomUUID(), "John", "Doe", "1234567890");
        List<Client> clientList = new ArrayList<>(List.of(client));

        when(clientRepository.load()).thenReturn(clientList);
        when(inputProvider.getInput()).thenReturn("2"); // Некорректный индекс

        // Act
        clientService.removeClient();

        // Assert
        assertEquals(1, clientList.size(), "Client list size should remain the same.");
        verify(clientRepository, never()).save(anyList());
    }

    @Test
    void remove_ShouldReturnFalseWhenInputIsInvalid() {
        // Arrange
        Client client = new Client(UUID.randomUUID(), "John", "Doe", "1234567890");
        List<Client> clientList = new ArrayList<>(List.of(client));

        when(clientRepository.load()).thenReturn(clientList);
        when(inputProvider.getInput()).thenReturn("invalid"); // Некорректный ввод

        // Act
        clientService.removeClient();

        // Assert
        assertEquals(1, clientList.size(), "Client list size should remain the same after invalid input.");
        verify(clientRepository, never()).save(anyList());
    }

}

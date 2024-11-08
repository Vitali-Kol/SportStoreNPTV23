package services;

import org.example.Model.Client;
import org.example.Services.ClientService;
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

class ClientServiceTest {

    private ClientService clientService;
    private List<Client> clients;
    private AppHelper<Client> appHelperClient;
    private Input inputProvider;
    private FileRepository<Client> clientRepository;

    @BeforeEach
    void setUp() {
        // Инициализация моков
        clients = new ArrayList<>();
        appHelperClient = mock(AppHelper.class);
        inputProvider = mock(Input.class);
        clientRepository = mock(FileRepository.class);

        // Инициализация ClientService с моками
        clientService = new ClientService(clients, appHelperClient, inputProvider, clientRepository);
    }

    @Test
    void testAddClientSuccess() {
        // Настройка поведения для добавления клиента
        Client client = new Client("Иван", "Иванов", "123456789");
        when(appHelperClient.create()).thenReturn(client);

        // Вызываем метод add и проверяем результат
        boolean result = clientService.add();

        // Проверяем, что клиент был добавлен
        assertTrue(result, "Клиент должен быть добавлен успешно");
        assertEquals(1, clients.size(), "Список клиентов должен содержать 1 элемент");
        assertEquals(client, clients.get(0), "Добавленный клиент должен соответствовать ожидаемому");

        // Проверяем, что клиент был сохранен в репозитории
        verify(clientRepository).save(clients);
        verify(appHelperClient).create();
    }

    @Test
    void testAddClientFailure() {
        // Настройка поведения для создания клиента (возвращаем null)
        when(appHelperClient.create()).thenReturn(null);

        // Вызываем метод add и проверяем результат
        boolean result = clientService.add();

        // Проверяем, что клиент не был добавлен
        assertFalse(result, "Клиент не должен быть добавлен");
        assertTrue(clients.isEmpty(), "Список клиентов должен оставаться пустым");

        // Проверяем, что метод save не был вызван
        verify(clientRepository, never()).save(anyList());
        verify(appHelperClient).create();
    }

    @Test
    void testPrintClients() {
        // Создаем список клиентов для теста
        clients.add(new Client("Иван", "Иванов", "123456789"));
        clients.add(new Client("Анна", "Петрова", "987654321"));

        // Вызываем метод print, чтобы проверить корректность вывода списка
        clientService.print();

        // Проверяем, что метод printList был вызван на appHelperClient
        verify(appHelperClient).printList(clients);
    }

    @Test
    void testListClients() {
        // Настраиваем репозиторий для возврата списка клиентов
        List<Client> mockClients = List.of(
                new Client("Иван", "Иванов", "123456789"),
                new Client("Анна", "Петрова", "987654321")
        );
        when(clientRepository.load()).thenReturn(mockClients);

        // Вызываем метод list и проверяем возвращаемый список
        List<Client> result = clientService.list();

        assertEquals(mockClients, result, "Список клиентов должен соответствовать загруженному из репозитория");
        verify(clientRepository).load();
    }
}

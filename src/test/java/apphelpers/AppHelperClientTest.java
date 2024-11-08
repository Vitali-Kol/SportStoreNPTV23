package apphelpers;

import org.example.AppHelperClient;
import org.example.Model.Client;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppHelperClientTest {

    private AppHelperClient appHelperClient;
    private FileRepository<Client> clientRepository;
    private Input inputProvider;

    @BeforeEach
    void setUp() {
        // Мокаем репозиторий и интерфейс ввода
        clientRepository = mock(FileRepository.class);
        inputProvider = mock(Input.class);

        // Инициализируем AppHelperClient с мок-объектами
        appHelperClient = new AppHelperClient(clientRepository, inputProvider);
    }

    @Test
    void testCreateClient() {
        // Настраиваем ввод для создания клиента
        when(inputProvider.getInput())
                .thenReturn("Иван")     // Имя клиента
                .thenReturn("Иванов")   // Фамилия клиента
                .thenReturn("123456789"); // Телефон клиента

        // Создаем клиента
        Client client = appHelperClient.create();

        // Проверяем, что клиент создан корректно
        assertNotNull(client, "Клиент должен быть создан");
        assertEquals("Иван", client.getFirstname());
        assertEquals("Иванов", client.getLastname());
        assertEquals("123456789", client.getPhone());
    }

    @Test
    void testPrintClientList() {
        // Создаем список клиентов для теста
        List<Client> clients = new ArrayList<>();
        clients.add(new Client("Иван", "Иванов", "123456789"));
        clients.add(new Client("Анна", "Петрова", "987654321"));

        // Настраиваем вывод списка клиентов
        appHelperClient.printList(clients);

    }
}

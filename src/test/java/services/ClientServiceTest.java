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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    private AppHelper<Client> appHelperClient;
    private Input inputProvider;
    private FileRepository<Client> clientRepository;
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        appHelperClient = mock(AppHelper.class);
        inputProvider = mock(Input.class);
        clientRepository = mock(FileRepository.class);
        clientService = new ClientService(appHelperClient, inputProvider, clientRepository);
    }

    @Test
    void testEditClientSuccess() {
        // Создаем клиента с уникальным ID
        Client originalClient = new Client();
        originalClient.setId(UUID.randomUUID());
        originalClient.setFirstname("John");
        originalClient.setLastname("Doe");
        originalClient.setPhone("123456789");

        // Моделируем ситуацию, когда клиент уже добавлен в репозиторий
        List<Client> clients = new ArrayList<>();
        clients.add(originalClient);
        when(clientRepository.load()).thenReturn(clients);

        // Создаем клиента с обновленными данными, но тем же ID
        Client updatedClient = new Client();
        updatedClient.setId(originalClient.getId()); // Устанавливаем тот же ID для успешного редактирования
        updatedClient.setFirstname("Jane");
        updatedClient.setLastname("Doe");
        updatedClient.setPhone("987654321");

        // Выполняем редактирование
        boolean result = clientService.edit(updatedClient);

        assertTrue(result, "Редактирование должно быть успешным");
        verify(clientRepository, times(1)).save(clients); // Проверяем, что изменения были сохранены
        assertTrue(clients.contains(updatedClient));
    }
}

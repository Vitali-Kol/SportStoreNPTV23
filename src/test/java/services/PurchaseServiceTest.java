package services;

import org.example.Model.Purchase;
import org.example.Services.PurchaseService;
import org.example.interfaces.AppHelper;
import org.example.interfaces.FileRepository;
import org.example.interfaces.Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseServiceTest {
    private AppHelper<Purchase> appHelperPurchase;
    private Input inputProvider;
    private FileRepository<Purchase> purchaseRepository;
    private PurchaseService purchaseService;

    @BeforeEach
    void setUp() {
        appHelperPurchase = mock(AppHelper.class);
        inputProvider = mock(Input.class);
        purchaseRepository = mock(FileRepository.class);

        purchaseService = new PurchaseService(appHelperPurchase, inputProvider, purchaseRepository);
    }

    @Test
    void testAddPurchaseSuccess() {
        Purchase purchase = new Purchase(UUID.randomUUID(), null, null, null); // можно указать реальные объекты вместо null
        List<Purchase> purchaseList = new ArrayList<>();

        when(appHelperPurchase.create()).thenReturn(purchase);
        when(purchaseRepository.load()).thenReturn(purchaseList);

        boolean result = purchaseService.add();

        assertTrue(result);
        assertEquals(1, purchaseList.size());
        assertEquals(purchase, purchaseList.get(0));
        verify(purchaseRepository, times(1)).save(purchaseList);
    }

    @Test
    void testAddPurchaseFailure() {
        when(appHelperPurchase.create()).thenReturn(null);

        boolean result = purchaseService.add();

        assertFalse(result);
        verify(purchaseRepository, never()).save(anyList());
    }

    @Test
    void testPrintPurchasesWhenEmpty() {
        when(purchaseRepository.load()).thenReturn(new ArrayList<>());

        purchaseService.print();

        verify(appHelperPurchase, never()).printList(anyList());
    }

    @Test
    void testPrintPurchasesWhenNotEmpty() {
        List<Purchase> purchaseList = List.of(
                new Purchase(UUID.randomUUID(), null, null, null),
                new Purchase(UUID.randomUUID(), null, null, null)
        );

        when(purchaseRepository.load()).thenReturn(purchaseList);

        purchaseService.print();

        verify(appHelperPurchase, times(1)).printList(purchaseList);
    }

    @Test
    void testRemovePurchaseSuccess() {
        Purchase purchaseToRemove = new Purchase(UUID.randomUUID(), null, null, null);
        List<Purchase> purchaseList = new ArrayList<>(List.of(purchaseToRemove));

        when(purchaseRepository.load()).thenReturn(purchaseList);
        when(inputProvider.getInput()).thenReturn("1");

        boolean result = purchaseService.remove(null);

        assertTrue(result);
        assertTrue(purchaseList.isEmpty());
        verify(purchaseRepository, times(1)).save(purchaseList);
    }

    @Test
    void testRemovePurchaseInvalidInput() {
        Purchase purchaseToRemove = new Purchase(UUID.randomUUID(), null, null, null);
        List<Purchase> purchaseList = new ArrayList<>(List.of(purchaseToRemove));

        when(purchaseRepository.load()).thenReturn(purchaseList);
        when(inputProvider.getInput()).thenReturn("не число");

        boolean result = purchaseService.remove(null);

        assertFalse(result);
        assertEquals(1, purchaseList.size());
        verify(purchaseRepository, never()).save(anyList());
    }

    @Test
    void testRemovePurchaseOutOfRange() {
        Purchase purchaseToRemove = new Purchase(UUID.randomUUID(), null, null, null);
        List<Purchase> purchaseList = new ArrayList<>(List.of(purchaseToRemove));

        when(purchaseRepository.load()).thenReturn(purchaseList);
        when(inputProvider.getInput()).thenReturn("2"); // Вне диапазона

        boolean result = purchaseService.remove(null);

        assertFalse(result);
        assertEquals(1, purchaseList.size());
        verify(purchaseRepository, never()).save(anyList());
    }
}

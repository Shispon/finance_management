package org.finance_manegement.server.service.impl;

import org.finance_manegement.server.models.Purpose;
import org.finance_manegement.server.repository.IRepository;
import org.finance_manegement.server.repository.impl.PurposeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PurposeServiceTest {

    private PurposeService purposeService;
    private IRepository<Purpose> purposeRepository;

    @BeforeEach
    void setUp() {
        // Мокируем репозиторий
        purposeRepository = mock(PurposeRepository.class);
        // Создаем сервис с мокированным репозиторием
        purposeService = new PurposeService(purposeRepository);
    }

    @Test
    void testAddPurpose() {
        Purpose purpose = new Purpose("Buy Laptop", 500.0, 100.0);
        purposeService.addPurpose(purpose);

        // Проверяем, что метод create был вызван в репозитории
        verify(purposeRepository, times(1)).create(purpose);
    }

    @Test
    void testGetAllPurposes() {
        Purpose purpose1 = new Purpose("Buy Laptop", 500.0, 100.0);
        Purpose purpose2 = new Purpose("Vacation", 1000.0, 200.0);
        when(purposeRepository.findAll()).thenReturn(List.of(purpose1, purpose2));

        List<Purpose> purposes = purposeService.getAllPurposes();

        assertEquals(2, purposes.size());
        assertTrue(purposes.contains(purpose1));
        assertTrue(purposes.contains(purpose2));
    }

    @Test
    void testUpdatePurpose() {
        Purpose purpose = new Purpose("Buy Laptop", 500.0, 100.0);
        when(purposeRepository.findById(1)).thenReturn(purpose);

        Purpose updatedPurpose = new Purpose("Buy Laptop", 600.0, 150.0);
        purposeService.updatePurpose(updatedPurpose, 1);

        // Проверяем, что метод update был вызван
        verify(purposeRepository, times(1)).update(updatedPurpose, 1);
    }

    @Test
    void testDeletePurpose() {
        Purpose purpose = new Purpose("Buy Laptop", 500.0, 100.0);
        when(purposeRepository.findById(1)).thenReturn(purpose);

        purposeService.deletePurpose(1);

        // Проверяем, что метод delete был вызван
        verify(purposeRepository, times(1)).delete(1);
    }

    @Test
    void testTrackProgress() {
        Purpose purpose = new Purpose("Buy Laptop", 500.0, 200.0);
        when(purposeRepository.findAll()).thenReturn(List.of(purpose));

        double progress = purposeService.trackProgress("Buy Laptop");

        // Проверяем, что прогресс вычисляется правильно
        assertEquals(40.0, progress);
    }

    @Test
    void testTrackProgressPurposeNotFound() {
        when(purposeRepository.findAll()).thenReturn(List.of());

        // Проверяем, что выбрасывается исключение, если цель не найдена
        assertThrows(IllegalArgumentException.class, () -> purposeService.trackProgress("Nonexistent Goal"));
    }

    @Test
    void testAddMoneyToPurpose() {
        Purpose purpose = new Purpose("Buy Laptop", 500.0, 100.0);
        when(purposeRepository.findById(1)).thenReturn(purpose);

        Purpose updatedPurpose = purposeService.addMoneyToPurpose(1, 50.0);

        // Проверяем, что сумма была увеличена на 50
        assertEquals(150.0, updatedPurpose.getCurrentAmount());
        verify(purposeRepository, times(1)).update(updatedPurpose, 1);
    }
}

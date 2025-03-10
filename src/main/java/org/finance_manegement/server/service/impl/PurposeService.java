package org.finance_manegement.server.service.impl;

import org.finance_manegement.server.models.Purpose;
import org.finance_manegement.server.repository.IRepository;
import org.finance_manegement.server.repository.impl.PurposeRepository;
import org.finance_manegement.server.service.IPurposeService;

import java.util.List;

public class PurposeService implements IPurposeService {
    private final IRepository<Purpose> purposeRepository ;

    public PurposeService(IRepository<Purpose> purposeRepository) {
        this.purposeRepository = purposeRepository;
    }

    @Override
    public void addPurpose(Purpose purpose) {
        purposeRepository.create(purpose);
    }

    @Override
    public List<Purpose> getAllPurposes() {
        return purposeRepository.findAll();
    }

    @Override
    public void updatePurpose(Purpose purpose, int id) {
        purposeRepository.update(purpose, id);
    }

    @Override
    public void deletePurpose(int id) {
        purposeRepository.delete(id);
    }

    @Override
    public double trackProgress(String name) {
        Purpose purpose = purposeRepository.findAll().stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Цель не найдена"));

        return (purpose.getCurrentAmount() / purpose.getTargetAmount()) * 100;
    }

    @Override
    public Purpose getPurposeById(int id) {
        return purposeRepository.findById(id);
    }

    @Override
    public Purpose addMoneyToPurpose(int id, double money) {
        Purpose purpose = getPurposeById(id);
        double newAmount = purpose.getCurrentAmount() + money;
        purpose.setCurrentAmount(newAmount);
        purposeRepository.update(purpose, id);
        return purpose;
    }
}

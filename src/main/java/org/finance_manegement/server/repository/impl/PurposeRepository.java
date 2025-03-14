package org.finance_manegement.server.repository.impl;

import org.finance_manegement.server.models.Purpose;
import org.finance_manegement.server.repository.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PurposeRepository implements IRepository<Purpose> {
    private static AtomicInteger nextPurposeId = new AtomicInteger(1);
    private List<Purpose> purposes = new ArrayList<>();
    @Override
    public void create(Purpose purpose) {
        purpose.setId(nextPurposeId.getAndIncrement());
        validatePurpose(purpose);
        purposes.add(purpose);
    }
    private void validatePurpose(Purpose purpose) {
        if (purposes.stream().filter(purpose1 -> purpose1.getName().equals(purpose.getName())).findAny().isPresent()) {
            throw new IllegalArgumentException("Purpose with name " + purpose.getName() + " already exists");
        }
    }
    @Override
    public List<Purpose> findAll() {
        return purposes;
    }

    @Override
    public Purpose findById(int id) {
        return purposes.stream().filter(purpose -> purpose.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void update(Purpose entity, int id) {
        Purpose updatedPurpose = findById(entity.getId());
        if (updatedPurpose != null) {
            updatedPurpose.setName(entity.getName());
            updatedPurpose.setCurrentAmount(entity.getCurrentAmount());
            updatedPurpose.setTargetAmount(entity.getTargetAmount());
        }
    }

    @Override
    public void delete(int id) {
        purposes.removeIf(purpose -> purpose.getId() == id);
    }
}

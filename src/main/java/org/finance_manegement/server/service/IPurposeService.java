package org.finance_manegement.server.service;

import org.finance_manegement.server.models.Purpose;

import java.util.List;

public interface IPurposeService {
    void addPurpose(Purpose purpose);
    List<Purpose> getAllPurposes();
    void updateGoal(Purpose purpose);
    void deletePurpose(Purpose purpose);
    double trackProgress(Purpose purpose);
}

package org.finance_manegement.server.service;

import org.finance_manegement.server.models.Purpose;

import java.util.List;

public interface IPurposeService {
    void addPurpose(Purpose purpose);
    List<Purpose> getAllPurposes();
    void updatePurpose(Purpose purpose, int id);
    void deletePurpose(int id);
    double trackProgress(String name);
    Purpose getPurposeById(int id);
    Purpose addMoneyToPurpose(int id, double money);
}

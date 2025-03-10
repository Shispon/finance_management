package org.finance_manegement.server.service.impl;

import org.finance_manegement.server.models.Purpose;
import org.finance_manegement.server.models.Transaction;
import org.finance_manegement.server.models.UserInfo;
import org.finance_manegement.server.repository.IRepository;
import org.finance_manegement.server.repository.impl.UserInfoRepository;
import org.finance_manegement.server.service.IUserInfoService;
import org.finance_manegement.server.service.exception.UserException;

import java.util.List;

public class UserInfoService implements IUserInfoService {
    private final IRepository<UserInfo> userInfoRepository = new UserInfoRepository();
    @Override
    public void createUserInfo(UserInfo userInfo) {
        userInfoRepository.create(userInfo);
    }

    @Override
    public void updateUserInfo(UserInfo userInfo, int id) {
        userInfoRepository.update(userInfo, userInfoRepository.findById(id).getId());
    }

    @Override
    public void deleteUserInfo(int id) {
        userInfoRepository.delete(id);
    }

    @Override
    public UserInfo getUserInfoById(int id) {
        return userInfoRepository.findById(id);
    }

    @Override
    public UserInfo getUserInfoByUserId(int userId) {
        try {
            return userInfoRepository.findAll().stream().filter(user -> user.getUserId() == userId).findFirst().orElse(null);
        } catch (Exception e) {
            throw new IllegalArgumentException("у данного пользователя еще нет транзакций");
        }
    }

    @Override
    public List<UserInfo> getAllUsersInfo() {
        return userInfoRepository.findAll();
    }

    @Override
    public UserInfo setBudget(int userId, Double budget) {
        UserInfo userInfo = getUserInfoById(userId);
        userInfo.setMonthlyBudget(budget);
        return userInfo;
    }
    public void addPurposeToUser(int userId, Purpose purpose) {
        UserInfo userInfo = getUserInfoByUserId(userId);
        userInfo.getPurposes().add(purpose);
        updateUserInfo(userInfo, userInfo.getId());
    }
    public List<Purpose> getPurposesByUser(int userId) {
        return getUserInfoByUserId(userId).getPurposes();
    }

    public void addTransactionToUser(int userId, Transaction transaction) {
        UserInfo userInfo = getUserInfoByUserId(userId);
        userInfo.getTransactions().add(transaction);
        updateUserInfo(userInfo, userInfo.getId());
    }

    public List<Transaction> getTransactionsByUser(int userId) {
        return getUserInfoByUserId(userId).getTransactions();
    }
}

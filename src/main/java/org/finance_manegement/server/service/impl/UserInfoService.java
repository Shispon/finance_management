package org.finance_manegement.server.service.impl;

import org.finance_manegement.server.models.Purpose;
import org.finance_manegement.server.models.Transaction;
import org.finance_manegement.server.models.UserInfo;
import org.finance_manegement.server.repository.IRepository;
import org.finance_manegement.server.repository.impl.*;
import org.finance_manegement.server.service.IUserInfoService;
import org.finance_manegement.server.service.exception.UserException;

import java.util.List;

public class UserInfoService implements IUserInfoService {
    private final IRepository<UserInfo> userInfoRepository = new UserInfoRepository();
    private final IRepository<Transaction> transactionRepository = new TransactionRepository();
    private final IRepository<Purpose> purposeRepository = new PurposeRepository();

    @Override
    public void createUserInfo(UserInfo userInfo) {
        userInfoRepository.create(userInfo);
    }

    @Override
    public void updateUserInfo(UserInfo userInfo, int id) {
        userInfoRepository.update(userInfo, id);
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
            return userInfoRepository.findAll().stream()
                    .filter(user -> user.getUserId() == userId)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new IllegalArgumentException("у данного пользователя еще нет информации");
        }
    }

    @Override
    public List<UserInfo> getAllUsersInfo() {
        return userInfoRepository.findAll();
    }

    @Override
    public UserInfo setBudget(int userId, Double budget) {
        UserInfo userInfo = getUserInfoById(userId);
        if (userInfo != null) {
            userInfo.setMonthlyBudget(budget);
            updateUserInfo(userInfo, userId);
        }
        return userInfo;
    }

    public void addPurposeToUser(int userId, Purpose purpose) {
        UserInfo userInfo = getUserInfoByUserId(userId);
        if (userInfo != null) {
            purpose.setUserInfoId(userInfo.getId());
            purposeRepository.create(purpose);
        }
    }

    public List<Purpose> getPurposesByUser(int userId) {
        UserInfo userInfo = getUserInfoByUserId(userId);
        if (userInfo != null) {
            return purposeRepository.findAll().stream()
                    .filter(purpose -> purpose.getUserInfoId() == userInfo.getId())
                    .toList();
        }
        return List.of();
    }

    public void addTransactionToUser(int userId, Transaction transaction) {
        UserInfo userInfo = getUserInfoByUserId(userId);
        if (userInfo != null) {
            transaction.setUserInfoId(userInfo.getId());
            transactionRepository.create(transaction);
        }
    }

    public List<Transaction> getTransactionsByUser(int userId) {
        UserInfo userInfo = getUserInfoByUserId(userId);
        if (userInfo != null) {
            return transactionRepository.findAll().stream()
                    .filter(transaction -> transaction.getUserInfoId() == userInfo.getId())
                    .toList();
        }
        return List.of();
    }
}
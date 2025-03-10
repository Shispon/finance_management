package org.finance_manegement.server.repository.impl;

import org.finance_manegement.server.models.User;
import org.finance_manegement.server.models.UserInfo;
import org.finance_manegement.server.repository.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UserInfoRepository implements IRepository<UserInfo> {
    private static AtomicInteger nextUserInfoId = new AtomicInteger(1); // Потокобезопасный счетчик
    private List<UserInfo> userInfos = new ArrayList<>();
    @Override
    public void create(UserInfo userInfo) {
        int id = nextUserInfoId.getAndIncrement();
        userInfo.setId(id);
        userInfos.add(userInfo);
    }

    @Override
    public List<UserInfo> findAll() {
        return userInfos;
    }

    @Override
    public UserInfo findById(int id) {
        return userInfos.stream().filter(userInfo -> userInfo.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void update(UserInfo userInfo, int id) {
        UserInfo user = findById(id);
        if(user != null) {
            userInfo.setUserId(userInfo.getUserId());
            userInfo.setPurposes(userInfo.getPurposes());
            userInfo.setTransactions(userInfo.getTransactions());
            userInfo.setMonthlyBudget(userInfo.getMonthlyBudget());
        }
    }

    @Override
    public void delete(int id) {
        if(findById(id) != null) {
            userInfos.removeIf(userInfo -> userInfo.getId() == id);
        }
    }
}

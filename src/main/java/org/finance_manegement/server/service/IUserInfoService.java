package org.finance_manegement.server.service;

import org.finance_manegement.server.models.UserInfo;

import java.util.List;

public interface IUserInfoService {
    void createUserInfo(UserInfo userInfo);
    void updateUserInfo(UserInfo userInfo);
    void deleteUserInfo(UserInfo userInfo);
    UserInfo getUserInfoById(int id);
    UserInfo getUserInfoByUserId(int userId);
    List<UserInfo> getAllUsersInfo();
}

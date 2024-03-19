package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

public interface UserService {

    /**
     * 微信登录，需要返回jwt令牌
     * @param userLoginDTO
     * @return
     */
        User wxLogin(UserLoginDTO userLoginDTO);


}

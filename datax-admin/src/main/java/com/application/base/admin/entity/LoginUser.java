package com.application.base.admin.entity;

import lombok.Data;

/**
 * Created by admin on 2019/11/17
 */
@Data
public class LoginUser {

    private String username;
    private String password;
    private Integer rememberMe;

}

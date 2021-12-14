package com.yjxxt.crm.query;

import com.yjxxt.crm.base.BaseQuery;

/**
 *条件查询
 */
public class UserQuery  extends BaseQuery {
    private String userName;
    private String email;
    private String phone;

    public UserQuery() {
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "UserQuery{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

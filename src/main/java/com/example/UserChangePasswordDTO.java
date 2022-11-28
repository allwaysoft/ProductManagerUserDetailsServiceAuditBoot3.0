package com.example;

import lombok.Data;

@Data
public class UserChangePasswordDTO {

    private Long id;

    private String password;

    @ValidPassword
    private String newPass;
    @ValidPassword
    private String confirmPass;

}

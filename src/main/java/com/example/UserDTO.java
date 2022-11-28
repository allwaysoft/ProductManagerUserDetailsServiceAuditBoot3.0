package com.example;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String email;
    @ValidPassword
    private String password;

    private String username;

    private String name;

    private String homepage;

    private Boolean enabled;

    private Boolean accountNonLocked;

}

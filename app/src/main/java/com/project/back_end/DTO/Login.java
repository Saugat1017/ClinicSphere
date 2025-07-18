package com.project.back_end.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    // 1. Email used for authentication
    private String email;
    // 2. Password used for login (typically hashed during authentication)
    private String password;
}

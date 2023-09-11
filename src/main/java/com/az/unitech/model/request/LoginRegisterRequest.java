package com.az.unitech.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginRegisterRequest {
    @NonNull
    private String pin;
    @NonNull
    private String password;
}

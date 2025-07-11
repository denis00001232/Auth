package com.stupor.auth.dto.kafka;

import com.stupor.auth.dto.controller.UserRegisterDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MogRegisteredDto {
    private String username;
    private String callSign;

    public MogRegisteredDto(UserRegisterDto userRegisterDto) {
        username = userRegisterDto.getUsername();
        callSign = "Зарегистрированный";
    }
}

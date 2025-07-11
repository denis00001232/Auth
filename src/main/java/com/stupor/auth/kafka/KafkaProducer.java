package com.stupor.auth.kafka;

import com.stupor.auth.dto.controller.UserLoginDto;
import com.stupor.auth.dto.kafka.MogDeletedDto;
import com.stupor.auth.dto.kafka.MogEnteredDto;
import com.stupor.auth.dto.kafka.MogQuitDto;
import com.stupor.auth.dto.kafka.MogRegisteredDto;
import com.stupor.auth.util.StuporMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, Object message) {
        kafkaTemplate.send(topic, StuporMapper.serialize(message));
    }

    public void sendLogin(UserLoginDto userLoginDto) {
        MogEnteredDto mogEnteredDto = new MogEnteredDto();
        mogEnteredDto.setUsername(userLoginDto.getUsername());
        mogEnteredDto.setCallSign(userLoginDto.getCallSign());
        send("mog-entered", mogEnteredDto);
    }


    public void sendLogout(String username) {
        MogQuitDto mogQuitDto = new MogQuitDto();
        mogQuitDto.setUsername(username);
        send("mog-quit", mogQuitDto);
    }

    public void sendRegister(MogRegisteredDto mogRegisteredDto) {
        send("mog-registered", mogRegisteredDto);
    }

    public void sendDeleted(MogDeletedDto mogDeletedDto) {
        send("mog-registered", mogDeletedDto);
    }



}

package com.omon4412.notificationservice.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewLoginData {
    protected UserDto userDto;
    protected SessionDetails sessionDetails;
    protected LocalDateTime timestamp;
}

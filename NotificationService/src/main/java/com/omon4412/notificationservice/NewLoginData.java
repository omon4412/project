package com.omon4412.notificationservice;

import lombok.Data;

@Data
public class NewLoginData {
    protected UserDto userDto;
    protected SessionDetails sessionDetails;
}

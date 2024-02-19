package com.omon4412.authservice.model;

import com.omon4412.authservice.dto.UserDto;
import lombok.Data;

@Data
public class NewLoginData {
    protected UserDto userDto;
    protected SessionDetails sessionDetails;
}

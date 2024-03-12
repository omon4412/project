package com.omon4412.authservice.dto;

import com.omon4412.authservice.model.SessionDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SessionDetailsDto {
    private String sessionId;
    private SessionDetails sessionDetails;
}

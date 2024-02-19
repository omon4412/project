package com.omon4412.authservice.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SessionDetails implements Serializable {
    private String userAgent;
    private String remoteAddress;
}

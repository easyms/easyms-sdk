package com.easyms.sampleapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Client
{
    private String name;
    private String lastName;
    private String email;
}

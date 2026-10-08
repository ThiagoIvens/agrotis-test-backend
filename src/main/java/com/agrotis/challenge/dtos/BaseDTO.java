package com.agrotis.challenge.dtos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDTO {
    private UUID id;
    private String name;
    private String registration;
    private String address;
}
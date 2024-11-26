package com.software.modsen.passengerservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "passengers")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {
    @Id
    private Long id;
    private String name;
    private String email;
    private String phone;
    private boolean deleted = Boolean.FALSE;
    @Version
    private int version;
}

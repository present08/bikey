package com.bikey.server.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Bikey {
    @Id
    private Long order_num;

    private String division = "";

    private String name = "";

    private String phone = "";

    private String model = "";

    private String model_option = "";

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;

    private boolean completeState = false;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate completionDate;

}

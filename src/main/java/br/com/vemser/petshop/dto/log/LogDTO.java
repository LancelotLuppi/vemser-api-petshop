package br.com.vemser.petshop.dto.log;

import lombok.Data;

import java.util.Date;

@Data
public class LogDTO {

    private Date date;
    private String level;
    private String message;
}

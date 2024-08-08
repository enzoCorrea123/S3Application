package com.cloud.s3.cloudapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class File{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFile;
    private String ref;
    private LocalDate data;
    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Task task;

}

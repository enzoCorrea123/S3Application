package com.cloud.s3.cloudapplication.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTask;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "task")
    private List<File> files;
    @Column(nullable = false)
    private String titulo;
}

package com.jwt.implementation.repository;
import com.jwt.implementation.model.FileDB;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileDB, String> {

   
  //List<FileDB> findAll();
    
} 
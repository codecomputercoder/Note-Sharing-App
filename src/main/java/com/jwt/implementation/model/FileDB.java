package com.jwt.implementation.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name="FilesTable")
public class FileDB {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

  private String name;

  private String type;

  private String username;

  @Lob
  private byte[] data;

  public FileDB() {
    // Default constructor
}
  
  public FileDB( String name, String type, String username, byte[] data) {
    this.name = name;
    this.type = type;
    this.username = username;
    this.data = data;
}

public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getType() {
    return type;
}

public void setType(String type) {
    this.type = type;
}

public String getUsername() {
    return username;
}

public void setUsername(String username) {
    this.username = username;
}

public byte[] getData() {
    return data;
}

public void setData(byte[] data) {
    this.data = data;
}


}

package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection="user")
public class User {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	@Column(unique = true)
	private String email;
	private String name;
	private String password;
	private String role = "USER";
	private boolean enabled = true;
	private LocalDate birthday;

	@Column(insertable = true, updatable = false)
	private LocalDate created;
	private LocalDate modified;
	
	public User() {
	}

	public User(String email, String name, String password, String birthday) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.birthday = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

	}

	public User(String email, String name, String password, LocalDate birthday) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.birthday = birthday;
	}

	public User(String email, String name, String password, String birthday, String role, boolean enabled) {
		this(email, name, password, birthday);
		this.role = role;
		this.enabled = enabled;
	}

	public User(String email, String name, String password, String birthday, String role, boolean enabled,
			String created, String modified) {
		this(email, name, password, birthday);
		this.role = role;
		this.enabled = enabled;
		this.created = LocalDate.parse(created, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		this.modified = LocalDate.parse(modified, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	public User(String email, String name, String password, String birthday, String role, boolean enabled,
			LocalDate created, LocalDate modified) {
		this(email, name, password, birthday);
		this.role = role;
		this.enabled = enabled;
		this.created = created;
		this.modified = modified;
	}

	public LocalDate getCreated() {
		return created;
	}

	public void setCreated(LocalDate created) {
		this.created = created;
	}

	public LocalDate getModified() {
		return modified;
	}

	public void setModified(LocalDate modified) {
		this.modified = modified;
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
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
	
	@Override 
	public String toString() {
		return " name: " + this.name + " role: " + this.role ;
	}

//    @PrePersist
//    void onCreate() {
//        this.setCreated(LocalDateTime.now());
//        this.setModified(LocalDateTime.now());
//    }
//
//	@PreUpdate
//    void onUpdate() {
//        this.setModified(LocalDateTime.now());
//    }
}

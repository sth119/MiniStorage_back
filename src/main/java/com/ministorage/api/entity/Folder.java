package com.ministorage.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Folder {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "folder_name")
	private String name;
	
	
	@Column(name = "folder_parentid")
	private Long parentId;
	
	@Column(name = "folder_userid")
	private Long userId;
	
	@Column(name = "is_trashed")
	private boolean isTrashed = false;

	
}

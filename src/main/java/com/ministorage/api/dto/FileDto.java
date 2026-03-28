package com.ministorage.api.dto;

import java.util.List;

import lombok.Data;

@Data
public class FileDto {
	private List<Long>  fileIds;
	private Long targetFolderId;
	private Long userId;
}

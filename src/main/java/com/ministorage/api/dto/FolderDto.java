package com.ministorage.api.dto;

import java.util.List;

import lombok.Data;

@Data
public class FolderDto {

	private List<Long> fileIds;
	private Long targetFolderId;
}

package com.ministorage.api.dto;

public record FileCreateRequest (
		String title,
		String url,
		String type,
		Long folderId
) {}

package com.ministorage.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ministorage.api.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {
    
    // 1. 내 파일 목록 조회 (휴지통 X, 순서대로)
    // 기존 Service에서 호출하는 이름: findByUserIdAndTrashedFalseOrderByOrderIndexAsc
    List<File> findByUserIdAndTrashedFalseOrderByOrderIndexAsc(Long userId);
    
    // 2. 내 휴지통 목록 조회 (휴지통 O)
    // 기존 Service에서 호출하는 이름: findByUserIdAndTrashedTrue
    List<File> findByUserIdAndTrashedTrue(Long userId);
}
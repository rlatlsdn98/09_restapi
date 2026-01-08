package com.ohgiraffers.cqrs.common.service;

import com.ohgiraffers.cqrs.exception.BusinessException;
import com.ohgiraffers.cqrs.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/*
 * [파일 저장 서비스]
 * 파일 업로드 및 삭제와 관련된 로직을 캡슐화한 서비스입니다.
 * 컨트롤러나 다른 서비스에서 파일 처리가 필요할 때 이 클래스를 주입받아 사용합니다.
 */
@Service
@Slf4j
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService(@Value("${image.image-dir}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir);
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            log.error("업로드 디렉터리 생성 실패: {}", e.getMessage());
            throw new BusinessException(ErrorCode.FILE_SAVE_ERROR);
        }
    }

    /* 파일 저장: UUID를 사용하여 파일명 중복을 방지합니다. */
    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.FILE_SAVE_ERROR);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        /* UUID.randomUUID()를 사용하여 고유한 파일명을 생성합니다. */
        String fileName = UUID.randomUUID()
                + (extension != null && !extension.isEmpty() ? "." + extension : "");
        Path targetLocation = uploadDir.resolve(fileName);

        try (InputStream inputStream = file.getInputStream()) {
            /* StandardCopyOption.REPLACE_EXISTING: 동일한 이름의 파일이 있으면 덮어씁니다. */
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            log.error("파일 저장 실패 [{}]: {}", fileName, ex.getMessage());
            throw new BusinessException(ErrorCode.FILE_SAVE_ERROR);
        }

        return fileName;
    }

    public void deleteFile(String fileName) {
        Path filePath = this.uploadDir.resolve(fileName);
        try {
            if (!Files.deleteIfExists(filePath)) {
                log.warn("삭제할 파일이 존재하지 않음: {}", filePath);
            }
        } catch (IOException ex) {
            log.error("파일 삭제 실패 [{}]: {}", fileName, ex.getMessage());
            throw new BusinessException(ErrorCode.FILE_DELETE_ERROR);
        }
    }
}
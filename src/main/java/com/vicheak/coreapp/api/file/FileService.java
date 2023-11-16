package com.vicheak.coreapp.api.file;

import com.vicheak.coreapp.api.file.web.FileDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    /**
     * This method is used to download any resources with the linked name
     * @param name is the path parameter from client
     * @return Resource
     */
    Resource downloadByName(String name);

    /**
     * This method is used to upload single resource into the system (server path)
     * @param file is the request part (multipart form) from client
     * @return FileDto
     */
    FileDto uploadSingle(MultipartFile file);

    /**
     * This method is used to upload single image but it has some restriction for image extension
     * -- List of image extension : .png, .jpg, .webp, .jpeg,...
     * @param file is the request path (multipart form) from client
     * @return FileDto
     */
    FileDto uploadSingleRestrictImage(MultipartFile file);

    /**
     * This method is used to upload multiple resources into the system
     * @param files is the list of request part (multipart form) from client
     * @return List<FileDto>
     */
    List<FileDto> uploadMultiple(List<MultipartFile> files);

    /**
     * This method is used to load specific file by linked name
     * @param name is the path parameter from client
     * @return FileDto
     * @throws IOException is the file exception
     */
    FileDto loadFileByName(String name) throws IOException;

    /**
     * This method is used to load all files from the system (server path)
     * @return List<FileDto>
     */
    List<FileDto> loadAllFiles();

    /**
     * This method is used to delete specific file by linked name
     * @param name is the path parameter from client
     */
    void deleteFileByName(String name);

    /**
     * This method is used to delete the whole resources from the system
     * -- Be aware for the api with this method --
     */
    void deleteAllFiles();

}

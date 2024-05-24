package com.vicheak.coreapp.api.file;

import com.vicheak.coreapp.api.file.web.FileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {

    @Value("${file.server-path}")
    private String serverPath;

    @Value("${file.base-uri}")
    private String baseUri;

    @Value("${file.download-uri}")
    private String downloadUri;

    @Override
    public Resource downloadByName(String name) {
        Path path = Paths.get(serverPath + name);

        if (Files.exists(path))
            //start loading the resource by file name
            return UrlResource.from(path.toUri());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Resource has not been found!");
    }

    @Override
    public FileDto uploadSingle(MultipartFile file) {
        return save(file);
    }

    @Override
    public FileDto uploadSingleRestrictImage(MultipartFile file) {
        //check a valid image extension
        String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        if (extension.equals("png") ||
                extension.equals("jpg") ||
                extension.equals("webp") ||
                extension.equals("jpeg"))
            return save(file);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Unsupported image extension!");
    }

    @Override
    public List<FileDto> uploadMultiple(List<MultipartFile> files) {
        return files.stream()
                .map(this::save)
                .toList();
    }

    @Override
    public FileDto loadFileByName(String name) throws IOException {
        Path path = Paths.get(serverPath + name);

        if (Files.exists(path)) {
            //start loading the resource by file name
            Resource resource = UrlResource.from(path.toUri());

            return FileDto.builder()
                    .name(name)
                    .uri(baseUri + name)
                    .downloadUri(downloadUri + name)
                    .size(resource.contentLength())
                    .extension(getExtension(name))
                    .build();
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Resource has not been found!");
    }

    @Override
    public List<FileDto> loadAllFiles() {
        List<FileDto> fileDtos = new ArrayList<>();
        Path path = Paths.get(serverPath);

        try (
                Stream<Path> pathStream = Files.list(path);
        ) {
            List<Path> pathList = pathStream.toList();
            if ((long) pathList.size() == 0)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "There are no resources in the system!");

            for (Path p : pathList) {
                Resource resource = UrlResource.from(p.toUri());
                fileDtos.add(FileDto.builder()
                        .name(resource.getFilename())
                        .uri(baseUri + resource.getFilename())
                        .downloadUri(downloadUri + resource.getFilename())
                        .size(resource.contentLength())
                        .extension(getExtension(Objects.requireNonNull(resource.getFilename())))
                        .build());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileDtos;
    }

    @Override
    public void deleteFileByName(String name) {
        Path path = Paths.get(serverPath + name);

        try {
            if (!Files.deleteIfExists(path))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Resource has not been found!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllFiles() {
        Path path = Paths.get(serverPath);

        try (
                Stream<Path> pathStream = Files.list(path);
        ) {
            for (Path p : pathStream.toList())
                Files.delete(p);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastDotIndex + 1);
    }

    private FileDto save(MultipartFile file) {
        if (file.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "File is empty! Cannot upload the file!");

        String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));

        //create unique file name by random uuid
        String name = UUID.randomUUID() + "." + extension;

        String uri = baseUri + name;
        Long size = file.getSize();

        //create file path (absolute path)
        Path path = Paths.get(serverPath + name);

        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return FileDto.builder()
                .name(name)
                .uri(uri)
                .downloadUri(downloadUri + name)
                .size(size)
                .extension(extension)
                .build();
    }

}
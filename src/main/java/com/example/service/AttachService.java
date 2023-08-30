package com.example.service;

import com.example.dto.AttachDTO;
import com.example.entity.AttachEntity;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttachService {

    @Value("${attach.folder.name}")
    private String folderName;
    @Value("${attach.url}")
    private String attachUrl;
    @Autowired
    private AttachRepository attachRepository;

    public AttachDTO save(MultipartFile file, Integer id) {
        String pathFolder = getYmDString(); // 2022/04/23
        File folder = new File(folderName + "/" + pathFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String key = UUID.randomUUID().toString(); // dasdasd-dasdasda-asdasda-asdasd
        String extension = getExtension(file.getOriginalFilename()); // jpg

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folderName + "/" + pathFolder + "/" + key + "." + extension);
            // attaches/2022/04/23/dasdasd-dasdasda-asdasda-asdasd.jpg
            Files.write(path, bytes);

            AttachEntity entity = new AttachEntity();

//            if (extension.equals("mp4")) {
//                    IContainer container = IContainer.make();
//                    container.open(path.toString(), IContainer.Type.READ, null);
//                    long duration = container.getDuration();
//                    entity.setDuration(duration);
//                    container.close();
//            }
            entity.setId(key);
            entity.setPath(pathFolder); // 2022/04/23
            entity.setSize(file.getSize());
            entity.setOriginName(file.getOriginalFilename());
            entity.setExtension(extension);
            entity.setPrtId(id);
            entity.setUrl(path.toString());

            attachRepository.save(entity);

            AttachDTO attachDTO = new AttachDTO();
            attachDTO.setId(key);
            attachDTO.setOriginName(entity.getOriginName());
            attachDTO.setUrl(entity.getUrl());

            return attachDTO;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] loadImageById(String id) {
        AttachEntity entity = get(id);
        try {
            BufferedImage originalImage = ImageIO.read(new File(url(entity.getPath(), entity.getId(), entity.getExtension())));
            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            ImageIO.write(originalImage, entity.getExtension(), boas);
            boas.flush();
            byte[] imageInByte = boas.toByteArray();
            boas.close();
            return imageInByte;
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public byte[] loadByIdGeneral(String id) {
        AttachEntity entity = get(id);
        try {
            File file = new File(url(entity.getPath(), entity.getId(), entity.getExtension()));
            byte[] bytes = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            fileInputStream.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public PageImpl<AttachDTO> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AttachEntity> pageObj = attachRepository.findAll(pageable);
        List<AttachDTO> attachDTOS = pageObj.stream().map(this::getAttachDTO).collect(Collectors.toList());
        return new PageImpl<>(attachDTOS, pageable, pageObj.getTotalElements());
    }


    public Boolean delete(String id) {
        Optional<AttachEntity> optional = attachRepository.findById(id);
        boolean b = false;
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Not found");
        }
        AttachEntity entity = optional.get();
        attachRepository.delete(entity);
        File file = new File("attaches/" + entity.getPath() + "/" + entity.getId() + "." + entity.getExtension());
        if (file.exists()) {
            b = file.delete();
        }
        return b;
    }

    public ResponseEntity<Resource> download(String id) {
        AttachEntity entity = get(id);
        try {
            Path file = Paths.get(url(entity.getPath(), entity.getId(), entity.getExtension()));
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOriginName() + "\"").body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public AttachDTO toDTO(AttachEntity entity) {
        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(entity.getId());
        attachDTO.setOriginName(entity.getOriginName());
        attachDTO.setSize(entity.getSize());
        attachDTO.setPath(entity.getPath());
        attachDTO.setCreatedData(entity.getCreatedData());
        attachDTO.setExtension(entity.getExtension());
        return attachDTO;
    }

    public AttachEntity get(String id) {
        return attachRepository.findById(id).orElseThrow(() -> new AppBadRequestException("File not found"));
    }

    public String url(String path, String id, String extension) {
        return folderName + "/" + path + "/" + id + "." + extension;
    }

    public AttachDTO getAttachDTO(AttachEntity entity) {
        return new AttachDTO(entity.getId(), entity.getOriginName(),
                entity.getSize(), getUrl(entity.getId()));
    }

    public AttachDTO getAttachWithURL(String attachId) {
        if (attachId == null) {
            return null;
        }
        return new AttachDTO(attachId, getUrl(attachId));
    }

    public String getUrl(String id) {
        return attachUrl + "/open/" + id + "/img";
    }

    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day; // 2022/04/23
    }

    public String getExtension(String fileName) { // mp3/jpg/npg/mp4.....
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }


    public AttachDTO getAttachVideo(String attachId) {
        Optional<AttachEntity> optional = attachRepository.findById(attachId);
        if (optional.isEmpty()) return null;
        AttachDTO attachDTO=getAttachWithURL(attachId);
        attachDTO.setDuration(optional.get().getDuration());
        return attachDTO;
    }
}

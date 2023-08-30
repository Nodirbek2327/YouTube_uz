package com.example.service;

import com.example.dto.EmailHistoryDTO;
import com.example.dto.FilterEmailHistoryDTO;
import com.example.dto.FilterResultDTO;
import com.example.entity.EmailHistoryEntity;
import com.example.entity.ProfileEntity;
import com.example.repository.CustomEmailHistoryRepository;
import com.example.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailHistoryService {
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;
    @Autowired
    private CustomEmailHistoryRepository customEmailHistoryRepository;
    public void sendEmail(String message, ProfileEntity profileEntity,String subject) {
        EmailHistoryEntity entity=new EmailHistoryEntity();
        entity.setMessage(message);
        entity.setTitle(subject);
        entity.setToEmail(profileEntity.getEmail());
        emailHistoryRepository.save(entity);
    }

    public PageImpl<EmailHistoryDTO> getAllByPagination(Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<EmailHistoryEntity> pageObj=emailHistoryRepository.findAll(pageable);
        List<EmailHistoryDTO> dtoList=pageObj.getContent().stream().map(s->toDto(s)).toList();
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }

    public PageImpl<EmailHistoryDTO> getByEmail(String email, Integer page, Integer size) {
        Pageable pageable=PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<EmailHistoryEntity> pageObj=emailHistoryRepository.findByToEmailAndVisibleTrue(email,pageable);
        List<EmailHistoryDTO>dtoList=pageObj.getContent().stream().map(this::toDto).toList();
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    public EmailHistoryDTO toDto(EmailHistoryEntity entity){
        EmailHistoryDTO dto=new EmailHistoryDTO();
        dto.setTitle(entity.getTitle());
        dto.setMessage(entity.getMessage());
        dto.setToEmail(entity.getToEmail());
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public PageImpl<EmailHistoryDTO> filterByPagination(FilterEmailHistoryDTO dto, Integer page, Integer size) {
        FilterResultDTO<EmailHistoryEntity> result=customEmailHistoryRepository.filterPagination(dto,page,size);
        Pageable pageable=PageRequest.of(page,size);
        List<EmailHistoryDTO> dtoList=result.getContent().stream().map(s->toDto(s)).toList();
        return new PageImpl<>(dtoList,pageable,result.getTotalElement());
    }
}

package com.example.repository;

import com.example.entity.EmailHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity,String >,
        PagingAndSortingRepository<EmailHistoryEntity,String> {
    Page<EmailHistoryEntity>findByToEmailAndVisibleTrue(String toEmail, Pageable pageable);
}

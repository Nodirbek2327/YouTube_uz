package com.example.repository;

import com.example.dto.FilterEmailHistoryDTO;
import com.example.dto.FilterResultDTO;
import com.example.entity.EmailHistoryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomEmailHistoryRepository {
    @Autowired
    private EntityManager entityManager;

    public FilterResultDTO<EmailHistoryEntity> filterPagination(FilterEmailHistoryDTO dto, Integer page, Integer size) {
        StringBuilder selectBuilder=new StringBuilder("from EmailHistoryEntity as e");
        StringBuilder countBuilder=new StringBuilder("select count(e) from EmailHistoryEntity as e");
        StringBuilder builder=new StringBuilder(" where e.visible=true");
        Map<String,Object>params=new LinkedHashMap<>();
        if(dto.getToEmail()!=null && !dto.getToEmail().isBlank()){
            builder.append(" and e.toEmail=:email");
            params.put("email",dto.getToEmail());
        }
        if(dto.getFromDate()!=null){
            builder.append(" and e.createdDate>=:from");
            params.put("from", LocalDateTime.of(dto.getFromDate(), LocalTime.MIN));
        }
        if(dto.getToDate()!=null){
            builder.append(" and e.createdDate<=:to");
            params.put("to",LocalDateTime.of(dto.getToDate(),LocalTime.MAX));
        }
        countBuilder.append(builder);
        builder.append(" order by e.createdDate ");
        selectBuilder.append(builder);
        Query selectQuery=entityManager.createQuery(selectBuilder.toString());
        Query countQuery=entityManager.createQuery(countBuilder.toString());
        for (Map.Entry<String,Object> p: params.entrySet()){
            selectQuery.setParameter(p.getKey(),p.getValue());
            countQuery.setParameter(p.getKey(),p.getValue());
        }
        selectQuery.setFirstResult(page*size);
        selectQuery.setMaxResults(size);
        List<EmailHistoryEntity>entityList=selectQuery.getResultList();
        Long totalElement=(Long) countQuery.getSingleResult();
        return new FilterResultDTO<EmailHistoryEntity>(entityList,totalElement);

    }
}

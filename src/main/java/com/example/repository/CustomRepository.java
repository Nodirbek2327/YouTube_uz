package com.example.repository;

import com.example.entity.VideoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomRepository {
    @Autowired
    private EntityManager entityManager;

    public List<VideoEntity> getVideoByTagId(Integer tagId, Integer page, Integer size) {
        String query = "select v from VideoEntity as v inner join v.videoTags as t where t.id=:tagId and v.visible=true ";
        Query selectQuery = entityManager.createQuery(query);
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);
        selectQuery.setParameter("tagId", tagId);
        return  selectQuery.getResultList();
    }
}

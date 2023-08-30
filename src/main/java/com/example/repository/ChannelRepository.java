package com.example.repository;

import com.example.entity.ChannelEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends CrudRepository<ChannelEntity,String>, PagingAndSortingRepository<ChannelEntity,String> {
    ChannelEntity findByName(String name);
    ChannelEntity findByNameAndProfileId(String name, Integer profileId);
    ChannelEntity  findByIdAndProfileId(String id,Integer profileId);
    List<ChannelEntity> findByProfileId(Integer profileId);

    Optional<ChannelEntity> findAllByIdAndVisibleTrue(String channelId);
}

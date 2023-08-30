package com.example.repository;

import com.example.entity.CommentEntity;
import com.example.mapper.CommentInfoMapperI;
import com.example.mapper.CommentListMapperI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<CommentEntity, String>,
        PagingAndSortingRepository<CommentEntity,String > {
    Optional<CommentEntity>findByIdAndVisibleTrue(String commentId);
   @Transactional
   @Modifying
    @Query("update CommentEntity set visible=false where id=:commentId")
    int deleteComment(@Param("commentId") String commentId);
    Page<CommentEntity>findAllByVisibleTrue(Pageable pageable);

    @Query("select c.id as commentId, c.content as content, c.createdDate as createdDate, " +
            " c.likeCount as likeCount, c.dislikeCount as dislikeCount, c.videoId as videoId," +
            " v.title as title, v.previewAttachId as previewAttachId, a.duration as duration" +
            "  from CommentEntity as c inner join c.video as v inner join v.attach as a " +
            "where c.profileId=:profileId and c.visible=true ")
    List<CommentListMapperI>getByProfileId(@Param("profileId")Integer profileId);

    @Query("select c.id as commentId, c.content as content, c.createdDate as createdDate, " +
            " c.likeCount as likeCount, c.dislikeCount as dislikeCount, c.videoId as videoId," +
            " p.id as profileId, p.name as profileName, p.surname as profileSurname, p.imageId as profilePhotoId" +
            "  from CommentEntity as c inner join c.profile as p " +
            "where c.videoId=:videoId and c.visible=true ")
    List<CommentInfoMapperI>getByVideoId(@Param("videoId")String  videoId);

    @Query("select c.id as commentId, c.content as content, c.createdDate as createdDate, " +
            " c.likeCount as likeCount, c.dislikeCount as dislikeCount, c.videoId as videoId," +
            " p.id as profileId, p.name as profileName, p.surname as profileSurname, p.imageId as profilePhotoId" +
            "  from CommentEntity as c inner join c.profile as p " +
            "where c.replyId=:replyId and c.visible=true ")
    List<CommentInfoMapperI>repliedComment(@Param("replyId")String  repliedId);
}

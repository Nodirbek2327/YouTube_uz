package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.CommentCreateDTO;
import com.example.dto.CommentDTO;
import com.example.dto.CommentUpdateDTO;
import com.example.service.CommentService;
import com.example.util.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@Tag(name = "Comment", description = "Comment api list.")
public class CommentController {
    @Autowired
    private CommentService commentService;
    //1. Crate Comment (USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    @Operation(summary = "create comment", description = "This api used for creating comment ...")
    public ResponseEntity<CommentDTO> create(@Valid @RequestBody CommentCreateDTO commentDTO){
        return ResponseEntity.ok(commentService.create(commentDTO));
    }
    // 2. Update Comment (USER AND OWNER)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{commentId}")
    @Operation(summary = "update comment", description = "This api used for updating comment ...")
    public ResponseEntity<CommentDTO> update(@PathVariable String commentId,
                                             @Valid @RequestBody CommentUpdateDTO commentDTO){
        return ResponseEntity.ok(commentService.update(commentId,commentDTO));
    }

    // 3. Delete Comment (USER AND OWNER, ADMIN)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{commentId}")
    @Operation(summary = "deleting comment", description = "This api used for deleting comment ...")
    public ResponseEntity<ApiResponseDTO> delete(@PathVariable String commentId){
        return ResponseEntity.ok(commentService.delete(commentId));
    }

    //4. Comment List Pagination (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    @Operation(summary = "pagination comment", description = "This api used for pagination comment ...")
    public ResponseEntity<PageImpl<CommentDTO>> pagination(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                                           @RequestParam(value = "size",defaultValue = "10")Integer size){
        return ResponseEntity.ok(commentService.pagination(page-1,size));
    }
//    5. Comment List By profileId(ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listByProfileId/{profileId}")
    @Operation(summary = " Comment List", description = "This api used for Comment List By profileId(ADMIN) ...")
    public ResponseEntity<List<CommentDTO>> commentList(@PathVariable Integer profileId){
    return ResponseEntity.ok(commentService.commentList(profileId));
    }
//  6. Comment List By Profile (murojat qilgan odamning comment lari) (USER AND OWNER)
    @GetMapping("/listbyOwner")
    @Operation(summary = " Comment List by owner", description = "This api used for Comment List By owner...")
    public ResponseEntity<List<CommentDTO>> commentListByOwner() {
        Integer prtId = SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(commentService.commentList(prtId));
    }
    //7. Comment List by videoId
        @GetMapping("/public/byVideoId/{videoId}")
        @Operation(summary = " Comment List by videoId", description = "This api used for Comment List By videoId...")
        public ResponseEntity<List<CommentDTO>> commentListByVideoId(@PathVariable String videoId){
            return ResponseEntity.ok(commentService.commentByVideoId(videoId));
        }
// 8. Get Comment Replied Comment by comment Id
        @GetMapping("/public/repliedComment/{commentId}")
        @Operation(summary = " Comment List Replied Comment", description = "This api used for Get Comment Replied Comment by comment Id...")
        public ResponseEntity<List<CommentDTO>> repliedComment(@PathVariable String commentId){
        return ResponseEntity.ok(commentService.repliedComment(commentId));
}
}

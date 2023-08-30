package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.PlayListDTO;
import com.example.dto.PlaylistShortInfoDTO;
import com.example.mapper.PlaylistMapper;
import com.example.service.PlayListService;
import com.example.util.SpringSecurityUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/playlist")
public class PlayListController {
    @Autowired
    private PlayListService playListService;

    //1. Create Playlist (USER)
    @PostMapping("")
    public ResponseEntity<PlayListDTO>createPlayList(@Valid @RequestBody PlayListDTO dto){
//        Integer profileId= SpringSecurityUtil.getProfileId();
        return ResponseEntity.ok(playListService.create(dto));
    }
    //2. Update Playlist(USER and OWNER)
    @PutMapping("/{playlistId}")
    public ResponseEntity<ApiResponseDTO>update(@PathVariable String playlistId,
                                                @Valid @RequestBody PlayListDTO dto){

        return ResponseEntity.ok(playListService.update(playlistId,dto));
    }
//     3. Change Playlist Status (USER and OWNER)
    @PutMapping("/updateStatus/{playlistId}")
    public ResponseEntity<ApiResponseDTO>updateStatus(@PathVariable String playlistId,
                                                      @RequestParam("status")AccessLevel status ){
        return ResponseEntity.ok(playListService.updateStatus(playlistId,status));
    }

    // 4. Delete Playlist (USER and OWNER, ADMIN)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<ApiResponseDTO>delete(@PathVariable String playlistId){
        return ResponseEntity.ok(playListService.delete(playlistId));
    }

    // 5. Playlist Pagination (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<PlayListDTO>>pagination(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                           @RequestParam(value = "size",defaultValue = "10") Integer size){
        return ResponseEntity.ok(playListService.pagination(page-1,size));
    }
    //    6. Playlist List By UserId (order by order number desc) (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<List<PlayListDTO>>getByUserId(@PathVariable Integer userId){
        return ResponseEntity.ok(playListService.getByUserId(userId));
    }
    // 7. Get User Playlist (order by order number desc) (murojat qilgan user ni)
    @GetMapping("/getOwnPlayList")
    public ResponseEntity<List<PlaylistShortInfoDTO>>getOwnPlayList(){
        return ResponseEntity.ok(playListService.getOwnPlayList());
    }
    //8.Get Channel Play List By ChannelKey (order by order_num desc) (only Public)
    @GetMapping("/public/getByChannelId/{channelId}")
    public ResponseEntity<List<PlaylistShortInfoDTO>>getByChannelId(@PathVariable String channelId){
        return ResponseEntity.ok(playListService.getByChannelId(channelId));
    }
    // 9. Get Playlist by id
    @GetMapping("/getById/{playlistId}")
    public ResponseEntity<PlaylistMapper> getById(@PathVariable String playlistId){
        return ResponseEntity.ok(playListService.getById(playlistId));
    }
}

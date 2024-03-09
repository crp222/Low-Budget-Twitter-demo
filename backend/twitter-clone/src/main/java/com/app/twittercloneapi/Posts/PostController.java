package com.app.twittercloneapi.Posts;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/post/create")
    public ResponseEntity<String> createPost(Principal principal,@RequestBody PostBody postBody) {
        if(principal == null){
            return ResponseEntity.badRequest().body("Felhasznalo nincs bejelentkezve!");
        }
        try {
            postService.createPost(principal.getName(), postBody.getText(), postBody.getFileBase64(),postBody.getFileType(),postBody.getParent());
            return ResponseEntity.ok().body("Poszt letrehozva!");
        }catch(PostException err){
            return ResponseEntity.badRequest().body(err.getMessage());
        }catch(Exception err){
            return ResponseEntity.internalServerError().body(null);
        }
        
    }

    @GetMapping("/public/post/getWithOffset")
    public ResponseEntity<List<Map<String,Object>>> getPost(@RequestParam String offset_str,@RequestParam String amount_str){
        try {
            int offset = Integer.parseInt(offset_str);
            int amount = Integer.parseInt(amount_str);
            return ResponseEntity.ok().body(postService.getPostsWithOffset(offset, amount));
        }catch(NullPointerException err){
            return ResponseEntity.badRequest().body(null);
        }catch(PostException err){
            return ResponseEntity.badRequest().body(null);
        }catch(Exception exception) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/public/comments")
    public ResponseEntity<List<Map<String,Object>>> getComments(@RequestParam String id_str) {
        try {
            int id = Integer.parseInt(id_str);
            return ResponseEntity.ok().body(postService.getComments(id));
        }catch(PostException err) {
            return ResponseEntity.badRequest().body(null);
        }catch(Exception err){
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/public/post/{id}")
    public ResponseEntity<Map<String,Object>> getPost(@PathVariable String id){
        try {
            int id_int = Integer.parseInt(id);
            return ResponseEntity.ok().body(postService.getPost(id_int));
        }catch(PostException err){
            return ResponseEntity.badRequest().body(null);
        }catch(Exception err){
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/admin/post/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id){
        try {
            int id_int = Integer.parseInt(id);
            postService.deletePost(id_int);
            return ResponseEntity.ok(null);
        }catch(Exception ignore){
            return ResponseEntity.internalServerError().body(null);
        }
    }
}

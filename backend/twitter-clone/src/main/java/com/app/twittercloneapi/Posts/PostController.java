package com.app.twittercloneapi.Posts;

import java.security.Principal;
import java.util.HashMap;
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

    /**
     * For later restrictions this has to be served in a controller!
     */
    @GetMapping("/public/post_resource/{id}")
    public ResponseEntity<Map<String,Object>> getPostResource(@PathVariable String id) {
        if(id.startsWith("-"))
            return ResponseEntity.ok().body(new HashMap<>());
        try {
            return ResponseEntity.ok().body(postService.findResource(id));
        }catch(Exception err){
            return ResponseEntity.internalServerError().body(null);
        }
    }
}

package com.app.twittercloneapi.User;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class UserInfoController {

    private static class LoginBody {
        private String profile_picture;
        LoginBody() {
        }
        public String getProfile_picture() {
            return profile_picture;
        }
        public void setProfile_picture(String profile_picture) {
            this.profile_picture = profile_picture;
        }
    }

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TextEncryptor encryptor;

    @GetMapping("/user/currentuser")
    public ResponseEntity<Map<String,Object>> currentUser(Principal principal) {
        try {
            return ResponseEntity.ok().body(userInfoService.loadUserByUsername(principal.getName()).toMap());
        }catch(Exception err){
            err.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("public/user/get")
    public ResponseEntity<Map<String,Object>> userInfoById(@RequestParam String id_str){
        try {
            int id = Integer.parseInt(id_str);
            return ResponseEntity.ok().body(userInfoService.loadUserById(id));
        }catch(Exception err){
            err.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/reg")
    public ResponseEntity<String> newUser(@RequestParam String username,@RequestParam String email,@RequestParam String password,@RequestParam String displayedName,@RequestBody UserInfoController.LoginBody body) {
        try {
            userInfoService.createuser(username, email, password, displayedName, body.profile_picture);
            return ResponseEntity.ok().body("Sikeres regisztracio!");
        }catch(UserException err){
            return ResponseEntity.status(400).body(err.getMessage());
        }catch(Exception err){
            err.printStackTrace();
            return ResponseEntity.status(500).body("Ismeretlen hiba tortent!");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username,@RequestParam String password){
        if(username.equals("admin"))
            // Basic admin account has to exist!
            userInfoService.createBasicAdmin();

        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,password);
        try {
            authenticationManager.authenticate(authenticationRequest);
            String data = username+":"+password;
            String token = userInfoService.getToken(data);
            if(token == null){
                token = encryptor.encrypt(data);
                userInfoService.setUserToken(data, token);
            }
            return ResponseEntity.ok().body(token);
        }catch(Exception err){
            return ResponseEntity.status(401).body("Rossz felhasznalonev vagy jelszo!");
        }
    }
}

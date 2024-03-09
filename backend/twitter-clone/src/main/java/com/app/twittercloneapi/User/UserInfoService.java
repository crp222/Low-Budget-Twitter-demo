package com.app.twittercloneapi.User;

import java.sql.Blob;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService{

    @Autowired
    private UserInfoRepository userInfoRepository;
    
    @Autowired 
    private PasswordEncoder passwordEncoder;

    // User tokens saved in memory.
    // Tokens should never be regenerated for any user
    // TODO : TOKEN EXPIRATION
    private final Map<String,String> userTokens = new HashMap<>();

    public void createBasicAdmin() {
           if(!userInfoRepository.findByUsername("admin").isPresent()){
            UserInfo admin = new UserInfo();
            admin.setDisplayedName("ADMIN");
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setUsername("admin");
            admin.setRole("ADMIN");
            userInfoRepository.save(admin);
        }
    }

    @Override
    public UserInfo loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> user = userInfoRepository.findByUsername(username);
        if(!user.isPresent())
            throw new UsernameNotFoundException("Felhasznalonev nem letezik!");
        return userInfoRepository.findByUsername(username).get();
    }

    public Map<String,Object> loadUserById(int id) throws Exception {
        Optional<UserInfo> user = userInfoRepository.findById(id);
        if(user.isPresent())
            return user.get().toMap();
        else
            throw new UserException("Felhasznalo nem talalhato meg!");
    }

    private Blob convertPicture(String picture_base64) throws Exception{
        if(picture_base64 == null || picture_base64.isEmpty())
            return null;
        byte[] decodedByte =  Base64.getDecoder().decode(picture_base64);
        if(decodedByte.length > 60000 )
            throw new UserException("A profilkep merete tul nagy!");
        Blob pic = new SerialBlob(decodedByte);
        return pic;
    }
    
    public void createuser(String username,String email,String password,String displayedName,String profilePic) throws Exception{
        if(userInfoRepository.findByUsername(username).isPresent())
            throw new UserException("Felhasznalonev mar foglalt!");
        UserInfo userInfo = new UserInfo(email,displayedName,convertPicture(profilePic),username,passwordEncoder.encode(password));
        //TODO : check email,password,... validity
        if(username.isEmpty() || email.isEmpty() || password.isEmpty() || displayedName.isEmpty()){
            throw new UserException("Csak a profilkep maradhat uresen!");
        }
        userInfoRepository.save(userInfo);
    }

    
    public int getUserId(String username) throws UserException{
        Optional<Integer> id = userInfoRepository.findUsernameId(username);
        if(!id.isPresent())
            throw new UserException("Nincs ilyen nevu felhasznalo!");
        return id.get();
    }

    /**
     * 
     * @param data Auth request data
     * @param token Encoded data
     */
    public void setUserToken(String data,String token){
        userTokens.put(data, token);
    }

    /**
     * @param data Auth request data
     * @return
     */
    public String getToken(String data) {
        return userTokens.get(data);
    }
}

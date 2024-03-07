package com.app.twittercloneapi.Posts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.app.twittercloneapi.User.UserException;
import com.app.twittercloneapi.User.UserInfoService;

@Service
public class PostService {

    private static String FILE_TYPES = "mp4 jpg jpeg avi png";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserInfoService userInfoService;
    
    // For the absolute path of "resources"
    @Value("classpath:application.properties")
    private Resource app_properties;

    /**
     * Saves the given file in the filesystem with a name of a number, and returns that number.
     * @param fileBase64 file in base64 string
     * @param fileType file extension
     * @return name of the saved file
     * @throws Exception
     */
    // TODO : There is a bug where it doesn't add a new image to the resources instead just returns one of the existing ones!
    private int saveResource(String fileBase64,String fileType) throws Exception {
        if(fileBase64 == null || fileBase64.isEmpty() || fileType == null)
            return -1;

        if(fileType != null && !FILE_TYPES.contains(fileType))
            throw new PostException("Fajl tipusa nem megfelelo! (csak mp4,avi,jpg,png lehetseges!)");
        
        byte[] decodedByte =  Base64.getDecoder().decode(fileBase64);
        
        if(decodedByte.length > 1300000)
            throw new PostException("Fajl merete tul nagy!"); 
        
        File resource_dir = new File(app_properties.getFile().getParentFile().getAbsolutePath()+"/post_resources");

        if(!resource_dir.exists() || !resource_dir.isDirectory())
            resource_dir.mkdir();

        String[] resources = resource_dir.list();

        int last_id = 0;
        if(resources.length > 0){
            last_id = Integer.parseInt(resources[resources.length-1].split("[.]")[0]); // ex: 5.jpg => 5
            last_id++;
    
            while(Arrays.stream(resources).anyMatch((last_id+"."+fileType)::equals))
                last_id++;
        }

        File new_file = new File(resource_dir.getAbsolutePath()+"/"+last_id+"."+fileType);
        FileOutputStream fileOutputStream = new FileOutputStream(new_file);
        fileOutputStream.write(decodedByte);
        fileOutputStream.close();
        return last_id;
    }

    public Map<String,Object> findResource(String id) throws Exception{
        File resource_dir = new File(app_properties.getFile().getParentFile().getAbsolutePath()+"/post_resources");
        File resource = null;
        if(!resource_dir.exists())
            return null;
        for(String filename : resource_dir.list()) {
            if(filename.startsWith(id)){
                resource = new File(resource_dir.getAbsolutePath()+"/"+filename);
                break;
            }
        }
        FileInputStream inputStream = new FileInputStream(resource);
        byte[] buffer = inputStream.readAllBytes();
        inputStream.close();

        // getting file type
        String extension = "";
        int i = resource.getName().lastIndexOf('.');
        if (i >= 0) { extension = resource.getName().substring(i+1); }


        Map<String,Object> result = new HashMap<>();
        result.put("bytes",buffer);
        result.put("filename",resource.getName());
        result.put("filetype",extension);

        return result;
    }

    public void createPost(String user,String content,String fileBase64,String fileType,int parent) throws Exception {
        Date date = Date.valueOf(LocalDate.now());
        try {
            if(content.length() >= 1000){
                throw new PostException("Szoveg merete tul hosszu!");
            }
            Post post = new Post(content,saveResource(fileBase64,fileType),date,userInfoService.getUserId(user),parent);
            postRepository.save(post);
        }catch(UserException err){
            throw new PostException(err.getMessage());
        }catch(PostException err){
            throw new PostException(err.getMessage());
        }catch(Exception err){
            throw new PostException("Poszt mentese nem sikerult!");
        }
    }

    public List<Map<String,Object>> getPostsWithOffset(int offset,int amount) throws Exception{
        if(amount > 100)
            throw new PostException("Nem lehet 100-nal tobbet lekerni egyszerre!");
        List<Map<String,Object>> list = new ArrayList<>();
        List<Post> result = postRepository.postsDateOrderInRange(amount,offset);
        for(Post p : result) {
            var m = p.toMap();
            try {
                m.put("resource",findResource(m.get("resource").toString()));
            }catch(Exception e){
                m.put("resource",null);
            }
            m.put("user",userInfoService.loadUserById((int)m.get("user")));
            list.add(m);
        }
            
        return list;
    }

    public List<Map<String,Object>> getComments(int id) throws Exception {
        List<Map<String,Object>> comments = new ArrayList<>();
        List<Post> result = postRepository.postCommentsDateOrderInRange(id);
        for(Post p : result) {
            var m = p.toMap();
            try {
                m.put("resource",findResource(m.get("resource").toString()));
            }catch(Exception e){
                m.put("resource",null);
            }
            m.put("user",userInfoService.loadUserById((int)m.get("user")));
            comments.add(m);
        }
        return comments;
    }

    public Map<String,Object> getPost(int id) throws Exception {
        Post post = postRepository.findById(id).orElse(null);
        if(post == null)
            throw new PostException("Poszt nem található!");
        Map<String,Object> res = post.toMap();
        try {
            res.put("resource",findResource(res.get("resource").toString()));
        }catch(Exception e){
            res.put("resource",null);
        }
        res.put("comments",getComments(id));
        res.put("user",userInfoService.loadUserById((int)res.get("user")));
        return res;
    }
}

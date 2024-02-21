import React, { useEffect, useState } from 'react'
import styles from  "../styles/post.module.css"
import ProfilePicture from "./ProfilePicture"
import CreatePost from "./CreatePost"

export default function Post(params) {

  const [Post, setPosts] = useState({content:"",resource:"-1",date:""});
  const [PostUser, setPostUser] = useState({displayed_name:""});
  const [User, setUser] = useState({displayed_name:""});
  const [ResourceDom, setResourceDom] = useState();


  useEffect(() => {
    setPosts(params.post);
  }, []);

  useEffect(() => {
      setUser(params.user);
  }, [params]);


  useEffect(()=>{
      getPostUser();
      getPostResource();
  },[Post])

  const getPostUser = async () => {
    if(!Post.user)
      return;
    let result = await fetch("http://localhost:8080/public/user/get?id_str="+Post.user);
    if(result.status === 200)
      setPostUser(await result.json());
    
  }

  const getPostResource = async () => {
    if(Post.resource == -1)
      return;
    let result = await fetch("http://localhost:8080/public/post_resource/"+Post.resource);
    let resource_dom = ""
    if(result.status === 200){
      let data = await result.json();
      if(["jpg","png","jpeg"].includes(data.filetype))
        resource_dom = <img src={"data:image/jpg;base64,"+data.bytes} />
      else if(["avi","mp4"].includes(data.filetype))
        resource_dom = <iframe src={"data:video/mp4;base64,"+data.bytes} />
    }
    setResourceDom(resource_dom)
  }

  return (
    <div className={styles.post}>
      <div className={styles.user}>
        <ProfilePicture className={styles.userpicture} user={PostUser}></ProfilePicture>
        <div>
          <div className='fs-4'>{PostUser.displayed_name}</div>
          <div className='fs-6 fw-light'>{(new Date(Post.date)).toDateString()}</div>
        </div>
      </div>
      <div className={styles.body}>
        <div className={styles.content + " fs-5"}>{Post.content}</div>
        {ResourceDom}
      </div>
      <div className={styles.commentCreator}>
          {User ? <CreatePost style={{width:"90%"}} postid={params.postid} user={User} parent={Post.id}></CreatePost> : ""}
      </div>
    </div>
  )
}

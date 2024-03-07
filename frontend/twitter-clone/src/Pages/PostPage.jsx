import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import ENV from "../env"
import styles from "../styles/post.module.css"
import ProfilePicture from "../components/Profile/ProfilePicture"
import Row from "react-bootstrap/Row"
import Container from 'react-bootstrap/Container'
import PostComponent from "../components/HomePage/Post"
import CreatePost from '../components/HomePage/CreatePost'

export default function PostPage(params) {

    const [User, setUser] = useState({ displayed_name: "" });
    const { id } = useParams();
    const [Post,setPost] = useState({
      user : {
        displayed_name : ""
      },
      comments : []
    });
    const [ResourceDom, setResourceDom] = useState();


    useEffect(()=>{
      console.log(params)
        setUser(params.user);
        fetchPostData();
    },[params])

    const fetchPostData = async() => {
        let result = await fetch(ENV.API_DOMAIN+"/public/post/"+id);
        if(result.status === 200){
          let data = await result.json();
          console.log(data);
          readPostResource(data);
          setPost(data);
        }
    }

    const readPostResource = async (data) => {
      if (!data.resource)
        return;
      let resource = data.resource;
      let resource_dom = ""
      if (["jpg", "png", "jpeg"].includes(resource.filetype))
        resource_dom = <img src={"data:image/jpg;base64," + resource.bytes} />
      else if (["avi", "mp4"].includes(resource.filetype))
        resource_dom = <iframe src={"data:video/mp4;base64," + resource.bytes} />
      setResourceDom(resource_dom)
    }

  return (
    <div className='mx-auto' style={{maxWidth:"800px",overflow:"hidden"}}>
        <div className={styles.post + " mt-2"}>
          <div className={styles.user}>
            <ProfilePicture className={styles.userpicture} user={Post.user}></ProfilePicture>
            <div>
              <div className='fs-4'>{Post.user.displayed_name}</div>
              <div className='fs-6 fw-light'>{(new Date(Post.date)).toDateString()}</div>
            </div>
          </div>
          <div className={styles.body}>
            <div className={styles.content + " fs-5"}>{Post.content}</div>
            {ResourceDom}
          </div>
        </div>
        <div className={styles.commentCreator}>
          {User ? <CreatePost style={{ width: "100%" }} postid="main" user={User} parent={Post.id}></CreatePost> : ""}
        </div>
        <Container className={styles.comments}>
            {Post.comments.map((e,i) => (
              <Row className='m-2' key={i}>
                  <PostComponent user={User} post={e} postid={"post-"+i}></PostComponent>
              </Row>
            ))}
        </Container>
    </div>
  )
}

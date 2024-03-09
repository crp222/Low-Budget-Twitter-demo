import React, { useEffect, useReducer, useState } from 'react'
import styles from "../../styles/post.module.css"
import ProfilePicture from "../Profile/ProfilePicture"
import CreatePost from "./CreatePost"
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import AlertLink from 'react-bootstrap/AlertLink'
import Comment from './Comment'
import ENV from "../../env"
import { useNavigate } from "react-router-dom";


export default function Post(params) {

  const [Post, setPosts] = useState({ 
    content: "", 
    resource: null, 
    date: "", 
    user: {
      displayed_name : ""
    } 
  });
  const [User, setUser] = useState({ displayed_name: "" }); // Current client user 
  const [ResourceDom, setResourceDom] = useState();
  const [Comments, setComments] = useState([]);
  const [CommentsLoaded, changeCommentsLoaded] = useReducer((state, action) => { return !state }, false);
  const [MoreComments, setMoreComments] = useState(false);
  let navigate = useNavigate(); 

  useEffect(() => {
    setUser(params.user);
    setPosts(params.post);
  }, [params]);

  useEffect(()=>{
      readPostResource(Post.resource);
  },[Post])

  const loadComments = async () => {
    let params = `id_str=${Post.id}`;
    let result = await fetch(ENV.API_DOMAIN+"/public/comments?" + params);
    if (result.status === 200) {
      /**@type {Array} */
      let comments = await result.json();
      if(comments.length > 2){
        setComments(comments.splice(0,2))
        setMoreComments(comments.length);
      }else
        setComments(comments);
    }
  }

  const readPostResource = async (resource) => {
    if (!resource)
      return;
    let resource_dom = ""
    if (["jpg", "png", "jpeg"].includes(resource.filetype))
      resource_dom = <img src={"data:image/jpg;base64," + resource.bytes} />
    else if (["avi", "mp4"].includes(resource.filetype))
      resource_dom = <iframe src={"data:video/mp4;base64," + resource.bytes} />
    setResourceDom(resource_dom)
  }

  return (
    <div className={styles.post + " mx-auto"}>
      <div className={styles.user} onClick={()=>navigate("/post/"+Post.id)}>
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
      <div>
        {
          CommentsLoaded ?
            Comments.length > 0 ?
              <Container>
                {
                  Comments.map((e, i) => (
                    <Row className='m-0' key={i}>
                      <hr style={{ width: "100%", color: "var(--bs-primary)", opacity: "0.95" }} />
                      <Comment commentid={i + "-" + params.postid} user={User} post={e}></Comment>
                    </Row>
                  ))
                }
                {MoreComments ? <a onClick={()=>navigate('/post/'+Post.id)} className='d-block text-center'>{MoreComments} További hozzászólás betöltése</a> : ""}                
              </Container>
              :
              <div className='p-2 text-align-center'>Nincsenek hozzászólások</div>
            : 
            <AlertLink onClick={() => { loadComments().then(() => { changeCommentsLoaded() }) }} style={{ display: "block" }} className='p-2 text-align-center fs-6 fw-light text-primary'>
              Hozzászólások betöltése!
            </AlertLink>
        }

      </div>
      <div className={styles.commentCreator}>
        {User ? <CreatePost style={{ width: "90%" }} postid={"createpost-"+params.postid} user={User} parent={Post.id}></CreatePost> : ""}
      </div>
    </div>
  )
}

import React, { useEffect, useReducer, useState } from 'react'
import styles from "../styles/post.module.css"
import ProfilePicture from "./ProfilePicture"
import CreatePost from "./CreatePost"
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import AlertLink from 'react-bootstrap/AlertLink'
import Comment from './Comment';

export default function Post(params) {

  const [Post, setPosts] = useState({ content: "", resource: "-1", date: "" });
  const [PostUser, setPostUser] = useState({ displayed_name: "" }); // Creator of this post
  const [User, setUser] = useState({ displayed_name: "" }); // Current client user 
  const [ResourceDom, setResourceDom] = useState();
  const [Comments, setComments] = useState([]);
  const [CommentsLoaded, changeCommentsLoaded] = useReducer((state, action) => { return !state }, false);

  useEffect(() => {
    setUser(params.user);
    setPosts(params.post);
  }, [params]);


  useEffect(() => {
    getPostUser();
    getPostResource();
  }, [Post])

  const getPostUser = async () => {
    if (!Post.user)
      return;
    let result = await fetch("http://localhost:8080/public/user/get?id_str=" + Post.user);
    if (result.status === 200)
      setPostUser(await result.json());

  }

  const getPostResource = async () => {
    if (Post.resource == -1)
      return;
    let result = await fetch("http://localhost:8080/public/post_resource/" + Post.resource);
    let resource_dom = ""
    if (result.status === 200) {
      let data = await result.json();
      if (["jpg", "png", "jpeg"].includes(data.filetype))
        resource_dom = <img src={"data:image/jpg;base64," + data.bytes} />
      else if (["avi", "mp4"].includes(data.filetype))
        resource_dom = <iframe src={"data:video/mp4;base64," + data.bytes} />
    }
    setResourceDom(resource_dom)
  }

  const loadComments = async () => {
    let params = `id_str=${Post.id}`;
    let result = await fetch("http://localhost:8080/public/comments?" + params);
    if (result.status === 200) {
      setComments(await result.json());
    }
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
      {
        !params.comment ?
          <div>
            {
              !CommentsLoaded ?
                <AlertLink onClick={() => { loadComments().then(() => { changeCommentsLoaded() }) }} style={{ display: "block" }} className='p-2 text-align-center fs-6 fw-light text-primary'>
                  Hozzászólások betöltése!
                </AlertLink>
                : ""
            }

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
                  </Container>
                  :
                  <div className='p-2 text-align-center'>Nincsenek hozzászólások</div>
                : ""
            }

          </div>
          : ""
      }
      <div className={styles.commentCreator}>
        {User ? <CreatePost style={{ width: "90%" }} postid={params.postid} user={User} parent={Post.id}></CreatePost> : ""}
      </div>
    </div>
  )
}

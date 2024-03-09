import React, { useEffect, useReducer, useRef, useState } from 'react'
import CreatePost from '../components/Basic/CreatePost';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Post from '../components/Basic/Post';

export default function Home(params) {

  const postsOffsetGap = 3;

  const [User, setUser] = useState(null);
  const [PostsOffset, updatePostsOffset] = useReducer((state,action)=>{return state + postsOffsetGap},0);
  const [PostsAmount, setPostsAmount] = useState(3);
  const [Posts, setPosts] = useState([]);

  const [LoadMorePosts, setLoadMorePosts] = useState(true);

  useEffect(()=>{
      window.addEventListener("scroll",()=>{
        if(isScrollInBottom()){
          setLoadMorePosts(true);
        }
      })
  },[])

  useEffect(() => {
      setUser(params.user);
  }, [params]);

  useEffect(()=>{
    if(LoadMorePosts === true){
      updatePostsOffset();
      setTimeout(() => {
        getPosts().then(()=>setLoadMorePosts(false));
      }, 100);
    }
  },[LoadMorePosts])

  function isScrollInBottom() {
    return (window.innerHeight + window.scrollY) >= document.body.offsetHeight;
  }

  const getPosts = async () => {
      let params = `amount_str=${PostsAmount}&offset_str=${PostsOffset}`;
      let result = await fetch("http://localhost:8080/public/post/getWithOffset?"+params);
      if(result.status === 200){
        let data = await result.json();
        setPosts([...Posts,...data]);
      }
  }

  return (
    <div>
      <div>
        {
          User ? <CreatePost user={User}></CreatePost> : ''
        }
      </div>
      <Container>
          {
            Posts.map((e,i)=>(
              <Row key={i} className='mt-3 p-0'>
                <Post postid={i} user={User} post={e}></Post>
              </Row>
            ))
          }
      </Container>
      <div className='text-center mb-3 mt-3'>{true ? "Loading..." : "..."}</div>
    </div>
  )
}

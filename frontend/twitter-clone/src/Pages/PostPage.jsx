import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom';
import ENV from "../env";

export default function PostPage(params) {

    const [User, setUser] = useState({ displayed_name: "" }); // Current client user 
    const { id } = useParams();
    const [Post,setPost] = useState();


    useEffect(()=>{
        setUser(params.user);
        fetchPostData();
    },[params])

    const fetchPostData = async() => {
        let result = await fetch(ENV.API_DOMAIN+"/public/post_withAll/"+id);
        if(result.status === 200){
          let data = await result.json();
          console.log(data);
        }
    }

  return (
    <div>

    </div>
  )
}

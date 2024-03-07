import React, { useEffect, useState } from 'react'
import styles from "../../styles/post.module.css"
import ProfilePicture from "../Profile/ProfilePicture"
import ENV from "../../env"

export default function Comment(params) {

    const [User, setUser] = useState({ displayed_name: "" });
    const [Post, setPosts] = useState({ 
        content: "", 
        resource: null, 
        date: "", 
        user: {
          displayed_name : ""
        } 
      });
    const [ResourceDom, setResourceDom] = useState();

    useEffect(() => {
        setUser(params.user);
        setPosts(params.post);
    }, [params]);

    useEffect(() => {
        readPostResource(Post.resource);
    }, [Post])

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
        <div style={{border:0}} className={"mt-3 mb-3 " + styles.post}>
            <div style={{border:0}} className={styles.user}>
                <ProfilePicture className={styles.userpicture} user={Post.user}></ProfilePicture>
                <div>
                    <div className='fs-4'>{Post.user.displayed_name}</div>
                    <div className='fs-6 fw-light'>{(new Date(Post.date)).toDateString()}</div>
                </div>
            </div>
            <div style={{border:0}} className={styles.body}>
                <div className={styles.content + " fs-5"}>{Post.content}</div>
                {ResourceDom}
            </div>
        </div>
    )
}

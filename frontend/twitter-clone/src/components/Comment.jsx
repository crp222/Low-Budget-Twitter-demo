import React, { useEffect, useState } from 'react'
import styles from "../styles/post.module.css"
import ProfilePicture from './ProfilePicture';

export default function Comment(params) {

    const [User, setUser] = useState({ displayed_name: "" });
    const [Post, setPosts] = useState({ content: "", resource: "-1", date: "" });
    const [PostUser, setPostUser] = useState({ displayed_name: "" });
    const [ResourceDom, setResourceDom] = useState();

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

    return (
        <div style={{border:0}} className={"mt-3 mb-3 " + styles.post}>
            <div style={{border:0}} className={styles.user}>
                <ProfilePicture className={styles.userpicture} user={PostUser}></ProfilePicture>
                <div>
                    <div className='fs-4'>{PostUser.displayed_name}</div>
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

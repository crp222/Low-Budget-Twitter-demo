import React, { useEffect, useRef, useState } from 'react'
import styles from "../styles/createpost.module.css";
import ProfilePicture from './ProfilePicture';
import Cookies from "js-cookie";
import OverlayTrigger from 'react-bootstrap/OverlayTrigger';
import Image from 'react-bootstrap/Image';
import Tooltip from "react-bootstrap/Tooltip"

export default function CreatePost(params) {

  const [User, setUser] = useState(null);
  const uploadRef = useRef();
  const [UploadedBase64, setUploadedBase64] = useState("");
  const [UploadedType, setUploadedType] = useState("");
  const [PostInput, setPostInput] = useState("");
  const [UploadedName, setUploadedName] = useState("");

  useEffect(() => {
      setUser(params.user);
  }, [params]);

    /**
     * 
     * @param {Event} event 
     */
    const readUploaded = (event) => {
      const fr = new FileReader();
      const file_path = event.target.files[0];
      fr.onload = (e) => {
          try {
             setUploadedName(file_path.name);
              setUploadedType(file_path.type.split("/")[1])
              let base64 = btoa(
                  new Uint8Array(e.target.result)
                    .reduce((data, byte) => data + String.fromCharCode(byte), '')
                );
              setUploadedBase64(base64);
          }catch{
              alert("A kep merete tul nagy!");
              uploadRef.current.value = null;
          }
      }
      fr.readAsArrayBuffer(file_path);
  }

  const createPost = async () => {
      let body = {
          fileBase64 : UploadedBase64,
          fileType : UploadedType,
          text : PostInput,
          parent : params.parent ? params.parent : -1,
      }
      let result = await fetch("http://localhost:8080/post/create",{
        method:"post",
        body : JSON.stringify(body),
        headers : {
          "Content-Type" : "application/json",
          "Authorization" : Cookies.get("usertoken")
        }
      })
      if(result.status == 200 || result.status == 400)
        alert(await result.text());
      else 
        alert("A post letrehozasa nem sikerult! :(");
      window.location.reload();
  }

  const ImagePreviewOverlay = (props) => {
    let preview = "Nincs kép/videó feltöltve!";

    if(UploadedType === "jpg" || UploadedType === "png" || UploadedType === "jpeg")
      preview = <Image width="300px" height="300px" src={"data:image/jpeg;base64,"+UploadedBase64}></Image>;
    else if(UploadedName)
      preview = UploadedName;

    return <Tooltip {...props} >
      {preview}
    </Tooltip>;
  }

  return (
      <div className={styles.postCreatorBody} style={params.style}>
          <ProfilePicture user={User} width="70px" height="70px" style={{borderRadius:"100%"}}></ProfilePicture>
          <textarea className={styles.postInput} placeholder={params.parent ? 'Comment hozzáadása' : 'Mi jár a fejedben?'} onChange={(e)=>setPostInput(e.target.value)}/>
          <OverlayTrigger placement='bottom' overlay={ImagePreviewOverlay}>
                <label htmlFor={"uploadInput" + (params.postid ? params.postid : "")} className={styles.imageUpload}><svg fill='rgb(13,110,253)' viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><g><path d="M0 0h24v24H0z" fill="none"/><path d="M21 15v3h3v2h-3v3h-2v-3h-3v-2h3v-3h2zm.008-12c.548 0 .992.445.992.993V13h-2V5H4v13.999L14 9l3 3v2.829l-3-3L6.827 19H14v2H2.992A.993.993 0 0 1 2 20.007V3.993A1 1 0 0 1 2.992 3h18.016zM8 7a2 2 0 1 1 0 4 2 2 0 0 1 0-4z"/></g></svg>
                </label>
          </OverlayTrigger>
          <input accept='video/*|image/*' ref={uploadRef} type="file" id={"uploadInput" + (params.postid ? params.postid : "")} hidden onChange={(e)=>readUploaded(e)}/>
          <button className={styles.postButton+" btn btn-outline-primary"} onClick={()=>createPost()}>Közzététel</button>
      </div>
  )
}

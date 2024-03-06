import React, { useEffect, useState } from 'react'
import Image from 'react-bootstrap/Image';

export default function ProfilePicture(params) {

    const [User, setUser] = useState({profile_picture:null});

    useEffect(() => {
      params.user ? setUser(params.user) : null;
    }, [params]);

  return (
    <Image className={params.className} style={params.style} width={params.width} height={params.height} src={User.profile_picture ? "data:image/jpeg;base64,"+User.profile_picture : 
                        "https://betterpet.com/wp-content/uploads/2023/06/fluffy-samoyed-puppy-running.jpeg"}></Image>
  )
}

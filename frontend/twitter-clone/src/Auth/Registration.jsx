import React, { useRef, useState } from 'react'
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';

export default function Registration() {

    const [Username, setUsername] = useState("");
    const [Password, setPassword] = useState("");
    const [Email, setEmail] = useState("");
    const [DisplayedName, setDisplayedName] = useState("");
    const [ProfilePicture, setProfilePicture] = useState("");
    const profilePicutreRef = useRef();

    /**
     * 
     * @param {Event} event 
     */
    const readProfilePicture = (event) => {
        const fr = new FileReader();
        const file_path = event.target.files[0];
        fr.onload = (e) => {
            try {
                let base64 = btoa(
                    new Uint8Array(e.target.result)
                      .reduce((data, byte) => data + String.fromCharCode(byte), '')
                  );
                setProfilePicture(base64);
            }catch{
                alert("A kep merete tul nagy!");
                profilePicutreRef.current.value = null;
            }
        }
        fr.readAsArrayBuffer(file_path);
    }

    const registration = () => {
        let params = `username=${Username}&password=${Password}&email=${Email}&displayedName=${DisplayedName}`
        fetch("http://localhost:8080/reg?"+params,{
            method:"POST",
            headers : {
                "Content-Type" : "application/json"
            },
            body : JSON.stringify({"profile_picture":ProfilePicture})
        })
        .then(res => {
            if(res.status == 200){
                alert("Sikeres regisztracio!");
                window.location = "/";
            }else {
                res.text().then(message => {
                    alert(message);
                })
            }
        })
    }

  return (
    <div>
        <Form>
            <Form.Group className="mb-3" controlId="email">
                <Form.Label>Email</Form.Label>
                <Form.Control type="email" placeholder="Email"  onChange={(e)=>setEmail(e.target.value)}/>
            </Form.Group>

            <Form.Group className="mb-3" controlId="password">
                <Form.Label>Jelszó</Form.Label>
                <Form.Control type="password" placeholder="Jelszó"  onChange={(e)=>setPassword(e.target.value)}/>
            </Form.Group>

            <Form.Group className="mb-3" controlId="username">
                <Form.Label>Felhasználónév</Form.Label>
                <Form.Control type="text" placeholder="Felhasználónév"  onChange={(e)=>setUsername(e.target.value)}/>
            </Form.Group>

            <Form.Group className="mb-3" controlId="displayed_name">
                <Form.Label>Megjelenített név</Form.Label>
                <Form.Control type="text" placeholder="Megjelenített név"  onChange={(e)=>setDisplayedName(e.target.value)}/>
            </Form.Group>

            <Form.Group className="mb-3" controlId="profile_picture">
                <Form.Label>Profilkép</Form.Label>
                <Form.Control type="file" accept=".jpg, .jpeg, .png" ref={profilePicutreRef} onChange={(e)=>readProfilePicture(e)}/>
            </Form.Group>

            <Button onClick={()=>registration()} variant="primary" type="button">
                Regisztráció
            </Button>
        </Form>
        <div style={{display:"none"}} className='fw-bold fs-1 text-white'>Profilkép feltöltése!</div>
    </div>
  )
}

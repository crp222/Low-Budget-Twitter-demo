import React, { useState } from 'react'
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Cookie from "js-cookie"

export default function Login() {

    const [Username, setUsername] = useState("");
    const [Password, setPassword] = useState("");

    const login = () => {
        fetch(`http://localhost:8080/login?username=${Username}&password=${Password}`, { 
            method: 'post', 
        }).then(res => {
            if(res.status == 200){
                res.text().then(token => {
                    console.log(token);
                    // TODO : expiration time
                    Cookie.set("usertoken",token);
                    alert("Siekeres bejelentkezes!");
                    window.location = "/";
                })
            }else {
                alert("Rossz felhasznalonev vagy jelszo!");
            }
        })
    }

  return (
    <div>
        <Form >
            <Form.Group className="mb-3" controlId="username">
                <Form.Label>Felhasználónév</Form.Label>
                <Form.Control type="text" placeholder="Felhasználónév"  onChange={(e)=>setUsername(e.target.value)}/>
            </Form.Group>
            <Form.Group className="mb-3" controlId="password">
                <Form.Label>Jelszó</Form.Label>
                <Form.Control type="password" placeholder="Jelszó"  onChange={(e)=>setPassword(e.target.value)}/>
            </Form.Group>

            <Form.Group className="mb-3">
                <a href='/reg'>Regisztráció</a>
            </Form.Group>
            <Button onClick={()=>login()} variant="primary" type="button">
                Bejelentkezés
            </Button>
        </Form>
    </div>
  )
}

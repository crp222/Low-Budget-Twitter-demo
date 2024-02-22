import React, { useEffect, useState } from 'react'
import Nav from 'react-bootstrap/Nav';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Cookie from "js-cookie"
import ProfilePicture from './components/ProfilePicture';

export default function NavBar(params) {

    const [User, setUser] = useState(null);

    useEffect(() => {
        setUser(params.user);
    }, [params]);

    const logout = () => {
       Cookie.remove("usertoken")
       window.location.reload();
    }

  return (
    <Nav style={{width:"300px"}} className='mt-2 border mx-auto d-flex align-items-center justify-content-center' variant="pills" activeKey="1">
        <Nav.Item>
            <Nav.Link href="/">
                Főoldal
            </Nav.Link>
        </Nav.Item>
        <Nav.Item>
            {
                !User ? 
                <Nav.Link title="Item" href='/login'>
                    Bejelentkezés
                </Nav.Link> 
                :
                <NavDropdown title="Profil">
                    <NavDropdown.Item><ProfilePicture width="200px" height="200px" user={User}></ProfilePicture></NavDropdown.Item>
                    <NavDropdown.Item className="fw-bold">Megjelenített név: {User.displayed_name}</NavDropdown.Item>
                    <NavDropdown.Item>Felhasználónév: {User.username}</NavDropdown.Item>
                    <NavDropdown.Item>Email: {User.email}</NavDropdown.Item>
                    <NavDropdown.Item>Rang: {User.role}</NavDropdown.Item>
                    <NavDropdown.Item className='text-danger' onClick={()=>logout()}>Logout</NavDropdown.Item>
                </NavDropdown>
            }
        </Nav.Item>
    </Nav>
  )
}

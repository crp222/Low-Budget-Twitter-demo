import * as React from "react";
import { createRoot } from "react-dom/client";
import {
  createBrowserRouter,
  RouterProvider,
  Route,
  Link,
} from "react-router-dom";
import Registration from "./Auth/Registration";
import Home from "./Home"
import 'bootstrap/dist/css/bootstrap.min.css';
import NavBar from "./NavBar"
import Login from "./Auth/Login"
import Cookie from "js-cookie"

async function getCurrentUserInformation() {
  let token = "";
  if(Cookie.get("usertoken"))
      token = Cookie.get("usertoken");

  let userinfo = await fetch("http://localhost:8080/user/currentuser",{
      headers:{
          "Authorization" : `${token}`,
      }
  })
  if(userinfo.status == 200){
    return await userinfo.json();
  }else {
    return null;
  }
}

async function startApp() {
  let userInfo = await getCurrentUserInformation();
  
  const router = createBrowserRouter([
    {
      path: "/",
      element: <Home user={userInfo}></Home>,
    },
    {
      path: "/reg",
      element: <Registration></Registration>,
    },
    {
      path: "/login",
      element: <Login></Login>,
    },
  ]);

  createRoot(document.getElementById("root")).render(
    <div>
      <NavBar user={userInfo}></NavBar>
      <RouterProvider router={router} />
    </div>
  );
}

startApp();

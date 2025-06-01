import { useState } from "react";
import { Outlet } from "react-router-dom";
import Nav from "./Nav";

const Layout = () => {
    const [token, setToken] = useState(null);
    return (
        <div>
            <Nav token={token} setToken={setToken} />
            <div>
                <Outlet context={{ token, setToken }} />
            </div>
        </div>
    )
}

export default Layout
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

const Nav = ({ token, setToken }) => {
    const [checkToken, setCheckToken] = useState()

    useEffect(() => {
        setCheckToken(localStorage.getItem("token"))
        console.log(token)
    }, [])

    const Logout = () => {
        localStorage.clear();
        setToken(null);
        setCheckToken(null)
    }

    return (
        <nav style={{ padding: '1rem', background: '#eee' }}>
            <Link to="/shipping">출고</Link>
            <Link to="/register">데이터 편집</Link>
            {checkToken == null ? <Link to="/login">login</Link> : <button onClick={Logout}>Logout</button>}
        </nav>
    )
}

export default Nav
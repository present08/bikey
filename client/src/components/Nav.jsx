import { Link } from "react-router-dom"

const Nav = () => {
    return (
        <nav style={{ padding: '1rem', background: '#eee' }}>
            <Link to="/shipping">출고</Link>
            <Link to="/register">데이터 편집</Link>
        </nav>
    )
}

export default Nav
import { Navigate } from "react-router-dom";

const PrivateRoute = ({ children, requiredRole }) => {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');

    if (!token) {
        alert("로그인이 필요합니다.")
        return <Navigate to="/login" />;
    }

    if (requiredRole && role !== requiredRole) {
        alert("접근 권한이 없습니다.")
        return <Navigate to="/" />;
    }

    return children;
}

export default PrivateRoute
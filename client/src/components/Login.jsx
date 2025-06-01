import { useState } from 'react';
import { useNavigate, useOutletContext } from 'react-router-dom';
import { toLogin } from './../api/BikeyApi';

const Login = () => {
    const [id, setId] = useState('');
    const [pw, setPw] = useState('');
    const navigate = useNavigate();
    const { setToken } = useOutletContext();

    const login = (e) => {
        e.preventDefault();

        const token = toLogin(id, pw);
        setToken(token);

        // 로그인 성공 시 홈으로 이동
        alert("안녕하세요.")
        navigate('/');
    };

    return (
        <div>
            <h2>로그인</h2>
            <form onSubmit={login}>
                <div>
                    <label>아이디: </label>
                    <input
                        type="id"
                        value={id}
                        onChange={(e) => setId(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>비밀번호: </label>
                    <input
                        type="password"
                        value={pw}
                        onChange={(e) => setPw(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">로그인</button>
            </form>
        </div>
    );
}

export default Login;

import axios from "axios";

const instance = axios.create({
    baseURL: 'http://localhost:8080',
});

// 요청 보낼 때 인터셉트 후 토큰을 헤더에 넣어 보냄
instance.interceptors.request.use(config => {
    const token = localStorage.getItem('token');

    if (token) config.headers.Authorization = token;

    return config;
})

// 토큰 시간 만료 시 처리
instance.interceptors.response.use(
    res => res,
    error => {
        if (error.response) {
            const status = error.response.status;
            const data = error.response.data;

            if (
                status === 401
                // && data?.error?.toLowerCase().includes('expired')
            ) {
                alert('세션이 만료되었습니다. 다시 로그인해주세요.');
                window.location.href = '/login';
            }
        }
        return Promise.reject(error);
    }
)

export default instance;
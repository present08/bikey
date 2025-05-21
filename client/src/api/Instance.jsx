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
        if (error.response && error.response.status === 401) {
            localStorage.clear();
            window.location.href = '/';
            return Promise.reject(error);
        }
    }
)

export default instance;
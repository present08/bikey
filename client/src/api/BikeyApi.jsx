import axios from "axios";
import axiosInstance from "./Instance";

const HOST = "http://localhost:8080"

// DB 매핑 리스트 업데이트(한개)
export const insert_product = async (division, productName, transName) => {
    try {
        if (division != "" && productName != "" && transName != "") {
            await axiosInstance.post(`${HOST}/register`, { division, productName, transName });
        } else {
            alert("입력이 잘못 되었습니다.")
        }
        return await get_product();
    } catch (error) {
        alert("권한이 없거나 재로그인하세요.")
        return;
    }
}

// DB 매핑 리스트 업데이트(파일)
export const insertfile = async (file) => {
    try {
        const formData = new FormData();
        formData.append("file", file);
        const response = await axiosInstance.post(`${HOST}/registerfile`, formData, {
            headers: { "Content-Type": "multipart/form-data" },
        });
        return await get_product()
    } catch (error) {
        alert("권한이 없거나 재로그인하세요.")
        return;
    }
}

// 매핑 데이터 가져오기
export const get_product = async () => {
    try {
        const response = await axiosInstance.get(`${HOST}/getProductAll`);
        return response.data;
    } catch (error) {
        alert("권한이 없거나 재로그인하세요.");
        return;
    }
}

// 운송장 분류
export const returnBikey = async (file) => {
    try {
        const formData = new FormData();
        formData.append("file", file);
        formData.append("password", "1234");
        const response = await axiosInstance.post(`${HOST}/excelupload`, formData, {
            headers: { "Content-Type": "multipart/form-data" },
        });
        return response.data
    } catch (error) {
        return;
    }
}

// login
export const toLogin = async (id, pw) => {
    try {
        const formData = new FormData();
        formData.append("username", id);
        formData.append("password", 1234);
        const res = await axios.post(`${HOST}/login`, formData);
        const token = res.headers.get('Authorization');
        localStorage.setItem('token', token);
        const payload = JSON.parse(atob(token.split('.')[1]));
        localStorage.setItem('role', payload.role);
        return res
    } catch (error) {
        console.log(error)
        alert("아이디 또는 비밀번호가 잘못되었습니다.")
    }
}
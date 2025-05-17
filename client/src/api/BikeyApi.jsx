import axios from "axios";

const HOST = "http://localhost:8080"

// DB 매핑 리스트 업데이트(한개)
export const insert_product = async (division, productName, transName) => {
    if (division != "" && productName != "" && transName != "") {
        await axios.post(`${HOST}/register`, { division, productName, transName });
    } else {
        alert("입력이 잘못 되었습니다.")
    }
    return await get_product();
}

// DB 매핑 리스트 업데이트(파일)
export const insertfile = async (file) => {
    const formData = new FormData();
    formData.append("file", file);
    const response = await axios.post(`${HOST}/registerfile`, formData, {
        headers: { "Content-Type": "multipart/form-data" },
    });
    return await get_product()
}

// 매핑 데이터 가져오기
export const get_product = async () => {
    const response = await axios.get(`${HOST}/getProductAll`);
    return response.data;
}

// 운송장 분류
export const returnBikey = async (file) => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("password", "1234");
    const response = await axios.post(`${HOST}/excelupload`, formData, {
        headers: { "Content-Type": "multipart/form-data" },
    });
    return response.data
}

// login
export const toLogin = async (id, pw) => {
    try {
        const res = await axios.post(`${HOST}/auth/login`, { id, pw });
        return res.data
    } catch (error) {
        console.log(error)
        alert("아이디 또는 비밀번호가 잘못되었습니다.")
    }
}
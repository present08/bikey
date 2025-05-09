import axios from "axios";

const HOST = "http://localhost:8080"


export const insert_product = async (division, productName, transName) => {
    if (division != "" && productName != "" && transName != "") {
        await axios.post(`${HOST}/register`, { division, productName, transName });
    } else {
        alert("입력이 잘못 되었습니다.")
    }
    return await get_product();
}

export const insertfile = async (file) => {
    const formData = new FormData();
    formData.append("file", file);
    const response = await axios.post(`${HOST}/registerfile`, formData, {
        headers: { "Content-Type": "multipart/form-data" },
    });
    return await get_product()
}

export const get_product = async () => {
    const response = await axios.get(`${HOST}/getProductAll`);
    return response.data;
}

export const returnBikey = async (file) => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("password", "1234");
    const response = await axios.post(`${HOST}/excelupload`, formData, {
        headers: { "Content-Type": "multipart/form-data" },
    });
    return response.data
}
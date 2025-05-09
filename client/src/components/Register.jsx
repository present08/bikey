import React, { useEffect, useRef, useState } from 'react';
import { get_product, insert_product, insertfile } from '../api/BikeyApi';

const Register = () => {
    const [division, setDivision] = useState("")
    const [productName, setProductName] = useState("")
    const [transName, setTransName] = useState("")
    const [productList, setProductList] = useState([])
    const [file, setFile] = useState(null)
    const first_input = useRef(null);
    useEffect(() => {
        const updateData = async () => {
            const products = await get_product();
            setProductList(products);
        };
        updateData();
    }, [])

    const register = async (division, productName, transName) => {
        const updateList = await insert_product(division, productName, transName);
        setProductList(updateList);
        setDivision('');
        setProductName('');
        setTransName('');
        setTimeout(() => {
            first_input.current?.focus();
        }, 0);
    }

    const enter_register = (e) => {
        if (e.key === "Enter") {
            register(division, productName, transName)
        }
    }

    const uploadfile = async () => {
        const updateList = await insertfile(file);
        setProductList(updateList)
    }

    return (
        <div>
            <table>
                <tr>
                    <th>구분</th>
                    <th>쇼핑몰에 등록된 이름</th>
                    <th>바꾸고 싶은 이름</th>
                </tr>
                <tr>
                    <td><input type="file" onChange={e => setFile(e.target.files[0])} /></td>
                    <td><button onClick={uploadfile}>File Upload</button></td>
                </tr>
                <tr>
                    <td><input type='text' value={division} onChange={e => setDivision(e.target.value)} ref={first_input} /></td>
                    <td><input type='text' value={productName} onChange={e => setProductName(e.target.value)} /></td>
                    <td><input type='text' value={transName} onChange={e => setTransName(e.target.value)} onKeyDown={enter_register} /></td>
                    <td><button onClick={() => register(division, productName, transName)}>등록</button></td>
                </tr>
                {productList.length > 0 ? (
                    productList.map(product => (
                        <tr key={product.id}>
                            <td>{product.division}</td>
                            <td>{product.product_name}</td>
                            <td>{product.trans_name}</td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="2">상품 목록이 없습니다.</td>
                    </tr>
                )}

            </table>
        </div>
    )
}

export default Register
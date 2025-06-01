import { useState } from "react";
import { returnBikey } from "../api/BikeyApi";

const Shipping = () => {
    const [file, setFile] = useState(null)
    const [orderList, setOrderList] = useState([])
    const uploadfile = async () => {
        const updateList = await returnBikey(file);
        setOrderList(updateList)
    }
    return (
        <div>
            <div>
                <input type="file" onChange={e => setFile(e.target.files[0])} />
                <button onClick={uploadfile}>File Upload</button>
            </div>
            <table border={"1"}>
                <tr>
                    <th>주문번호</th>
                    <th>주문구분</th>
                    <th>주문자명</th>
                    <th>연락처</th>
                    <th>주문내역</th>
                    <th>주문일자</th>
                    <th>처리현황</th>
                    <th>처리완료일</th>
                </tr>
                {orderList.length > 0 ? (
                    orderList.map(order => (
                        <tr key={order.order_num}>
                            <td>{order.order_num}</td>
                            <td>{order.division}</td>
                            <td>{order.name}</td>
                            <td>{order.phone}</td>
                            <td>{order.model}/{order.model_option}</td>
                            <td>{order.order_date}</td>
                            <td>{order.complete_state}</td>
                            <td>{order.completion_date}</td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td>주문이 없습니다.</td>
                    </tr>
                )}
            </table>
        </div>
    )
}

export default Shipping
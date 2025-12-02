import { useEffect, useState } from "react";
import { getBookings, cancelBooking } from "../api/Api";

export default function Bookings() {
    const [bookings, setBookings] = useState([]);
    const [err, setErr] = useState(null);

    useEffect(() => { load() }, []);

    async function load() {
        try {
            const userId = localStorage.getItem('userId');
            const data = await getBookings(userId);
            setBookings(data);
        } catch (e) {
            setErr(e.message);
        }
    }

    async function handleCancel(bookingId) {
        if (!window.confirm("Отменить бронирование?")) return;
        try {
            await cancelBooking(bookingId);
            await load();
        } catch (e) {
            alert("Ошибка: " + e.message);
        }
    }

    return (
        <>
            <h2>Мои бронирования</h2>
            {err && <div className="error">{err}</div>}
            <ul>
                {bookings.map(b => (
                    <li key={b.id}>
                        {b.tour?.name || "Тур удалён"} - {b.bookingDate} - {"Cтатус: "} {b.status}
                        <button 
                            style={{ marginLeft: "10px" }} 
                            onClick={() => handleCancel(b.id)}
                        >
                            Отменить
                        </button>
                    </li>
                ))}
            </ul>
        </>
    );
}

import { useEffect, useState } from "react";
import { getAllBookingsForAgent, updateBookingStatus } from "../api/Api";

export default function AgentBookings() {
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);

    async function load() {
        try {
            const data = await getAllBookingsForAgent();
            setBookings(data);
        } catch (e) {
            console.error(e);
            alert("Не удалось загрузить бронирования");
        }
        setLoading(false);
    }

    useEffect(() => {
        load();
    }, []);

    async function changeStatus(id, status) {
        try {
            await updateBookingStatus(id, status);
            load();
        } catch (e) {
            console.error(e);
            alert("Ошибка изменения статуса");
        }
    }

    if (loading) return <p>Загрузка...</p>;

    return (
        <div>
            <h2>Бронирования пользователей</h2>

            {bookings.length === 0 && <p>Нет бронирований.</p>}

            {bookings.map(b => (
                <div key={b.id} className="card">
                    <p><b>Тур:</b> {b.tour?.title}</p>
                    <p><b>Пользователь:</b> {b.user?.username}</p>
                    <p><b>Статус:</b> {b.status}</p>

                    <button onClick={() => changeStatus(b.id, "APPROVED")}>
                        Одобрить
                    </button>

                    <button onClick={() => changeStatus(b.id, "REJECTED")}>
                        Отклонить
                    </button>

                    <button onClick={() => changeStatus(b.id, "COMPLETED")}>
                        Завершить
                    </button>
                </div>
            ))}
        </div>
    );
}

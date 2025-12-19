import { useEffect, useState } from "react";
import { getAllBookingsForAgent, updateBookingStatus } from "../api/Api";

export default function AgentBookings() {
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [statusFilter, setStatusFilter] = useState("ALL");

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

    const filteredBookings = bookings.filter(b =>
        statusFilter === "ALL" ? true : b.status === statusFilter
    );

    if (loading) return <p>Загрузка...</p>;

    return (
        <div>
            <h2>Бронирования пользователей</h2>

            {/* Фильтр по статусу */}
            <select
                value={statusFilter}
                onChange={e => setStatusFilter(e.target.value)}
                style={{ marginBottom: "15px" }}
            >
                <option value="ALL">Все</option>
                <option value="PENDING">Ожидают</option>
                <option value="APPROVED">Одобрены</option>
                <option value="REJECTED">Отклонены</option>
                <option value="COMPLETED">Завершены</option>
            </select>

            {filteredBookings.length === 0 && <p>Нет бронирований.</p>}

            {filteredBookings.map(b => (
                <div key={b.id} className="card">
                    <p><b>Тур:</b> {b.tour?.name}</p>
                    <p><b>Пользователь:</b> {b.user?.username}</p>
                    <p><b>Статус:</b> {b.status}</p>

                    <button
                        className="booking_button"
                        onClick={() => changeStatus(b.id, "APPROVED")}
                    >
                        Одобрить
                    </button>

                    <button
                        className="booking_button"
                        onClick={() => changeStatus(b.id, "REJECTED")}
                    >
                        Отклонить
                    </button>

                    <button
                        className="booking_button"
                        onClick={() => changeStatus(b.id, "COMPLETED")}
                    >
                        Завершить
                    </button>
                </div>
            ))}
        </div>
    );
}

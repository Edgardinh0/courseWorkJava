import { useState, useEffect } from "react";
import { 
    getAllTours, 
    filterTours, 
    createBooking,
    deleteTour,
    getReviews,
    addReview,
    deleteReview
} from "../api/Api";

export default function Tours({ role, currentUser }) {
    const [tours, setTours] = useState([]);
    const [reviews, setReviews] = useState({}); // { tourId: [reviews] }
    const [newReview, setNewReview] = useState({});
    const [toursWithReview, setToursWithReview] = useState(new Set());
    const [filters, setFilters] = useState({
        startDate: "",
        endDate: "",
        minPrice: "",
        maxPrice: ""
    });
    const [err, setErr] = useState(null);

    useEffect(() => { loadTours(); }, []);

    async function loadTours() {
        try {
            const data = await getAllTours();
            setTours(data);

            const reviewsMap = {};
            const withReview = new Set();
            const userId = parseInt(currentUser?.id || localStorage.getItem("userId"));

            for (const tour of data) {
                const tourReviews = await getReviews(tour.id);
                reviewsMap[tour.id] = tourReviews;

                if (role === "TRAVELER" && tourReviews.some(r => r.user?.id === userId)) {
                    withReview.add(tour.id);
                }
            }

            setReviews(reviewsMap);
            setToursWithReview(withReview);
        } catch (e) {
            setErr(e.message);
        }
    }

    async function onFilter(e) {
        e.preventDefault();
        try {
            const params = {};
            if (filters.startDate) params.startDate = filters.startDate;
            if (filters.endDate) params.endDate = filters.endDate;
            if (filters.minPrice) params.minPrice = filters.minPrice;
            if (filters.maxPrice) params.maxPrice = filters.maxPrice;

            const data = await filterTours(params);
            setTours(data);

            const reviewsMap = {};
            const withReview = new Set();
            const userId = parseInt(currentUser?.id || localStorage.getItem("userId"));

            for (const tour of data) {
                const tourReviews = await getReviews(tour.id);
                reviewsMap[tour.id] = tourReviews;

                if (role === "TRAVELER" && tourReviews.some(r => r.user?.id === userId)) {
                    withReview.add(tour.id);
                }
            }

            setReviews(reviewsMap);
            setToursWithReview(withReview);

        } catch (e) {
            setErr(e.message);
        }
    }

    async function handleBooking(tourId) {
        try {
            const userId = currentUser?.id || localStorage.getItem("userId");
            if (!userId) throw new Error("Необходимо авторизоваться, чтобы забронировать тур");
            await createBooking(userId, tourId);
            alert("Бронирование успешно!");
        } catch (e) {
            alert("Ошибка: " + e.message);
        }
    }

    async function handleDelete(tourId) {
    if (!window.confirm("Удалить этот тур?")) return;

    try {
        await deleteTour(tourId);
        await loadTours();
    } catch (e) {
        const msg =
            e.response?.data?.message ||
            "Нельзя удалить тур, на который уже есть бронирование";

        alert(msg);
    }
}


    async function handleAddReview(tourId) {
        const userId = currentUser?.id || localStorage.getItem("userId");
        if (!userId) {
            alert("Необходимо авторизоваться, чтобы оставить отзыв");
            return;
        }

        const { comment, rating } = newReview[tourId] || {};
        if (!comment || !rating) {
            alert("Введите комментарий и рейтинг");
            return;
        }

        try {
            await addReview(tourId, comment, rating);
            const updated = await getReviews(tourId);
            setReviews({ ...reviews, [tourId]: updated });

            setToursWithReview(prev => new Set(prev).add(tourId));
            setNewReview({ ...newReview, [tourId]: { comment: "", rating: "" } });
        } catch (e) {
            alert("Ошибка при добавлении отзыва: " + e.message);
        }
    }

    async function handleDeleteReview(reviewId, tourId) {
        const userId = currentUser?.id || localStorage.getItem("userId");
        if (!userId) {
            alert("Не удалось определить пользователя");
            return;
        }

        try {
            await deleteReview(reviewId);
            const updated = await getReviews(tourId);
            setReviews({ ...reviews, [tourId]: updated });

            const userReviewExists = updated.some(r => r.user?.id === parseInt(userId));
            if (!userReviewExists) {
                setToursWithReview(prev => {
                    const copy = new Set(prev);
                    copy.delete(tourId);
                    return copy;
                });
            }

        } catch (e) {
            alert("Ошибка при удалении отзыва: " + e.message);
        }
    }

    return (
        <div>
            <h2>Туры</h2>

            {/* Фильтр */}
            <form onSubmit={onFilter} className="filter">
                <input 
                    type="date"
                    placeholder="Дата начала"
                    value={filters.startDate}
                    onChange={e => setFilters({ ...filters, startDate: e.target.value })}
                />
                <input 
                    type="date"
                    placeholder="Дата окончания"
                    value={filters.endDate}
                    onChange={e => setFilters({ ...filters, endDate: e.target.value })}
                />
                <input 
                    type="number"
                    placeholder="Мин цена"
                    value={filters.minPrice}
                    onChange={e => setFilters({ ...filters, minPrice: e.target.value })}
                />
                <input 
                    type="number"
                    placeholder="Макс цена"
                    value={filters.maxPrice}
                    onChange={e => setFilters({ ...filters, maxPrice: e.target.value })}
                />
                <button type="submit">Фильтр</button>
                <button type="button" onClick={loadTours}>Сброс</button>
            </form>

            {err && <div className="error">{err}</div>}

            {/* Список туров */}
            <ul className="tours">
                {tours.map(t => (
                    <li key={t.id} className="tour">
                        <div><strong>{t.name}</strong> — {t.destination}</div>
                        <div>{t.startDate} → {t.endDate}</div>
                        <div>{t.price}$</div>

                        {/* бронирование только для авторизованных */}
                        {role === "TRAVELER" && (
                            <button onClick={() => handleBooking(t.id)}>Забронировать</button>
                        )}

                        {/* удаление тура */}
                        {(role === "ADMIN" || role === "AGENT") && (
                            <button className="danger" onClick={() => handleDelete(t.id)}>Удалить</button>
                        )}

                        {/* отзывы */}
                        <h4>Отзывы:</h4>
                        <ul>
                        {reviews[t.id]?.length > 0 ? reviews[t.id].map(r => (
                            <li key={r.id} className="review">
                                <span>⭐ {r.rating} — {r.comment}</span>
                                {(role === "ADMIN" || (role === "TRAVELER" && r.user?.id === parseInt(localStorage.getItem("userId")))) && (
                                    <button onClick={() => handleDeleteReview(r.id, t.id)}>
                                        Удалить
                                    </button>
                                )}
                            </li>
                        )) : <li>Отзывов пока нет</li>}
                        </ul>

                        {/* форма добавления отзыва только для авторизованных путешественников, если они ещё не оставили отзыв */}
                        {role === "TRAVELER" && !toursWithReview.has(t.id) && (
                            <div style={{ marginTop: "10px" }}>
                                <input
                                    type="text"
                                    placeholder="Комментарий"
                                    value={newReview[t.id]?.comment || ""}
                                    onChange={e => setNewReview({
                                        ...newReview,
                                        [t.id]: { ...newReview[t.id], comment: e.target.value }
                                    })}
                                />
                                <select 
                                    value={newReview[t.id]?.rating || ""}
                                    onChange={e => setNewReview({
                                        ...newReview,
                                        [t.id]: { ...newReview[t.id], rating: e.target.value}
                                    })}
                                >
                                    <option value="" disabled>Выберите оценку</option>
                                    <option value="1">⭐</option>
                                    <option value="2">⭐⭐</option>
                                    <option value="3">⭐⭐⭐</option>
                                    <option value="4">⭐⭐⭐⭐</option>
                                    <option value="5">⭐⭐⭐⭐⭐</option>
                                </select>
                                <button onClick={() => handleAddReview(t.id)}>Оставить отзыв</button>
                            </div>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
}

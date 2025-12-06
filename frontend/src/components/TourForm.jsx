import { useState } from "react";
import { addTour } from "../api/Api";

export default function TourForm({onAdded}) {
    const [tour, setTour] = useState({
        name:'',
        destination:'',
        startDate:'',
        endDate:'',
        price:''
    });
    const [err, setErr] = useState(null);

    async function submit(e) {
        e.preventDefault();
        setErr(null);

        const start = new Date(tour.startDate);
        const end = new Date(tour.endDate);
        const today = new Date();
        today.setHours(0,0,0,0);

        if (start < today) {
            setErr("Дата начала не может быть в прошлом");
            return;
        }

        if (end < start) {
            setErr("Дата окончания должна быть позже даты начала");
            return;
        }

        if (Number(tour.price) <= 0) {
            setErr("Цена не может быть меньше или равна 0")
            return;
        }

        try {
            const payload = { ...tour, price: Number(tour.price) };
            await addTour(payload);
            setTour({name:'', destination:'', startDate:'', endDate:'', price:''});
            onAdded();
        } catch (e) {
            setErr(e.message);
        }
    }


    return (
        <div className="card">
            <h3>Добавить тур</h3>
            <form onSubmit={submit}>
                <input placeholder="Название" 
                       value={tour.name} 
                       onChange={e=>setTour({...tour, name: e.target.value})}
                       required />

                <input placeholder="Направление"
                       value={tour.destination}
                       onChange={e=>setTour({...tour, destination: e.target.value})}
                       required />

                <input type="date"
                       value={tour.startDate}
                       onChange={e=>setTour({...tour, startDate: e.target.value})}
                       required />

                <input type="date"
                       value={tour.endDate}
                       onChange={e=>setTour({...tour, endDate: e.target.value})}
                       required />

                <input type="number" 
                       placeholder="Цена"
                       value={tour.price === '' ? '' : tour.price}
                       onChange={e=>setTour({...tour, price: e.target.value})}
                       required />

                <button type="submit">Добавить</button>
            </form>
            {err && <div className="error">{err}</div>}
        </div>
    );
}

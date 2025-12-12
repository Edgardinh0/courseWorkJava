import { useState } from "react";
import { predictPopularity } from "../api/Api";

export default function PredictPopularity() {
    const [dest, setDest] = useState("");
    const [result, setResult] = useState("");
    const [loading, setLoading] = useState(false);

    async function handlePredict() {
        setLoading(true);
        setResult('');
        
        try {
            const res = await predictPopularity(dest);
            setResult(res);
        } catch (e) {
            alert("Ошибка: " + e.message);
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="predict">
            <h3>Прогноз популярности направления</h3>

            <input
                type="text"
                placeholder="Введите направление"
                value={dest}
                onChange={e => setDest(e.target.value)}
            />

            <button onClick={handlePredict} className="predict_button" disabled={loading}>{loading ? 'Загрузка...' : 'Предсказать'}</button>

            {result && <div className="result">{result}</div>}
        </div>
    );
}

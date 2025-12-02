import { useState } from "react";
import { recommend } from "../api/Api";

export default function Recommend() {
    const [pref, setPref] = useState('')
    const [res, setRes] = useState(null)

    async function onAsk(e) {
        e.preventDefault()
        try {
            const r = await recommend(pref)
            setRes(r)
        } catch (err) {
            setRes('Ошибка: ' + err.message)
        }
    }

    return (
        <div className="card">
            <h3>Рекомендации (ИИ)</h3>
            <form onSubmit={onAsk}>
                <input value={pref} onChange={e=>setPref(e.target.value)} placeholder="пожелания (пляж, история, бюджет...)" />
                <button type="submit">Запросить</button>
            </form>
            {res && <div className="result">{res}</div>}
        </div>
    )
}
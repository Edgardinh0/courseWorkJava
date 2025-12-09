import { useState } from "react";
import { recommend } from "../api/Api";

export default function Recommend() {
const [prefs, setPrefs] = useState("");
const [result, setResult] = useState("");


    async function handleRecommend() {
        try {
            const res = await recommend(prefs);
            setResult(res);
        } catch (e) {
            alert("Ошибка: " + e.message);
        }
    }

    return (
        <div className="recommend">
            <h3>ИИ рекомендации туров</h3>
            <input
                type="text"
                placeholder="Ваши предпочтения"
                value={prefs}
                onChange={e => setPrefs(e.target.value)}
            />
            <button className='recommend_button' onClick={handleRecommend}>Получить рекомендации</button>
            {result && <div className="result">{result}</div>}
        </div>
    );
}

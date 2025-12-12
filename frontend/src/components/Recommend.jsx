import { useState } from "react";
import { recommend } from "../api/Api";

export default function Recommend() {
    const [prefs, setPrefs] = useState("");
    const [result, setResult] = useState("");
    const [loading, setLoading] = useState(false);


    async function handleRecommend() {
        setLoading(true)
        setResult('')
        
        try {
            const res = await recommend(prefs);
            setResult(res);
        } catch (e) {
            alert("Ошибка: " + e.message);
        } finally {
            setLoading(false)
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
            <button className='recommend_button' onClick={handleRecommend} disabled={loading}>{loading ? "Поиск..." : "Получить рекомендации"}</button>
            {result && !loading && <div className="result">{result}</div>}
        </div>
    );
}

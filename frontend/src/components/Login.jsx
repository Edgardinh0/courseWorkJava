import { useState, useEffect } from "react";
import { login, getCaptcha } from "../api/Api";

export default function Login({onLogin}) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [err, setErr] = useState(null);
    const [captchaImg, setCaptchaImg] = useState("");
    const [captcha, setCaptcha] = useState("");

    useEffect(() => {
        loadCaptcha();
    }, []);

    async function loadCaptcha() {
        const img = await getCaptcha();
        setCaptchaImg(img);
    }   


    async function submit(e) {
        e.preventDefault();
        try {
            const user = await login(username, password, captcha);
            onLogin(user);
        } catch (e) {
            setErr(e.message);
            loadCaptcha(); // обновляем капчу при ошибке
        }
    }



    return (
        <div className="card">
            <h2>Login</h2>
            <form onSubmit={submit}>
                <input placeholder="Username" value={username}
                    onChange={e => setUsername(e.target.value)} required />

                <input placeholder="Password" type="password" value={password}
                    onChange={e => setPassword(e.target.value)} required />

                {captchaImg && (
                    <div>
                        <img src={captchaImg} alt="captcha"
                            style={{ cursor: "pointer" }}
                            onClick={loadCaptcha} />
                    </div>
                )}

                <input placeholder="Введите код с картинки"
                    value={captcha}
                    onChange={e => setCaptcha(e.target.value)}
                    required />

                <button type="submit">Войти</button>
            </form>
            {err && <div className="error">{err}</div>}
        </div>
    )
}
import { useEffect, useState } from "react";
import { register, getCaptcha } from "../api/Api";

export default function Register({onRegistered}) {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [role, setRole] = useState('TRAVELER')
    const [err, setErr] = useState(null)
    const usernameRegex = /^[a-zA-Z0-9._]{3,30}$/;
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

        if (!usernameRegex.test(username)) {
            setErr("Логин может содержать только латинские буквы, цифры, точку и _ (3–30 символов)");
            return;
        }

        try {
            await register(username, password, role, captcha);
            onRegistered();
        } catch (e) {
            setErr(e.message);
            loadCaptcha();
        }
    }


    return (
        <div className="card">
            <h2>Register</h2>
            <form onSubmit={submit}>
                <input placeholder="Username" value={username} onChange={e=>setUsername(e.target.value)} required/>
                <input placeholder="Password" type='password' value={password} onChange={e=>setPassword(e.target.value)} required/>
                <select value={role} onChange={e=>setRole(e.target.value)}>
                    <option value="TRAVELER">Traveler</option>
                    <option value="AGENT">Agent</option>
                    <option value="ADMIN">Admin</option>
                </select>
                <img src={captchaImg} alt="captcha"
                    onClick={loadCaptcha}
                    style={{ cursor: "pointer" }} />
                <input placeholder="Введите код с картинки"
                    value={captcha}
                    onChange={e => setCaptcha(e.target.value)}
                    required />
                <button type="submit">Register</button>
            </form>
            {err && <div className="error">{err}</div>}
        </div>
    )
}
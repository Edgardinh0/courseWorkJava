import { useState } from "react";
import { login } from "../api/Api";

export default function Login({onLogin}) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [err, setErr] = useState(null);

    async function submit(e) {
    e.preventDefault()
    try {
        const user = await login(username, password);
        onLogin(user);
    } catch (e) {
        setErr(e.message)
    }
}


    return (
        <div className="card">
            <h2>Login</h2>
            <form onSubmit={submit}>
                <input placeholder="Username" value={username} onChange={e=>setUsername(e.target.value)} required/>
                <input placeholder="Password" type='password' value={password} onChange={e=>setPassword(e.target.value)} required/>
                <button type="submit">Войти</button>
            </form>
            {err && <div className="error">{err}</div>}
        </div>
    )
}
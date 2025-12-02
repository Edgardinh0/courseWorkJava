import { useState } from "react";
import { register } from "../api/Api";

export default function Register({onRegistered}) {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [role, setRole] = useState('TRAVELER')
    const [err, setErr] = useState(null)

    async function submit(e) {
        e.preventDefault()
        try {
            await register(username, password, role)
            onRegistered()
        } catch(e) {
            setErr(e.message)
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
                <button type="submit">Register</button>
            </form>
            {err && <div className="error">{err}</div>}
        </div>
    )
}
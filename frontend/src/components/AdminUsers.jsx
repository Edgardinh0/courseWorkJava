import { useState, useEffect } from "react";
import { getUsers, deleteUser } from "../api/Api";

export default function AdminUsers () {
    const [users, setUsers] = useState([])
    const [err, setErr] = useState(null)

    useEffect(() => { load() }, [])

    async function load() {
        try {
            const data = await getUsers()
            setUsers(data)
        } catch(e) {
            setErr(e.message)
        }
    }

    async function onDelete(id) {
        if (!window.confirm('Удалить пользователя?')) return
        try {
            await deleteUser(id)
            load()
        } catch(e) {
            alert(e.message)
        }
    }

    return (
        <>
            <h2>Пользователи</h2>
            {err && <div className="error">{err}</div>}
            <table className="users">
                <thead><tr><th>ID</th><th>Username</th><th>Role</th><th>Action</th></tr></thead>
                <tbody>
                    {users.map(u => (
                        <tr key={u.id}>
                            <td>{u.id}</td>
                            <td>{u.username}</td>
                            <td>{u.role}</td>
                            <td><button onClick={()=>onDelete(u.id)}>Удалить</button></td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </>
    )
}
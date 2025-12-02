import Logo from '/public/logo.png'

export default function NavBar({ onLogout, user, role, setView}) {
    return (
        <nav className="nav">
            <div style={{display: "flex", alignItems: "center", gap:12}}>
                <img src={Logo} alt="logo" style={{height:36}}></img>
                <div className='brand'>Travel Booking</div>
            </div>
            <div className='links'>
                <button onClick={() => setView('tours')}>Туры</button>
                {user && role === 'TRAVELER' && <button onClick={() => setView('bookings')}>Мои бронирования</button>}
                {role === 'ADMIN' && <button onClick={() => setView('admin')}>Панель Admin</button>}
                {role === 'AGENT' && <button onClick={() => setView('manage')}>Управление турами</button>}
                {role === 'AGENT' && (<button onClick={() => setView('agentBookings')}>Брон. клиентов</button>)}
            </div>
            <div className='auth'>
                {user ? (
                    <>
                        <span style={{marginRight:8}}>{user}</span>
                        <button onClick={onLogout}>Выйти из аккаунта</button>
                    </>
                ) : (
                    <>
                        <button onClick={() => setView('login')}>Login</button>
                        <button onClick={() => setView('register')}>Register</button>
                    </>
                )}
            </div>
        </nav>
    )
}
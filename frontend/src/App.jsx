import { useState, useEffect } from 'react'
import NavBar from './components/NavBar'
import Login from './components/Login'
import Register from './components/Register'
import Tours from './components/Tours'
import Bookings from './components/Bookings'
import TourForm from './components/TourForm'
import AdminUsers from './components/AdminUsers'
import Recommend from './components/Recommend'
import AgentBookings from './components/AgentBookings'
import PredictPopularity from './components/PredictPopularity'
import { logout } from './api/Api'
import './App.css'

function App() {
  const [view, setView] = useState('tours')
  const [user, setUser] = useState(localStorage.getItem('username') || null)
  const [role, setRole] = useState(localStorage.getItem('role') || null)

  useEffect(() => {
    const map = { admin: 'ADMIN', agent: 'AGENT', traveler: 'TRAVELER'}
    if (user && !role) {
      const r = map[user]
      if (r) {
        localStorage.setItem('role', r)
        setRole(r)
      }
    }
  }, [user])

  function handleLogout() {
    logout()
    setUser(null)
    setRole(null)
    setView('tours')
  }

  function onLogin(user) {
    const username = user?.username || user;
    const role = (user?.role && user.role.startsWith('ROLE_')) ? user.role.substring(5) : (localStorage.getItem('role') || null);

    setUser(username);
    setRole(role);
    localStorage.setItem('username', username);
    localStorage.setItem('role', role);
    setView('tours');
}


  return (
    <>
      <NavBar onLogout={handleLogout} user={user} role={role} setView={setView} />
      <div className='container'>
        {view === 'login' && <Login onLogin={onLogin}/>}
        {view === 'register' && <Register onRegistered={() => setView('login')} />}
        {role === 'TRAVELER' && <Recommend />}
        {role === 'AGENT' && <PredictPopularity />}
        {view === 'tours' && <Tours role={role} currentUser={user} />}
        {view === 'manage' && role === 'AGENT' && <TourForm onAdded={() => setView('tours')} />}
        {view === 'bookings' && <Bookings />}
        {view === 'admin' && role === 'ADMIN' && <AdminUsers />}
        {view === 'agentBookings' && role === 'AGENT' && <AgentBookings />}
      </div>
    </>
  )
}

export default App

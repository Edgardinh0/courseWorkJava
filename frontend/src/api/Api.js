import { API_BASE } from "./Config";

function authHeader() {
    const cred = localStorage.getItem('cred')
    if (!cred) return { 'Content-Type': 'application/json'}
    return {Authorization: `Basic ${cred}`, 'Content-Type': 'application/json'}
}

export async function login(username, password, captcha) {
    const res = await fetch(`${API_BASE}/auth/login`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        credentials: "include",
        body: JSON.stringify({ username, password, captcha })
    });

    if (!res.ok) {
        const msg = await res.text();
        throw new Error(msg);
    }

    const user = await res.json();

    localStorage.setItem('cred', btoa(`${username}:${password}`));
    localStorage.setItem('username', user.username);

    const roleShort = user.role?.startsWith('ROLE_')
        ? user.role.substring(5)
        : user.role;

    localStorage.setItem('role', roleShort);
    localStorage.setItem('userId', user.id);

    return { ...user, roleShort };
}



export function logout() {
    localStorage.removeItem('cred')
    localStorage.removeItem('username')
    localStorage.removeItem('role')
    localStorage.removeItem('userId')
}

export async function register(username, password, role, captcha) {
    const res = await fetch(`${API_BASE}/auth/register`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        credentials: "include",
        body: JSON.stringify({ username, password, role, captcha })
    });

    if (!res.ok) {
        const msg = await res.text();
        throw new Error(msg);
    }

    return await res.json();
}


export async function getAllTours() {
    const res = await fetch(`${API_BASE}/tours`, {headers: authHeader()})
    if (!res.ok) throw new Error('Failed to load tours')
    return res.json()
}

export async function filterTours(params) {
    const q = new URLSearchParams(params).toString()
    const res = await fetch(`${API_BASE}/tours/filter?${q}`, {headers: authHeader()})
    if (!res.ok) throw new Error("Filter failed")
    return res.json();
}

export async function addTour(tour) {
    const res = await fetch(`${API_BASE}/agent/tours`, {
        method: 'POST',
        headers: authHeader(),
        body: JSON.stringify(tour)
    })
    if (!res.ok) throw new Error('Add tour failed')
    return res.json()
}

export async function deleteTour(tourId) {
    const res = await fetch(`${API_BASE}/tours/${tourId}`, {
        method: 'DELETE',
        headers: authHeader(),
        credentials: 'include'
    });
    if (!res.ok) throw new Error('Delete tour failed');
    return await res.text();
}

export async function createBooking(userId, tourId) {
    const res = await fetch(`${API_BASE}/traveler/booking?userId=${userId}&tourId=${tourId}`, {
        method: 'POST',
        headers: authHeader()
    })
    if (!res.ok) throw new Error('Booking failed')
    return res.json()
}

export async function getBookings(userId) {
    const res =await fetch(`${API_BASE}/traveler/bookings?userId=${userId}`, {headers: authHeader()})
    if (!res.ok) throw new Error('Failed to load bookings')
    return res.json()
}

export async function cancelBooking(bookingId) {
    const res = await fetch(`${API_BASE}/traveler/booking/${bookingId}`, {
        method: "DELETE",
        headers: authHeader()
    });
    if (!res.ok) throw new Error("Не удалось отменить бронирование");
    return res.text();
}


export async function getUsers() {
    const res = await fetch(`${API_BASE}/admin/users`, { headers: authHeader() })
    if (!res.ok) throw new Error('Faield to get users')
    return res.json()
}

export async function deleteUser(id) {
    const res = await fetch(`${API_BASE}/admin/users/${id}`, {
        method: 'DELETE',
        headers: authHeader()
    })
    if (!res.ok) throw new Error('Delete user failed')
    return true
}

export async function recommend(preferences) {
    const res = await fetch(`${API_BASE}/ai/recommend?preferences=${encodeURIComponent(preferences)}`, {headers: authHeader()})
    if (!res.ok) throw new Error('AI failed')
    return res.text()
}

export async function predictPopularity(destination) {
    const res = await fetch(
        `${API_BASE}/ai/predict?destination=${encodeURIComponent(destination)}`,
        { headers: authHeader() }
    );
    if (!res.ok) throw new Error("AI popularity failed");
    return res.text();
}


export async function getReviews(tourId) {
    const res = await fetch(`${API_BASE}/reviews/tour/${tourId}`, {
        headers: authHeader()
    });
    if (!res.ok) throw new Error("Failed to load reviews");
    return res.json();
}

export async function addReview(tourId, comment, rating) {
    const params = new URLSearchParams({ comment, rating });
    const res = await fetch(`${API_BASE}/reviews/tour/${tourId}?${params}`, {
        method: "POST",
        headers: authHeader()
    });
    if (!res.ok) throw new Error("Failed to add review");
    return res.json();
}

export async function deleteReview(reviewId) {
    const res = await fetch(`${API_BASE}/reviews/${reviewId}`, {
        method: "DELETE",
        headers: authHeader()
    });
    if (!res.ok) throw new Error("Failed to delete review");
    return res.text();
}

export async function getAllBookingsForAgent() {
    const res = await fetch(`${API_BASE}/agent/bookings`, {
        headers: authHeader()
    });
    if (!res.ok) throw new Error("Failed to load bookings for agent");
    return res.json();
}

export async function updateBookingStatus(bookingId, status) {
    const res = await fetch(`${API_BASE}/agent/bookings/${bookingId}/status?status=${status}`, {
        method: "PUT",
        headers: authHeader()
    });
    if (!res.ok) throw new Error("Failed to update status");
    return res.json();
}

export async function getCaptcha() {
    const res = await fetch(`${API_BASE}/auth/captcha`, {
        credentials: "include"
    });
    if (!res.ok) throw new Error("Не удалось получить капчу");
    return res.text();
}


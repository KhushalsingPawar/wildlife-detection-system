import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from 'react'
import { api, setAuthToken } from '../api/client'

const TOKEN_KEY = 'apexguard_token'
const USER_KEY = 'apexguard_user'

const AuthContext = createContext(null)

function readStoredUser() {
  try {
    const raw = localStorage.getItem(USER_KEY)
    return raw ? JSON.parse(raw) : null
  } catch {
    return null
  }
}

function readStoredToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => readStoredToken())
  const [user, setUser] = useState(() => readStoredUser())

  useEffect(() => {
    setAuthToken(token)
  }, [token])

  const login = useCallback(async (email, password) => {
    const { data } = await api.post('/api/auth/login', { email, password })
    const t =
      data.token ??
      data.accessToken ??
      data.jwt ??
      (data.id != null ? `session:${data.id}` : null) ??
      (data.email ? `session:${data.email}` : null)
    if (!t) {
      throw new Error('Invalid login response: expected user id or email from server')
    }
    const u = {
      id: data.id,
      name: data.name,
      email: data.email ?? email,
      role: data.role,
      phone: data.phone,
    }
    localStorage.setItem(TOKEN_KEY, t)
    localStorage.setItem(USER_KEY, JSON.stringify(u))
    setAuthToken(t)
    setToken(t)
    setUser(u)
    return { token: t, user: u }
  }, [])

  const signup = useCallback(async (payload) => {
    await api.post('/api/auth/signup', {
      name: payload.name,
      email: payload.email,
      password: payload.password,
    })
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
    setAuthToken(null)
    setToken(null)
    setUser(null)
  }, [])

  const canUseManualControls = useMemo(() => {
    const r = user?.role
    return r === 'ADMIN' || r === 'FOREST_OFFICER'
  }, [user])

  const value = useMemo(
    () => ({
      token,
      user,
      isAuthenticated: Boolean(token),
      login,
      signup,
      logout,
      canUseManualControls,
    }),
    [token, user, login, signup, logout, canUseManualControls],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}

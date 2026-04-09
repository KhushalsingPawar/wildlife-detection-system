import { useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import { Hexagon, Loader2 } from 'lucide-react'
import { useAuth } from '../context/AuthContext'
import { getApiErrorMessage } from '../utils/httpError'

const MotionDiv = motion.div

const glass =
  'backdrop-blur-md bg-white/5 border border-white/10 rounded-2xl shadow-xl shadow-black/30'

export function Login() {
  const { login, isAuthenticated } = useAuth()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await login(email, password)
      navigate('/dashboard', { replace: true })
    } catch (err) {
      setError(getApiErrorMessage(err, 'Login failed. Check credentials.'))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="flex min-h-dvh flex-col items-center justify-center bg-[#0B0F1A] p-4">
      <MotionDiv
        initial={false}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.35 }}
        className={`w-full max-w-md p-8 ${glass}`}
      >
        <div className="mb-8 flex flex-col items-center gap-2 text-center">
          <Hexagon className="size-12 text-cyan-400/90" strokeWidth={1.25} />
          <h1 className="text-xl font-semibold tracking-tight text-slate-100">
            ApexGuard
          </h1>
          <p className="text-sm text-slate-500">Wildlife AI Detection System</p>
        </div>
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <div>
            <label
              htmlFor="email"
              className="mb-1 block text-xs font-medium uppercase tracking-wider text-slate-500"
            >
              Email
            </label>
            <input
              id="email"
              type="email"
              autoComplete="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="w-full rounded-lg border border-white/10 bg-black/30 px-3 py-2.5 text-slate-100 outline-none ring-cyan-500/30 placeholder:text-slate-600 focus:border-cyan-500/40 focus:ring-2"
              placeholder="you@forest.gov"
            />
          </div>
          <div>
            <label
              htmlFor="password"
              className="mb-1 block text-xs font-medium uppercase tracking-wider text-slate-500"
            >
              Password
            </label>
            <input
              id="password"
              type="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="w-full rounded-lg border border-white/10 bg-black/30 px-3 py-2.5 text-slate-100 outline-none ring-cyan-500/30 placeholder:text-slate-600 focus:border-cyan-500/40 focus:ring-2"
            />
          </div>
          {error && (
            <p className="rounded-lg border border-red-500/30 bg-red-500/10 px-3 py-2 text-sm text-red-300">
              {error}
            </p>
          )}
          <button
            type="submit"
            disabled={loading}
            className="flex items-center justify-center gap-2 rounded-lg bg-cyan-600 py-3 text-sm font-semibold text-white transition hover:bg-cyan-500 disabled:opacity-60"
          >
            {loading ? (
              <Loader2 className="size-4 animate-spin" />
            ) : (
              'Sign in'
            )}
          </button>
        </form>
        <p className="mt-6 text-center text-sm text-slate-500">
          No account?{' '}
          <Link
            to="/signup"
            className="font-medium text-cyan-400 hover:text-cyan-300"
          >
            Create one
          </Link>
        </p>
      </MotionDiv>
    </div>
  )
}

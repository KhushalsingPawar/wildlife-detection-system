import { useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import { Hexagon, Loader2 } from 'lucide-react'
import { useAuth } from '../context/AuthContext'
import { getApiErrorMessage } from '../utils/httpError'

const MotionDiv = motion.div

const glass =
  'backdrop-blur-md bg-white/5 border border-white/10 rounded-2xl shadow-xl shadow-black/30'

const ROLES = ['USER', 'FOREST_OFFICER', 'ADMIN']

export function Signup() {
  const { signup, isAuthenticated } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({
    name: '',
    phone: '',
    email: '',
    password: '',
    role: 'USER',
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />
  }

  function update(field, value) {
    setForm((f) => ({ ...f, [field]: value }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await signup({
        name: form.name,
        phone: form.phone,
        email: form.email,
        password: form.password,
        role: form.role,
      })
      navigate('/login', { replace: true })
    } catch (err) {
      setError(getApiErrorMessage(err, 'Signup failed. Try again.'))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="flex min-h-dvh flex-col items-center justify-center bg-[#0B0F1A] p-4 py-10">
      <MotionDiv
        initial={false}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.35 }}
        className={`w-full max-w-md p-8 ${glass}`}
      >
        <div className="mb-8 flex flex-col items-center gap-2 text-center">
          <Hexagon className="size-12 text-cyan-400/90" strokeWidth={1.25} />
          <h1 className="text-xl font-semibold tracking-tight text-slate-100">
            Create account
          </h1>
          <p className="text-sm text-slate-500">ApexGuard operator access</p>
          <p className="text-xs text-slate-600">
            Signup sends name, email, and password to match your Spring{' '}
            <code className="text-slate-500">User</code> entity. Add phone/role
            on the server if you want them saved.
          </p>
        </div>
        <form onSubmit={handleSubmit} className="flex flex-col gap-3">
          <Field
            label="Name"
            value={form.name}
            onChange={(v) => update('name', v)}
            required
          />
          <Field
            label="Phone"
            type="tel"
            value={form.phone}
            onChange={(v) => update('phone', v)}
            required
          />
          <Field
            label="Email"
            type="email"
            autoComplete="email"
            value={form.email}
            onChange={(v) => update('email', v)}
            required
          />
          <Field
            label="Password"
            type="password"
            autoComplete="new-password"
            value={form.password}
            onChange={(v) => update('password', v)}
            required
          />
          <div>
            <label className="mb-1 block text-xs font-medium uppercase tracking-wider text-slate-500">
              Role
            </label>
            <select
              value={form.role}
              onChange={(e) => update('role', e.target.value)}
              className="w-full rounded-lg border border-white/10 bg-black/30 px-3 py-2.5 text-slate-100 outline-none focus:border-cyan-500/40 focus:ring-2 focus:ring-cyan-500/30"
            >
              {ROLES.map((r) => (
                <option key={r} value={r}>
                  {r.replace('_', ' ')}
                </option>
              ))}
            </select>
          </div>
          {error && (
            <p className="rounded-lg border border-red-500/30 bg-red-500/10 px-3 py-2 text-sm text-red-300">
              {error}
            </p>
          )}
          <button
            type="submit"
            disabled={loading}
            className="mt-2 flex items-center justify-center gap-2 rounded-lg bg-cyan-600 py-3 text-sm font-semibold text-white transition hover:bg-cyan-500 disabled:opacity-60"
          >
            {loading ? (
              <Loader2 className="size-4 animate-spin" />
            ) : (
              'Sign up'
            )}
          </button>
        </form>
        <p className="mt-6 text-center text-sm text-slate-500">
          Already registered?{' '}
          <Link
            to="/login"
            className="font-medium text-cyan-400 hover:text-cyan-300"
          >
            Sign in
          </Link>
        </p>
      </MotionDiv>
    </div>
  )
}

function Field({ label, type = 'text', value, onChange, required, autoComplete }) {
  const id = label.toLowerCase().replace(/\s/g, '-')
  return (
    <div>
      <label
        htmlFor={id}
        className="mb-1 block text-xs font-medium uppercase tracking-wider text-slate-500"
      >
        {label}
      </label>
      <input
        id={id}
        type={type}
        autoComplete={autoComplete}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        required={required}
        className="w-full rounded-lg border border-white/10 bg-black/30 px-3 py-2.5 text-slate-100 outline-none ring-cyan-500/30 placeholder:text-slate-600 focus:border-cyan-500/40 focus:ring-2"
      />
    </div>
  )
}

import { useCallback, useEffect, useState } from 'react'
import { motion, AnimatePresence } from 'framer-motion'
import { Plus, Trash2, Loader2 } from 'lucide-react'
import { api } from '../api/client'

const MotionLi = motion.li
const MotionToast = motion.div

const glass =
  'backdrop-blur-md bg-white/5 border border-white/10 rounded-xl shadow-lg shadow-black/20'

function parseList(data) {
  if (Array.isArray(data)) {
    return data.map((x) =>
      typeof x === 'string' ? x : x.name ?? x.animal ?? String(x),
    )
  }
  return []
}

export function Authorised() {
  const [animals, setAnimals] = useState([])
  const [input, setInput] = useState('')
  const [loading, setLoading] = useState(true)
  const [busy, setBusy] = useState(false)
  const [toast, setToast] = useState(null)

  const showToast = (message, variant = 'info') => {
    setToast({ message, variant })
    window.setTimeout(() => setToast(null), 3200)
  }

  const refresh = useCallback(async () => {
    setLoading(true)
    try {
      const { data } = await api.get('/api/authorised/all')
      setAnimals(parseList(data))
    } catch {
      setAnimals([])
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    refresh()
  }, [refresh])

  async function addAnimal(e) {
    e.preventDefault()
    const name = input.trim()
    if (!name) return
    setBusy(true)
    try {
      await api.post(
        `/api/authorised/add?animal=${encodeURIComponent(name)}`,
      )
      setInput('')
      await refresh()
    } catch (err) {
      showToast(err.response?.data?.message || 'Add failed', 'error')
    } finally {
      setBusy(false)
    }
  }

  async function removeAnimal(name) {
    const ok = window.confirm(`Remove “${name}” from the whitelist?`)
    if (!ok) return
    setBusy(true)
    try {
      await api.post(
        `/api/authorised/remove?animal=${encodeURIComponent(name)}`,
      )
      showToast(`Removed ${name}`, 'ok')
      await refresh()
    } catch (err) {
      showToast(err.response?.data?.message || 'Remove failed', 'error')
    } finally {
      setBusy(false)
    }
  }

  return (
    <div className="mx-auto max-w-3xl space-y-6">
      <header>
        <h1 className="text-2xl font-semibold tracking-tight text-slate-100">
          Authorised animals
        </h1>
        <p className="text-sm text-slate-500">
          Whitelist names that should not trigger threat alerts
        </p>
      </header>

      <form
        onSubmit={addAnimal}
        className={`flex flex-col gap-3 p-4 sm:flex-row sm:items-end ${glass}`}
      >
        <div className="min-w-0 flex-1">
          <label
            htmlFor="animal"
            className="mb-1 block text-xs font-medium uppercase tracking-wider text-slate-500"
          >
            Animal name
          </label>
          <input
            id="animal"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="e.g. domestic dog (collared)"
            className="w-full rounded-lg border border-white/10 bg-black/30 px-4 py-3 text-slate-100 outline-none placeholder:text-slate-600 focus:border-cyan-500/40 focus:ring-2 focus:ring-cyan-500/30"
          />
        </div>
        <button
          type="submit"
          disabled={busy || !input.trim()}
          className="flex items-center justify-center gap-2 rounded-lg bg-cyan-600 px-6 py-3 text-sm font-semibold text-white transition hover:bg-cyan-500 disabled:opacity-50"
        >
          {busy ? (
            <Loader2 className="size-4 animate-spin" />
          ) : (
            <Plus className="size-4" />
          )}
          Add animal
        </button>
      </form>

      <div className={`p-4 ${glass}`}>
        <h2 className="mb-4 text-sm font-semibold uppercase tracking-widest text-slate-400">
          Current whitelist
        </h2>
        {loading ? (
          <p className="text-sm text-slate-500">Loading…</p>
        ) : animals.length === 0 ? (
          <p className="text-sm text-slate-500">No animals whitelisted yet.</p>
        ) : (
          <ul className="grid gap-2 sm:grid-cols-2">
            <AnimatePresence mode="popLayout">
              {animals.map((name) => (
                <MotionLi
                  key={name}
                  layout
                  initial={false}
                  animate={{ opacity: 1, scale: 1 }}
                  exit={{ opacity: 0, scale: 0.96 }}
                  className="flex items-center justify-between gap-2 rounded-lg border border-white/10 bg-black/20 px-3 py-2.5"
                >
                  <span className="truncate font-medium text-slate-200">
                    {name}
                  </span>
                  <button
                    type="button"
                    disabled={busy}
                    onClick={() => removeAnimal(name)}
                    className="shrink-0 rounded-md border border-red-500/30 bg-red-500/10 p-2 text-red-300 transition hover:bg-red-500/20 disabled:opacity-50"
                    aria-label={`Remove ${name}`}
                  >
                    <Trash2 className="size-4" />
                  </button>
                </MotionLi>
              ))}
            </AnimatePresence>
          </ul>
        )}
      </div>

      <AnimatePresence>
        {toast && (
          <MotionToast
            initial={false}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: 8 }}
            className={`fixed bottom-24 left-1/2 z-[100] max-w-md -translate-x-1/2 rounded-xl border px-4 py-3 text-sm shadow-xl md:bottom-8 ${
              toast.variant === 'error'
                ? 'border-red-500/40 bg-red-950/90 text-red-100'
                : 'border-emerald-500/40 bg-emerald-950/90 text-emerald-100'
            }`}
          >
            {toast.message}
          </MotionToast>
        )}
      </AnimatePresence>
    </div>
  )
}

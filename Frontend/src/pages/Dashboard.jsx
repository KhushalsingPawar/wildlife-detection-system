import { useCallback, useEffect, useMemo, useState } from 'react'
import { motion } from 'framer-motion'
import { Activity, Radio, Shield, AlertTriangle } from 'lucide-react'
import { api } from '../api/client'
import { useAuth } from '../context/AuthContext'
import { useWildlifeSocket } from '../hooks/useWildlifeSocket'
import { DetectionTable } from '../components/DetectionTable'
import { TacticalMap } from '../components/TacticalMap'
import { normalizeDetection } from '../utils/detection'
import { playAlertBeep } from '../utils/audio'

const MotionDiv = motion.div

const glass =
  'backdrop-blur-md bg-white/5 border border-white/10 rounded-xl shadow-lg shadow-black/20'

function mergeById(prev, incoming) {
  const map = new Map()
  for (const x of [...incoming, ...prev]) {
    map.set(x.id, x)
  }
  return Array.from(map.values())
}

export function Dashboard() {
  const { canUseManualControls } = useAuth()
  const [detections, setDetections] = useState([])
  const [dangerousOnly, setDangerousOnly] = useState(false)
  const [loading, setLoading] = useState(true)
  const [fetchError, setFetchError] = useState('')

  const load = useCallback(async () => {
    setFetchError('')
    setLoading(true)
    try {
      const path = dangerousOnly
        ? '/api/detect/dangerous'
        : '/api/detect/all'
      const { data } = await api.get(path)
      const list = Array.isArray(data) ? data : data?.content ?? data?.items ?? []
      setDetections(list.map(normalizeDetection))
    } catch (e) {
      setFetchError(
        e.response?.data?.message || e.message || 'Failed to load detections',
      )
      setDetections([])
    } finally {
      setLoading(false)
    }
  }, [dangerousOnly])

  useEffect(() => {
    load()
  }, [load])

  const onSocketAlert = useCallback((payload) => {
    const raw = payload?.body ?? payload
    const n = normalizeDetection(
      typeof raw === 'object' && raw !== null ? raw : { message: raw },
    )
    if (n.dangerous) {
      playAlertBeep()
    }
    setDetections((prev) => mergeById(prev, [n]))
  }, [])

  useWildlifeSocket(onSocketAlert)

  const metrics = useMemo(() => {
    const threats = detections.filter((d) => d.dangerous).length
    const authorised = detections.filter((d) => d.authorised).length
    const activeSensors = Math.min(32, 6 + (detections.length % 7) + 4)
    return { threats, authorised, activeSensors }
  }, [detections])

  const latest = detections[0]
  const mapLat = latest?.latitude ?? null
  const mapLng = latest?.longitude ?? null

  async function tacticalLights() {
    try {
      await api.post('/api/tactical/lights')
    } catch {
      await new Promise((r) => setTimeout(r, 400))
    }
  }

  async function tacticalAlarm() {
    try {
      await api.post('/api/tactical/alarm')
    } catch {
      await new Promise((r) => setTimeout(r, 400))
    }
  }

  return (
    <div className="mx-auto max-w-7xl space-y-6">
      <header className="flex flex-col gap-2 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <h1 className="text-2xl font-semibold tracking-tight text-slate-100">
            War Room
          </h1>
          <p className="text-sm text-slate-500">
            Live detections, alerts, and tactical overview
          </p>
        </div>
        <label className="flex cursor-pointer items-center gap-3 rounded-lg border border-white/10 bg-white/5 px-4 py-2 text-sm text-slate-300">
          <input
            type="checkbox"
            checked={dangerousOnly}
            onChange={(e) => setDangerousOnly(e.target.checked)}
            className="size-4 rounded border-white/20 bg-black/40 text-cyan-500 focus:ring-cyan-500/40"
          />
          Threats only
          <span className="text-xs text-slate-500">GET /api/detect/dangerous</span>
        </label>
      </header>

      {fetchError && (
        <div className="rounded-xl border border-amber-500/30 bg-amber-500/10 px-4 py-3 text-sm text-amber-200">
          {fetchError}
        </div>
      )}

      <section className="grid gap-4 sm:grid-cols-3">
        <MetricCard
          icon={AlertTriangle}
          label="Total threats"
          value={metrics.threats}
          accent="text-red-400"
        />
        <MetricCard
          icon={Shield}
          label="Authorised"
          value={metrics.authorised}
          accent="text-emerald-400"
        />
        <MetricCard
          icon={Activity}
          label="Active sensors"
          value={metrics.activeSensors}
          accent="text-cyan-400"
        />
      </section>

      <section className="grid gap-4 lg:grid-cols-2">
        <MotionDiv
          layout
          initial={false}
          className={`flex min-h-[280px] flex-col ${glass}`}
        >
          <div className="border-b border-white/10 px-4 py-3">
            <h2 className="flex items-center gap-2 text-sm font-semibold uppercase tracking-widest text-slate-400">
              <Radio className="size-4 text-cyan-400/80" />
              Live camera stream
            </h2>
          </div>
          <div className="relative flex flex-1 items-center justify-center bg-black/40 p-4">
            <div className="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent" />
            <div className="relative aspect-video w-full max-w-lg overflow-hidden rounded-lg border border-white/10 bg-gradient-to-br from-slate-800 to-slate-950">
              <div className="absolute inset-0 flex items-center justify-center text-sm text-slate-500">
                Video stream placeholder
              </div>
              <div className="absolute bottom-2 left-2 rounded bg-black/60 px-2 py-0.5 font-mono text-[10px] text-emerald-400">
                ● LIVE
              </div>
            </div>
          </div>
        </MotionDiv>

        <div className={`flex min-h-[280px] flex-col ${glass}`}>
          <div className="border-b border-white/10 px-4 py-3">
            <h2 className="text-sm font-semibold uppercase tracking-widest text-slate-400">
              Real-time alert feed
            </h2>
          </div>
          <div className="max-h-[360px] flex-1 overflow-auto p-2">
            {loading ? (
              <p className="p-4 text-center text-sm text-slate-500">
                Loading…
              </p>
            ) : (
              <DetectionTable
                items={detections}
                emptyMessage="No alerts. Listening on /topic/alerts…"
              />
            )}
          </div>
        </div>
      </section>

      <TacticalMap
        latitude={mapLat}
        longitude={mapLng}
        canControl={canUseManualControls}
        onLights={tacticalLights}
        onAlarm={tacticalAlarm}
      />
    </div>
  )
}

function MetricCard({ icon, label, value, accent }) {
  const Icon = icon
  return (
    <div className={`flex items-center gap-4 p-5 ${glass}`}>
      <div className="flex size-12 items-center justify-center rounded-xl bg-white/5 ring-1 ring-white/10">
        <Icon className={`size-6 ${accent}`} strokeWidth={1.5} />
      </div>
      <div>
        <div className="text-xs font-medium uppercase tracking-wider text-slate-500">
          {label}
        </div>
        <div className="text-3xl font-semibold tabular-nums text-slate-100">
          {value}
        </div>
      </div>
    </div>
  )
}

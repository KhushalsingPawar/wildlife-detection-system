import { Radio } from 'lucide-react'
import { useAuth } from '../context/AuthContext'
import { TacticalMap } from '../components/TacticalMap'

const glass =
  'backdrop-blur-md bg-white/5 border border-white/10 rounded-xl shadow-lg shadow-black/20'

const MOCK_DETECTION = { lat: 12.991, lng: 77.594 }

export function LiveFeed() {
  const { canUseManualControls } = useAuth()

  async function tacticalLights() {
    try {
      await fetch('/api/tactical/lights', { method: 'POST' })
    } catch {
      await new Promise((r) => setTimeout(r, 400))
    }
  }

  async function tacticalAlarm() {
    try {
      await fetch('/api/tactical/alarm', { method: 'POST' })
    } catch {
      await new Promise((r) => setTimeout(r, 400))
    }
  }

  return (
    <div className="mx-auto max-w-7xl space-y-6">
      <header>
        <h1 className="text-2xl font-semibold tracking-tight text-slate-100">
          Live feed
        </h1>
        <p className="text-sm text-slate-500">
          Full-screen stream view with tactical overlay
        </p>
      </header>

      <div className={`overflow-hidden ${glass}`}>
        <div className="flex items-center gap-2 border-b border-white/10 px-4 py-3">
          <Radio className="size-4 text-red-400" />
          <span className="text-sm font-medium uppercase tracking-wider text-slate-400">
            Camera
          </span>
          <span className="ml-auto font-mono text-[10px] text-emerald-400">
            ● LIVE
          </span>
        </div>

        <div className="relative aspect-video w-full bg-gradient-to-br from-slate-900 via-[#0d1321] to-black">
          <img
            src="http://localhost:5000/video_feed"
            alt="Live Camera Feed"
            className="w-full h-full object-cover"
          />

          <div
            className="absolute inset-0 opacity-30"
            style={{
              backgroundImage: `
                linear-gradient(rgba(255,255,255,0.03) 1px, transparent 1px),
                linear-gradient(90deg, rgba(255,255,255,0.03) 1px, transparent 1px)
              `,
              backgroundSize: '24px 24px',
            }}
          />
        </div>
      </div>

      <TacticalMap
        latitude={MOCK_DETECTION.lat}
        longitude={MOCK_DETECTION.lng}
        canControl={canUseManualControls}
        onLights={tacticalLights}
        onAlarm={tacticalAlarm}
      />
    </div>
  )
}
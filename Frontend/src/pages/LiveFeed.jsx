import { useEffect, useState } from 'react'
import { Radio } from 'lucide-react'
import { useAuth } from '../context/AuthContext'
import { TacticalMap } from '../components/TacticalMap'
import { api } from '../api/client'

const glass =
  'backdrop-blur-md bg-white/5 border border-white/10 rounded-xl shadow-lg shadow-black/20'

/** Placeholder detection point for demo map */
const MOCK_DETECTION = { lat: 12.991, lng: 77.594 }

export function LiveFeed() {
  const { canUseManualControls } = useAuth()
  const [videoUrl, setVideoUrl] = useState('') // URL of latest S3 video

  // Fetch latest video URL from backend API
  useEffect(() => {
    async function fetchLatestVideo() {
      try {
        const res = await api.get('/api/detect/latest-video') // backend endpoint
        if (res.data && res.data.videoUrl) {
          setVideoUrl(res.data.videoUrl)
        }
      } catch (err) {
        console.error('Error fetching latest video:', err)
      }
    }

    fetchLatestVideo()
    const interval = setInterval(fetchLatestVideo, 5000) // refresh every 5 seconds
    return () => clearInterval(interval)
  }, [])

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
          {videoUrl ? (
            <video
              src={videoUrl}
              controls
              autoPlay
              muted
              loop
              className="w-full h-full object-cover"
            />
          ) : (
            <div className="absolute inset-0 flex items-center justify-center text-slate-500">
              Loading live feed...
            </div>
          )}

          {/* Optional grid overlay */}
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
import { useMemo } from 'react'
import { MapPin, Navigation } from 'lucide-react'
import { haversineKm, mockResponseMinutes } from '../utils/detection'

/** Mock Forest Department base (replace with real coords from backend if available) */
const FOREST_DEPT = { lat: 12.9716, lng: 77.5946, label: 'Forest Dept' }

const glass =
  'backdrop-blur-md bg-white/5 border border-white/10 rounded-xl overflow-hidden'

export function TacticalMap({ latitude, longitude, canControl, onLights, onAlarm }) {
  const hasPoint =
    typeof latitude === 'number' &&
    typeof longitude === 'number' &&
    Number.isFinite(latitude) &&
    Number.isFinite(longitude)

  const distanceKm = useMemo(() => {
    if (!hasPoint) return null
    return haversineKm(FOREST_DEPT.lat, FOREST_DEPT.lng, latitude, longitude)
  }, [hasPoint, latitude, longitude])

  const etaMin = mockResponseMinutes(distanceKm)

  return (
    <div className={`${glass} p-4`}>
      <div className="mb-3 flex flex-wrap items-center justify-between gap-2">
        <h2 className="text-sm font-semibold uppercase tracking-widest text-slate-400">
          Tactical Map
        </h2>
        {etaMin != null && (
          <div className="flex items-center gap-2 rounded-lg border border-cyan-500/20 bg-cyan-500/10 px-3 py-1 text-xs text-cyan-200">
            <Navigation className="size-3.5 shrink-0" />
            Est. response: ~{etaMin} min
            {distanceKm != null && (
              <span className="text-slate-500">
                ({distanceKm.toFixed(1)} km)
              </span>
            )}
          </div>
        )}
      </div>

      <div className="relative aspect-[16/9] w-full overflow-hidden rounded-lg bg-gradient-to-br from-slate-900 via-[#0d1321] to-black ring-1 ring-white/10">
        <div
          className="absolute inset-0 opacity-40"
          style={{
            backgroundImage: `
              linear-gradient(rgba(255,255,255,0.04) 1px, transparent 1px),
              linear-gradient(90deg, rgba(255,255,255,0.04) 1px, transparent 1px)
            `,
            backgroundSize: '32px 32px',
          }}
        />
        <div className="absolute left-[12%] top-[55%] flex flex-col items-center">
          <MapPin className="size-6 text-amber-400/90 drop-shadow-lg" />
          <span className="mt-1 max-w-[100px] truncate text-[10px] text-amber-200/80">
            {FOREST_DEPT.label}
          </span>
        </div>
        {hasPoint && (
          <div
            className="absolute flex flex-col items-center"
            style={{
              left: `${18 + ((longitude + 180) % 360) * 0.08}%`,
              top: `${22 + ((90 - latitude) / 180) * 55}%`,
            }}
          >
            <span className="map-pulse absolute inline-flex size-6 rounded-full bg-red-500/50" />
            <span className="relative z-10 inline-flex size-3 rounded-full bg-red-500 shadow-[0_0_20px_rgba(239,68,68,0.9)] ring-2 ring-red-300/50" />
            <span className="mt-1 text-[10px] text-red-300">Detection</span>
          </div>
        )}
        {!hasPoint && (
          <div className="absolute inset-0 flex items-center justify-center text-sm text-slate-500">
            Awaiting GPS from detection…
          </div>
        )}
      </div>

      {canControl && (
        <div className="mt-4 flex flex-wrap gap-2">
          <button
            type="button"
            onClick={onLights}
            className="rounded-lg border border-amber-500/30 bg-amber-500/10 px-4 py-2 text-sm font-medium text-amber-200 transition hover:bg-amber-500/20"
          >
            Activate Warning Lights
          </button>
          <button
            type="button"
            onClick={onAlarm}
            className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-2 text-sm font-medium text-red-200 transition hover:bg-red-500/20"
          >
            Play Sound Alarm
          </button>
        </div>
      )}
      {!canControl && (
        <p className="mt-3 text-xs text-slate-500">
          View only — manual tactical controls are restricted to Admin and Forest
          Officer roles.
        </p>
      )}
    </div>
  )
}

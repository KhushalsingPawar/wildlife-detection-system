import { motion } from 'framer-motion'
import { format } from '../utils/formatTime'

const MotionTr = motion.tr

const glass =
  'backdrop-blur-md bg-white/5 border border-white/10 rounded-xl overflow-hidden'

export function DetectionTable({ items, emptyMessage = 'No detections yet.' }) {
  if (!items?.length) {
    return (
      <div className={`p-8 text-center text-sm text-slate-500 ${glass}`}>
        {emptyMessage}
      </div>
    )
  }

  return (
    <div className={`overflow-x-auto ${glass}`}>
      <table className="w-full min-w-[640px] border-collapse text-left text-sm">
        <thead>
          <tr className="border-b border-white/10 bg-black/20 text-xs uppercase tracking-wider text-slate-500">
            <th className="px-4 py-3 font-medium">Animal</th>
            <th className="px-4 py-3 font-medium">Time</th>
            <th className="px-4 py-3 font-medium">Confidence</th>
            <th className="px-4 py-3 font-medium">Location</th>
            <th className="px-4 py-3 font-medium">SMS</th>
          </tr>
        </thead>
        <tbody>
          {items.map((d, index) => {
            const isDanger = d.dangerous
            const isAuth = d.authorised && !isDanger
            return (
              <MotionTr
                key={d.id}
                layout
                initial={false}
                animate={{
                  opacity: 1,
                  y: 0,
                  boxShadow: isDanger
                    ? '0 0 24px rgba(239, 68, 68, 0.25)'
                    : isAuth
                      ? '0 0 16px rgba(34, 197, 94, 0.2)'
                      : 'none',
                }}
                transition={{
                  delay: index === 0 ? 0 : 0,
                  duration: 0.35,
                  ease: [0.22, 1, 0.36, 1],
                }}
                className={`border-b border-white/5 ${
                  isDanger
                    ? 'bg-red-950/40'
                    : isAuth
                      ? 'border-l-4 border-l-emerald-500/80 bg-emerald-950/20'
                      : 'hover:bg-white/[0.03]'
                }`}
              >
                <td className="px-4 py-3 font-medium text-slate-100">
                  {d.animalName}
                  {isAuth && (
                    <span className="ml-2 rounded border border-emerald-500/40 bg-emerald-500/10 px-1.5 py-0.5 text-[10px] font-semibold uppercase text-emerald-400">
                      Whitelisted
                    </span>
                  )}
                </td>
                <td className="px-4 py-3 text-slate-400 tabular-nums">
                  {format(d.time)}
                </td>
                <td className="px-4 py-3 text-slate-300 tabular-nums">
                  {(d.confidence * (d.confidence <= 1 ? 100 : 1)).toFixed(1)}%
                </td>
                <td className="max-w-[200px] truncate px-4 py-3 font-mono text-xs text-slate-400">
                  {d.location}
                </td>
                <td className="px-4 py-3">
                  {isDanger ? (
                    <span className="rounded bg-red-500/20 px-2 py-0.5 text-xs font-semibold text-red-300">
                      {d.smsStatus || 'SMS SENT'}
                    </span>
                  ) : (
                    <span className="text-slate-500">—</span>
                  )}
                </td>
              </MotionTr>
            )
          })}
        </tbody>
      </table>
    </div>
  )
}

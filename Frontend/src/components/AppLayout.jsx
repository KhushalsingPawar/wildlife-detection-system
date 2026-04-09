import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { AnimatePresence } from 'framer-motion'
import { useLocation } from 'react-router-dom'
import {
  LayoutDashboard,
  ShieldCheck,
  Radio,
  LogOut,
  Hexagon,
} from 'lucide-react'
import { useAuth } from '../context/AuthContext'
import { PageTransition } from './PageTransition'

const nav = [
  { to: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
  { to: '/authorised', label: 'Whitelist', icon: ShieldCheck },
  { to: '/live-feed', label: 'Live Feed', icon: Radio },
]

const glass =
  'backdrop-blur-md bg-white/5 border border-white/10 shadow-lg shadow-black/20'

export function AppLayout() {
  const { logout, user } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  function handleLogout() {
    logout()
    navigate('/login', { replace: true })
  }

  return (
    <div className="flex min-h-dvh flex-col bg-[#0B0F1A] md:flex-row">
      <aside
        className={`hidden w-56 shrink-0 flex-col border-r border-white/10 p-4 md:flex ${glass}`}
      >
        <div className="mb-8 flex items-center gap-2 px-2">
          <Hexagon className="size-8 text-cyan-400/90" strokeWidth={1.5} />
          <div className="text-left">
            <div className="text-xs font-semibold uppercase tracking-widest text-slate-500">
              ApexGuard
            </div>
            <div className="text-sm text-slate-200">Wildlife AI</div>
          </div>
        </div>
        <nav className="flex flex-1 flex-col gap-1">
          {nav.map((item) => {
            const NavIcon = item.icon
            return (
              <NavLink
                key={item.to}
                to={item.to}
                className={({ isActive }) =>
                  `flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm transition-colors ${
                    isActive
                      ? 'bg-cyan-500/15 text-cyan-300'
                      : 'text-slate-400 hover:bg-white/5 hover:text-slate-200'
                  }`
                }
              >
                <NavIcon className="size-4 shrink-0 opacity-80" />
                {item.label}
              </NavLink>
            )
          })}
        </nav>
        <div className={`mt-auto rounded-lg p-3 text-xs ${glass}`}>
          <div className="truncate font-medium text-slate-200">
            {user?.name || user?.email || 'Operator'}
          </div>
          <div className="truncate text-slate-500">{user?.role || '—'}</div>
          <button
            type="button"
            onClick={handleLogout}
            className="mt-3 flex w-full items-center justify-center gap-2 rounded-md border border-white/10 bg-white/5 py-2 text-slate-300 transition hover:bg-white/10"
          >
            <LogOut className="size-3.5" />
            Logout
          </button>
        </div>
      </aside>

      <main className="min-h-0 flex-1 overflow-auto pb-20 md:pb-6">
        <AnimatePresence mode="wait" initial={false}>
          <PageTransition key={location.pathname} className="p-4 md:p-6">
            <Outlet />
          </PageTransition>
        </AnimatePresence>
      </main>

      <nav
        className={`fixed bottom-0 left-0 right-0 z-50 flex border-t border-white/10 md:hidden ${glass}`}
      >
        {nav.map((item) => {
          const NavIcon = item.icon
          return (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) =>
                `flex flex-1 flex-col items-center gap-1 py-3 text-[10px] uppercase tracking-wide ${
                  isActive ? 'text-cyan-400' : 'text-slate-500'
                }`
              }
            >
              <NavIcon className="size-5" />
              {item.label.split(' ')[0]}
            </NavLink>
          )
        })}
        <button
          type="button"
          onClick={handleLogout}
          className="flex flex-1 flex-col items-center gap-1 py-3 text-[10px] uppercase tracking-wide text-slate-500"
        >
          <LogOut className="size-5" />
          Out
        </button>
      </nav>
    </div>
  )
}

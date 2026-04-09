/**
 * Turns Axios/fetch errors into a short message for the UI.
 * 502/503/504 from Vite’s proxy usually mean the backend was unreachable.
 */
export function getApiErrorMessage(err, fallback = 'Something went wrong.') {
  if (!err) return fallback

  if (typeof err === 'string') return err

  const data = err.response?.data
  if (data != null) {
    if (typeof data === 'string') return data
    if (data.message) {
      return Array.isArray(data.message) ? data.message.join(' ') : String(data.message)
    }
    if (data.detail) {
      return String(data.detail)
    }
    if (data.errors && Array.isArray(data.errors)) {
      const parts = data.errors.map((e) =>
        typeof e === 'string' ? e : e.defaultMessage || e.message || JSON.stringify(e),
      )
      return parts.join(' ')
    }
    if (data.error) {
      const base =
        typeof data.error === 'string'
          ? data.error
          : String(data.error?.message ?? data.error)
      return data.path ? `${base} (${data.path})` : base
    }
  }

  const status = err.response?.status
  if (status === 502 || status === 503 || status === 504) {
    return 'Cannot reach the API server (bad gateway). Start your Spring Boot app on the port this frontend expects (default 8080 with Vite proxy), or set VITE_API_BASE_URL in a .env file to your real API base URL.'
  }
  if (status === 404) {
    return 'API endpoint not found — check that the backend exposes POST /api/auth/signup (or update the path in the app).'
  }

  if (err.code === 'ERR_NETWORK' || err.message === 'Network Error') {
    return 'Network error — is the API running? If you use VITE_API_BASE_URL, ensure CORS allows this origin.'
  }

  return err.message || fallback
}

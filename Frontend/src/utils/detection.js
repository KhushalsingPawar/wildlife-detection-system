function formatLocation(raw) {
  const lat = raw.latitude ?? raw.lat
  const lng = raw.longitude ?? raw.lng ?? raw.lon
  if (lat != null && lng != null) {
    return `${Number(lat).toFixed(4)}, ${Number(lng).toFixed(4)}`
  }
  return raw.location ?? raw.address ?? '—'
}

export function normalizeDetection(raw) {
  const dangerous = Boolean(raw.dangerous ?? raw.isDangerous)
  const authorised = Boolean(raw.authorised ?? raw.whitelisted ?? raw.whitelist)
  return {
    id:
      raw.id ??
      raw.detectionId ??
      `${raw.animalName ?? 'x'}-${raw.timestamp ?? Date.now()}-${Math.random()}`,
    animalName: raw.animalName ?? raw.animal_name ?? raw.name ?? 'Unknown',
    time: raw.time ?? raw.timestamp ?? raw.detectedAt ?? new Date().toISOString(),
    confidence:
      typeof raw.confidence === 'number'
        ? raw.confidence
        : Number(raw.confidence ?? raw.confidenceScore ?? 0),
    location: formatLocation(raw),
    latitude: raw.latitude ?? raw.lat ?? null,
    longitude: raw.longitude ?? raw.lng ?? raw.lon ?? null,
    smsStatus:
      raw.smsStatus ??
      raw.sms_status ??
      (dangerous ? 'SMS SENT' : '—'),
    dangerous,
    authorised,
  }
}

export function haversineKm(lat1, lon1, lat2, lon2) {
  const R = 6371
  const dLat = ((lat2 - lat1) * Math.PI) / 180
  const dLon = ((lon2 - lon1) * Math.PI) / 180
  const a =
    Math.sin(dLat / 2) ** 2 +
    Math.cos((lat1 * Math.PI) / 180) *
      Math.cos((lat2 * Math.PI) / 180) *
      Math.sin(dLon / 2) ** 2
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
  return R * c
}

/** Mock ETA: ~2.5 min per km of travel, minimum 1 min */
export function mockResponseMinutes(distanceKm) {
  if (!Number.isFinite(distanceKm)) return null
  return Math.max(1, Math.round(distanceKm * 2.5))
}

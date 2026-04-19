let audioCtx

export function playAlertBeep() {
    try {
        const Ctx = window.AudioContext || window.webkitAudioContext
        if (!Ctx) return
        if (!audioCtx) audioCtx = new Ctx()
        const osc = audioCtx.createOscillator()
        const gain = audioCtx.createGain()
        osc.connect(gain)
        gain.connect(audioCtx.destination)
        osc.frequency.value = 880
        osc.type = 'square'
        gain.gain.setValueAtTime(0.12, audioCtx.currentTime)
        gain.gain.exponentialRampToValueAtTime(0.01, audioCtx.currentTime + 0.15)
        osc.start(audioCtx.currentTime)
        osc.stop(audioCtx.currentTime + 0.15)
    } catch {}
}
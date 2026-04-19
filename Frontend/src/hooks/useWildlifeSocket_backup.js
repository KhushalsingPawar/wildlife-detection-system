import { useEffect, useRef } from 'react'
import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'
import { useAuth } from '../context/AuthContext'

/**
 * Wildlife WebSocket Hook
 * Connects to Spring Boot STOMP via SockJS
 * Subscribes to /topic/alerts
 */
export function useWildlifeSocket(onAlert) {
    const { token } = useAuth()
    const onAlertRef = useRef(onAlert)

    // Keep latest callback reference
    useEffect(() => {
        onAlertRef.current = onAlert
    }, [onAlert])

    useEffect(() => {
        if (!token) return

        // WebSocket URL from env or default /ws
        const sockUrl =
            import.meta.env.VITE_WS_URL || '/ws'

        const client = new Client({
            webSocketFactory: () => new SockJS(sockUrl),

            reconnectDelay: 5000, // reconnect every 5s if disconnected
            heartbeatIncoming: 10000, // 10s heartbeat incoming
            heartbeatOutgoing: 10000, // 10s heartbeat outgoing

            connectHeaders: token ? { Authorization: `Bearer ${token}` } : {},

            onConnect: () => {

                client.subscribe('/topic/alerts', (message) => {
                    try {
                        const data = JSON.parse(message.body)
                        onAlertRef.current ? .(data) // ✅ Correct optional chaining
                    } catch (err) {
                        console.warn('Parse error:', err)
                        onAlertRef.current ? .(message.body)
                    }
                })
            },

            onStompError: (frame) => {
                console.warn('❌ STOMP Error:', frame.headers ? .message || frame.body)
            },

            onWebSocketError: (e) => {},

            debug: (str) => {},
        })

        client.activate()

        return () => {
            client.deactivate()
        }
    }, [token])
}
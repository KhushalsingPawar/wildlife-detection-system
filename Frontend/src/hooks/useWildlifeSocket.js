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

    // keep latest callback
    useEffect(() => {
        onAlertRef.current = onAlert
    }, [onAlert])

    useEffect(() => {
        // ✅ IMPORTANT: use proxy path
        const sockUrl =
            import.meta.env.VITE_WS_URL || '/ws'

        const client = new Client({
            webSocketFactory: () => new SockJS(sockUrl),

            reconnectDelay: 5000,
            heartbeatIncoming: 10000,
            heartbeatOutgoing: 10000,

            connectHeaders: token ?
                { Authorization: `Bearer ${token}` } :
                {},

            onConnect: () => {
                console.log('✅ WebSocket Connected')

                client.subscribe('/topic/alerts', (message) => {
                    try {
                        const data = JSON.parse(message.body)
                        onAlertRef.current ? .(data)
                    } catch (err) {
                        console.warn('Parse error:', err)
                        onAlertRef.current ? .(message.body)
                    }
                })
            },

            onStompError: (frame) => {
                console.warn('❌ STOMP', frame.headers ? .message || frame.body)
            },

            onWebSocketError: (e) => {
                console.warn('❌ WS Error', e)
            },

            debug: (str) => {
                console.log('[STOMP]', str)
            },
        })

        client.activate()

        return () => {
            console.log('🛑 WebSocket Disconnected')
            client.deactivate()
        }
    }, [token])
}
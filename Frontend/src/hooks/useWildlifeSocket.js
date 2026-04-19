// src/hooks/useWildlifeSocket.js
import { useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { useAuth } from '../context/AuthContext';

/**
 * Custom Hook: useWildlifeSocket
 * Connects to Spring Boot STOMP over SockJS
 * Calls onAlert callback whenever a message arrives
 */
export function useWildlifeSocket(onAlert) {
    const { token } = useAuth();
    const onAlertRef = useRef(onAlert);
    const clientRef = useRef(null);

    // Keep the latest callback reference
    useEffect(() => {
        onAlertRef.current = onAlert;
    }, [onAlert]);

    useEffect(() => {
        if (!token) return;

        // Function to create and activate STOMP client
        const connectWebSocket = () => {
            const client = new Client({
                webSocketFactory: () => new SockJS(
                    import.meta.env.VITE_WS_URL || '/ws'),
                reconnectDelay: 5000,
                heartbeatIncoming: 10000,
                heartbeatOutgoing: 10000,
                connectHeaders: { Authorization: `Bearer ${token}` },

                onConnect: () => {

                    // Subscribe to topic
                    client.subscribe('/topic/alerts', (message) => {
                        try {
                            const data = JSON.parse(message.body);
                            // Standard 'if' check to prevent formatter errors
                            if (onAlertRef.current) {
                                onAlertRef.current(data);
                            }
                        } catch (err) {
                            console.warn('⚠️ Parse error:', err);
                            // Standard 'if' check for raw messages
                            if (onAlertRef.current) {
                                onAlertRef.current(message.body);
                            }
                        }
                    });
                },

                onStompError: (frame) => {
                    // Manual check for headers to avoid syntax errors
                    const msg = (frame.headers && frame.headers.message) ? frame.headers.message : frame.body;
                    console.error('❌ STOMP Error:', msg);
                },

                onWebSocketError: (error) => {},

                onDisconnect: () => {},

                debug: (msg) => {},
            });

            client.activate();
            clientRef.current = client;
        };

        connectWebSocket();

        return () => {
            if (clientRef.current) {
                clientRef.current.deactivate();
                clientRef.current = null;
            }
        };
    }, [token]);

}
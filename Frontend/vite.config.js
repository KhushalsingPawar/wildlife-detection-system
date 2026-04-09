import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
    plugins: [react(), tailwindcss()],
    // sockjs-client expects Node's `global`; browsers expose `globalThis` instead
    define: {
        global: 'globalThis',
    },
    server: {
        proxy: {
            '/api': {
                target: 'http://localhost:8082',
                changeOrigin: true,
                secure: false,
            },
            '/ws': {
                target: 'http://localhost:8082',
                changeOrigin: true,
                ws: true,
            },
        },
    },
})
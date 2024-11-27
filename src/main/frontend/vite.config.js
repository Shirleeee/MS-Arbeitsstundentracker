import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import { fileURLToPath, URL } from 'node:url'

// Use 'path-browserify' instead of 'path'
import path from 'path-browserify'

export default defineConfig({
  build: {
    outDir: path.resolve(__dirname, '../resources/static'), // Correctly set the output directory here
  },
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),

    },
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8081/',
        changeOrigin: true,
      },
    },
  },
})

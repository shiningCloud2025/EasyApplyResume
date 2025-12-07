import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver()],
      imports: [
        'vue',
        'vue-router',
        'pinia'
      ],
      dts: true
    }),
    Components({
      resolvers: [ElementPlusResolver()],
      dts: true
    })
  ],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
      '@components': resolve(__dirname, 'src/components'),
      '@views': resolve(__dirname, 'src/views'),
      '@api': resolve(__dirname, 'src/api'),
      '@utils': resolve(__dirname, 'src/utils'),
      '@assets': resolve(__dirname, 'src/assets'),
      '@store': resolve(__dirname, 'src/store'),
      '@components': resolve(__dirname, 'src/components')
    }
  },
  server: {
    host: '0.0.0.0',
    port: 3000,
    open: true,
    proxy: {
      '/admin': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/admin/, '/admin')
      }
    }
  },
  build: {
    target: 'es2015',
    outDir: 'dist',
    assetsDir: 'assets',
    minify: 'terser',
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true
      }
    },
    rollupOptions: {
      output: {
        chunkFileNames: 'assets/js/[name]-[hash].js',
        entryFileNames: 'assets/js/[name]-[hash].js',
        assetFileNames: 'assets/[ext]/[name]-[hash].[ext]'
      }
    }
  },
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: '@use "@/styles/variables.scss" as *;'
      }
    }
  }
})
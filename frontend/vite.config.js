import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  base: '/Hostel-Maintanace-Portal/',
  plugins: [react()],
  server: { port: 5173 }
});

<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="home-bg">
        <div class="hero-section">
          <div class="logo-area">
            <img src="/assets/2look_letras.png" style="width:140px" alt="2LOOK" />
          </div>
          <div class="hero-text">
            <h1>Bienvenido</h1>
            <p>Explora estilos y encuentra tu barbero</p>
          </div>
        </div>

        <div class="search-section">
          <div class="search-bar" @click="router.push('/buscar')">
            <ion-icon name="search-outline" class="search-icon"></ion-icon>
            <input type="text" placeholder="Buscar" readonly style="cursor:pointer" />
            <ion-icon name="options-outline" class="filter-btn"></ion-icon>
          </div>
        </div>

        <div class="barbers-scroll">
          <div
            v-for="barber in barbers"
            :key="barber.id"
            class="barber-card"
            @click="router.push(`/barbero/${barber.id}`)"
          >
            <div class="barber-avatar-card">
              <img :src="barber.img" style="width:100%; height:100%; object-fit:cover; border-radius:10px" alt="" />
            </div>
            <div class="barber-info">
              <div class="barber-name">{{ barber.name }}</div>
              <div class="barber-stars">
                <span v-for="i in 5" :key="i" :class="['star', i <= barber.rating ? 'filled' : '']">★</span>
                <span class="rating-text">({{ barber.ratingText }})</span>
              </div>
            </div>
            <button class="reserve-btn" @click.stop="reservar(barber)">
              Reservar
            </button>
          </div>
        </div>

        <div class="section-content">
          <button class="cta-btn" @click="router.push('/elegir-barbero')">
            Reservar Cita →
          </button>
          <div class="map-section">
            <h3>Donde estamos</h3>
            <div class="map-placeholder">
              <div id="leaflet-map" style="width:100%; height:100%;"></div>
              <div class="map-label">Barcelona, España</div>
            </div>
          </div>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import { searchOutline, optionsOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'
import { bookingStore } from '@/store/user'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png'
import markerIcon from 'leaflet/dist/images/marker-icon.png'
import markerShadow from 'leaflet/dist/images/marker-shadow.png'
delete (L.Icon.Default.prototype as any)._getIconUrl
L.Icon.Default.mergeOptions({ iconUrl: markerIcon, iconRetinaUrl: markerIcon2x, shadowUrl: markerShadow })

addIcons({ 'search-outline': searchOutline, 'options-outline': optionsOutline })

const router = useRouter()
let map: L.Map | null = null

const barbers = ref([
  { id: 1, name: 'Charles Smith', rating: 4, ratingText: '4.0', initials: 'CS', img: '/assets/Rectangle.png' },
  { id: 2, name: 'Richard Jones', rating: 5, ratingText: '5.0', initials: 'RJ', img: '/assets/Rectangle__5.png' },
  { id: 3, name: 'Marc Andrew', rating: 3, ratingText: '3.0', initials: 'MA', img: '/assets/Rectangle__4.png' },
])

onMounted(async () => {
  await nextTick()
  setTimeout(() => {
    const lat = 41.3851
    const lng = 2.1734

    map = L.map('leaflet-map', {
      center: [lat, lng],
      zoom: 15,
      zoomControl: false,
      attributionControl: false,
    })

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      crossOrigin: true,
    }).addTo(map)

    const customIcon = L.divIcon({
      html: `<div style="width:18px;height:18px;background:#4a8fe8;border:3px solid white;border-radius:50%;box-shadow:0 0 10px rgba(74,143,232,0.7);"></div>`,
      iconSize: [18, 18],
      iconAnchor: [9, 9],
      className: '',
    })

    L.marker([lat, lng], { icon: customIcon }).addTo(map)
  }, 200)
})

onUnmounted(() => {
  map?.remove()
  map = null
})

const reservar = (barber: any) => {
  bookingStore.barberoId = barber.id
  bookingStore.barberoNombre = barber.name
  bookingStore.barberoImg = barber.img
  bookingStore.barberoInitials = barber.initials
  bookingStore.barberoColor = barber.color
  router.push('/reservar')
}
</script>

<style scoped>
ion-page { --ion-safe-area-top: 0px; }
.home-bg { 
  min-height: 100%;
  background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%);
  padding-bottom: 30px;
}
.hero-section { padding: 50px 0 20px; }
.logo-area { display: flex; justify-content: center; margin-bottom: 0px; padding: 0 24px; }
.hero-text { padding: 0 24px; }
.hero-text h1 { font-family: 'Rajdhani', sans-serif; font-size: 40px; font-weight: 700; color: white; margin: 0 0 6px; }
.hero-text p { font-size: 15px; color: rgba(255,255,255,0.55); margin: 0; }
.search-section { padding: 0 24px 16px; }
.search-bar { background: rgba(255,255,255,0.07); border: 1px solid rgba(255,255,255,0.1); border-radius: 14px; display: flex; align-items: center; padding: 0 14px; height: 48px; gap: 8px; cursor: pointer; }
.search-icon { color: rgba(255,255,255,0.4); font-size: 18px; }
.search-bar input { flex: 1; background: none; border: none; outline: none; color: white; font-size: 15px; font-family: 'DM Sans', sans-serif; cursor: pointer; }
.search-bar input::placeholder { color: rgba(255,255,255,0.35); }
.filter-btn { color: rgba(255,255,255,0.5); font-size: 20px; }
.barbers-scroll { display: flex; gap: 14px; overflow-x: auto; padding: 4px 24px 16px 24px; scroll-snap-type: x mandatory; -webkit-overflow-scrolling: touch; scrollbar-width: none; scroll-padding-left: 24px; }
.barbers-scroll::-webkit-scrollbar { display: none; }
.barber-card { min-width: calc(100vw - 72px); flex-shrink: 0; background: rgba(255,255,255,0.07); border: 1px solid rgba(255,255,255,0.1); border-radius: 16px; padding: 14px 16px; display: flex; align-items: center; gap: 14px; cursor: pointer; scroll-snap-align: start; transition: background 0.2s; }
.barber-avatar-card { width: 62px; height: 68px; border-radius: 10px; overflow: hidden; flex-shrink: 0; }
.barber-info { flex: 1; min-width: 0; }
.barber-name { font-weight: 600; font-size: 16px; color: white; margin-bottom: 5px; }
.star { color: rgba(255,255,255,0.2); font-size: 14px; }
.star.filled { color: #f5c518; }
.rating-text { font-size: 12px; color: rgba(255,255,255,0.4); margin-left: 3px; }
.reserve-btn { background: #1a2d42; border: 1px solid rgba(255,255,255,0.15); border-radius: 10px; color: white; padding: 9px 16px; font-family: 'DM Sans', sans-serif; font-size: 13px; font-weight: 600; cursor: pointer; white-space: nowrap; flex-shrink: 0; }
.section-content { padding: 4px 24px 0; }
.cta-btn { width: 100%; background: rgba(255,255,255,0.08); border: 1px solid rgba(255,255,255,0.12); border-radius: 14px; color: white; padding: 18px; font-family: 'DM Sans', sans-serif; font-size: 16px; font-weight: 600; cursor: pointer; margin: 8px 0 28px; text-align: center; transition: background 0.2s; }
.cta-btn:hover { background: rgba(255,255,255,0.12); }
.map-section h3 { color: white; font-family: 'Rajdhani', sans-serif; font-size: 22px; margin: 0 0 12px; }
.map-placeholder { height: 210px; background: #1c2f3f; border-radius: 16px; position: relative; overflow: hidden; border: 1px solid rgba(255,255,255,0.07); }
.map-label { position: absolute; bottom: 10px; left: 50%; transform: translateX(-50%); background: rgba(0,0,0,0.55); border-radius: 20px; padding: 4px 14px; color: white; font-size: 12px; white-space: nowrap; z-index: 1000; pointer-events: none; }
</style>
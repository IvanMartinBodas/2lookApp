<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="page-bg">
        <div class="page-header">
          <button class="back-btn" @click="router.back()">
            <ion-icon name="arrow-back-outline"></ion-icon>
          </button>
        </div>

        <div v-if="!barber" class="empty-state">
          <p>Barbero no encontrado</p>
        </div>

        <div v-else class="page-content">

          
          <div class="profile-card">
            <div class="barber-big-avatar">
              <img :src="barber.img" :alt="barber.name" />
            </div>
            <h2>{{ barber.name }}</h2>
            <p class="barber-age">{{ barber.age }} años · {{ barber.title }}</p>
            <div class="barber-stars">
              <span v-for="i in 5" :key="i" :class="['star', i <= barber.rating ? 'filled' : '']">★</span>
              <span class="rating-txt">({{ barber.ratingText }})</span>
            </div>
          </div>

          <div class="section-block">
            <h4>Cortes de pelo</h4>
            <swiper :slides-per-view="1.3" :space-between="12" class="cuts-swiper">
              <swiper-slide v-for="(foto, i) in barber.cortes" :key="i">
                <div class="cut-slide">
                  <img :src="foto" alt="corte" />
                </div>
              </swiper-slide>
            </swiper>
          </div>

          <div class="section-block">
            <h4>Valoraciones últimos 6 meses</h4>
            <div class="chart-wrapper">
              <canvas ref="chartCanvas"></canvas>
            </div>
          </div>

          
          <button class="btn-primary" @click="router.push('/reservar')">
            Reservar cita
          </button>

        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, computed, watch, onUnmounted, nextTick } from 'vue'
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import { arrowBackOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter, useRoute } from 'vue-router'


import { Swiper, SwiperSlide } from 'swiper/vue'
import 'swiper/css'

import {
  Chart,
  LineElement,
  PointElement,
  LineController,
  CategoryScale,
  LinearScale,
  Filler,
  Tooltip,
} from 'chart.js'
Chart.register(LineElement, PointElement, LineController, CategoryScale, LinearScale, Filler, Tooltip)

addIcons({ 'arrow-back-outline': arrowBackOutline })

const router = useRouter()
const route = useRoute()
const chartCanvas = ref<HTMLCanvasElement | null>(null)
let chartInstance: Chart | null = null


const fotosCortes = ['/assets/1.jpg','/assets/2.jpg','/assets/3.jpg','/assets/4.jpg','/assets/5.jpg']

const barberos = [
  { id: 1, name: 'Charles Smith', age: 33, title: 'Licenciado en Ecoles de Coiffure/Barberie', rating: 4, ratingText: '4.0', img: '/assets/Rectangle.png', cortes: fotosCortes, valoraciones: [3.5,3.8,4.0,3.9,4.1,4.0] },
  { id: 2, name: 'Richard Jones', age: 28, title: 'Licenciado en Ecoles de Coiffure/Barberie', rating: 5, ratingText: '5.0', img: '/assets/Rectangle__5.png', cortes: fotosCortes, valoraciones: [4.2,4.5,4.7,4.8,4.9,5.0] },
  { id: 3, name: 'Marc Andrew', age: 35, title: 'Licenciado en Ecoles de Coiffure/Barberie', rating: 3, ratingText: '3.0', img: '/assets/Rectangle__4.png', cortes: fotosCortes, valoraciones: [3.0,2.8,3.1,2.9,3.0,3.0] },
]

const barber = computed(() => {
  const id = Number(route.params.id)
  return barberos.find(b => b.id === id) || null
})

const meses = ['Sep','Oct','Nov','Dic','Ene','Feb']

function crearGrafica() {
  if (!chartCanvas.value || !barber.value) return
  if (chartInstance) { chartInstance.destroy(); chartInstance = null }

  const ctx = chartCanvas.value.getContext('2d')
  if (!ctx) return

  const gradient = ctx.createLinearGradient(0, 0, 0, 160)
  gradient.addColorStop(0, 'rgba(74, 143, 232, 0.4)')
  gradient.addColorStop(1, 'rgba(74, 143, 232, 0.0)')

  chartInstance = new Chart(ctx, {
    type: 'line',
    data: {
      labels: meses,
      datasets: [{
        label: 'Valoración',
        data: barber.value.valoraciones,
        borderColor: '#4a8fe8',
        backgroundColor: gradient,
        borderWidth: 2.5,
        pointBackgroundColor: '#4a8fe8',
        pointBorderColor: '#fff',
        pointBorderWidth: 2,
        pointRadius: 5,
        tension: 0.4,
        fill: true,
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false },
        tooltip: {
          backgroundColor: '#1a2d42',
          titleColor: 'rgba(255,255,255,0.6)',
          bodyColor: '#fff',
          borderColor: 'rgba(255,255,255,0.1)',
          borderWidth: 1,
          callbacks: { label: (ctx) => ` ${ctx.parsed.y.toFixed(1)} ★` },
        },
      },
      scales: {
        x: { grid: { color: 'rgba(255,255,255,0.05)' }, ticks: { color: 'rgba(255,255,255,0.5)', font: { size: 12 } } },
        y: { min: 1, max: 5, grid: { color: 'rgba(255,255,255,0.05)' }, ticks: { color: 'rgba(255,255,255,0.5)', font: { size: 12 }, stepSize: 1, callback: (val) => `${val}★` } },
      },
    },
  })
}

watch(barber, async (val) => {
  if (val) { await nextTick(); crearGrafica() }
}, { immediate: true })

onUnmounted(() => { chartInstance?.destroy(); chartInstance = null })
</script>

<style scoped>
.page-bg { min-height: 100%; background: linear-gradient(160deg,#1a3a5c 0%,#0d1b2a 40%,#080f18 100%); }
.page-header { display: flex; align-items: center; gap: 16px; padding: 30px 24px 4px; }
.back-btn { background: rgba(255,255,255,0.08); border: none; border-radius: 10px; width: 40px; height: 40px; display: flex; align-items: center; justify-content: center; color: white; font-size: 20px; cursor: pointer; flex-shrink: 0; }
.empty-state { text-align: center; padding: 80px 24px; color: rgba(255,255,255,0.4); font-size: 15px; font-family: 'DM Sans',sans-serif; }
.page-content { padding: 0 24px 40px; }
.profile-card { text-align: center; padding: 4px 0 24px; }
.barber-big-avatar { width: 120px; height: 120px; border-radius: 50%; overflow: hidden; margin: 0 auto 16px; border: 3px solid rgba(255,255,255,0.1); }
.barber-big-avatar img { width: 100%; height: 100%; object-fit: cover; }
h2 { font-family: 'Rajdhani',sans-serif; font-size: 28px; color: white; margin: 0 0 6px; font-weight: 700; }
.barber-age { color: rgba(255,255,255,0.5); font-size: 13px; margin: 0 0 12px; line-height: 1.4; }
.star { font-size: 18px; color: rgba(255,255,255,0.2); }
.star.filled { color: #f5c518; }
.rating-txt { font-size: 13px; color: rgba(255,255,255,0.4); margin-left: 6px; }
.section-block { background: rgba(255,255,255,0.05); border-radius: 16px; border: 1px solid rgba(255,255,255,0.08); padding: 4px 0; margin-bottom: 20px; overflow: hidden; }
h4 { color: rgba(255,255,255,0.5); font-size: 13px; text-transform: uppercase; letter-spacing: 0.08em; padding: 14px 16px 10px; margin: 0; border-bottom: 1px solid rgba(255,255,255,0.06); }
.cuts-swiper { padding: 14px 16px 16px !important; }
.cut-slide { border-radius: 12px; overflow: hidden; height: 160px; }
.cut-slide img { width: 100%; height: 100%; object-fit: cover; }
.chart-wrapper { padding: 12px 8px 16px; height: 180px; position: relative; }
.btn-primary { width: 100%; padding: 16px; background: linear-gradient(135deg,#1e4fd8,#2d6be4); border: none; border-radius: 14px; color: white; font-size: 16px; font-weight: 600; cursor: pointer; font-family: 'DM Sans',sans-serif; }
</style>
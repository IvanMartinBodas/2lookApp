<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="page-bg">
        <div class="page-header">
          <button class="back-btn" @click="router.back()">
            <ion-icon :icon="arrowBackOutline"></ion-icon>
          </button>
        </div>

        <div class="page-content">
          <h2>¿Qué corte quieres?</h2>
          <p class="subtitle">Elige tú mismo o deja que la IA te recomiende el mejor corte según tu rostro.</p>

          <!-- Opción A: Elegir manualmente -->
          <div class="opcion-card" @click="mostrarSelector = !mostrarSelector">
            <div class="opcion-icon">✂️</div>
            <div class="opcion-info">
              <h3>Elige tu corte</h3>
              <p>Selecciona entre nuestros servicios disponibles</p>
            </div>
            <ion-icon :icon="mostrarSelector ? chevronUpOutline : chevronDownOutline" class="opcion-chevron"></ion-icon>
          </div>

          <!-- Lista de servicios desplegable -->
          <div v-if="mostrarSelector" class="selector-wrap">
            <div v-if="cargando" class="skeleton-row"></div>
            <div v-else>
              <div
                v-for="srv in servicios"
                :key="srv.id"
                :class="['servicio-item', servicioSeleccionado?.id === srv.id ? 'selected' : '']"
                @click="seleccionarServicio(srv)"
              >
                <div class="srv-left">
                  <span class="srv-nombre">{{ srv.nombre }}</span>
                  <span class="srv-desc">{{ srv.descripcion || srv.duracionMin + ' min' }}</span>
                </div>
                <div class="srv-right">
                  <span class="srv-precio">{{ srv.precio }}€</span>
                  <ion-icon
                    :icon="servicioSeleccionado?.id === srv.id ? checkmarkCircle : ellipseOutline"
                    :style="{ color: servicioSeleccionado?.id === srv.id ? '#4a8fe8' : 'rgba(255,255,255,0.2)', fontSize: '22px' }"
                  ></ion-icon>
                </div>
              </div>
            </div>
            <button class="btn-continuar" :disabled="!servicioSeleccionado" @click="continuarConServicio">
              Continuar con {{ servicioSeleccionado?.nombre || '...' }} →
            </button>
          </div>

          <!-- Opción B: IA -->
          <div v-if="!desdeIA" class="opcion-card ia" @click="irAIA">
            <div class="opcion-icon">🤖</div>
            <div class="opcion-info">
              <h3>Recomendación IA</h3>
              <p>Hazte una foto y Gemini detectará la forma de tu cara y te recomendará el corte ideal</p>
            </div>
            <ion-icon :icon="sparklesOutline" class="opcion-chevron ia-icon"></ion-icon>
          </div>

          <div v-if="!desdeIA" class="ia-hint">
            <ion-icon :icon="informationCircleOutline"></ion-icon>
            <span>La IA analiza tu morfología facial y sugiere los 3 cortes que mejor te quedan</span>
          </div>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import {
  arrowBackOutline, chevronDownOutline, chevronUpOutline,
  checkmarkCircle, ellipseOutline, sparklesOutline, informationCircleOutline
} from 'ionicons/icons'
import { useRouter, useRoute } from 'vue-router'
import { bookingStore } from '@/store/user'
import { servicioApi } from '@/store/api'

const router = useRouter()
const route = useRoute()
const desdeIA = route.query.desde === 'reserva'
const mostrarSelector = ref(false)
const cargando = ref(true)
const servicios = ref<any[]>([])
const servicioSeleccionado = ref<any>(null)

onMounted(async () => {
  try {
    servicios.value = await servicioApi.getAll()
  } catch {
    servicios.value = [
      { id: 1, nombre: 'Taper Fade', descripcion: 'Degradado suave', precio: 10, duracionMin: 30 },
      { id: 2, nombre: 'Burst Fade', descripcion: 'Degradado en arco', precio: 10, duracionMin: 30 },
      { id: 3, nombre: 'Corte Clásico', descripcion: 'Tijera y máquina', precio: 10, duracionMin: 30 },
      { id: 4, nombre: 'Corte Texturizado', descripcion: 'Moderno con textura', precio: 10, duracionMin: 30 },
    ]
  } finally {
    cargando.value = false
  }
})

const seleccionarServicio = (srv: any) => {
  servicioSeleccionado.value = srv
}

const continuarConServicio = () => {
  bookingStore.servicioId = servicioSeleccionado.value.id
  bookingStore.servicioNombre = servicioSeleccionado.value.nombre
  bookingStore.servicioPrecio = servicioSeleccionado.value.precio
  router.push('/elegir-barbero')
}

const irAIA = () => {
  bookingStore.desdeIA = true
  window.location.replace('/tabs/camara')
}
</script>

<style scoped>
ion-page { --ion-safe-area-top: 0px; }
.page-bg { min-height: 100%; background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%); }
.page-header { padding: 50px 24px 10px; }
.back-btn { background: rgba(255,255,255,0.08); border: none; border-radius: 10px; width: 40px; height: 40px; display: flex; align-items: center; justify-content: center; color: white; font-size: 20px; cursor: pointer; }
.page-content { padding: 0 24px 40px; }
h2 { font-family: 'Rajdhani', sans-serif; font-size: 30px; font-weight: 700; color: white; margin: 0 0 8px; }
.subtitle { color: rgba(255,255,255,0.5); font-size: 14px; margin: 0 0 28px; line-height: 1.5; }
.opcion-card { display: flex; align-items: center; gap: 14px; background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.1); border-radius: 18px; padding: 18px 16px; margin-bottom: 12px; cursor: pointer; transition: all 0.2s; }
.opcion-card:hover { background: rgba(255,255,255,0.09); }
.opcion-card.ia { border-color: rgba(74,143,232,0.3); background: rgba(74,143,232,0.07); }
.opcion-icon { font-size: 32px; flex-shrink: 0; }
.opcion-info { flex: 1; }
.opcion-info h3 { color: white; font-size: 17px; font-weight: 700; margin: 0 0 4px; font-family: 'Rajdhani', sans-serif; }
.opcion-info p { color: rgba(255,255,255,0.5); font-size: 13px; margin: 0; line-height: 1.4; }
.opcion-chevron { color: rgba(255,255,255,0.4); font-size: 20px; flex-shrink: 0; }
.ia-icon { color: #4a8fe8 !important; }
.selector-wrap { background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); border-radius: 16px; padding: 8px; margin-bottom: 12px; }
.skeleton-row { height: 56px; background: rgba(255,255,255,0.05); border-radius: 12px; margin: 4px; animation: pulse 1.5s infinite; }
@keyframes pulse { 0%,100%{opacity:0.4} 50%{opacity:0.8} }
.servicio-item { display: flex; align-items: center; justify-content: space-between; padding: 14px 12px; border-radius: 12px; cursor: pointer; transition: background 0.2s; margin-bottom: 2px; }
.servicio-item:hover { background: rgba(255,255,255,0.05); }
.servicio-item.selected { background: rgba(74,143,232,0.12); }
.srv-left { display: flex; flex-direction: column; gap: 2px; }
.srv-nombre { color: white; font-size: 15px; font-weight: 600; }
.srv-desc { color: rgba(255,255,255,0.4); font-size: 12px; }
.srv-right { display: flex; align-items: center; gap: 10px; }
.srv-precio { color: white; font-weight: 700; font-size: 15px; }
.btn-continuar { width: 100%; margin-top: 8px; background: linear-gradient(135deg, #2d5986, #1a3a5c); border: none; border-radius: 12px; color: white; padding: 16px; font-family: 'DM Sans', sans-serif; font-size: 15px; font-weight: 600; cursor: pointer; }
.btn-continuar:disabled { opacity: 0.4; cursor: not-allowed; }
.ia-hint { display: flex; align-items: flex-start; gap: 8px; background: rgba(74,143,232,0.08); border: 1px solid rgba(74,143,232,0.15); border-radius: 12px; padding: 12px 14px; margin-top: 8px; }
.ia-hint ion-icon { color: #4a8fe8; font-size: 18px; flex-shrink: 0; margin-top: 1px; }
.ia-hint span { color: rgba(255,255,255,0.5); font-size: 13px; line-height: 1.5; }
</style>
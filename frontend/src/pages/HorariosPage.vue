<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="page-bg">
        <div class="page-header">
          <button class="back-btn" @click="router.back()">
            <ion-icon name="arrow-back-outline"></ion-icon>
          </button>
          <h3>Horarios</h3>
        </div>
        <div class="page-content">
          <div v-for="period in periods" :key="period.name" class="period-section">
            <h4>{{ period.name }}</h4>
            <div class="time-grid">
              <div
                v-for="time in period.times"
                :key="time"
                :class="['time-slot', selectedTime === time ? 'selected' : '']"
                @click="selectedTime = time" >
                {{ time }}
              </div>
            </div>
          </div>
        </div>
        <div class="bottom-bar">
          <div class="bottom-info">
            <div class="bottom-time">{{ selectedTime || '18:30 - 19:00' }}</div>
            <div class="bottom-price">10,00 €</div>
          </div>
          <button class="reserve-btn" @click="goToCitas">Reservar →</button>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import { arrowBackOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'
import { bookingStore } from '@/store/user'

addIcons({ 'arrow-back-outline': arrowBackOutline })

const router = useRouter()
const selectedTime = ref('')

const goToCitas = () => {
  bookingStore.hora = selectedTime.value || '18:30'
  bookingStore.tieneCita = true
  router.replace('/tabs').then(() => {
    router.replace('/tabs/citas')
  })
}

const periods = ref([
  { name: 'Mañana', times: ['09:00','09:30','10:00','10:30','11:00','11:30','12:00','12:30'] },
  { name: 'Tarde', times: ['15:00','15:30','16:00','16:30','17:00','17:30','18:00','18:30'] },
])
</script>

<style scoped>
.page-bg { min-height: 100%; background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%); display: flex; flex-direction: column; }
.page-header { display: flex; align-items: center; gap: 16px; padding: 56px 24px 16px; }
h3 { color: white; font-size: 18px; margin: 0; font-family: 'Rajdhani', sans-serif; font-weight: 600; }
.back-btn { background: rgba(255,255,255,0.08); border: none; border-radius: 10px; width: 40px; height: 40px; display: flex; align-items: center; justify-content: center; color: white; font-size: 20px; cursor: pointer; flex-shrink: 0; }
.page-content { flex: 1; padding: 0 20px; overflow-y: auto; padding-bottom: 100px; }
.period-section { margin-bottom: 24px; }
h4 { color: rgba(255,255,255,0.6); font-size: 14px; font-weight: 500; margin: 0 0 14px; text-align: center; }
.time-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; }
.time-slot { background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.08); border-radius: 12px; padding: 12px 8px; text-align: center; color: rgba(255,255,255,0.7); font-size: 13px; cursor: pointer; transition: all 0.2s; }
.time-slot:hover { background: rgba(74,143,232,0.15); }
.time-slot.selected { background: #4a8fe8; border-color: #4a8fe8; color: white; font-weight: 600; }
.bottom-bar { position: fixed; bottom: 0; left: 0; right: 0; background: rgba(10,20,30,0.95); backdrop-filter: blur(10px); border-top: 1px solid rgba(255,255,255,0.08); padding: 16px 24px 32px; display: flex; align-items: center; justify-content: space-between; }
.bottom-info { display: flex; flex-direction: column; gap: 2px; }
.bottom-time { color: rgba(255,255,255,0.6); font-size: 13px; }
.bottom-price { color: white; font-size: 20px; font-weight: 700; }
.reserve-btn { background: #1a3a5c; border: none; border-radius: 12px; color: white; padding: 14px 24px; font-family: 'DM Sans', sans-serif; font-size: 15px; font-weight: 600; cursor: pointer; }
</style>
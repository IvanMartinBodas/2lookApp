<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="page-bg">
        <div class="page-header">
          <button class="back-btn" @click="router.back()">
            <ion-icon name="arrow-back-outline"></ion-icon>
          </button>
          <h3>Seleccionar fecha y hora</h3>
        </div>

        <div class="page-content">
          <div class="calendar-card">
            <div class="cal-header">
              <button class="cal-nav" @click="prevMonth">‹</button>
              <span class="cal-title">{{ monthName }} {{ currentYear }}</span>
              <button class="cal-nav" @click="nextMonth">›</button>
            </div>
            <div class="cal-weekdays">
              <span v-for="d in weekdays" :key="d">{{ d }}</span>
            </div>
            <div class="cal-days">
              <span
                v-for="day in calendarDays"
                :key="day.date + day.day"
                :class="['cal-day', day.isCurrentMonth ? '' : 'other', day.date === selectedDate ? 'selected' : '', day.disabled ? 'disabled' : '']"
                @click="!day.disabled && day.isCurrentMonth && (selectedDate = day.date)"
              >
                {{ day.day }}
              </span>
            </div>
          </div>

          <div class="summary-card">
            <div class="summary-row">
              <span>Corte de pelo:</span>
              <span class="price">10,00 €</span>
            </div>
            <div class="summary-row">
              <div class="barber-thumb" :style="{ background: bookingStore.barberoColor }">
                <img v-if="bookingStore.barberoImg" :src="bookingStore.barberoImg" style="width:100%; height:100%; object-fit:cover; border-radius:8px" alt="" />
                <span v-else>{{ bookingStore.barberoInitials }}</span>
              </div>
              <span class="barber-name-sm">{{ bookingStore.barberoNombre }}</span>
              <button class="change-btn" @click="router.push('/elegir-barbero')">Cambiar</button>
            </div>
            <div class="summary-time">{{ selectedDate || '26 Feb' }} 18:30 - 19:00</div>
          </div>
        </div>

        <div class="bottom-bar">
          <div class="bottom-info">
            <div class="bottom-time">18:30 - 19:00</div>
            <div class="bottom-price">10,00 €</div>
          </div>
          <button class="reserve-btn" @click="guardarYContinuar">Reservar →</button>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import { arrowBackOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'
import { bookingStore } from '@/store/user'

addIcons({ 'arrow-back-outline': arrowBackOutline })

const router = useRouter()
const today = new Date()
const currentMonth = ref(today.getMonth())
const currentYear = ref(today.getFullYear())
const selectedDate = ref('')

const weekdays = ['L', 'M', 'X', 'J', 'V', 'S', 'D']
const monthNames = ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre']
const monthName = computed(() => monthNames[currentMonth.value])

const calendarDays = computed(() => {
  const days = []
  const first = new Date(currentYear.value, currentMonth.value, 1)
  let startDay = first.getDay() - 1
  if (startDay < 0) startDay = 6

  for (let i = 0; i < startDay; i++) {
    const d = new Date(currentYear.value, currentMonth.value, -startDay + i + 1)
    days.push({ day: d.getDate(), date: '', isCurrentMonth: false, disabled: true })
  }

  const daysInMonth = new Date(currentYear.value, currentMonth.value + 1, 0).getDate()
  for (let i = 1; i <= daysInMonth; i++) {
    const date = `${i} ${monthNames[currentMonth.value].slice(0,3)}`
    const d = new Date(currentYear.value, currentMonth.value, i)
    days.push({ day: i, date, isCurrentMonth: true, disabled: d < today })
  }

  return days
})

const prevMonth = () => {
  if (currentMonth.value === 0) { currentMonth.value = 11; currentYear.value-- }
  else currentMonth.value--
}

const nextMonth = () => {
  if (currentMonth.value === 11) { currentMonth.value = 0; currentYear.value++ }
  else currentMonth.value++
}

const guardarYContinuar = () => {
  bookingStore.fecha = selectedDate.value || '26 Feb'
  router.push('/horarios')
}
</script>

<style scoped>
ion-page { --ion-safe-area-top: 0px; }
.page-bg { min-height: 100%; background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%); display: flex; flex-direction: column; }
.page-header { display: flex; align-items: center; gap: 16px; padding: 50px 24px 16px; }
h3 { color: rgba(255,255,255,0.7); font-size: 15px; margin: 0; font-weight: 400; }
.back-btn { background: rgba(255,255,255,0.08); border: none; border-radius: 10px; width: 40px; height: 40px; display: flex; align-items: center; justify-content: center; color: white; font-size: 20px; cursor: pointer; flex-shrink: 0; }
.page-content { flex: 1; padding: 0 20px; overflow-y: auto; }
.calendar-card { background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.08); border-radius: 20px; padding: 20px; margin-bottom: 20px; }
.cal-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 20px; }
.cal-nav { background: rgba(255,255,255,0.08); border: none; border-radius: 8px; width: 32px; height: 32px; color: white; font-size: 20px; cursor: pointer; display: flex; align-items: center; justify-content: center; }
.cal-title { color: white; font-weight: 600; font-size: 16px; }
.cal-weekdays { display: grid; grid-template-columns: repeat(7, 1fr); margin-bottom: 8px; }
.cal-weekdays span { text-align: center; color: rgba(255,255,255,0.4); font-size: 12px; padding: 6px 0; }
.cal-days { display: grid; grid-template-columns: repeat(7, 1fr); gap: 2px; }
.cal-day { aspect-ratio: 1; display: flex; align-items: center; justify-content: center; border-radius: 50%; font-size: 14px; color: white; cursor: pointer; transition: background 0.2s; }
.cal-day:hover:not(.disabled):not(.other) { background: rgba(74,143,232,0.2); }
.cal-day.other { color: rgba(255,255,255,0.15); cursor: default; }
.cal-day.disabled { color: rgba(255,255,255,0.2); cursor: not-allowed; }
.cal-day.selected { background: #4a8fe8; color: white; font-weight: 700; }
.summary-card { background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.08); border-radius: 16px; padding: 16px; margin-bottom: 100px; }
.summary-row { display: flex; align-items: center; gap: 10px; margin-bottom: 12px; color: rgba(255,255,255,0.7); font-size: 14px; }
.price { color: white; font-weight: 700; margin-left: auto; }
.barber-thumb { width: 36px; height: 38px; border-radius: 8px; overflow: hidden; display: flex; align-items: center; justify-content: center; color: white; font-size: 12px; font-weight: 700; flex-shrink: 0; }
.barber-name-sm { flex: 1; color: white; font-weight: 500; }
.change-btn { background: #1a3a5c; border: none; border-radius: 8px; color: white; padding: 6px 14px; font-size: 12px; cursor: pointer; font-family: 'DM Sans', sans-serif; }
.summary-time { color: rgba(255,255,255,0.5); font-size: 13px; text-align: right; }
.bottom-bar { position: fixed; bottom: 0; left: 0; right: 0; background: rgba(10,20,30,0.95); backdrop-filter: blur(10px); border-top: 1px solid rgba(255,255,255,0.08); padding: 16px 24px 32px; display: flex; align-items: center; justify-content: space-between; }
.bottom-info { display: flex; flex-direction: column; gap: 2px; }
.bottom-time { color: rgba(255,255,255,0.6); font-size: 13px; }
.bottom-price { color: white; font-size: 20px; font-weight: 700; }
.reserve-btn { background: #1a3a5c; border: none; border-radius: 12px; color: white; padding: 14px 24px; font-family: 'DM Sans', sans-serif; font-size: 15px; font-weight: 600; cursor: pointer; }
</style>
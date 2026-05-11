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
                :key="day.key"
                :class="['cal-day', day.isCurrentMonth ? '' : 'other', day.isCurrentMonth && day.date === selectedDate ? 'selected' : '', day.disabled ? 'disabled' : '']"
                @click="!day.disabled && day.isCurrentMonth && selectDate(day.date)"
              >{{ day.day }}</span>
            </div>
          </div>

          <div class="selected-info-row">
            <div v-if="selectedDate" class="info-chip date-chip">
              <span>{{ selectedDate }}</span>
            </div>
            <div v-else class="info-chip placeholder-chip">
              <span>Toca un día para elegir</span>
            </div>

            <div v-if="selectedTime" class="info-chip time-chip" @click="showTimePicker = true">
              <span>{{ selectedTime }}</span>
            </div>
            <div v-else-if="selectedDate" class="info-chip pick-time-chip" @click="showTimePicker = true">
              <span>Elige hora</span>
              <span class="chip-arrow">›</span>
            </div>
          </div>

          <div class="summary-card">
            <div class="summary-row">
              <span>{{ bookingStore.servicioNombre || 'Corte de pelo' }}</span>
              <span class="price">{{ (bookingStore.servicioPrecio || 10).toFixed(2).replace('.',',') }} €</span>
            </div>
            <div class="summary-divider"></div>
            <div class="summary-row">
              <div class="barber-thumb" :style="{ background: bookingStore.barberoColor }">
                <img v-if="bookingStore.barberoImg" :src="bookingStore.barberoImg" style="width:100%;height:100%;object-fit:cover;border-radius:8px" alt="" />
                <span v-else>{{ bookingStore.barberoInitials }}</span>
              </div>
              <span class="barber-name-sm">{{ bookingStore.barberoNombre }}</span>
              <button class="change-btn" @click="router.push('/elegir-barbero')">Cambiar</button>
            </div>
          </div>
        </div>

        <div class="bottom-bar">
          <div class="bottom-info">
            <div class="bottom-time">{{ selectedTime ? `${selectedDate} · ${selectedTime}` : 'Elige fecha y hora' }}</div>
            <div class="bottom-price">{{ (bookingStore.servicioPrecio || 10).toFixed(2).replace('.',',') }} €</div>
          </div>
          <button class="reserve-btn" :disabled="!selectedDate || !selectedTime || guardando" @click="confirmarReserva">
            <ion-spinner v-if="guardando" name="crescent" style="width:14px;height:14px;margin-right:6px"></ion-spinner>
            {{ guardando ? 'Reservando...' : 'Confirmar reserva' }}
          </button>
        </div>
      </div>
    </ion-content>

    <Transition name="sheet">
      <div v-if="showTimePicker" class="sheet-overlay" @click="showTimePicker = false">
        <div class="time-sheet" @click.stop>
          <div class="sheet-handle"></div>
          <div class="sheet-header">
            <div class="sheet-title-wrap">
              <div>
                <p class="sheet-title">Elige una hora</p>
                <p class="sheet-sub">{{ selectedDate }}</p>
              </div>
            </div>
            <button class="sheet-close" @click="showTimePicker = false">✕</button>
          </div>

          <div class="sheet-body">
            <div v-for="period in periods" :key="period.name" class="period-section">
              <div class="period-header">
                <span class="period-dot"></span>
                <span class="period-name">{{ period.name }}</span>
              </div>
              <div class="hour-grid">
                <button
                  v-for="time in period.times"
                  :key="time"
                  :class="['hour-btn', selectedTime === time ? 'active' : '', isBlocked(time) ? 'blocked' : '']"
                  :disabled="isBlocked(time)"
                  @click="!isBlocked(time) && pickTime(time)"
                >
                  <span v-if="isBlocked(time)" class="blocked-line">{{ time }}</span>
                  <span v-else>{{ time }}</span>
                </button>
              </div>
            </div>
            <p class="avail-note">Las horas en gris ya están reservadas</p>
          </div>
        </div>
      </div>
    </Transition>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { IonPage, IonContent, IonIcon, IonSpinner } from '@ionic/vue'
import { arrowBackOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'
import { bookingStore, userStore, restaurarSesion } from '@/store/user'
import { reservaApi } from '@/store/api'

addIcons({ 'arrow-back-outline': arrowBackOutline })

const router = useRouter()
const today = new Date()
const todayStart = new Date(today.getFullYear(), today.getMonth(), today.getDate())
const currentMonth = ref(today.getMonth())
const currentYear  = ref(today.getFullYear())
const selectedDate = ref('')
const selectedTime = ref('')
const showTimePicker = ref(false)
const guardando    = ref(false)

const weekdays   = ['L','M','X','J','V','S','D']
const monthNames = ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre']
const monthName  = computed(() => monthNames[currentMonth.value])

const periods = [
  { name: 'Mañana', times: ['09:00','09:30','10:00','10:30','11:00','11:30','12:00','12:30'] },
  { name: 'Tarde',  times: ['15:00','15:30','16:00','16:30','17:00','17:30','18:00','18:30'] },
]

const blockedBase: Record<number, string[]> = {
  1: ['09:00','10:30','12:00','15:30','17:00','18:00'],
  2: ['09:00','09:30','11:00','15:00','16:30','18:30'],
  3: ['10:00','11:30','12:30','16:00','17:30','18:00'],
}

const extraPool = ['09:30','10:00','11:00','12:00','15:30','16:30','17:00','18:30']

function isBlocked(time: string): boolean {
  const barberoId = bookingStore.barberoId ?? 1
  const base = blockedBase[barberoId] ?? blockedBase[1]
  if (base.includes(time)) return true
  const day = parseInt(selectedDate.value?.split(' ')[0] || '1')
  const seed = (day * 3 + barberoId * 7) % extraPool.length
  const extra1 = extraPool[seed]
  const extra2 = extraPool[(seed + 3) % extraPool.length]
  return time === extra1 || time === extra2
}

const calendarDays = computed(() => {
  const days = []
  const first = new Date(currentYear.value, currentMonth.value, 1)
  let startDay = first.getDay() - 1
  if (startDay < 0) startDay = 6

  for (let i = 0; i < startDay; i++) {
    const d = new Date(currentYear.value, currentMonth.value, -startDay + i + 1)
    days.push({ key: `prev-${i}`, day: d.getDate(), date: '', isCurrentMonth: false, disabled: true })
  }

  const daysInMonth = new Date(currentYear.value, currentMonth.value + 1, 0).getDate()
  for (let i = 1; i <= daysInMonth; i++) {
    const date = `${i} ${monthNames[currentMonth.value].slice(0,3)}`
    const d = new Date(currentYear.value, currentMonth.value, i)
    days.push({ key: date, day: i, date, isCurrentMonth: true, disabled: d < todayStart })
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

function selectDate(date: string) {
  selectedDate.value = date
  selectedTime.value = ''
  showTimePicker.value = true
}

function pickTime(time: string) {
  selectedTime.value = time
  showTimePicker.value = false
}

function buildFechaISO(fechaStr: string, hora: string): string {
  try {
    const meses: Record<string, number> = {
      'Ene':0,'Feb':1,'Mar':2,'Abr':3,'May':4,'Jun':5,
      'Jul':6,'Ago':7,'Sep':8,'Oct':9,'Nov':10,'Dic':11
    }
    const [dia, mes] = fechaStr.split(' ')
    const month = meses[mes] ?? new Date().getMonth()
    const [h, m] = hora.split(':')
    const date = new Date(new Date().getFullYear(), month, parseInt(dia), parseInt(h), parseInt(m))
    return date.toISOString()
  } catch { return new Date().toISOString() }
}

const confirmarReserva = async () => {
  if (!selectedDate.value || !selectedTime.value) return
  guardando.value = true

  if (!userStore.id) restaurarSesion()

  bookingStore.fecha = selectedDate.value
  bookingStore.hora  = selectedTime.value
  bookingStore.tieneCita = true

  const fechaISO = buildFechaISO(selectedDate.value, selectedTime.value)

  try {
    if (userStore.id) {
      const creada = await reservaApi.create({
        cliente:  { id: userStore.id },
        barbero:  { id: bookingStore.barberoId },
        servicio: { id: bookingStore.servicioId || 1 },
        fechaCita: fechaISO,
        estado: 'PENDIENTE',
        cantidadPagada: bookingStore.servicioPrecio || 10
      })
      bookingStore.reservaId = creada?.id ?? null
    }
  } catch (e) {
    console.warn('No se pudo guardar la reserva:', e)
  } finally {
    guardando.value = false
  }

  router.push('/metodo-pago')
}
</script>

<style scoped>
ion-page { --ion-safe-area-top: 0px; }
.page-bg { min-height: 100%; background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%); display: flex; flex-direction: column; }
.page-header { display: flex; align-items: center; gap: 16px; padding: 50px 24px 16px; }
h3 { color: rgba(255,255,255,0.7); font-size: 15px; margin: 0; font-weight: 400; }
.back-btn { background: rgba(255,255,255,0.08); border: none; border-radius: 10px; width: 40px; height: 40px; display: flex; align-items: center; justify-content: center; color: white; font-size: 20px; cursor: pointer; flex-shrink: 0; }
.page-content { flex: 1; padding: 0 20px; padding-bottom: 110px; }
.calendar-card { background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.08); border-radius: 20px; padding: 20px; margin-bottom: 16px; }
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
.selected-info-row { display: flex; gap: 10px; margin-bottom: 16px; flex-wrap: wrap; }
.info-chip { display: flex; align-items: center; gap: 7px; border-radius: 30px; padding: 10px 16px; font-size: 14px; font-family: 'DM Sans', sans-serif; font-weight: 500; }
.date-chip { background: rgba(74,143,232,0.15); border: 1px solid rgba(74,143,232,0.35); color: white; }
.placeholder-chip { background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.1); color: rgba(255,255,255,0.4); }
.time-chip { background: rgba(74,143,232,0.15); border: 1px solid rgba(74,143,232,0.35); color: white; cursor: pointer; }
.pick-time-chip { background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.15); color: rgba(255,255,255,0.6); cursor: pointer; }
.chip-edit { font-size: 13px; color: rgba(255,255,255,0.5); margin-left: 4px; }
.chip-arrow { font-size: 18px; color: rgba(255,255,255,0.4); margin-left: 2px; }
.summary-card { background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.08); border-radius: 16px; padding: 16px; margin-bottom: 16px; }
.summary-row { display: flex; align-items: center; gap: 10px; color: rgba(255,255,255,0.8); font-size: 14px; font-weight: 500; }
.summary-divider { height: 1px; background: rgba(255,255,255,0.07); margin: 12px 0; }
.price { color: white; font-weight: 700; margin-left: auto; font-size: 16px; }
.barber-thumb { width: 36px; height: 38px; border-radius: 8px; overflow: hidden; display: flex; align-items: center; justify-content: center; color: white; font-size: 12px; font-weight: 700; flex-shrink: 0; }
.barber-name-sm { flex: 1; color: white; font-weight: 500; }
.change-btn { background: rgba(255,255,255,0.07); border: 1px solid rgba(255,255,255,0.15); border-radius: 10px; color: rgba(255,255,255,0.7); padding: 8px 14px; font-size: 13px; font-weight: 500; cursor: pointer; font-family: 'DM Sans', sans-serif; }
.bottom-bar { position: fixed; bottom: 0; left: 0; right: 0; background: rgba(10,20,30,0.97); backdrop-filter: blur(12px); border-top: 1px solid rgba(255,255,255,0.08); padding: 16px 24px 32px; display: flex; align-items: center; justify-content: space-between; }
.bottom-info { display: flex; flex-direction: column; gap: 2px; }
.bottom-time { color: rgba(255,255,255,0.6); font-size: 13px; }
.bottom-price { color: white; font-size: 20px; font-weight: 700; }
.reserve-btn { background: #4a8fe8; border: none; border-radius: 12px; color: white; padding: 14px 24px; font-family: 'DM Sans', sans-serif; font-size: 15px; font-weight: 600; cursor: pointer; display: flex; align-items: center; }
.reserve-btn:disabled { opacity: 0.4; cursor: not-allowed; }
.sheet-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.6); z-index: 500; display: flex; align-items: flex-end; }
.time-sheet { background: #0f1e2e; border-radius: 28px 28px 0 0; width: 100%; padding: 12px 0 40px; border-top: 1px solid rgba(255,255,255,0.1); }
.sheet-handle { width: 40px; height: 4px; background: rgba(255,255,255,0.2); border-radius: 2px; margin: 0 auto 20px; }
.sheet-header { display: flex; align-items: center; justify-content: space-between; padding: 0 22px 18px; }
.sheet-title-wrap { display: flex; align-items: center; gap: 12px; }
.sheet-title { color: white; font-size: 17px; font-weight: 700; margin: 0 0 2px; font-family: 'Rajdhani', sans-serif; }
.sheet-sub { color: rgba(255,255,255,0.45); font-size: 13px; margin: 0; font-family: 'DM Sans', sans-serif; }
.sheet-close { background: rgba(255,255,255,0.08); border: none; border-radius: 50%; width: 32px; height: 32px; color: rgba(255,255,255,0.6); font-size: 14px; cursor: pointer; display: flex; align-items: center; justify-content: center; }
.sheet-body { padding: 0 22px; }
.period-section { margin-bottom: 20px; }
.period-header { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; }
.period-dot { width: 8px; height: 8px; border-radius: 50%; background: #4a8fe8; flex-shrink: 0; }
.period-name { color: rgba(255,255,255,0.5); font-size: 13px; font-weight: 600; letter-spacing: 0.5px; text-transform: uppercase; font-family: 'DM Sans', sans-serif; }
.hour-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; }
.hour-btn { background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.09); border-radius: 14px; padding: 13px 4px; color: rgba(255,255,255,0.75); font-size: 14px; font-weight: 500; cursor: pointer; font-family: 'DM Sans', sans-serif; transition: all 0.15s; }
.hour-btn:hover:not(.blocked) { background: rgba(74,143,232,0.15); border-color: rgba(74,143,232,0.3); }
.hour-btn.active { background: #4a8fe8; border-color: #4a8fe8; color: white; font-weight: 700; box-shadow: 0 4px 16px rgba(74,143,232,0.4); }
.hour-btn.blocked { background: rgba(255,255,255,0.02); border-color: rgba(255,255,255,0.04); cursor: not-allowed; }
.blocked-line { color: rgba(255,255,255,0.2); text-decoration: line-through; font-size: 13px; }
.avail-note { color: rgba(255,255,255,0.25); font-size: 12px; text-align: center; margin: 4px 0 0; font-family: 'DM Sans', sans-serif; }
.sheet-enter-active, .sheet-leave-active { transition: all 0.28s cubic-bezier(0.32, 0.72, 0, 1); }
.sheet-enter-from .time-sheet, .sheet-leave-to .time-sheet { transform: translateY(100%); }
.sheet-enter-from, .sheet-leave-to { background: rgba(0,0,0,0); }
</style>

<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="page-bg">
        <div class="page-content">

          <div class="success-circle">
            <svg class="check-svg" viewBox="0 0 52 52">
              <circle class="check-circle" cx="26" cy="26" r="24" fill="none" stroke="#4a8fe8" stroke-width="2.5"/>
              <path class="check-tick" fill="none" stroke="white" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" d="M14 26 l8 8 l16-16"/>
            </svg>
          </div>

          <h2>¡Reserva confirmada!</h2>
          <p class="sub">Tu cita ha sido registrada. ¡Hasta pronto!</p>

          <div class="cita-card">
            <div class="card-top">
              <div class="barber-thumb">
                <img :src="bookingStore.barberoImg || '/assets/Rectangle.png'" alt="" />
              </div>
              <div class="cita-info">
                <div class="cita-name">{{ bookingStore.servicioNombre || 'Corte de pelo' }}</div>
                <div class="cita-barber">{{ bookingStore.barberoNombre }}</div>
              </div>
              <div class="cita-price">{{ (bookingStore.servicioPrecio || 10).toFixed(2).replace('.', ',') }} €</div>
            </div>
            <div class="cita-date">📅 {{ bookingStore.fecha }} · {{ bookingStore.hora }}</div>
          </div>

          <button class="reminder-btn" @click="activarRecordatorio" :disabled="recordatorioActivado">
            <ion-icon :name="recordatorioActivado ? 'checkmark-circle-outline' : 'notifications-outline'"></ion-icon>
            {{ recordatorioActivado ? 'Recordatorio activado' : 'Activar recordatorio 24h antes' }}
          </button>

          <p v-if="notifError" class="notif-error">{{ notifError }}</p>

          <button class="home-btn" @click="cerrarYVolver">Ir al inicio</button>
        </div>

        <div class="modal-overlay" v-if="showModal" @click="cerrarYVolver">
          <div class="modal-card" @click.stop>
            <div class="modal-check">✓</div>
            <h3>¡Recordatorio activado!</h3>
            <p>Recibirás una notificación 24 horas antes de tu cita.</p>
            <p class="modal-sub">{{ bookingStore.fecha }} · {{ bookingStore.hora.split(' - ')[0] }} · {{ bookingStore.barberoNombre }}</p>
            <button class="modal-btn" @click="cerrarYVolver">Volver al inicio</button>
          </div>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import { notificationsOutline, checkmarkCircleOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { bookingStore } from '@/store/user'

addIcons({ 'notifications-outline': notificationsOutline, 'checkmark-circle-outline': checkmarkCircleOutline })

const showModal = ref(false)
const recordatorioActivado = ref(false)
const notifError = ref('')

onMounted(() => {
  bookingStore.tieneCita = true
  bookingStore.pagado = true
})

function buildFechaCita(): Date | null {
  try {
    const meses: Record<string, number> = {
      'Ene':0,'Feb':1,'Mar':2,'Abr':3,'May':4,'Jun':5,
      'Jul':6,'Ago':7,'Sep':8,'Oct':9,'Nov':10,'Dic':11
    }
    const fecha = bookingStore.fecha || ''
    const hora  = bookingStore.hora  || '09:00'
    const [diaStr, mesStr] = fecha.split(' ')
    const [h, m] = hora.split(':')
    const month = meses[mesStr]
    if (month === undefined) return null
    return new Date(new Date().getFullYear(), month, parseInt(diaStr), parseInt(h), parseInt(m))
  } catch { return null }
}

const activarRecordatorio = async () => {
  notifError.value = ''
  if (!('Notification' in window)) { notifError.value = 'Tu navegador no soporta notificaciones'; return }
  let permiso = Notification.permission
  if (permiso === 'default') permiso = await Notification.requestPermission()
  if (permiso !== 'granted') { notifError.value = 'Permiso de notificaciones denegado'; return }

  const fechaCita = buildFechaCita()
  if (!fechaCita) { notifError.value = 'No se pudo calcular la fecha de la cita'; return }

  new Notification('2LOOK · Recordatorio activado', {
    body: `Te avisaremos 24h antes de tu cita con ${bookingStore.barberoNombre} (${bookingStore.fecha} · ${bookingStore.hora})`,
    icon: '/assets/2look_letras.png',
  })

  const msHasta24h = fechaCita.getTime() - 24 * 60 * 60 * 1000 - Date.now()
  if (msHasta24h > 0) {
    setTimeout(() => {
      new Notification('2LOOK · ¡Tu cita es mañana!', {
        body: `Mañana tienes cita con ${bookingStore.barberoNombre} a las ${bookingStore.hora}. ¡No llegues tarde!`,
        icon: '/assets/2look_letras.png',
      })
    }, msHasta24h)
  }

  recordatorioActivado.value = true
  showModal.value = true
}

const cerrarYVolver = () => {
  showModal.value = false
  window.location.replace('/tabs/home')
}
</script>

<style scoped>
ion-page { --ion-safe-area-top: 0px; }
.page-bg { min-height: 100%; background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%); display: flex; flex-direction: column; }
.page-content { flex: 1; padding: 60px 24px 40px; display: flex; flex-direction: column; align-items: center; }

.success-circle { width: 90px; height: 90px; margin-bottom: 24px; }
.check-svg { width: 100%; height: 100%; }
.check-circle { stroke-dasharray: 160; stroke-dashoffset: 160; animation: drawCircle 0.6s ease forwards; }
.check-tick { stroke-dasharray: 50; stroke-dashoffset: 50; animation: drawTick 0.4s ease 0.5s forwards; }
@keyframes drawCircle { to { stroke-dashoffset: 0; } }
@keyframes drawTick   { to { stroke-dashoffset: 0; } }

h2 { font-family: 'Rajdhani', sans-serif; font-size: 28px; font-weight: 700; color: white; margin: 0 0 8px; text-align: center; }
.sub { color: rgba(255,255,255,0.45); font-size: 14px; margin: 0 0 28px; text-align: center; font-family: 'DM Sans', sans-serif; }

.cita-card { background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.1); border-radius: 16px; padding: 16px; margin-bottom: 16px; width: 100%; }
.card-top { display: flex; align-items: center; gap: 12px; margin-bottom: 10px; }
.barber-thumb { width: 46px; height: 46px; border-radius: 50%; overflow: hidden; flex-shrink: 0; }
.barber-thumb img { width: 100%; height: 100%; object-fit: cover; }
.cita-info { flex: 1; display: flex; flex-direction: column; gap: 2px; min-width: 0; }
.cita-name { color: white; font-size: 15px; font-weight: 600; }
.cita-barber { color: rgba(255,255,255,0.5); font-size: 13px; }
.cita-price { color: white; font-size: 17px; font-weight: 700; white-space: nowrap; flex-shrink: 0; align-self: flex-start; }
.cita-date { color: rgba(255,255,255,0.45); font-size: 13px; text-align: right; font-family: 'DM Sans', sans-serif; }

.reminder-btn { width: 100%; background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.1); border-radius: 14px; color: white; padding: 16px; font-family: 'DM Sans', sans-serif; font-size: 14px; font-weight: 500; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 10px; margin-bottom: 12px; transition: background 0.2s; }
.reminder-btn:not(:disabled):hover { background: rgba(255,255,255,0.1); }
.reminder-btn ion-icon { font-size: 20px; color: #4a8fe8; }
.reminder-btn:disabled { opacity: 0.6; cursor: default; }
.notif-error { color: #ff6b6b; font-size: 13px; text-align: center; margin: 0 0 12px; font-family: 'DM Sans', sans-serif; }

.home-btn { width: 100%; background: #4a8fe8; border: none; border-radius: 14px; color: white; padding: 16px; font-family: 'DM Sans', sans-serif; font-size: 15px; font-weight: 600; cursor: pointer; margin-top: 4px; }

.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.65); display: flex; align-items: center; justify-content: center; z-index: 1000; padding: 24px; }
.modal-card { background: #1a2d42; border: 1px solid rgba(255,255,255,0.12); border-radius: 24px; padding: 32px 24px; width: 100%; max-width: 320px; text-align: center; }
.modal-check { font-size: 48px; color: #4a8fe8; font-weight: 700; margin-bottom: 12px; }
h3 { font-family: 'Rajdhani', sans-serif; font-size: 24px; color: white; margin: 0 0 12px; font-weight: 700; }
.modal-card p { color: rgba(255,255,255,0.7); font-size: 14px; line-height: 1.6; margin: 0 0 10px; font-family: 'DM Sans', sans-serif; }
.modal-sub { color: rgba(255,255,255,0.45); font-size: 13px; }
.modal-btn { width: 100%; margin-top: 20px; background: linear-gradient(135deg, #2d5986, #1a3a5c); border: none; border-radius: 12px; color: white; padding: 14px; font-family: 'DM Sans', sans-serif; font-size: 15px; font-weight: 600; cursor: pointer; }
</style>

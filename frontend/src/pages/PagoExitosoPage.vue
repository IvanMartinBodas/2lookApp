<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="page-bg">
        <div class="page-content">
          <div class="logo-area">
            <img src="/assets/2look_letras.png" style="width:130px" alt="2LOOK" />
          </div>

          <h2>Citas confirmadas</h2>

          <div class="cita-card">
            <div class="card-top">
              <div class="barber-thumb">
                <img :src="bookingStore.barberoImg || '/assets/Rectangle.png'" alt="" />
              </div>
              <div class="cita-info">
                <div class="cita-name">Corte de pelo {{ bookingStore.barberoNombre }}</div>
                <div class="cita-price">10,00 €</div>
              </div>
            </div>
            <div class="cita-date">{{ bookingStore.fecha }} · {{ bookingStore.hora }}</div>
          </div>

          <button class="reminder-btn" @click="activarRecordatorio">
            <ion-icon name="notifications-outline"></ion-icon>
            Activar recordatorio
          </button>
        </div>

        <div class="modal-overlay" v-if="showModal" @click="cerrarYVolver">
          <div class="modal-card" @click.stop>
            <div class="modal-icon">🔔</div>
            <h3>¡Recordatorio activado!</h3>
            <p>Te enviaremos una notificación 24 horas antes de tu cita para que no te olvides.</p>
            <p class="modal-sub">📅 {{ bookingStore.fecha }} · {{ bookingStore.hora }} · {{ bookingStore.barberoNombre }}</p>
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
import { notificationsOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'
import { bookingStore } from '@/store/user'

addIcons({ 'notifications-outline': notificationsOutline })

const router = useRouter()
const showModal = ref(false)

onMounted(() => {
  bookingStore.tieneCita = true
  bookingStore.pagado = true
})

const activarRecordatorio = () => {
  showModal.value = true
}

const cerrarYVolver = () => {
  showModal.value = false
  router.replace('/tabs/home')
}
</script>

<style scoped>
ion-page { --ion-safe-area-top: 0px; }
.page-bg { min-height: 100%; background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%); }
.page-content { padding: 50px 24px 40px; }
.logo-area { display: flex; justify-content: center; margin-bottom: 28px; }
h2 { font-family: 'Rajdhani', sans-serif; font-size: 30px; font-weight: 700; color: white; margin: 0 0 24px; }
.cita-card { background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.1); border-radius: 16px; padding: 16px; margin-bottom: 24px; }
.card-top { display: flex; align-items: center; gap: 12px; margin-bottom: 10px; }
.barber-thumb { width: 46px; height: 46px; border-radius: 50%; overflow: hidden; flex-shrink: 0; }
.barber-thumb img { width: 100%; height: 100%; object-fit: cover; }
.cita-info { flex: 1; display: flex; justify-content: space-between; align-items: center; }
.cita-name { color: white; font-size: 15px; font-weight: 600; }
.cita-price { color: white; font-size: 17px; font-weight: 700; }
.cita-date { color: rgba(255,255,255,0.5); font-size: 13px; text-align: right; }
.reminder-btn { width: 100%; background: #1a2535; border: none; border-radius: 14px; color: white; padding: 16px; font-family: 'DM Sans', sans-serif; font-size: 15px; font-weight: 500; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 10px; }
.reminder-btn ion-icon { font-size: 20px; }
.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.65); display: flex; align-items: center; justify-content: center; z-index: 1000; padding: 24px; }
.modal-card { background: #1a2d42; border: 1px solid rgba(255,255,255,0.12); border-radius: 24px; padding: 32px 24px; width: 100%; max-width: 320px; text-align: center; }
.modal-icon { font-size: 48px; margin-bottom: 16px; }
h3 { font-family: 'Rajdhani', sans-serif; font-size: 24px; color: white; margin: 0 0 12px; font-weight: 700; }
.modal-card p { color: rgba(255,255,255,0.7); font-size: 14px; line-height: 1.6; margin: 0 0 10px; font-family: 'DM Sans', sans-serif; }
.modal-sub { color: rgba(255,255,255,0.5); font-size: 13px; }
.modal-btn { width: 100%; margin-top: 20px; background: linear-gradient(135deg, #2d5986, #1a3a5c); border: none; border-radius: 12px; color: white; padding: 14px; font-family: 'DM Sans', sans-serif; font-size: 15px; font-weight: 600; cursor: pointer; }
</style>
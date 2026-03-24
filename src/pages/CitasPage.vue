<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="page-bg">
        <div class="page-header">
          <img src="/assets/2look_letras.png" style="width:140px" alt="2LOOK" />
        </div>

        <div class="page-content">
          <h2>Tienes una cita assignada</h2>

          <div class="cita-card" v-if="cita">
            <div class="cita-header">
              <div class="barber-thumb">
                <img :src="cita.barberoImg || '/assets/Rectangle.png'" alt="" />
              </div>
              <div class="cita-info">
                <div class="cita-name">{{ cita.service }} {{ cita.barber }}</div>
              </div>
              <div class="cita-price">{{ cita.price }}</div>
            </div>
            <div class="cita-date">{{ bookingStore.fecha }} {{ bookingStore.hora }}</div>
            <div class="cita-actions">
              <button class="btn-outline" @click="showCancelModal = true">Cancelar</button>
              <button class="btn-primary" v-if="!bookingStore.pagado" @click="router.push('/metodo-pago')">Pagar</button>
              <div v-else class="pagado-badge">Pagado</div>
            </div>
          </div>
        </div>

        <div class="modal-overlay" v-if="showCancelModal" @click="showCancelModal = false">
          <div class="modal-card" @click.stop>
            <p>¿Seguro que quieres cancelar la cita?</p>
            <button class="btn-primary" @click="cancelCita">Sí</button>
            <button class="btn-outline" @click="showCancelModal = false">No</button>
          </div>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { IonPage, IonContent } from '@ionic/vue'
import { useRouter } from 'vue-router'
import { bookingStore } from '@/store/user'

const router = useRouter()
const showCancelModal = ref(false)
const cita = ref<any>(null)

watch(() => bookingStore.tieneCita, (val) => {
  if (val) {
    cita.value = {
      service: 'Corte de pelo',
      barber: bookingStore.barberoNombre,
      price: '10,00 €',
      date: '19 Dic 18:30 - 19:00',
      barberoImg: bookingStore.barberoImg,
    }
  } else {
    cita.value = null
  }
}, { immediate: true })

const cancelCita = () => {
  showCancelModal.value = false
  cita.value = null
  bookingStore.tieneCita = false
  bookingStore.pagado = false
}
</script>

<style scoped>
ion-page { --ion-safe-area-top: 0px; }
ion-content { --overflow: scroll; }

.page-bg {
  min-height: 100%;
  background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%);
  padding-bottom: 40px;
}
.page-header {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 50px 0px 20px;
}
.page-content { padding: 0 24px; }
h2 {
  font-family: 'Rajdhani', sans-serif;
  font-size: 26px;
  color: white;
  margin: 0 0 20px;
  font-weight: 600;
}
.cita-card {
  background: rgba(255,255,255,0.06);
  border: 1px solid rgba(255,255,255,0.1);
  border-radius: 16px;
  padding: 16px;
}
.cita-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}
.barber-thumb {
  width: 46px;
  height: 46px;
  border-radius: 10px;
  overflow: hidden;
  flex-shrink: 0;
  background: #1a2d42;
}
.barber-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.cita-info { flex: 1; }
.cita-name {
  color: white;
  font-weight: 600;
  font-size: 15px;
  font-family: 'DM Sans', sans-serif;
}
.cita-price {
  color: white;
  font-weight: 700;
  font-size: 16px;
  font-family: 'DM Sans', sans-serif;
  white-space: nowrap;
}
.cita-date {
  color: rgba(255,255,255,0.5);
  font-size: 13px;
  text-align: right;
  margin-bottom: 16px;
  font-family: 'DM Sans', sans-serif;
}
.cita-actions {
  display: flex;
  gap: 12px;
}
.btn-outline {
  flex: 1;
  background: transparent;
  border: 1px solid rgba(255,255,255,0.25);
  border-radius: 10px;
  color: white;
  padding: 12px;
  font-family: 'DM Sans', sans-serif;
  font-size: 14px;
  cursor: pointer;
}
.btn-primary {
  flex: 1;
  background: #1a3a5c;
  border: none;
  border-radius: 10px;
  color: white;
  padding: 12px;
  font-family: 'DM Sans', sans-serif;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}
.modal-card {
  background: #1a2d42;
  border: 1px solid rgba(255,255,255,0.1);
  border-radius: 20px;
  padding: 28px 24px;
  width: 100%;
  max-width: 300px;
  text-align: center;
}
.modal-card p {
  color: white;
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 20px;
  font-family: 'DM Sans', sans-serif;
}
.modal-card button {
  width: 100%;
  margin-bottom: 10px;
}
.pagado-badge { 
  flex: 1; 
  text-align: center; 
  color: white; 
  font-size: 14px; 
  font-weight: 600; 
  background: rgba(255,255,255,0.08); 
  border: 1px solid rgba(255,255,255,0.15); 
  border-radius: 10px;
  padding: 12px;
}
</style>
<template>
  <ion-page>
    <ion-content :fullscreen="true" :scroll-y="true">
      <div class="page-bg">
        <div class="page-content">
          <h1>Historial de cortes</h1>
          <p class="subtitle">Aqui puedes ver el historial de todas tus citas</p>
          <span class="section-label">Citas completadas</span>

          <div class="historial-list">
            <div class="historial-card" v-for="cita in historial" :key="cita.id">
              <img :src="cita.img" class="barber-img" alt="" />
              <div class="card-info">
                <div class="barber-name">{{ cita.barber }}</div>
                <button class="resena-btn" @click="abrirResena(cita)">Reseña</button>
              </div>
              <div class="card-date">
                <ion-icon
                  :name="cita.liked ? 'heart' : 'heart-outline'"
                  class="heart"
                  :style="{ color: cita.liked ? '#eb445a' : 'rgba(255,255,255,0.4)' }"
                  @click="cita.liked = !cita.liked"
                ></ion-icon>
                <span class="mes">{{ cita.mes }}</span>
                <span class="dia">{{ cita.dia }}</span>
                <span class="hora">{{ cita.hora }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="modal-overlay" v-if="showModal" @click="cerrarModal">
        <div class="modal-card" @click.stop>
          <div v-if="!enviado">
            <h3>Valorar servicio</h3>
            <p class="modal-sub">{{ citaSeleccionada?.barber }}</p>
            <div class="stars-row">
              <span
                v-for="i in 5"
                :key="i"
                class="star"
                :class="{ filled: i <= estrellas }"
                @click="estrellas = i"
              >★</span>
            </div>
            <textarea
              v-model="comentario"
              class="resena-input"
              placeholder="Escribe tu reseña aquí..."
              rows="4"
            ></textarea>
            <button class="modal-btn" @click="enviarResena">Enviar reseña</button>
            <button class="modal-cancel" @click="cerrarModal">Cancelar</button>
          </div>
          <div v-else class="enviado-content">
            <div class="enviado-icon">✅</div>
            <h3>¡Reseña enviada!</h3>
            <p>Se ha enviado tu valoración a <strong>{{ citaSeleccionada?.barber }}</strong></p>
            <div class="stars-row">
              <span v-for="i in 5" :key="i" class="star" :class="{ filled: i <= estrellas }">★</span>
            </div>
            <button class="modal-btn" @click="cerrarModal">Cerrar</button>
          </div>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import { heart, heartOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'

addIcons({ heart, 'heart-outline': heartOutline })

const historial = ref([
  { id: 1, barber: 'Charles Smith', img: '/assets/Rectangle.png', mes: 'Octubre', dia: '29', hora: '12:00', liked: true },
  { id: 2, barber: 'Charles Smith', img: '/assets/Rectangle.png', mes: 'Noviembre', dia: '25', hora: '10:30', liked: true },
  { id: 3, barber: 'Marc Andrew', img: '/assets/Rectangle__4.png', mes: 'Octubre', dia: '2', hora: '15:30', liked: false },
  { id: 4, barber: 'Charles Smith', img: '/assets/Rectangle.png', mes: 'Septiembre', dia: '13', hora: '19:30', liked: true },
  { id: 5, barber: 'Richard Jones', img: '/assets/Rectangle__5.png', mes: 'Agosto', dia: '8', hora: '11:00', liked: false },
  { id: 6, barber: 'Marc Andrew', img: '/assets/Rectangle__4.png', mes: 'Julio', dia: '21', hora: '09:30', liked: true },
  { id: 7, barber: 'Charles Smith', img: '/assets/Rectangle.png', mes: 'Julio', dia: '5', hora: '17:00', liked: false },
  { id: 8, barber: 'Richard Jones', img: '/assets/Rectangle__5.png', mes: 'Junio', dia: '18', hora: '14:30', liked: true },
])

const showModal = ref(false)
const enviado = ref(false)
const estrellas = ref(0)
const comentario = ref('')
const citaSeleccionada = ref<any>(null)

const abrirResena = (cita: any) => {
  citaSeleccionada.value = cita
  estrellas.value = 0
  comentario.value = ''
  enviado.value = false
  showModal.value = true
}

const enviarResena = () => {
  if (estrellas.value === 0) return
  enviado.value = true
}

const cerrarModal = () => {
  showModal.value = false
  enviado.value = false
}
</script>

<style scoped>
ion-page { --ion-safe-area-top: 0px; }
.page-bg { min-height: 100%; background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%); }
.page-content { padding: 50px 20px 40px; }
h1 { font-family: 'Rajdhani', sans-serif; font-size: 32px; font-weight: 700; color: white; margin: 0 0 6px; }
.subtitle { color: rgba(255,255,255,0.5); font-size: 14px; margin: 0 0 8px; }
.section-label { color: rgba(255,255,255,0.4); font-size: 13px; display: block; margin-bottom: 20px; }
.historial-list { display: flex; flex-direction: column; gap: 14px; }
.historial-card { background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.1); border-radius: 16px; display: flex; align-items: center; gap: 14px; padding: 12px; overflow: hidden; }
.barber-img { width: 90px; height: 90px; border-radius: 10px; object-fit: cover; flex-shrink: 0; }
.card-info { flex: 1; }
.barber-name { color: white; font-size: 17px; font-weight: 700; margin-bottom: 10px; font-family: 'Rajdhani', sans-serif; }
.resena-btn { background: #1a2d42; border: 1px solid rgba(255,255,255,0.12); border-radius: 20px; color: white; padding: 8px 20px; font-family: 'DM Sans', sans-serif; font-size: 13px; cursor: pointer; }
.card-date { display: flex; flex-direction: column; align-items: center; gap: 2px; min-width: 70px; }
.heart { font-size: 20px; cursor: pointer; margin-bottom: 4px; }
.mes { color: rgba(255,255,255,0.6); font-size: 12px; }
.dia { color: white; font-size: 28px; font-weight: 700; font-family: 'Rajdhani', sans-serif; line-height: 1; }
.hora { color: rgba(255,255,255,0.6); font-size: 12px; }
.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.65); display: flex; align-items: center; justify-content: center; z-index: 1000; padding: 24px; }
.modal-card { background: #1a2d42; border: 1px solid rgba(255,255,255,0.12); border-radius: 24px; padding: 28px 24px; width: 100%; max-width: 320px; text-align: center; }
h3 { font-family: 'Rajdhani', sans-serif; font-size: 24px; color: white; margin: 0 0 6px; font-weight: 700; }
.modal-sub { color: rgba(255,255,255,0.5); font-size: 14px; margin: 0 0 16px; }
.stars-row { display: flex; justify-content: center; gap: 8px; margin-bottom: 16px; }
.star { font-size: 32px; color: rgba(255,255,255,0.2); cursor: pointer; transition: color 0.2s; }
.star.filled { color: #f5c518; }
.resena-input { width: 100%; background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.1); border-radius: 12px; color: white; padding: 12px; font-family: 'DM Sans', sans-serif; font-size: 14px; resize: none; box-sizing: border-box; margin-bottom: 16px; }
.resena-input::placeholder { color: rgba(255,255,255,0.3); }
.modal-btn { width: 100%; background: linear-gradient(135deg, #2d5986, #1a3a5c); border: none; border-radius: 12px; color: white; padding: 14px; font-family: 'DM Sans', sans-serif; font-size: 15px; font-weight: 600; cursor: pointer; margin-bottom: 10px; }
.modal-cancel { width: 100%; background: transparent; border: 1px solid rgba(255,255,255,0.2); border-radius: 12px; color: white; padding: 12px; font-family: 'DM Sans', sans-serif; font-size: 14px; cursor: pointer; }
.enviado-content { display: flex; flex-direction: column; align-items: center; }
.enviado-icon { font-size: 48px; margin-bottom: 12px; }
.enviado-content p { color: rgba(255,255,255,0.7); font-size: 14px; margin: 0 0 16px; }
.enviado-content strong { color: white; }
</style>
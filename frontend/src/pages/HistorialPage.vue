<template>
  <ion-page>
    <ion-content :fullscreen="true" :scroll-y="true">
      <div class="page-bg">
        <div class="page-content">
          <h1>Historial de cortes</h1>
          <p class="subtitle">Aquí puedes ver el historial de todas tus citas</p>
          <span class="section-label">Todas tus visitas</span>

          <div v-if="loading" style="display:flex;justify-content:center;padding:40px 0">
            <ion-spinner name="circular" color="light"></ion-spinner>
          </div>

          <div v-else-if="historial.length === 0" style="display:none"></div>

          <div class="historial-list" v-else>
            <div class="historial-card" v-for="cita in historial" :key="cita.id">
              <img :src="cita.img" class="barber-img" alt="" />
              <div class="card-info">
                <div class="barber-name">{{ cita.barber }}</div>
                <div class="servicio-name">{{ cita.servicio }}</div>
                <button class="resena-btn" @click="abrirResena(cita)">Valorar</button>
              </div>
              <div class="card-date">
                <ion-icon
                  :name="cita.liked ? 'heart' : 'heart-outline'"
                  class="heart"
                  :style="{ color: cita.liked ? '#eb445a' : 'rgba(255,255,255,0.4)' }"
                  @click="toggleLike(cita)"
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
            <button class="modal-btn" :disabled="enviando" @click="enviarResena">
              {{ enviando ? 'Enviando...' : 'Enviar reseña' }}
            </button>
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
import { ref, onMounted } from 'vue'
import { IonPage, IonContent, IonIcon, IonSpinner } from '@ionic/vue'
import { heart, heartOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { userStore } from '@/store/user'
import { reservaApi, opinionApi } from '@/store/api'

function getFotoPorNombre(nombre: string): string {
  const n = nombre.toLowerCase()
  if (n.includes('charles')) return '/assets/Rectangle.png'
  if (n.includes('richard')) return '/assets/Rectangle__5.png'
  if (n.includes('marc'))    return '/assets/Rectangle__4.png'
  return '/assets/Rectangle.png'
}

addIcons({ heart, 'heart-outline': heartOutline })

const loading = ref(true)
const historial = ref<any[]>([])

const showModal = ref(false)
const enviado = ref(false)
const enviando = ref(false)
const estrellas = ref(0)
const comentario = ref('')
const citaSeleccionada = ref<any>(null)

const mesesNombres = ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre']

function getLikedIds(): Set<number> {
  try {
    const raw = localStorage.getItem('2look_liked_' + userStore.id)
    return raw ? new Set(JSON.parse(raw)) : new Set()
  } catch { return new Set() }
}

function saveLikedIds(ids: Set<number>) {
  localStorage.setItem('2look_liked_' + userStore.id, JSON.stringify([...ids]))
}

function toggleLike(cita: any) {
  cita.liked = !cita.liked
  const ids = getLikedIds()
  if (cita.liked) ids.add(cita.id)
  else ids.delete(cita.id)
  saveLikedIds(ids)
}

onMounted(async () => {
  const likedIds = getLikedIds()

  try {
    if (userStore.id) {
      const data = await reservaApi.getByCliente(userStore.id)
      const completadas = data.filter((r: any) => r.estado === 'COMPLETADA' || r.estado === 'PAGADA')
      historial.value = completadas.map((r: any) => {
        const iso = r.fechaCita?.endsWith('Z') ? r.fechaCita : r.fechaCita + 'Z'
        const d = new Date(iso)
        return {
          id: r.id,
          barber: r.barbero?.persona?.nombre || 'Barbero',
          servicio: r.servicio?.nombre || 'Corte de pelo',
          img: r.barbero?.fotoUrl || getFotoPorNombre(r.barbero?.persona?.nombre || ''),
          mes: mesesNombres[d.getMonth()],
          dia: d.getDate().toString(),
          hora: `${d.getHours().toString().padStart(2,'0')}:${d.getMinutes().toString().padStart(2,'0')}`,
          liked: likedIds.has(r.id),
          barberoId: r.barbero?.id,
        }
      })
    }
  } catch { /* vacío */ }

  if (historial.value.length === 0) {
    historial.value = [
      { id:  1, barber: 'Charles Smith', servicio: 'Taper Fade',        img: '/assets/Rectangle.png',    mes: 'Mayo',      dia: '3',  hora: '10:00', liked: likedIds.has(1),  barberoId: null },
      { id:  2, barber: 'Charles Smith', servicio: 'Taper Fade',        img: '/assets/Rectangle.png',    mes: 'Abril',     dia: '5',  hora: '11:00', liked: likedIds.has(2),  barberoId: null },
      { id:  3, barber: 'Charles Smith', servicio: 'Taper Fade',        img: '/assets/Rectangle.png',    mes: 'Marzo',     dia: '8',  hora: '10:30', liked: likedIds.has(3),  barberoId: null },
      { id:  4, barber: 'Richard Jones', servicio: 'Low Fade',          img: '/assets/Rectangle__5.png', mes: 'Febrero',   dia: '10', hora: '09:00', liked: likedIds.has(4),  barberoId: null },
      { id:  5, barber: 'Richard Jones', servicio: 'Low Fade',          img: '/assets/Rectangle__5.png', mes: 'Enero',     dia: '13', hora: '17:00', liked: likedIds.has(5),  barberoId: null },
      { id:  6, barber: 'Charles Smith', servicio: 'Burst Fade',        img: '/assets/Rectangle.png',    mes: 'Diciembre', dia: '9',  hora: '11:00', liked: likedIds.has(6),  barberoId: null },
      { id:  7, barber: 'Marc Andrew',   servicio: 'Corte Clásico',     img: '/assets/Rectangle__4.png', mes: 'Noviembre', dia: '11', hora: '09:30', liked: likedIds.has(7),  barberoId: null },
      { id:  8, barber: 'Marc Andrew',   servicio: 'Corte Texturizado', img: '/assets/Rectangle__4.png', mes: 'Octubre',   dia: '14', hora: '16:00', liked: likedIds.has(8),  barberoId: null },
      { id:  9, barber: 'Charles Smith', servicio: 'Drop Fade',         img: '/assets/Rectangle.png',    mes: 'Septiembre',dia: '8',  hora: '10:00', liked: likedIds.has(9),  barberoId: null },
      { id: 10, barber: 'Charles Smith', servicio: 'Taper Fade',        img: '/assets/Rectangle.png',    mes: 'Agosto',    dia: '12', hora: '12:00', liked: likedIds.has(10), barberoId: null },
    ]
  }

  loading.value = false
})

const abrirResena = (cita: any) => {
  citaSeleccionada.value = cita
  estrellas.value = 0
  comentario.value = ''
  enviado.value = false
  showModal.value = true
}

const enviarResena = async () => {
  if (estrellas.value === 0) return
  enviando.value = true
  try {
    if (userStore.id && citaSeleccionada.value?.barberoId) {
      await opinionApi.create({
        cliente: { id: userStore.id },
        barbero: { id: citaSeleccionada.value.barberoId },
        puntuacion: estrellas.value,
        comentario: comentario.value,
      })
    }
  } catch { }
  enviando.value = false
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
.empty-state { text-align: center; padding: 60px 0; color: rgba(255,255,255,0.3); font-family: 'DM Sans', sans-serif; font-size: 14px; }
.historial-list { display: flex; flex-direction: column; gap: 14px; }
.historial-card { background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.1); border-radius: 16px; display: flex; align-items: center; gap: 14px; padding: 12px; overflow: hidden; }
.barber-img { width: 90px; height: 90px; border-radius: 10px; object-fit: cover; flex-shrink: 0; }
.card-info { flex: 1; }
.barber-name { color: white; font-size: 17px; font-weight: 700; margin-bottom: 10px; font-family: 'Rajdhani', sans-serif; }
.servicio-name { color: rgba(255,255,255,0.5); font-size: 13px; font-family: 'DM Sans', sans-serif; margin-bottom: 8px; }
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
.modal-btn:disabled { opacity: 0.5; cursor: not-allowed; }
.modal-cancel { width: 100%; background: transparent; border: 1px solid rgba(255,255,255,0.2); border-radius: 12px; color: white; padding: 12px; font-family: 'DM Sans', sans-serif; font-size: 14px; cursor: pointer; }
.enviado-content { display: flex; flex-direction: column; align-items: center; }
.enviado-icon { font-size: 48px; margin-bottom: 12px; }
.enviado-content p { color: rgba(255,255,255,0.7); font-size: 14px; margin: 0 0 16px; }
.enviado-content strong { color: white; }
</style>
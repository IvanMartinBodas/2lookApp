<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="page-bg">
        <div class="page-header">
          <img src="/assets/2look_letras.png" style="width:140px" alt="2LOOK" />
        </div>

        <div class="page-content">
          <h2>Mis citas</h2>

          <div v-if="loading" style="display:flex;justify-content:center;padding:40px 0">
            <ion-spinner name="circular" color="light"></ion-spinner>
          </div>

          <div v-else-if="citas.length === 0" class="empty-state">
            <p>No tienes citas próximas</p>
          </div>

          <div v-else>
            <div class="cita-card" v-for="cita in citas" :key="cita.id">
              <div class="cita-header">
                <div class="barber-thumb">
                  <img :src="cita.barberoImg || '/assets/Rectangle.png'" alt="" />
                </div>
                <div class="cita-info">
                  <div class="cita-name">{{ cita.servicio }} · {{ cita.barberoNombre }}</div>
                  <div class="cita-estado" :class="cita.estado.toLowerCase()">{{ cita.estado }}</div>
                </div>
                <div class="cita-price">{{ cita.precio }}</div>
              </div>
              <div class="cita-date">{{ cita.fecha }}</div>
              <div class="cita-actions">
                <button class="btn-outline" @click="confirmarCancelar(cita)">Cancelar</button>
                <button class="btn-primary" v-if="cita.estado !== 'PAGADA'" @click="pagar(cita)">Pagar</button>
                <div v-else class="pagado-badge">Pagado</div>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-overlay" v-if="showCancelModal" @click="showCancelModal = false">
          <div class="modal-card" @click.stop>
            <p>¿Cancelar esta cita? No se podrá deshacer.</p>
            <button class="btn-primary" @click="cancelCita">Sí</button>
            <button class="btn-outline" @click="showCancelModal = false">No</button>
          </div>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { IonPage, IonContent, IonSpinner, onIonViewWillEnter } from '@ionic/vue'
import { useRouter } from 'vue-router'
import { bookingStore, userStore, restaurarSesion } from '@/store/user'
import { reservaApi } from '@/store/api'

const router = useRouter()
const showCancelModal = ref(false)
const loading = ref(true)
const citas = ref<any[]>([])
const citaACancelar = ref<any>(null)

const meses = ['Ene','Feb','Mar','Abr','May','Jun','Jul','Ago','Sep','Oct','Nov','Dic']

function getFotoPorNombre(nombre: string): string {
  const n = nombre.toLowerCase()
  if (n.includes('charles')) return '/assets/Rectangle.png'
  if (n.includes('richard')) return '/assets/Rectangle__5.png'
  if (n.includes('marc'))    return '/assets/Rectangle__4.png'
  return '/assets/Rectangle.png'
}

function formatFecha(fechaCita: string): string {
  try {
    // Añadir Z para que JS trate como UTC y muestre hora local correcta
    const isoUtc = fechaCita.endsWith('Z') ? fechaCita : fechaCita + 'Z'
    const d = new Date(isoUtc)
    return `${d.getDate()} ${meses[d.getMonth()]} · ${d.getHours().toString().padStart(2,'0')}:${d.getMinutes().toString().padStart(2,'0')}`
  } catch {
    return fechaCita
  }
}

async function cargarCitas() {
  loading.value = true
  if (!userStore.id) restaurarSesion()
  try {
    if (userStore.id) {
      const data = await reservaApi.getByCliente(userStore.id)
      const activas = data.filter((r: any) => r.estado !== 'CANCELADA')
      citas.value = activas.map((r: any) => ({
        id: r.id,
        barberoNombre: r.barbero?.persona?.nombre || 'Barbero',
        barberoImg: r.barbero?.fotoUrl || getFotoPorNombre(r.barbero?.persona?.nombre || ''),
        servicio: r.servicio?.nombre || 'Corte de pelo',
        precio: r.cantidadPagada ? `${Number(r.cantidadPagada).toFixed(2)} €` : (r.servicio?.precio ? `${Number(r.servicio.precio).toFixed(2)} €` : '10,00 €'),
        fecha: formatFecha(r.fechaCita),
        estado: r.estado,
        reservaId: r.id,
        fromBackend: true,
      }))
    }
  } catch {
    citas.value = []
  }

  loading.value = false
}

onMounted(cargarCitas)
onIonViewWillEnter(cargarCitas)

function confirmarCancelar(cita: any) {
  citaACancelar.value = cita
  showCancelModal.value = true
}

async function cancelCita() {
  showCancelModal.value = false
  const cita = citaACancelar.value
  if (!cita) return

  try {
    await reservaApi.updateEstado(cita.reservaId, 'CANCELADA')
  } catch { }
  citas.value = citas.value.filter(c => c.id !== cita.id)
}

function pagar(cita: any) {
  bookingStore.barberoNombre = cita.barberoNombre
  bookingStore.barberoImg = cita.barberoImg || '/assets/Rectangle.png'
  bookingStore.servicioNombre = cita.servicio || 'Corte de pelo'
  bookingStore.servicioPrecio = parseFloat(cita.precio) || 10
  // cita.fecha tiene formato "8 May · 18:30" — separar en fecha y hora
  const partes = (cita.fecha || '').split(' · ')
  bookingStore.fecha = partes[0] || cita.fecha
  bookingStore.hora  = partes[1] || ''
  bookingStore.reservaId = cita.fromBackend ? cita.reservaId : null
  router.push('/metodo-pago')
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
  font-size: 30px;
  color: white;
  margin: 0 0 20px;
  font-weight: 700;
}
.empty-state {
  text-align: center;
  padding: 60px 0;
  color: rgba(255,255,255,0.35);
  font-family: 'DM Sans', sans-serif;
  font-size: 15px;
}
.cita-card {
  background: rgba(255,255,255,0.06);
  border: 1px solid rgba(255,255,255,0.1);
  border-radius: 16px;
  padding: 16px;
  margin-bottom: 14px;
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
.cita-estado {
  font-size: 12px;
  margin-top: 3px;
  font-family: 'DM Sans', sans-serif;
  text-transform: capitalize;
}
.cita-estado.pendiente { color: #f5c518; }
.cita-estado.confirmada { color: #2dd36f; }
.cita-estado.pagado { color: #2dd36f; }
.cita-estado.pagada { color: #2dd36f; }
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
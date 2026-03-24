<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="page-bg">
        <div class="page-header">
          <button class="back-btn" @click="router.back()">
            <ion-icon name="arrow-back-outline"></ion-icon>
          </button>
        </div>
        <div class="page-content">
          <h2>Elige tu Barbero</h2>
          <p class="subtitle">Selecciona uno de los barberos disponibles a continuación.</p>

          <div
            v-for="barber in barbers"
            :key="barber.id"
            :class="['barber-card', selected === barber.id ? 'selected' : '']"
            @click="seleccionar(barber)"
          >
            <div class="barber-avatar">
              <img :src="barber.img" style="width:100%; height:100%; object-fit:cover; border-radius:10px" alt="" />
            </div>
            <div class="barber-info">
              <div class="barber-name">{{ barber.name }}</div>
              <div class="barber-stars">
                <span v-for="i in 5" :key="i" :class="['star', i <= barber.rating ? 'filled' : '']">★</span>
                <span class="rating-txt">({{ barber.ratingText }})</span>
              </div>
            </div>
            <div class="select-indicator">
              <ion-icon :name="selected === barber.id ? 'checkmark-circle' : 'ellipse-outline'"></ion-icon>
            </div>
          </div>

          <button class="btn-primary" style="margin-top: 32px" :disabled="!selected" @click="continuar">
            Continuar
          </button>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import { arrowBackOutline, checkmarkCircle, ellipseOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'
import { bookingStore } from '@/store/user'

addIcons({ 'arrow-back-outline': arrowBackOutline, 'checkmark-circle': checkmarkCircle, 'ellipse-outline': ellipseOutline })

const router = useRouter()
const selected = ref<number | null>(null)

const barbers = ref([
  { id: 1, name: 'Richard Jones', rating: 5, ratingText: '5.0', initials: 'RJ', color: '#2d5986', img: '/assets/Rectangle__5.png' },
  { id: 2, name: 'Marc Andrew',   rating: 3, ratingText: '3.0', initials: 'MA', color: '#1a5c3a', img: '/assets/Rectangle__4.png' },
  { id: 3, name: 'Charles Smith', rating: 4, ratingText: '4.0', initials: 'CS', color: '#8B4513', img: '/assets/Rectangle.png'  },
])

const seleccionar = (barber: any) => {
  selected.value = barber.id
}

const continuar = () => {
  const barber = barbers.value.find(b => b.id === selected.value)
  if (barber) {
    bookingStore.barberoId = barber.id
    bookingStore.barberoNombre = barber.name
    bookingStore.barberoImg = barber.img
    bookingStore.barberoInitials = barber.initials
    bookingStore.barberoColor = barber.color
  }
  router.push('/reservar')
}
</script>

<style scoped>
ion-page { --ion-safe-area-top: 0px; }
.page-bg { min-height: 100%; background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%); }
.page-header { padding: 50px 24px 10px; }
.back-btn { background: rgba(255,255,255,0.08); border: none; border-radius: 10px; width: 40px; height: 40px; display: flex; align-items: center; justify-content: center; color: white; font-size: 20px; cursor: pointer; }
.page-content { padding: 16px 24px 40px; }
h2 { font-family: 'Rajdhani', sans-serif; font-size: 30px; color: white; margin: 0 0 8px; font-weight: 600; }
.subtitle { color: rgba(255,255,255,0.5); font-size: 14px; margin: 0 0 28px; line-height: 1.5; }
.barber-card { display: flex; align-items: center; gap: 14px; background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.08); border-radius: 16px; padding: 14px 16px; margin-bottom: 12px; cursor: pointer; transition: all 0.2s; }
.barber-card.selected { background: rgba(74,143,232,0.12); border-color: rgba(74,143,232,0.4); }
.barber-avatar { width: 60px; height: 66px; border-radius: 10px; overflow: hidden; flex-shrink: 0; }
.barber-info { flex: 1; }
.barber-name { color: white; font-weight: 600; font-size: 17px; margin-bottom: 4px; }
.star { color: rgba(255,255,255,0.2); font-size: 14px; }
.star.filled { color: #f5c518; }
.rating-txt { font-size: 12px; color: rgba(255,255,255,0.4); margin-left: 4px; }
.select-indicator { font-size: 24px; color: rgba(255,255,255,0.3); }
.barber-card.selected .select-indicator { color: #4a8fe8; }
.btn-primary { width: 100%; background: linear-gradient(135deg, #2d5986 0%, #1a3a5c 100%); color: white; border: none; border-radius: 12px; padding: 16px; font-family: 'DM Sans', sans-serif; font-size: 16px; font-weight: 600; cursor: pointer; }
.btn-primary:disabled { opacity: 0.4; cursor: not-allowed; }
</style>
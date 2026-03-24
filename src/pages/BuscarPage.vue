<template>
  <ion-page>
    <ion-content :fullscreen="true" :scroll-y="false">
      <div class="page-bg">
        <div class="search-section">
          <button class="back-btn" @click="router.back()">
            <ion-icon name="arrow-back-outline"></ion-icon>
          </button>
          <div class="search-bar">
            <ion-icon name="search-outline" class="search-icon"></ion-icon>
            <input
              v-model="query"
              type="text"
              placeholder="Buscar"
              autofocus
              @keyup.enter="buscar"
            />
            <button class="filter-btn" @click="showFilter = true">
              <ion-icon name="options-outline"></ion-icon>
            </button>
          </div>
        </div>

        <div class="divider"></div>

        <div class="recientes-section">
          <div class="recientes-header">
            <span class="recientes-title">Recientes</span>
            <button class="clear-btn" @click="recientes = []">Limpiar todo</button>
          </div>
          <div class="recientes-list">
            <div
              v-for="(item, i) in recientes"
              :key="i"
              class="reciente-item"
              @click="query = item; buscar()"
            >
              <span>{{ item }}</span>
              <button class="remove-btn" @click.stop="recientes.splice(i, 1)">
                <ion-icon name="close-outline"></ion-icon>
              </button>
            </div>
          </div>
          <div v-if="recientes.length === 0" class="empty-recientes">
            <p>Sin búsquedas recientes</p>
          </div>
        </div>

        <div class="sheet-overlay" v-if="showFilter" @click="showFilter = false">
          <div class="filter-sheet" @click.stop>
            <div class="sheet-handle"></div>
            <h4 class="sheet-title">Rating Barber</h4>
            <div class="rating-stars">
              <span
                v-for="i in 5"
                :key="i"
                :class="['star', i <= filterRating ? 'filled' : '']"
                @click="filterRating = i"
              >★</span>
              <span class="rating-val">({{ filterRating }}.0)</span>
            </div>
            <div class="slider-track">
              <div class="slider-fill" :style="{ width: (filterRating / 5 * 100) + '%' }"></div>
              <input type="range" min="1" max="5" v-model.number="filterRating" class="slider" />
            </div>
            <button class="btn-aplicar" @click="aplicarFiltro">Aplicar filtro</button>
          </div>
        </div>

      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import { searchOutline, optionsOutline, closeOutline, arrowBackOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'

addIcons({
  'search-outline': searchOutline,
  'options-outline': optionsOutline,
  'close-outline': closeOutline,
  'arrow-back-outline': arrowBackOutline
})

const router = useRouter()
const query = ref('')
const showFilter = ref(false)
const filterRating = ref(4)
const recientes = ref(['Charles Smith', 'Richard Jones', 'Marc Andrew'])

const barberos = [
  { id: 1, name: 'Charles Smith', rating: 4 },
  { id: 2, name: 'Richard Jones', rating: 5 },
  { id: 3, name: 'Marc Andrew',   rating: 3 },
]

function buscar() {
  const match = barberos.find(b => b.name.toLowerCase().includes(query.value.toLowerCase()))
  if (match) {
    router.push(`/barbero/${match.id}`)
  } else {
    router.push({ path: '/resultados', query: { rating: filterRating.value } })
  }
}

function aplicarFiltro() {
  showFilter.value = false
  const match = barberos.find(b => b.rating === filterRating.value)
  if (match) {
    router.push(`/barbero/${match.id}`)
  } else {
    router.push({ path: '/resultados', query: { rating: filterRating.value } })
  }
}
</script>

<style scoped>
.page-bg {
  min-height: 100%;
  background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%);
}
.search-section {
  display: flex; align-items: center; gap: 12px;
  padding: 56px 20px 20px;
}
.back-btn {
  width: 40px; height: 40px;
  background: rgba(255,255,255,0.08);
  border: none; border-radius: 10px;
  color: white; font-size: 20px;
  display: flex; align-items: center; justify-content: center;
  cursor: pointer; flex-shrink: 0;
}
.search-bar {
  flex: 1;
  background: rgba(255,255,255,0.07);
  border: 1px solid rgba(255,255,255,0.1);
  border-radius: 14px;
  display: flex; align-items: center;
  padding: 0 14px; height: 48px; gap: 8px;
}
.search-icon { color: rgba(255,255,255,0.4); font-size: 18px; }
.search-bar input {
  flex: 1; background: none; border: none; outline: none;
  color: white; font-size: 15px; font-family: 'DM Sans', sans-serif;
}
.search-bar input::placeholder { color: rgba(255,255,255,0.35); }
.filter-btn {
  background: none; border: none;
  color: rgba(255,255,255,0.5);
  font-size: 20px; cursor: pointer; display: flex; padding: 0;
}
.divider { height: 1px; background: rgba(255,255,255,0.06); margin: 0 20px; }
.recientes-section { padding: 24px 20px 0; }
.recientes-header {
  display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;
}
.recientes-title { color: white; font-family: 'Rajdhani', sans-serif; font-size: 20px; font-weight: 700; }
.clear-btn {
  background: none; border: none; color: rgba(255,255,255,0.4);
  font-size: 13px; cursor: pointer; font-family: 'DM Sans', sans-serif;
}
.reciente-item {
  display: flex; align-items: center; justify-content: space-between;
  padding: 14px 0; border-bottom: 1px solid rgba(255,255,255,0.05); cursor: pointer;
}
.reciente-item span {
  color: rgba(255,255,255,0.8); font-family: 'DM Sans', sans-serif; font-size: 15px;
}
.remove-btn {
  background: none; border: none; color: rgba(255,255,255,0.35);
  font-size: 18px; cursor: pointer; display: flex;
}
.empty-recientes p { color: rgba(255,255,255,0.3); text-align: center; padding: 30px 0; }

.sheet-overlay {
  position: fixed; inset: 0; background: rgba(0,0,0,0.5);
  z-index: 1000; display: flex; align-items: flex-end;
}
.filter-sheet {
  background: #1a2d42; border-radius: 24px 24px 0 0;
  padding: 20px 24px 40px; width: 100%;
  border-top: 1px solid rgba(255,255,255,0.08);
}
.sheet-handle {
  width: 40px; height: 4px; background: rgba(255,255,255,0.2);
  border-radius: 2px; margin: 0 auto 24px;
}
.sheet-title { color: white; font-size: 16px; font-weight: 600; margin: 0 0 16px; font-family: 'DM Sans', sans-serif; }
.rating-stars { display: flex; align-items: center; gap: 6px; margin-bottom: 16px; }
.star { font-size: 22px; color: rgba(255,255,255,0.2); cursor: pointer; }
.star.filled { color: #f5c518; }
.rating-val { color: rgba(255,255,255,0.5); font-size: 14px; margin-left: 4px; }
.slider-track {
  position: relative; height: 8px; background: rgba(255,255,255,0.1);
  border-radius: 4px; margin-bottom: 28px;
}
.slider-fill { position: absolute; height: 100%; background: #4a8fe8; border-radius: 4px; pointer-events: none; }
.slider { position: absolute; inset: 0; opacity: 0; width: 100%; cursor: pointer; }
.btn-aplicar {
  width: 100%; padding: 16px;
  background: linear-gradient(135deg, #1e4fd8, #2d6be4);
  border: none; border-radius: 14px;
  color: white; font-size: 16px; font-weight: 600;
  cursor: pointer; font-family: 'DM Sans', sans-serif;
}
</style>
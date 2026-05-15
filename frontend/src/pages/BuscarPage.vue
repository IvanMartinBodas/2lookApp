<template>
  <ion-page>
    <ion-content :fullscreen="true" :scroll-y="true">
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
              placeholder="Buscar barbero..."
              autofocus
              @keyup.enter="buscar"
              @input="buscar"
            />
            <button v-if="query" class="filter-btn" @click="query = ''; resultados = []">
              <ion-icon name="close-outline"></ion-icon>
            </button>
            <button class="filter-btn" @click="showFilter = true">
              <ion-icon name="options-outline"></ion-icon>
            </button>
          </div>
        </div>

        <div class="divider"></div>

        <!-- Resultados de búsqueda -->
        <div v-if="resultados.length > 0" class="resultados-section">
          <div class="recientes-header">
            <span class="recientes-title">Resultados ({{ resultados.length }})</span>
          </div>
          <div
            v-for="b in resultados"
            :key="b.id"
            class="resultado-item"
            @click="irBarbero(b)"
          >
            <div class="result-avatar">
              <img :src="b.img" alt="" />
            </div>
            <div class="result-info">
              <div class="result-name">{{ b.name }}</div>
              <div class="result-stars">
                <span v-for="i in 5" :key="i" :class="['star', i <= b.rating ? 'filled' : '']">★</span>
                <span class="rating-txt">({{ b.ratingText }})</span>
              </div>
            </div>
            <ion-icon name="chevron-forward-outline" class="chevron"></ion-icon>
          </div>
        </div>

        <div v-else-if="query && !loading" class="empty-resultados">
          <p>Sin resultados para "{{ query }}"</p>
        </div>


        <div v-if="!query" class="recientes-section">
          <div class="recientes-header">
            <span class="recientes-title">Búsquedas recientes</span>
            <button class="clear-btn" @click="recientes = []">Borrar todo</button>
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
            <h4 class="sheet-title">Filtrar por valoración</h4>
            <p class="sheet-sub">Mínimo de estrellas</p>
            <div class="filter-stars">
              <span
                v-for="i in 5"
                :key="i"
                :class="['fstar', i <= filterRating ? 'filled' : '']"
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
import { ref, onMounted } from 'vue'
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import { searchOutline, optionsOutline, closeOutline, arrowBackOutline, chevronForwardOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'
import { barberoApi } from '@/store/api'

addIcons({
  'search-outline': searchOutline,
  'options-outline': optionsOutline,
  'close-outline': closeOutline,
  'arrow-back-outline': arrowBackOutline,
  'chevron-forward-outline': chevronForwardOutline,
})

const router = useRouter()
const query = ref('')
const showFilter = ref(false)
const filterRating = ref(4)
const loading = ref(true)
const recientes = ref<string[]>(['Charles Smith', 'Richard Jones', 'Marc Andrew'])
const resultados = ref<any[]>([])

function getFotoPorNombre(nombre: string): string {
  const n = (nombre || '').toLowerCase()
  if (n.includes('charles')) return '/assets/Rectangle.png'
  if (n.includes('richard')) return '/assets/Rectangle__5.png'
  if (n.includes('marc'))    return '/assets/Rectangle__4.png'
  return '/assets/Rectangle.png'
}

let todosBarberos: any[] = []
let cargaPromise: Promise<void> | null = null

async function cargarBarberos() {
  try {
    const data = await barberoApi.getAll()
    todosBarberos = data.map((b: any) => ({
      id: b.id,
      name: b.persona?.nombre || 'Barbero',
      rating: Math.round(b.valoracion || 0),
      ratingText: (b.valoracion || 0).toFixed(1),
      img: b.fotoUrl || getFotoPorNombre(b.persona?.nombre || ''),
    }))
  } catch {
    todosBarberos = [
      { id: 1, name: 'Charles Smith', rating: 4, ratingText: '4.0', img: '/assets/Rectangle.png' },
      { id: 2, name: 'Richard Jones', rating: 5, ratingText: '5.0', img: '/assets/Rectangle__5.png' },
      { id: 3, name: 'Marc Andrew',   rating: 3, ratingText: '3.0', img: '/assets/Rectangle__4.png' },
    ]
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  cargaPromise = cargarBarberos()
})

async function buscar() {
  if (!query.value.trim()) { resultados.value = []; return }
  // Esperar a que los barberos terminen de cargar antes de filtrar
  if (cargaPromise) await cargaPromise
  resultados.value = todosBarberos.filter(b =>
    b.name.toLowerCase().includes(query.value.toLowerCase())
  )
}

function irBarbero(b: any) {
  if (!recientes.value.includes(b.name)) recientes.value.unshift(b.name)
  router.push(`/barbero/${b.id}`)
}

function aplicarFiltro() {
  showFilter.value = false
  resultados.value = todosBarberos.filter(b => b.rating >= filterRating.value)
  query.value = `★ ${filterRating.value}+`
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
.resultados-section { padding: 24px 20px 0; }
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
.empty-recientes p, .empty-resultados p {
  color: rgba(255,255,255,0.3); text-align: center; padding: 30px 0;
  font-family: 'DM Sans', sans-serif; font-size: 14px;
}
.resultado-item {
  display: flex; align-items: center; gap: 14px;
  background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.08);
  border-radius: 14px; padding: 12px 14px; margin-bottom: 10px; cursor: pointer;
}
.result-avatar {
  width: 50px; height: 50px; border-radius: 10px; overflow: hidden; flex-shrink: 0;
  background: #1a2d42;
}
.result-avatar img { width: 100%; height: 100%; object-fit: cover; }
.result-info { flex: 1; }
.result-name { color: white; font-weight: 600; font-size: 16px; margin-bottom: 4px; font-family: 'DM Sans', sans-serif; }
.result-stars { display: flex; align-items: center; gap: 2px; }
.star { font-size: 13px; color: rgba(255,255,255,0.2); }
.star.filled { color: #f5c518; }
.rating-txt { font-size: 12px; color: rgba(255,255,255,0.4); margin-left: 4px; font-family: 'DM Sans', sans-serif; }
.chevron { font-size: 18px; color: rgba(255,255,255,0.3); flex-shrink: 0; }

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
.sheet-title { color: white; font-size: 16px; font-weight: 600; margin: 0 0 4px; font-family: 'DM Sans', sans-serif; }
.sheet-sub { color: rgba(255,255,255,0.4); font-size: 13px; margin: 0 0 16px; font-family: 'DM Sans', sans-serif; }
.filter-stars { display: flex; align-items: center; gap: 8px; margin-bottom: 16px; }
.fstar { font-size: 28px; color: rgba(255,255,255,0.15); cursor: pointer; transition: transform 0.12s, color 0.12s; line-height: 1; }
.fstar:hover { transform: scale(1.15); }
.fstar.filled { color: #f5c518; }
.rating-val { color: rgba(255,255,255,0.5); font-size: 14px; margin-left: 4px; font-family: 'DM Sans', sans-serif; }
.slider-track { position: relative; height: 8px; background: rgba(255,255,255,0.1); border-radius: 4px; margin-bottom: 28px; }
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
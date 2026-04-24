<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="page-bg">
        <div class="profile-content">
          <div class="avatar-section">
            <div class="profile-avatar">
              <img v-if="userStore.photo" :src="userStore.photo" style="width:100%; height:100%; object-fit:cover; border-radius:50%" alt="" />
              <div v-else class="avatar-img">{{ iniciales }}</div>
            </div>
            <h2 class="user-name">{{ userStore.name }}</h2>
          </div>

          <div class="menu-list">
            <div class="menu-item" v-for="item in menuItems" :key="item.label" @click="handleMenu(item)">
              <div class="menu-icon" :class="item.danger ? 'danger-icon' : ''">
                <ion-icon :name="item.icon"></ion-icon>
              </div>
              <span :class="item.danger ? 'danger-label' : ''">{{ item.label }}</span>
              <ion-icon name="chevron-forward-outline" class="chevron"></ion-icon>
            </div>
          </div>
        </div>
      </div>

      <Transition name="toast">
        <div v-if="toastMsg" class="toast">{{ toastMsg }}</div>
      </Transition>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import {
  personOutline, notificationsOutline, lockClosedOutline,
  cardOutline, starOutline, trashOutline, flagOutline,
  logOutOutline, chevronForwardOutline
} from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'
import { userStore, cerrarSesion } from '@/store/user'

addIcons({
  'person-outline': personOutline,
  'notifications-outline': notificationsOutline,
  'lock-closed-outline': lockClosedOutline,
  'card-outline': cardOutline,
  'star-outline': starOutline,
  'trash-outline': trashOutline,
  'flag-outline': flagOutline,
  'log-out-outline': logOutOutline,
  'chevron-forward-outline': chevronForwardOutline,
})

const router = useRouter()
const toastMsg = ref('')

const iniciales = computed(() => {
  const n = userStore.name || ''
  const p = n.trim().split(' ')
  if (p.length >= 2) return (p[0][0] + p[1][0]).toUpperCase()
  return n.substring(0, 2).toUpperCase()
})

const menuItems = [
  { label: 'Editar perfil',        icon: 'person-outline',        route: '/editar-perfil', dev: false },
  { label: 'Notificaciones',       icon: 'notifications-outline', route: null,             dev: true  },
  { label: 'Privacidad',           icon: 'lock-closed-outline',   route: null,             dev: true  },
  { label: 'Pago',                 icon: 'card-outline',          route: null,             dev: true  },
  { label: 'Reseñas',              icon: 'star-outline',          route: null,             dev: true  },
  { label: 'Reportar un problema', icon: 'flag-outline',          route: null,             dev: true  },
  { label: 'Eliminar cuenta',      icon: 'trash-outline',         route: null,             dev: true, danger: true },
  { label: 'Cerrar sesión',        icon: 'log-out-outline',       route: 'logout',         dev: false },
]

let toastTimer: any = null

function showToast(msg: string) {
  toastMsg.value = msg
  clearTimeout(toastTimer)
  toastTimer = setTimeout(() => { toastMsg.value = '' }, 2200)
}

const handleMenu = (item: any) => {
  if (item.route === 'logout') {
    cerrarSesion()
    window.location.replace('/login')
    return
  }
  if (item.dev) {
    showToast('🚧  Próximamente disponible')
    return
  }
  if (item.route) router.push(item.route)
}
</script>

<style scoped>
.page-bg { min-height: 100%; background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%); }
.profile-content { padding: 50px 24px 30px; }
.avatar-section { display: flex; flex-direction: column; align-items: center; margin-bottom: 32px; }
.profile-avatar { width: 110px; height: 110px; border-radius: 50%; border: 3px solid rgba(255,255,255,0.2); overflow: hidden; margin-bottom: 14px; }
.avatar-img { width: 100%; height: 100%; background: linear-gradient(135deg, #2d5986, #1a3a5c); display: flex; align-items: center; justify-content: center; color: white; font-size: 32px; font-weight: 700; }
.user-name { font-family: 'Rajdhani', sans-serif; font-size: 26px; color: white; margin: 0; font-weight: 700; }
.menu-list { background: rgba(255,255,255,0.04); border-radius: 16px; border: 1px solid rgba(255,255,255,0.06); overflow: hidden; }
.menu-item { display: flex; align-items: center; gap: 14px; padding: 16px 18px; border-bottom: 1px solid rgba(255,255,255,0.05); cursor: pointer; transition: background 0.2s; }
.menu-item:last-child { border-bottom: none; }
.menu-item:hover { background: rgba(255,255,255,0.05); }
.menu-icon { width: 32px; height: 32px; background: rgba(255,255,255,0.08); border-radius: 8px; display: flex; align-items: center; justify-content: center; }
.menu-icon.danger-icon { background: rgba(255,80,80,0.12); }
.menu-icon.danger-icon ion-icon { color: #ff6b6b; }
.menu-icon ion-icon { font-size: 18px; color: rgba(255,255,255,0.75); }
.menu-item span { flex: 1; color: rgba(255,255,255,0.85); font-size: 15px; }
.danger-label { color: #ff6b6b !important; }
.chevron { color: rgba(255,255,255,0.3); font-size: 16px; }

.toast { position: fixed; bottom: 90px; left: 50%; transform: translateX(-50%); background: rgba(30,45,60,0.97); border: 1px solid rgba(255,255,255,0.12); border-radius: 20px; padding: 12px 22px; color: white; font-size: 14px; font-family: 'DM Sans', sans-serif; white-space: nowrap; z-index: 1000; box-shadow: 0 4px 20px rgba(0,0,0,0.4); }
.toast-enter-active, .toast-leave-active { transition: all 0.25s ease; }
.toast-enter-from, .toast-leave-to { opacity: 0; transform: translateX(-50%) translateY(10px); }
</style>

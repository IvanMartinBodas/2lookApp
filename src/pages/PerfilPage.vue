<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="page-bg">
        <div class="profile-content">
          <div class="avatar-section">
            <div class="profile-avatar">
              <img v-if="userStore.photo" :src="userStore.photo" style="width:100%; height:100%; object-fit:cover; border-radius:50%" alt="" />
              <div v-else class="avatar-img">IM</div>
            </div>
            <h2 class="user-name">{{ userStore.name }}</h2>
          </div>

          <div class="menu-list">
            <div class="menu-item" v-for="item in menuItems" :key="item.label" @click="handleMenu(item)">
              <div class="menu-icon">
                <ion-icon :name="item.icon"></ion-icon>
              </div>
              <span>{{ item.label }}</span>
              <ion-icon name="chevron-forward-outline" class="chevron"></ion-icon>
            </div>
          </div>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import {
  personOutline, notificationsOutline, lockClosedOutline,
  cardOutline, starOutline, trashOutline, flagOutline,
  logOutOutline, chevronForwardOutline
} from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'
import { userStore } from '@/store/user'

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

const menuItems = [
  { label: 'Editar perfil', icon: 'person-outline', route: '/editar-perfil' },
  { label: 'Notificaciones', icon: 'notifications-outline', route: null },
  { label: 'Privacidad', icon: 'lock-closed-outline', route: null },
  { label: 'Pago', icon: 'card-outline', route: null },
  { label: 'Resenas', icon: 'star-outline', route: '/resenas' },
  { label: 'Eliminar cuenta', icon: 'trash-outline', route: null },
  { label: 'Reportar un problema', icon: 'flag-outline', route: null },
  { label: 'Log out', icon: 'log-out-outline', route: '/login' },
]

const handleMenu = (item: any) => {
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
.section-label { color: rgba(255,255,255,0.5); font-size: 13px; text-transform: uppercase; letter-spacing: 0.1em; margin-bottom: 12px; }
.menu-list { background: rgba(255,255,255,0.04); border-radius: 16px; border: 1px solid rgba(255,255,255,0.06); overflow: hidden; }
.menu-item { display: flex; align-items: center; gap: 14px; padding: 16px 18px; border-bottom: 1px solid rgba(255,255,255,0.05); cursor: pointer; transition: background 0.2s; }
.menu-item:last-child { border-bottom: none; }
.menu-item:hover { background: rgba(255,255,255,0.05); }
.menu-icon { width: 32px; height: 32px; background: rgba(255,255,255,0.08); border-radius: 8px; display: flex; align-items: center; justify-content: center; }
.menu-icon ion-icon { font-size: 18px; color: rgba(255,255,255,0.75); }
.menu-item span { flex: 1; color: rgba(255,255,255,0.85); font-size: 15px; }
.chevron { color: rgba(255,255,255,0.3); font-size: 16px; }
</style>
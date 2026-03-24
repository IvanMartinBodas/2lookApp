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
          <div class="avatar-section">
            <div class="profile-avatar">
              <img v-if="userStore.photo" :src="userStore.photo" style="width:100%; height:100%; object-fit:cover; border-radius:50%" alt="" />
              <span v-else>IM</span>
            </div>
            <button class="change-photo-btn" @click="changePhoto">Cambiar foto</button>
          </div>

          <div class="field-group"><label>Nombre</label><input v-model="form.nombre" class="app-input" placeholder="Iván" /></div>
          <div class="field-group"><label>Apellidos</label><input v-model="form.apellidos" class="app-input" placeholder="Martín" /></div>
          <div class="field-group"><label>Email</label><input v-model="form.email" type="email" class="app-input" placeholder="ivan@gmail.com" /></div>
          <div class="field-group"><label>Creacion de la cuenta</label><input v-model="form.fecha" class="app-input" placeholder="01/01/2024" readonly /></div>

          <button class="save-btn" @click="router.back()">Guardar cambios</button>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import { arrowBackOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'
import { userStore } from '@/store/user'

addIcons({ 'arrow-back-outline': arrowBackOutline })

const router = useRouter()
const form = reactive({ nombre: 'Iván', apellidos: 'Martín', email: 'ivan@gmail.com', fecha: '24/02/2026' })

const changePhoto = () => {
  userStore.photo = '/assets/Ellipse_36.png'
}
</script>

<style scoped>
.page-bg { min-height: 100%; background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%); }
.page-header { padding: 56px 24px 10px; }
.back-btn { background: rgba(255,255,255,0.08); border: none; border-radius: 10px; width: 40px; height: 40px; display: flex; align-items: center; justify-content: center; color: white; font-size: 20px; cursor: pointer; }
.page-content { padding: 0 24px 40px; }
.avatar-section { display: flex; flex-direction: column; align-items: center; margin-bottom: 28px; }
.profile-avatar { width: 100px; height: 100px; border-radius: 50%; overflow: hidden; background: linear-gradient(135deg, #2d5986, #1a3a5c); display: flex; align-items: center; justify-content: center; color: white; font-size: 28px; font-weight: 700; border: 3px solid rgba(255,255,255,0.15); margin-bottom: 10px; }
.change-photo-btn { background: none; border: none; color: #4a8fe8; font-family: 'DM Sans', sans-serif; font-size: 14px; cursor: pointer; }
.field-group { margin-bottom: 20px; }
.field-group label { display: block; font-size: 13px; color: rgba(255,255,255,0.6); margin-bottom: 8px; text-transform: uppercase; letter-spacing: 0.05em; }
.save-btn { width: 100%; background: rgba(255,255,255,0.08); border: 1px solid rgba(255,255,255,0.15); border-radius: 14px; color: white; padding: 18px; font-family: 'DM Sans', sans-serif; font-size: 15px; font-weight: 600; cursor: pointer; margin-top: 12px; transition: background 0.2s; }
.save-btn:hover { background: rgba(255,255,255,0.14); }
</style>
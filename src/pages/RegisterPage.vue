<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="register-bg">
        <div class="register-content">
          <div class="top-bar fade-in-up">
            <button class="back-btn" @click="router.back()">
              <ion-icon name="arrow-undo-outline"></ion-icon>
            </button>
          </div>

          <h2 class="fade-in-up" style="animation-delay: 0.1s">Sign up</h2>

          <div class="field-group fade-in-up" style="animation-delay: 0.15s">
            <label>Nombre de usuario</label>
            <input v-model="form.username" type="text" class="app-input" placeholder="Usuario" />
          </div>

          <div class="field-group fade-in-up" style="animation-delay: 0.2s">
            <label>Email</label>
            <input v-model="form.email" type="email" class="app-input" placeholder="nombreusuario@gmail.com" />
          </div>

          <div class="field-group fade-in-up" style="animation-delay: 0.25s">
            <label>Confirma tu email</label>
            <input v-model="form.emailConfirm" type="email" class="app-input" placeholder="nombreusuario@gmail.com" />
          </div>

          <div class="field-group fade-in-up" style="animation-delay: 0.3s">
            <label>Contraseña</label>
            <div class="input-wrapper">
              <input v-model="form.password" :type="showPass ? 'text' : 'password'" class="app-input" placeholder="••••••••••••••••" />
              <button class="eye-btn" @click="showPass = !showPass">
                <ion-icon :name="showPass ? 'eye-off-outline' : 'eye-outline'"></ion-icon>
              </button>
            </div>
          </div>

          <div class="field-group fade-in-up" style="animation-delay: 0.35s">
            <label>Confirma tu contraseña</label>
            <div class="input-wrapper">
              <input v-model="form.passwordConfirm" :type="showPass2 ? 'text' : 'password'" class="app-input" placeholder="••••••••••••••••" />
              <button class="eye-btn" @click="showPass2 = !showPass2">
                <ion-icon :name="showPass2 ? 'eye-off-outline' : 'eye-outline'"></ion-icon>
              </button>
            </div>
          </div>

          <button class="btn-primary fade-in-up" style="animation-delay: 0.4s; margin-top: 32px" @click="router.push('/login')">
            Sign up
          </button>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import { arrowUndoOutline, eyeOutline, eyeOffOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'

addIcons({ 'arrow-undo-outline': arrowUndoOutline, 'eye-outline': eyeOutline, 'eye-off-outline': eyeOffOutline })

const router = useRouter()
const showPass = ref(false)
const showPass2 = ref(false)
const form = reactive({
  username: '', email: '', emailConfirm: '', password: '', passwordConfirm: ''
})
</script>

<style scoped>
.register-bg {
  min-height: 100%;
  background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 45%, #080f18 100%);
}

.register-content {
  padding: 60px 32px 40px;
}

.top-bar {
  margin-bottom: 24px;
}

.back-btn {
  background: rgba(255,255,255,0.08);
  border: none;
  border-radius: 10px;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
  cursor: pointer;
}

h2 {
  font-family: 'Rajdhani', sans-serif;
  font-size: 36px;
  color: white;
  margin: 0 0 28px;
  font-weight: 600;
}

.field-group {
  margin-bottom: 20px;
}

.field-group label {
  display: block;
  font-size: 14px;
  color: rgba(255,255,255,0.8);
  margin-bottom: 8px;
}

.input-wrapper {
  position: relative;
}

.input-wrapper .app-input {
  padding-right: 48px !important;
}

.eye-btn {
  position: absolute;
  right: 14px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: rgba(255,255,255,0.5);
  cursor: pointer;
  font-size: 20px;
  display: flex;
  align-items: center;
}
</style>

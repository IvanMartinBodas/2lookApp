<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="login-bg">
        <div class="bg-circle"></div>
        <div class="login-content">
          <div class="logo-area fade-in-up" style="animation-delay: 0.1s">
            <img src="/assets/2look_letras.png" style="width:160px; filter: brightness(0) invert(1)" alt="2LOOK" />
          </div>

          <div class="form-area">
            <h2 class="fade-in-up" style="animation-delay: 0.2s">Login</h2>

            <div class="field-group fade-in-up" style="animation-delay: 0.3s">
              <label>Email</label>
              <input
                v-model="email"
                type="email"
                class="app-input"
                placeholder="nombreusuario@gmail.com"
              />
            </div>

            <div class="field-group fade-in-up" style="animation-delay: 0.35s">
              <label>Contraseña</label>
              <div class="input-wrapper">
                <input
                  v-model="password"
                  :type="showPassword ? 'text' : 'password'"
                  class="app-input"
                  placeholder="••••••••••••••••"
                />
                <button class="eye-btn" @click="showPassword = !showPassword">
                  <ion-icon :name="showPassword ? 'eye-off-outline' : 'eye-outline'"></ion-icon>
                </button>
              </div>
            </div>

            <button class="guest-btn fade-in-up" style="animation-delay: 0.4s" @click="goHome">
              Entrar como invitado
            </button>

            <button class="btn-primary fade-in-up" style="animation-delay: 0.45s" @click="goHome">
              Login
            </button>

            <p class="signup-link fade-in-up" style="animation-delay: 0.5s">
              ¿No tienes cuenta?
              <span @click="router.push('/register')">Sign up</span>
            </p>
          </div>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import { eyeOutline, eyeOffOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'

addIcons({ 'eye-outline': eyeOutline, 'eye-off-outline': eyeOffOutline })

const router = useRouter()
const email = ref('')
const password = ref('')
const showPassword = ref(false)

const goHome = () => router.push('/tabs/home')
</script>

<style scoped>
ion-page { --ion-safe-area-top: 0px; }

.login-bg {
  width: 100%;
  min-height: 100%;
  background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 45%, #080f18 100%);
  position: relative;
  overflow: hidden;
}

.bg-circle {
  position: absolute;
  width: 600px;
  height: 600px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(26,58,92,0.4) 0%, transparent 70%);
  top: -200px;
  left: 50%;
  transform: translateX(-50%);
  pointer-events: none;
}

.login-content {
  position: relative;
  z-index: 1;
  padding: 0 32px 40px;
  display: flex;
  flex-direction: column;
}

.logo-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 110px 0 20px;
  gap: 4px;
}

.form-area h2 {
  font-family: 'Rajdhani', sans-serif;
  font-size: 34px;
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

.guest-btn {
  background: none;
  border: none;
  color: rgba(255,255,255,0.6);
  font-family: 'DM Sans', sans-serif;
  font-size: 14px;
  cursor: pointer;
  padding: 0;
  margin-bottom: 20px;
  text-decoration: underline;
  text-underline-offset: 3px;
}

.signup-link {
  text-align: center;
  color: rgba(255,255,255,0.6);
  font-size: 14px;
  margin-top: 24px;
}

.signup-link span {
  color: white;
  font-weight: 700;
  cursor: pointer;
}
</style>
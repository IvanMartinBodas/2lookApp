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

            <!-- Error banner -->
            <div v-if="errorMsg" class="error-banner fade-in-up">
              ⚠️ {{ errorMsg }}
            </div>

            <div class="field-group fade-in-up" style="animation-delay: 0.3s">
              <label>Email</label>
              <input
                v-model="email"
                type="email"
                class="app-input"
                placeholder="nombreusuario@gmail.com"
                :disabled="cargando"
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
                  :disabled="cargando"
                  @keyup.enter="login"
                />
                <button class="eye-btn" @click="showPassword = !showPassword">
                  <ion-icon :name="showPassword ? 'eye-off-outline' : 'eye-outline'"></ion-icon>
                </button>
              </div>
            </div>

            <button class="guest-btn fade-in-up" style="animation-delay: 0.4s" @click="goHomeGuest">
              Entrar como invitado
            </button>

            <button
              class="btn-primary fade-in-up"
              style="animation-delay: 0.45s"
              @click="login"
              :disabled="cargando"
            >
              <ion-spinner v-if="cargando" name="crescent" style="width:18px;height:18px;margin-right:8px"></ion-spinner>
              {{ cargando ? 'Entrando...' : 'Login' }}
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
import { IonPage, IonContent, IonIcon, IonSpinner } from '@ionic/vue'
import { eyeOutline, eyeOffOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'
import { setUsuario } from '@/store/user'
import { BASE_URL } from '@/store/api'

addIcons({ 'eye-outline': eyeOutline, 'eye-off-outline': eyeOffOutline })

const router = useRouter()
const email = ref('')
const password = ref('')
const showPassword = ref(false)
const cargando = ref(false)
const errorMsg = ref('')

const login = async () => {
  errorMsg.value = ''

  if (!email.value || !password.value) {
    errorMsg.value = 'Introduce tu email y contraseña'
    return
  }

  cargando.value = true
  try {
    const res = await fetch(`${BASE_URL}/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: email.value, password: password.value })
    })

    const data = await res.json()

    if (!res.ok) {
      errorMsg.value = data.error || 'Error al iniciar sesión'
      return
    }

    setUsuario(data)
    router.replace('/tabs/home')

  } catch (e) {
    errorMsg.value = 'Sin conexión al servidor'
  } finally {
    cargando.value = false
  }
}

const goHomeGuest = () => {
  router.replace('/tabs/home')
}
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
  width: 600px; height: 600px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(26,58,92,0.4) 0%, transparent 70%);
  top: -200px; left: 50%;
  transform: translateX(-50%);
  pointer-events: none;
}
.login-content {
  position: relative; z-index: 1;
  padding: 0 32px 40px;
  display: flex; flex-direction: column;
}
.logo-area {
  display: flex; flex-direction: column; align-items: center;
  padding: 110px 0 20px; gap: 4px;
}
.form-area h2 {
  font-family: 'Rajdhani', sans-serif;
  font-size: 34px; color: white;
  margin: 0 0 28px; font-weight: 600;
}
.error-banner {
  background: rgba(235, 68, 90, 0.15);
  border: 1px solid rgba(235, 68, 90, 0.4);
  border-radius: 12px;
  padding: 12px 16px;
  color: #ff6b7a;
  font-size: 14px;
  margin-bottom: 20px;
  font-family: 'DM Sans', sans-serif;
}
.field-group { margin-bottom: 20px; }
.field-group label {
  display: block; font-size: 14px;
  color: rgba(255,255,255,0.8); margin-bottom: 8px;
}
.input-wrapper { position: relative; }
.input-wrapper .app-input { padding-right: 48px !important; }
.eye-btn {
  position: absolute; right: 14px; top: 50%;
  transform: translateY(-50%);
  background: none; border: none;
  color: rgba(255,255,255,0.5);
  cursor: pointer; font-size: 20px;
  display: flex; align-items: center;
}
.guest-btn {
  background: none; border: none;
  color: rgba(255,255,255,0.6);
  font-family: 'DM Sans', sans-serif;
  font-size: 14px; cursor: pointer;
  padding: 0; margin-bottom: 20px;
  text-decoration: underline;
  text-underline-offset: 3px;
  display: block;
}
.btn-primary {
  display: flex; align-items: center; justify-content: center;
}
.btn-primary:disabled { opacity: 0.6; cursor: not-allowed; }
.signup-link {
  text-align: center;
  color: rgba(255,255,255,0.6);
  font-size: 14px; margin-top: 24px;
}
.signup-link span {
  color: white; font-weight: 700; cursor: pointer;
}

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to   { opacity: 1; transform: translateY(0); }
}
.fade-in-up { animation: fadeInUp 0.5s ease forwards; opacity: 0; }
</style>
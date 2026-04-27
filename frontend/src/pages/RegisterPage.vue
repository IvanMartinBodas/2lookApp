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

          <!-- Error / Éxito -->
          <div v-if="errorMsg" class="error-banner fade-in-up">⚠️ {{ errorMsg }}</div>

          <div class="field-group fade-in-up" style="animation-delay: 0.15s">
            <label>Nombre completo</label>
            <input v-model="form.nombre" type="text" class="app-input" placeholder="Iván Martín" :disabled="cargando" />
          </div>

          <div class="field-group fade-in-up" style="animation-delay: 0.2s">
            <label>Email</label>
            <input v-model="form.email" type="email" class="app-input" placeholder="ejemplo@gmail.com" :disabled="cargando" />
          </div>

          <div class="field-group fade-in-up" style="animation-delay: 0.25s">
            <label>Confirma tu email</label>
            <input v-model="form.emailConfirm" type="email" class="app-input" placeholder="ejemplo@gmail.com" :disabled="cargando" />
          </div>

          <div class="field-group fade-in-up" style="animation-delay: 0.3s">
            <label>Contraseña</label>
            <div class="input-wrapper">
              <input v-model="form.password" :type="showPass ? 'text' : 'password'" class="app-input" placeholder="••••••••••••••••" :disabled="cargando" />
              <button class="eye-btn" @click="showPass = !showPass">
                <ion-icon :name="showPass ? 'eye-off-outline' : 'eye-outline'"></ion-icon>
              </button>
            </div>
          </div>

          <div class="field-group fade-in-up" style="animation-delay: 0.35s">
            <label>Confirma tu contraseña</label>
            <div class="input-wrapper">
              <input v-model="form.passwordConfirm" :type="showPass2 ? 'text' : 'password'" class="app-input" placeholder="••••••••••••••••" :disabled="cargando" />
              <button class="eye-btn" @click="showPass2 = !showPass2">
                <ion-icon :name="showPass2 ? 'eye-off-outline' : 'eye-outline'"></ion-icon>
              </button>
            </div>
          </div>

          <button
            class="btn-primary fade-in-up"
            style="animation-delay: 0.4s; margin-top: 32px"
            @click="register"
            :disabled="cargando"
          >
            <ion-spinner v-if="cargando" name="crescent" style="width:18px;height:18px;margin-right:8px"></ion-spinner>
            {{ cargando ? 'Creando cuenta...' : 'Sign up' }}
          </button>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { IonPage, IonContent, IonIcon, IonSpinner, useIonRouter } from '@ionic/vue'
import { arrowUndoOutline, eyeOutline, eyeOffOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'
import { setUsuario } from '@/store/user'
import { BASE_URL } from '@/store/api'

addIcons({ 'arrow-undo-outline': arrowUndoOutline, 'eye-outline': eyeOutline, 'eye-off-outline': eyeOffOutline })

const router = useRouter()
const ionRouter = useIonRouter()
const showPass = ref(false)
const showPass2 = ref(false)
const cargando = ref(false)
const errorMsg = ref('')

const form = reactive({
  nombre: '', email: '', emailConfirm: '', password: '', passwordConfirm: ''
})

const register = async () => {
  errorMsg.value = ''

  if (!form.nombre || !form.email || !form.password) {
    errorMsg.value = 'Rellena todos los campos'
    return
  }
  if (form.email !== form.emailConfirm) {
    errorMsg.value = 'Los emails no coinciden'
    return
  }
  if (form.password !== form.passwordConfirm) {
    errorMsg.value = 'Las contraseñas no coinciden'
    return
  }
  if (form.password.length < 8) {
    errorMsg.value = 'La contraseña debe tener al menos 8 caracteres'
    return
  }

  cargando.value = true
  try {
    const res = await fetch(`${BASE_URL}/api/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        nombre: form.nombre,
        email: form.email,
        password: form.password
      })
    })

    const data = await res.json()

    if (!res.ok) {
      errorMsg.value = data.error || 'Error al crear la cuenta'
      return
    }

    setUsuario(data)
    window.location.replace('/tabs/home')

  } catch (e) {
    errorMsg.value = 'Sin conexión al servidor'
  } finally {
    cargando.value = false
  }
}
</script>

<style scoped>
.register-bg {
  min-height: 100%;
  background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 45%, #080f18 100%);
}
.register-content { padding: 60px 32px 40px; }
.top-bar { margin-bottom: 24px; }
.back-btn {
  background: rgba(255,255,255,0.08); border: none;
  border-radius: 10px; width: 40px; height: 40px;
  display: flex; align-items: center; justify-content: center;
  color: white; font-size: 20px; cursor: pointer;
}
h2 {
  font-family: 'Rajdhani', sans-serif;
  font-size: 36px; color: white;
  margin: 0 0 28px; font-weight: 600;
}
.error-banner {
  background: rgba(235, 68, 90, 0.15);
  border: 1px solid rgba(235, 68, 90, 0.4);
  border-radius: 12px; padding: 12px 16px;
  color: #ff6b7a; font-size: 14px;
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
.btn-primary {
  display: flex; align-items: center; justify-content: center; width: 100%;
}
.btn-primary:disabled { opacity: 0.6; cursor: not-allowed; }

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to   { opacity: 1; transform: translateY(0); }
}
.fade-in-up { animation: fadeInUp 0.5s ease forwards; opacity: 0; }
</style>
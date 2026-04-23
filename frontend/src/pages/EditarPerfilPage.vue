<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="page-bg">
        <div class="page-header">
          <button class="back-btn" @click="router.back()">
            <ion-icon name="arrow-back-outline"></ion-icon>
          </button>
          <h3>Editar perfil</h3>
        </div>

        <div class="page-content">
          <div class="avatar-section">
            <div class="profile-avatar" @click="inputFoto?.click()">
              <img v-if="fotoPreview || userStore.photo" :src="fotoPreview || userStore.photo" style="width:100%; height:100%; object-fit:cover; border-radius:50%" alt="" />
              <span v-else>{{ iniciales }}</span>
              <div class="avatar-cam-overlay">
                <ion-icon name="camera-outline"></ion-icon>
              </div>
            </div>
            <input ref="inputFoto" type="file" accept="image/*" style="display:none" @change="onFotoSelected" />
            <p class="avatar-hint">Toca para cambiar foto</p>
          </div>

          <div class="field-group">
            <label>Nombre completo</label>
            <input v-model="form.nombre" class="app-input" placeholder="Tu nombre" />
          </div>

          <div class="field-group">
            <label>Email</label>
            <input v-model="form.email" type="email" class="app-input" placeholder="tu@email.com" />
          </div>

          <div class="field-group">
            <label>Nueva contraseña</label>
            <input v-model="form.password" type="password" class="app-input" placeholder="Dejar en blanco para no cambiar" />
          </div>

          <div class="field-group">
            <label>Miembro desde</label>
            <input :value="fechaFormateada" class="app-input readonly" readonly />
          </div>

          <p v-if="errorMsg" class="error-msg">{{ errorMsg }}</p>
          <p v-if="okMsg" class="ok-msg">{{ okMsg }}</p>

          <button class="save-btn" :disabled="guardando" @click="guardar">
            <ion-spinner v-if="guardando" name="crescent" style="width:14px;height:14px;margin-right:6px"></ion-spinner>
            {{ guardando ? 'Guardando...' : 'Guardar cambios' }}
          </button>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { reactive, ref, computed, onMounted } from 'vue'
import { IonPage, IonContent, IonIcon, IonSpinner } from '@ionic/vue'
import { arrowBackOutline, cameraOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'
import { userStore, restaurarSesion, setUsuario, guardarFotoPerfil } from '@/store/user'
import { personaApi } from '@/store/api'

addIcons({ 'arrow-back-outline': arrowBackOutline, 'camera-outline': cameraOutline })

const router = useRouter()
const guardando = ref(false)
const errorMsg = ref('')
const okMsg = ref('')
const fotoPreview = ref('')
const inputFoto = ref<HTMLInputElement | null>(null)

function onFotoSelected(e: Event) {
  const file = (e.target as HTMLInputElement).files?.[0]
  if (!file) return
  const reader = new FileReader()
  reader.onload = (ev) => {
    fotoPreview.value = ev.target?.result as string
  }
  reader.readAsDataURL(file)
}

const form = reactive({ nombre: '', email: '', password: '', fechaRegistro: '' })

const iniciales = computed(() => {
  const n = form.nombre || userStore.name || ''
  const partes = n.trim().split(' ')
  if (partes.length >= 2) return (partes[0][0] + partes[1][0]).toUpperCase()
  return n.substring(0, 2).toUpperCase()
})

const fechaFormateada = computed(() => {
  if (!form.fechaRegistro) return ''
  try {
    const d = new Date(form.fechaRegistro)
    return `${d.getDate().toString().padStart(2,'0')}/${(d.getMonth()+1).toString().padStart(2,'0')}/${d.getFullYear()}`
  } catch { return form.fechaRegistro }
})

onMounted(async () => {
  if (!userStore.id) restaurarSesion()
  if (userStore.id) {
    try {
      const persona = await personaApi.getById(userStore.id)
      form.nombre = persona.nombre || ''
      form.email = persona.email || ''
      form.fechaRegistro = persona.fechaRegistro || ''
    } catch {
      form.nombre = userStore.name || ''
      form.email = userStore.email || ''
    }
  } else {
    form.nombre = userStore.name || ''
    form.email = userStore.email || ''
  }
})

async function guardar() {
  if (!form.nombre.trim() || !form.email.trim()) {
    errorMsg.value = 'Nombre y email son obligatorios'
    return
  }
  errorMsg.value = ''
  okMsg.value = ''
  guardando.value = true

  try {
    const payload: any = { nombre: form.nombre.trim(), email: form.email.trim() }
    if (form.password.trim()) payload.password = form.password.trim()

    const updated = await personaApi.update(userStore.id!, payload)
    setUsuario({
      id: updated.id,
      nombre: updated.nombre,
      email: updated.email,
      rol: updated.rol,
    })
    if (fotoPreview.value) guardarFotoPerfil(fotoPreview.value)
    form.password = ''
    setTimeout(() => router.back(), 800)
  } catch (e: any) {
    errorMsg.value = e?.message || 'Error al guardar'
  } finally {
    guardando.value = false
  }
}
</script>

<style scoped>
.page-bg { min-height: 100%; background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%); display: flex; flex-direction: column; }
.page-header { display: flex; align-items: center; gap: 16px; padding: 56px 24px 16px; }
h3 { color: white; font-size: 18px; margin: 0; font-family: 'Rajdhani', sans-serif; font-weight: 600; }
.back-btn { background: rgba(255,255,255,0.08); border: none; border-radius: 10px; width: 40px; height: 40px; display: flex; align-items: center; justify-content: center; color: white; font-size: 20px; cursor: pointer; flex-shrink: 0; }
.page-content { padding: 0 24px 40px; }
.avatar-section { display: flex; flex-direction: column; align-items: center; margin-bottom: 28px; }
.profile-avatar { position: relative; width: 90px; height: 90px; border-radius: 50%; overflow: hidden; background: linear-gradient(135deg, #2d5986, #1a3a5c); display: flex; align-items: center; justify-content: center; color: white; font-size: 26px; font-weight: 700; border: 3px solid rgba(255,255,255,0.15); cursor: pointer; }
.avatar-cam-overlay { position: absolute; inset: 0; background: rgba(0,0,0,0.45); display: flex; align-items: center; justify-content: center; opacity: 0; transition: opacity 0.2s; border-radius: 50%; }
.profile-avatar:hover .avatar-cam-overlay { opacity: 1; }
.avatar-cam-overlay ion-icon { font-size: 26px; color: white; }
.avatar-hint { color: rgba(255,255,255,0.35); font-size: 12px; margin: 8px 0 0; font-family: 'DM Sans', sans-serif; }
.field-group { margin-bottom: 20px; }
.field-group label { display: block; font-size: 12px; color: rgba(255,255,255,0.5); margin-bottom: 8px; text-transform: uppercase; letter-spacing: 0.06em; font-family: 'DM Sans', sans-serif; }
.app-input { width: 100%; background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.1); border-radius: 12px; padding: 14px 16px; color: white; font-family: 'DM Sans', sans-serif; font-size: 15px; outline: none; box-sizing: border-box; }
.app-input::placeholder { color: rgba(255,255,255,0.3); }
.app-input:focus { border-color: rgba(74,143,232,0.5); background: rgba(74,143,232,0.06); }
.app-input.readonly { color: rgba(255,255,255,0.4); cursor: default; }
.error-msg { color: #ff6b6b; font-size: 13px; margin: 0 0 12px; font-family: 'DM Sans', sans-serif; }
.ok-msg { color: #2dd36f; font-size: 13px; margin: 0 0 12px; font-family: 'DM Sans', sans-serif; }
.save-btn { width: 100%; background: #4a8fe8; border: none; border-radius: 14px; color: white; padding: 18px; font-family: 'DM Sans', sans-serif; font-size: 15px; font-weight: 600; cursor: pointer; margin-top: 8px; display: flex; align-items: center; justify-content: center; transition: opacity 0.2s; }
.save-btn:disabled { opacity: 0.5; cursor: not-allowed; }
</style>

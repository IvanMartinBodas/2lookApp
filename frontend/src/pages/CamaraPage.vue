<template>
  <ion-page>

    <!-- ───── FASE: CÁMARA ───── -->
    <div v-if="fase === 'camara'" class="camara-container">
      <div class="camara-preview">
        <video ref="videoEl" class="video-stream" autoplay playsinline muted></video>

        <div class="camara-header">
          <div class="ia-badge">
            <ion-icon :icon="sparklesOutline" />
            <span>IA</span>
          </div>
        </div>

        <div class="guia-encuadre">
          <div class="guia-oval"></div>
          <p class="guia-texto">Centra tu cara aquí</p>
        </div>

        <div class="controles">
          <input
            ref="inputFile"
            type="file"
            accept="image/*"
            style="display:none"
            @change="onFileSelected"
          />
          <button class="shutter" @click="hacerFoto">
            <div class="shutter-inner"></div>
          </button>
        </div>

        <canvas ref="canvasEl" style="display:none"></canvas>
      </div>
    </div>

    <!-- ───── FASE: CARGANDO ───── -->
    <div v-else-if="fase === 'cargando'" class="cargando-container">
      <div class="foto-fondo" :style="fotoCapturada ? 'background-image:url(' + fotoCapturada + ');background-size:cover;background-position:center' : ''"></div>
      <div class="overlay-oscuro"></div>
      <div class="cargando-contenido">
        <div class="spinner-container">
          <ion-spinner name="circular" color="light" class="spinner-grande"></ion-spinner>
        </div>
        <h2 class="cargando-titulo">{{ mensajeCarga }}</h2>
        <p class="cargando-subtexto">{{ submensajeCarga }}</p>
      </div>
    </div>

    <!-- ───── FASE: RESULTADO ───── -->
    <div v-else-if="fase === 'resultado'" class="resultado-container">
      <ion-content :fullscreen="true">

        <div class="foto-visor">
          <img v-if="fotoCapturada" :src="fotoCapturada" class="foto-real" />
          <div class="foto-badge">
            <ion-icon :icon="sparklesOutline" />
            <span>Analizada con IA</span>
          </div>
        </div>

        <div class="resultado-contenido">

          <div class="forma-row">
            <span class="forma-emoji">{{ resultado.emoji }}</span>
            <div>
              <p class="forma-label">Forma detectada</p>
              <h2 class="forma-nombre">{{ resultado.forma }}</h2>
            </div>
          </div>

          <p class="resultado-descripcion">{{ resultado.descripcion }}</p>

          <h3 class="seccion-titulo">✂️ Top 3 cortes recomendados</h3>

          <div v-for="(corte, i) in resultado.cortes" :key="i" class="corte-tarjeta">
            <div class="corte-imagen-wrap">
              <img v-if="corte.imagenUrl" :src="corte.imagenUrl" class="corte-imagen" alt="" />
              <div v-else-if="corte.cargandoImagen" class="corte-imagen-loading">
                <ion-spinner name="crescent" color="light"></ion-spinner>
              </div>
              <div v-else class="corte-imagen-placeholder">
                <ion-icon :icon="cutOutline" style="font-size:28px;color:rgba(255,255,255,0.3)"></ion-icon>
              </div>
            </div>
            <div class="corte-body">
              <div class="corte-header-row">
                <div class="corte-numero">{{ i + 1 }}</div>
                <h4 class="corte-nombre">{{ corte.nombre }}</h4>
              </div>
              <p class="corte-descripcion">{{ corte.descripcion }}</p>
            </div>
          </div>

          <ion-button expand="block" class="btn-reservar" @click="irAReservar">
            ✂️&nbsp; Reservar cita con un barbero
          </ion-button>

          <ion-button expand="block" fill="outline" class="btn-nueva-foto" @click="reiniciar">
            📷&nbsp; Hacer otra foto
          </ion-button>

        </div>
      </ion-content>
    </div>

    <!-- ───── FASE: ERROR ───── -->
    <div v-else-if="fase === 'error'" class="error-container">
      <div class="error-contenido">
        <div class="error-icon">⚠️</div>
        <h2>Algo ha fallado</h2>
        <p>{{ mensajeError }}</p>
        <button class="btn-reintentar" @click="reiniciar">Intentar de nuevo</button>
      </div>
    </div>

  </ion-page>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { IonPage, IonContent, IonButton, IonIcon, IonSpinner } from '@ionic/vue'
import { sparklesOutline, cutOutline } from 'ionicons/icons'

const GEMINI_API_KEY = 'AIzaSyBVmoW6Y-Hf2aru-NFqfwpcO_WjqkFSfFI'
const FAL_API_KEY = 'TU_FAL_API_KEY_AQUI'

type Fase = 'camara' | 'cargando' | 'resultado' | 'error'

const router = useRouter()
const fase = ref<Fase>('camara')
const fotoCapturada = ref('')
const mensajeCarga = ref('Analizando tu cara...')
const submensajeCarga = ref('La IA está detectando la forma de tu rostro')
const mensajeError = ref('')

const videoEl = ref<HTMLVideoElement | null>(null)
const canvasEl = ref<HTMLCanvasElement | null>(null)
const inputFile = ref<HTMLInputElement | null>(null)
let stream: MediaStream | null = null

interface Corte {
  nombre: string
  descripcion: string
  imagenUrl: string
  cargandoImagen: boolean
}

interface Resultado {
  forma: string
  emoji: string
  descripcion: string
  cortes: Corte[]
}

const resultado = ref<Resultado>({
  forma: '',
  emoji: '🥚',
  descripcion: '',
  cortes: []
})

onMounted(async () => {
  await iniciarCamara()
})

onUnmounted(() => {
  pararCamara()
})

async function iniciarCamara() {
  try {
    stream = await navigator.mediaDevices.getUserMedia({
      video: { facingMode: 'user', width: { ideal: 640 }, height: { ideal: 640 } },
      audio: false
    })
    if (videoEl.value) videoEl.value.srcObject = stream
  } catch {
    console.log('Sin acceso a cámara directa, se usará input file')
  }
}

function pararCamara() {
  stream?.getTracks().forEach(t => t.stop())
  stream = null
}

function hacerFoto() {
  if (stream && videoEl.value && canvasEl.value) {
    const video = videoEl.value
    const canvas = canvasEl.value
    canvas.width = video.videoWidth || 640
    canvas.height = video.videoHeight || 640
    canvas.getContext('2d')?.drawImage(video, 0, 0)
    fotoCapturada.value = canvas.toDataURL('image/jpeg', 0.85)
    pararCamara()
    procesarFoto()
  } else {
    inputFile.value?.click()
  }
}

function onFileSelected(e: Event) {
  const file = (e.target as HTMLInputElement).files?.[0]
  if (!file) return
  const reader = new FileReader()
  reader.onload = (ev) => {
    fotoCapturada.value = ev.target?.result as string
    procesarFoto()
  }
  reader.readAsDataURL(file)
}

async function procesarFoto() {
  fase.value = 'cargando'
  mensajeCarga.value = 'Analizando tu rostro...'
  submensajeCarga.value = 'Gemini está detectando la forma de tu cara'

  try {
    const analisis = await analizarConGemini(fotoCapturada.value)

    resultado.value = {
      forma: analisis.forma,
      emoji: analisis.emoji,
      descripcion: analisis.descripcion,
      cortes: analisis.cortes.map((c: any) => ({
        nombre: c.nombre,
        descripcion: c.descripcion,
        imagenUrl: '',
        cargandoImagen: true
      }))
    }

    fase.value = 'resultado'
    generarImagenesCortes(analisis.cortes)

  } catch (err: any) {
    console.error(err)
    mensajeError.value = err?.message || 'Error al procesar la imagen'
    fase.value = 'error'
  }
}

const GEMINI_MODELS = ['gemini-2.5-flash', 'gemini-2.0-flash', 'gemini-1.5-flash']

async function analizarConGemini(dataUrl: string) {
  const base64 = dataUrl.split(',')[1]
  const mimeType = dataUrl.split(';')[0].split(':')[1]

  const prompt = `Eres un experto en estética y barbería. Analiza esta foto y responde SOLO con JSON válido, sin markdown ni texto extra:
{"forma":"Nombre forma cara (Ovalada/Redonda/Cuadrada/Rectangular/Corazón/Diamante/Triangular)","emoji":"emoji representativo","descripcion":"1-2 frases motivadoras sobre la forma detectada","cortes":[{"nombre":"nombre corte 1","descripcion":"1-2 frases por qué le favorece","promptImagen":"men haircut [nombre] style, barbershop reference photo, clean professional, white background, no face"},{"nombre":"nombre corte 2","descripcion":"descripción","promptImagen":"prompt en inglés"},{"nombre":"nombre corte 3","descripcion":"descripción","promptImagen":"prompt en inglés"}]}`

  let lastError = ''

  for (const model of GEMINI_MODELS) {
    try {
      console.log(`Intentando con modelo: ${model}`)
      const res = await fetch(
        `https://generativelanguage.googleapis.com/v1beta/models/${model}:generateContent?key=${GEMINI_API_KEY}`,
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            contents: [{ parts: [
              { inline_data: { mime_type: mimeType, data: base64 } },
              { text: prompt }
            ]}],
            generationConfig: { temperature: 0.3, maxOutputTokens: 2048 }
          })
        }
      )

      if (!res.ok) {
        const err = await res.json()
        const msg = err?.error?.message || ''
        // Error de quota, probar siguiente modelo
        if (msg.includes('quota') || msg.includes('RESOURCE_EXHAUSTED') || res.status === 429) {
          console.warn(`Quota agotada en ${model}, probando siguiente...`)
          lastError = msg
          continue
        }
        throw new Error(`Gemini: ${msg || res.status}`)
      }

      const data = await res.json()
      const texto = data.candidates?.[0]?.content?.parts?.[0]?.text || ''

      let jsonStr = texto
        .split('```json').join('')
        .split('```').join('')
        .trim()
      const match = jsonStr.match(/\{[\s\S]*\}/)
      if (match) jsonStr = match[0]

      try {
        return JSON.parse(jsonStr)
      } catch (parseErr) {
        console.error('JSON raw de Gemini:', JSON.stringify(jsonStr))
        try {
          const repaired = jsonStr + '"}]}'
          return JSON.parse(repaired)
        } catch {
          throw new Error('No se pudo parsear la respuesta de Gemini')
        }
      }

    } catch (err: any) {
      if (err.message?.includes('quota') || err.message?.includes('RESOURCE_EXHAUSTED')) {
        lastError = err.message
        continue
      }
      throw err
    }
  }

  throw new Error(`Quota agotada en todos los modelos. ${lastError}`)
}

async function generarImagenesCortes(cortes: any[]) {
  await Promise.allSettled(
    cortes.map((corte: any, i: number) => generarImagenCorte(corte.promptImagen, i))
  )
}

async function generarImagenCorte(prompt: string, index: number) {
  if (FAL_API_KEY === 'TU_FAL_API_KEY_AQUI') {
    await new Promise(r => setTimeout(r, 800))
    resultado.value.cortes[index].cargandoImagen = false
    return
  }

  try {
    const res = await fetch('https://fal.run/fal-ai/fast-sdxl', {
      method: 'POST',
      headers: {
        'Authorization': `Key ${FAL_API_KEY}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        prompt: prompt + ', barbershop haircut reference, professional photo, clean look',
        image_size: 'square_hd',
        num_inference_steps: 25,
        num_images: 1
      })
    })

    if (!res.ok) throw new Error(`fal.ai ${res.status}`)
    const data = await res.json()
    const url = data?.images?.[0]?.url
    if (url) resultado.value.cortes[index].imagenUrl = url

  } catch (err) {
    console.warn(`Imagen corte ${index} no generada:`, err)
  } finally {
    resultado.value.cortes[index].cargandoImagen = false
  }
}

function irAReservar() {

  if (resultado.value.cortes.length > 0) {
    bookingStore.servicioId = 1
    bookingStore.servicioNombre = resultado.value.cortes[0].nombre
    bookingStore.servicioPrecio = 10
    bookingStore.desdeIA = true
  }
  router.push('/elegir-barbero')
}

function reiniciar() {
  fotoCapturada.value = ''
  resultado.value = { forma: '', emoji: '🥚', descripcion: '', cortes: [] }
  fase.value = 'camara'
  iniciarCamara()
}
</script>

<style scoped>
ion-page { --background: #000; }

.camara-container { display: flex; flex-direction: column; height: 100vh; background: #000; }
.camara-preview { position: relative; flex: 1; display: flex; flex-direction: column; overflow: hidden; }
.video-stream { position: absolute; inset: 0; width: 100%; height: 100%; object-fit: cover; background: #111; }
.camara-header { position: relative; z-index: 10; display: flex; align-items: center; justify-content: center; padding: 48px 16px 12px; }
.ia-badge { display: flex; align-items: center; gap: 6px; background: rgba(255,255,255,0.15); border: 1px solid rgba(255,255,255,0.4); border-radius: 20px; padding: 5px 14px; color: #fff; font-size: 13px; font-weight: 700; letter-spacing: 1px; backdrop-filter: blur(4px); }
.guia-encuadre { position: relative; z-index: 10; flex: 1; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 16px; }
.guia-oval { width: 200px; height: 260px; border-radius: 50%; border: 2px dashed rgba(255,255,255,0.6); }
.guia-texto { color: rgba(255,255,255,0.7); font-size: 14px; margin: 0; font-family: 'DM Sans', sans-serif; }
.controles { position: relative; z-index: 10; display: flex; justify-content: center; padding: 0 40px 60px; }
.shutter { width: 78px; height: 78px; border-radius: 50%; background: rgba(255,255,255,0.2); border: 3px solid #fff; display: flex; align-items: center; justify-content: center; cursor: pointer; }
.shutter:active .shutter-inner { transform: scale(0.9); }
.shutter-inner { width: 62px; height: 62px; border-radius: 50%; background: #fff; transition: transform 0.1s; }

.cargando-container { position: relative; height: 100vh; display: flex; background: #000; overflow: hidden; }
.foto-fondo { position: absolute; inset: 0; background: #222; filter: blur(6px); transform: scale(1.05); }
.overlay-oscuro { position: absolute; inset: 0; background: rgba(0,0,0,0.6); }
.cargando-contenido { position: relative; flex: 1; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 16px; z-index: 1; }
.spinner-container { width: 130px; height: 130px; border-radius: 50%; background: rgba(255,255,255,0.08); border: 2px solid rgba(255,255,255,0.15); display: flex; align-items: center; justify-content: center; }
.spinner-grande { transform: scale(2.5); }
.cargando-titulo { color: #fff; font-size: 20px; font-weight: 700; margin: 8px 0 0; font-family: 'Rajdhani', sans-serif; }
.cargando-subtexto { color: rgba(255,255,255,0.6); font-size: 14px; text-align: center; padding: 0 40px; margin: 0; }

.resultado-container { height: 100vh; background: #f5f5f5; }
.foto-visor { position: relative; height: 46vh; background: #1a1a2e; overflow: hidden; }
.foto-real { width: 100%; height: 100%; object-fit: cover; display: block; }
.foto-badge { position: absolute; bottom: 14px; left: 14px; background: rgba(0,0,0,0.65); border-radius: 20px; padding: 6px 14px; color: #fff; font-size: 13px; font-weight: 700; backdrop-filter: blur(6px); display: flex; align-items: center; gap: 6px; }
.resultado-contenido { padding: 20px 16px 100px; display: flex; flex-direction: column; gap: 14px; }
.forma-row { display: flex; align-items: center; gap: 12px; background: #fff; border-radius: 16px; padding: 14px 16px; box-shadow: 0 2px 12px rgba(0,0,0,0.07); }
.forma-emoji { font-size: 32px; }
.forma-label { font-size: 11px; color: #999; margin: 0; text-transform: uppercase; letter-spacing: 0.5px; }
.forma-nombre { font-size: 18px; font-weight: 800; color: #1a1a2e; margin: 2px 0 0; }
.resultado-descripcion { font-size: 14px; color: #666; line-height: 1.6; margin: 0; background: #fff; border-radius: 14px; padding: 14px 16px; box-shadow: 0 2px 12px rgba(0,0,0,0.05); }
.seccion-titulo { font-size: 18px; font-weight: 800; color: #1a1a2e; margin: 4px 0 0; }

.corte-tarjeta { background: #fff; border-radius: 18px; overflow: hidden; box-shadow: 0 2px 14px rgba(0,0,0,0.08); }
.corte-imagen-wrap { width: 100%; height: 160px; background: #1a1a2e; display: flex; align-items: center; justify-content: center; overflow: hidden; }
.corte-imagen { width: 100%; height: 100%; object-fit: cover; }
.corte-imagen-loading, .corte-imagen-placeholder { display: flex; align-items: center; justify-content: center; width: 100%; height: 100%; }
.corte-body { padding: 14px 16px 16px; }
.corte-header-row { display: flex; align-items: center; gap: 10px; margin-bottom: 6px; }
.corte-numero { min-width: 28px; height: 28px; border-radius: 50%; background: #1a1a2e; color: #fff; font-size: 13px; font-weight: 700; display: flex; align-items: center; justify-content: center; }
.corte-nombre { font-size: 16px; font-weight: 700; color: #1a1a2e; margin: 0; }
.corte-descripcion { font-size: 13px; color: #777; line-height: 1.5; margin: 0; }

.btn-reservar { --background: #1a1a2e; --border-radius: 14px; --padding-top: 16px; --padding-bottom: 16px; font-size: 16px; font-weight: 700; margin-top: 8px; }
.btn-nueva-foto { --border-radius: 14px; --color: #1a1a2e; --border-color: #1a1a2e; font-size: 15px; font-weight: 600; }

.error-container { height: 100vh; background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%); display: flex; align-items: center; justify-content: center; }
.error-contenido { text-align: center; padding: 32px; }
.error-icon { font-size: 56px; margin-bottom: 16px; }
.error-contenido h2 { color: white; font-family: 'Rajdhani', sans-serif; font-size: 26px; margin: 0 0 10px; }
.error-contenido p { color: rgba(255,255,255,0.6); font-size: 14px; margin: 0 0 28px; line-height: 1.6; }
.btn-reintentar { background: #4a8fe8; border: none; border-radius: 14px; color: white; padding: 14px 32px; font-size: 16px; font-weight: 600; cursor: pointer; font-family: 'DM Sans', sans-serif; }
</style>
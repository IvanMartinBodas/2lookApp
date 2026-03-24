<template>
  <ion-page>
    <div v-if="fase === 'camara'" class="camara-container">
      <div class="camara-preview">
        <div class="video-stream"></div>
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
          <button class="shutter" @click="hacerFoto">
            <div class="shutter-inner"></div>
          </button>
        </div>
      </div>
    </div>

    <div v-else-if="fase === 'cargando'" class="cargando-container">
      <div class="foto-fondo"></div>
      <div class="overlay-oscuro"></div>
      <div class="cargando-contenido">
        <div class="spinner-container">
          <ion-spinner name="circular" color="light" class="spinner-grande"></ion-spinner>
        </div>
        <h2 class="cargando-titulo">Analizando tu cara...</h2>
        <p class="cargando-subtexto">La IA está detectando la forma de tu rostro</p>
      </div>
    </div>

    <div v-else-if="fase === 'resultado'" class="resultado-container">
      <ion-content :fullscreen="true">
        <div class="foto-visor">
          <button class="flecha flecha-izq" @click="cambiarVista">
            <ion-icon :icon="chevronBackOutline" />
          </button>
          <div class="foto-wrap">
            <img v-if="vistaActual === 'normal'" src="/assets/sinIA.png" class="foto-real" />
            <img v-else src="/assets/fotoIA.png" class="foto-real" />
            <div class="foto-badge">
              <span v-if="vistaActual === 'normal'"> Original</span>
              <span v-else> Con IA</span>
            </div>
          </div>
          <button class="flecha flecha-der" @click="cambiarVista">
            <ion-icon :icon="chevronForwardOutline" />
          </button>
          <div class="puntos-indicador">
            <span :class="['punto', { activo: vistaActual === 'normal' }]"></span>
            <span :class="['punto', { activo: vistaActual === 'ia' }]"></span>
          </div>
        </div>

        <div class="resultado-contenido">
          <div class="forma-row">
            <span class="forma-emoji">🥚</span>
            <div>
              <p class="forma-label">Forma detectada</p>
              <h2 class="forma-nombre">Cara Ovalada</h2>
            </div>
          </div>

          <p class="resultado-descripcion">
            Tu cara tiene proporciones equilibradas. Casi cualquier corte te favorece, tienes suerte.
          </p>

          <h3 class="seccion-titulo"> Cortes recomendados</h3>

          <div class="corte-tarjeta">
            <div class="corte-numero">1</div>
            <div class="corte-info">
              <h4 class="corte-nombre">Undercut clásico</h4>
              <p class="corte-descripcion">Corto a los lados y largo arriba. Versátil, moderno y fácil de mantener en casa.</p>
            </div>
          </div>

          <div class="corte-tarjeta">
            <div class="corte-numero">2</div>
            <div class="corte-info">
              <h4 class="corte-nombre">Fade + textura</h4>
              <p class="corte-descripcion">Degradado en los laterales con textura en la parte superior. Muy popular ahora mismo.</p>
            </div>
          </div>

          <div class="corte-tarjeta">
            <div class="corte-numero">3</div>
            <div class="corte-info">
              <h4 class="corte-nombre">Quiff</h4>
              <p class="corte-descripcion">Volumen en la parte delantera hacia arriba. Elegante para ocasiones formales e informales.</p>
            </div>
          </div>

          <ion-button expand="block" class="btn-reservar" @click="irAReservar">
             &nbsp; Reservar cita con un barbero
          </ion-button>

          <ion-button expand="block" fill="outline" class="btn-nueva-foto" @click="reiniciar">
             &nbsp; Hacer otra foto
          </ion-button>

        </div>
      </ion-content>
    </div>

  </ion-page>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  IonPage, IonContent, IonButton, IonIcon, IonSpinner
} from '@ionic/vue'
import {
  sparklesOutline,
  chevronBackOutline,
  chevronForwardOutline
} from 'ionicons/icons'

const router = useRouter()

const fase = ref('camara')
const vistaActual = ref('normal')

const hacerFoto = () => {
  fase.value = 'cargando'
  setTimeout(() => {
    vistaActual.value = 'normal'
    fase.value = 'resultado'
  }, 2000)
}

const cambiarVista = () => {
  vistaActual.value = vistaActual.value === 'normal' ? 'ia' : 'normal'
}

const reiniciar = () => {
  vistaActual.value = 'normal'
  fase.value = 'camara'
}

const irAReservar = () => {
  router.push('/elegir-barbero')
}
</script>

<style scoped>
ion-page { --background: #000; }

.camara-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #000;
}
.camara-preview {
  position: relative;
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
.video-stream {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  background: #111;
}

.camara-header {
  position: relative;
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48px 16px 12px;
}
.ia-badge {
  display: flex;
  align-items: center;
  gap: 6px;
  background: rgba(255,255,255,0.15);
  border: 1px solid rgba(255,255,255,0.4);
  border-radius: 20px;
  padding: 5px 14px;
  color: #fff;
  font-size: 13px;
  font-weight: 700;
  letter-spacing: 1px;
  backdrop-filter: blur(4px);
}
.ia-badge ion-icon { font-size: 14px; }

.guia-encuadre {
  position: relative;
  z-index: 10;
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
}
.guia-oval {
  width: 200px;
  height: 260px;
  border-radius: 50%;
  border: 2px dashed rgba(255,255,255,0.5);
}
.guia-texto {
  color: rgba(255,255,255,0.7);
  font-size: 14px;
  margin: 0;
}

.controles {
  position: relative;
  z-index: 10;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0 40px 140px;
}
.shutter {
  width: 78px;
  height: 78px;
  border-radius: 50%;
  background: rgba(255,255,255,0.2);
  border: 3px solid #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}
.shutter:active .shutter-inner { transform: scale(0.9); }
.shutter-inner {
  width: 62px;
  height: 62px;
  border-radius: 50%;
  background: #fff;
  transition: transform 0.1s;
}

.cargando-container {
  position: relative;
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #000;
  overflow: hidden;
}
.foto-fondo {
  position: absolute;
  inset: 0;
  background: #222;
  filter: blur(4px);
}
.overlay-oscuro {
  position: absolute;
  inset: 0;
  background: rgba(0,0,0,0.55);
}
.cargando-contenido {
  position: relative;
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
  z-index: 1;
}
.spinner-container {
  width: 130px;
  height: 130px;
  border-radius: 50%;
  background: rgba(255,255,255,0.08);
  border: 2px solid rgba(255,255,255,0.15);
  display: flex;
  align-items: center;
  justify-content: center;
}
.spinner-grande { transform: scale(2.5); }
.cargando-titulo {
  color: #fff;
  font-size: 20px;
  font-weight: 700;
  margin: 8px 0 0;
}
.cargando-subtexto {
  color: rgba(255,255,255,0.6);
  font-size: 14px;
  text-align: center;
  padding: 0 40px;
  margin: 0;
}

.resultado-container {
  height: 100vh;
  background: #f5f5f5;
}
.foto-visor {
  position: relative;
  height: 48vh;
  background: #1a1a2e;
  display: flex;
  align-items: center;
  justify-content: center;
}
.foto-wrap {
  position: relative;
  width: 100%;
  height: 100%;
}
.foto-real {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}
.foto-badge {
  position: absolute;
  bottom: 16px;
  left: 16px;
  background: rgba(0,0,0,0.65);
  border-radius: 20px;
  padding: 6px 14px;
  color: #fff;
  font-size: 13px;
  font-weight: 700;
  backdrop-filter: blur(6px);
}
.flecha {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  z-index: 10;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(255,255,255,0.15);
  border: 1px solid rgba(255,255,255,0.3);
  color: #fff;
  font-size: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  backdrop-filter: blur(4px);
}
.flecha-izq { left: 12px; }
.flecha-der { right: 12px; }
.puntos-indicador {
  position: absolute;
  bottom: 14px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 8px;
  z-index: 10;
}
.punto {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: rgba(255,255,255,0.3);
  transition: background 0.2s;
}
.punto.activo { background: #FFD60A; }
.resultado-contenido {
  padding: 20px 20px 100px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.forma-row {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #fff;
  border-radius: 16px;
  padding: 14px 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.07);
}
.forma-emoji { font-size: 32px; }
.forma-label {
  font-size: 11px;
  color: #999;
  margin: 0;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}
.forma-nombre {
  font-size: 18px;
  font-weight: 800;
  color: #1a1a2e;
  margin: 2px 0 0;
}
.resultado-descripcion {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0;
}
.seccion-titulo {
  font-size: 18px;
  font-weight: 800;
  color: #1a1a2e;
  margin: 4px 0 0;
}
.corte-tarjeta {
  background: #fff;
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: flex-start;
  gap: 14px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.07);
}
.corte-numero {
  min-width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #1a1a2e;
  color: #fff;
  font-size: 14px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 2px;
}
.corte-info { flex: 1; }
.corte-nombre {
  font-size: 15px;
  font-weight: 700;
  color: #1a1a2e;
  margin: 0 0 4px;
}
.corte-descripcion {
  font-size: 13px;
  color: #777;
  line-height: 1.5;
  margin: 0;
}
.btn-reservar {
  --background: #1a1a2e;
  --border-radius: 14px;
  --padding-top: 16px;
  --padding-bottom: 16px;
  font-size: 16px;
  font-weight: 700;
  margin-top: 8px;
}
.btn-nueva-foto {
  --border-radius: 14px;
  --color: #1a1a2e;
  --border-color: #1a1a2e;
  --padding-top: 14px;
  --padding-bottom: 14px;
  font-size: 15px;
  font-weight: 600;
}
</style>
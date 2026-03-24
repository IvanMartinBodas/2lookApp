<template>
  <ion-page>
    <div class="splash-bg">

      <div class="logo-wrapper">
        <svg class="arco-svg" viewBox="0 0 160 160" fill="none" xmlns="http://www.w3.org/2000/svg">
          <circle cx="80" cy="80" r="75" stroke="rgba(255,255,255,0.15)" stroke-width="3" />
          <circle
            cx="80" cy="80" r="75"
            stroke="url(#arcGrad)"
            stroke-width="3"
            stroke-linecap="round"
            stroke-dasharray="320"
            stroke-dashoffset="80"
            class="arco-giratorio"
          />
          <defs>
            <linearGradient id="arcGrad" x1="0" y1="0" x2="160" y2="160" gradientUnits="userSpaceOnUse">
              <stop offset="0%" stop-color="#ffffff" stop-opacity="0" />
              <stop offset="100%" stop-color="#ffffff" stop-opacity="1" />
            </linearGradient>
          </defs>
        </svg>

        <div class="logo-circulo">
          <img src="/assets/Logo.png" class="Logo-img" alt="2look" />
        </div>
      </div>
    </div>
  </ion-page>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { IonPage } from '@ionic/vue'
import { useRouter } from 'vue-router'

const router = useRouter()

onMounted(() => {
  setTimeout(() => {
    router.replace('/login')
  }, 2800)
})
</script>

<style scoped>
ion-page {
  --background: #004780;
}

.splash-bg {
  width: 100%;
  height: 100vh;
  background: linear-gradient(180deg, #004780 0%, #121212 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 28px;
}

.logo-wrapper {
  position: relative;
  width: 160px;
  height: 160px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.arco-svg {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  animation: girar 1.6s linear infinite;
}

@keyframes girar {
  from { transform: rotate(0deg); }
  to   { transform: rotate(360deg); }
}

.logo-circulo {
  width: 130px;
  height: 130px;
  border-radius: 50%;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 32px rgba(0, 0, 0, 0.3);
  z-index: 1;
}

.logo-img {
  width: 60px;
  height: 60px;
  object-fit: contain;
}

.logo-text {
  width: 140px;
  object-fit: contain;
  filter: brightness(0) invert(1);
}
</style>
<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="page-bg">
        <div class="page-header">
          <button class="back-btn" @click="router.back()">
            <ion-icon name="arrow-back-outline"></ion-icon>
          </button>
          <h3>Metodos de pago</h3>
        </div>
        <div class="page-content">
          <div
            v-for="method in methods"
            :key="method.id"
            :class="['payment-card', selectedMethod === method.id ? 'selected' : '']"
            @click="selectedMethod = method.id"
          >
            <div class="payment-icon">
              <ion-icon :name="method.icon"></ion-icon>
            </div>
            <span>{{ method.name }}</span>
            <div class="radio-circle" :class="{ active: selectedMethod === method.id }"></div>
          </div>

          <div v-if="selectedMethod === 'card'" class="card-form">
            <div class="card-display">
              <div class="card-chip"></div>
              <div class="card-number">•••• •••• •••• {{ cardLast4 || '0000' }}</div>
              <div class="card-expiry">{{ cardExpiry || 'MM/YY' }}</div>
            </div>
            <input v-model="cardNumber" type="text" class="app-input" placeholder="Número de tarjeta" maxlength="19" />
            <div class="card-row">
              <input v-model="cardExpiry" type="text" class="app-input" placeholder="MM/AA" />
              <input v-model="cardCvv" type="text" class="app-input" placeholder="CVV" maxlength="3" />
            </div>
          </div>
        </div>
        <div class="bottom-bar">
          <div class="bottom-info">
            <div class="bottom-time">18:30 - 19:00</div>
            <div class="bottom-price">10,00 €</div>
          </div>
          <button class="reserve-btn" @click="router.push('/pago-exitoso')">Pagar →</button>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { IonPage, IonContent, IonIcon } from '@ionic/vue'
import { arrowBackOutline, walletOutline, logoGoogle } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'

addIcons({ 'arrow-back-outline': arrowBackOutline, 'wallet-outline': walletOutline, 'logo-google': logoGoogle })

const router = useRouter()
const selectedMethod = ref('wallet')
const cardNumber = ref('')
const cardExpiry = ref('')
const cardCvv = ref('')
const cardLast4 = computed(() => cardNumber.value.replace(/\s/g, '').slice(-4))

const methods = ref([
  { id: 'wallet', name: 'Wallet', icon: 'wallet-outline' },
  { id: 'googlepay', name: 'Google Pay', icon: 'logo-google' },
  { id: 'card', name: 'Tarjeta de crédito', icon: 'card-outline' },
])
</script>

<style scoped>
.page-bg { min-height: 100%; background: linear-gradient(160deg, #1a3a5c 0%, #0d1b2a 40%, #080f18 100%); display: flex; flex-direction: column; }
.page-header { display: flex; align-items: center; gap: 16px; padding: 56px 24px 16px; }
h3 { color: white; font-size: 18px; margin: 0; font-family: 'Rajdhani', sans-serif; font-weight: 600; }
.back-btn { background: rgba(255,255,255,0.08); border: none; border-radius: 10px; width: 40px; height: 40px; display: flex; align-items: center; justify-content: center; color: white; font-size: 20px; cursor: pointer; flex-shrink: 0; }
.page-content { flex: 1; padding: 0 20px; padding-bottom: 100px; }
.payment-card { display: flex; align-items: center; gap: 14px; background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.08); border-radius: 14px; padding: 18px 16px; margin-bottom: 12px; cursor: pointer; transition: all 0.2s; }
.payment-card.selected { background: rgba(74,143,232,0.1); border-color: rgba(74,143,232,0.35); }
.payment-icon { width: 40px; height: 40px; background: rgba(255,255,255,0.08); border-radius: 10px; display: flex; align-items: center; justify-content: center; color: white; font-size: 20px; }
.payment-card span { flex: 1; color: white; font-size: 15px; font-weight: 500; }
.radio-circle { width: 22px; height: 22px; border-radius: 50%; border: 2px solid rgba(255,255,255,0.25); flex-shrink: 0; transition: all 0.2s; }
.radio-circle.active { border-color: #4a8fe8; background: #4a8fe8; box-shadow: 0 0 0 4px rgba(74,143,232,0.2); }
.card-form { margin-top: 8px; }
.card-display { background: linear-gradient(135deg, #2d5986, #1a3a5c); border-radius: 16px; padding: 24px; margin-bottom: 16px; }
.card-chip { width: 36px; height: 28px; background: rgba(255,255,255,0.2); border-radius: 6px; margin-bottom: 20px; }
.card-number { color: white; font-size: 18px; letter-spacing: 0.2em; margin-bottom: 12px; }
.card-expiry { color: rgba(255,255,255,0.6); font-size: 14px; }
.card-row { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; margin-top: 12px; }
.bottom-bar { position: fixed; bottom: 0; left: 0; right: 0; background: rgba(10,20,30,0.95); backdrop-filter: blur(10px); border-top: 1px solid rgba(255,255,255,0.08); padding: 16px 24px 32px; display: flex; align-items: center; justify-content: space-between; }
.bottom-info { display: flex; flex-direction: column; gap: 2px; }
.bottom-time { color: rgba(255,255,255,0.6); font-size: 13px; }
.bottom-price { color: white; font-size: 20px; font-weight: 700; }
.reserve-btn { background: #4a8fe8; border: none; border-radius: 12px; color: white; padding: 14px 24px; font-family: 'DM Sans', sans-serif; font-size: 15px; font-weight: 600; cursor: pointer; }
</style>
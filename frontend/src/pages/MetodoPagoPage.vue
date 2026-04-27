<template>
  <ion-page>
    <ion-content :fullscreen="true">
      <div class="page-bg">
        <div class="page-header">
          <button class="back-btn" @click="router.back()">
            <ion-icon name="arrow-back-outline"></ion-icon>
          </button>
          <h3>Método de pago</h3>
        </div>
        <div class="page-content">
          <div
            v-for="method in methods"
            :key="method.id"
            :class="['payment-card', selectedMethod === method.id ? 'selected' : '']"
            @click="onSelectMethod(method.id)"
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
              <div class="card-number">Stripe · Pago seguro</div>
              <div class="card-expiry">Test: 4242 4242 4242 4242 · CVC: 123</div>
            </div>
            <div ref="cardEl" class="stripe-card-mount"></div>
            <p v-if="errorMsg" class="error-msg">{{ errorMsg }}</p>
          </div>
        </div>

        <div class="bottom-bar">
          <div class="bottom-info">
            <div class="bottom-time">{{ bookingStore.hora }}</div>
            <div class="bottom-price">{{ formatoPrecio(bookingStore.servicioPrecio) }}</div>
          </div>
          <button class="reserve-btn" :disabled="pagando" @click="pagar">
            <ion-spinner v-if="pagando" name="crescent" style="width:14px;height:14px;margin-right:6px"></ion-spinner>
            {{ pagando ? 'Procesando...' : `Pagar ${formatoPrecio(bookingStore.servicioPrecio)}` }}
          </button>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import { IonPage, IonContent, IonIcon, IonSpinner } from '@ionic/vue'
import { arrowBackOutline, walletOutline, logoGoogle, cardOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'
import { useRouter } from 'vue-router'
import { loadStripe, type Stripe, type StripeElements, type StripeCardElement } from '@stripe/stripe-js'
import { bookingStore } from '@/store/user'
import { pagoApi, reservaApi } from '@/store/api'

addIcons({ 'arrow-back-outline': arrowBackOutline, 'wallet-outline': walletOutline, 'logo-google': logoGoogle, 'card-outline': cardOutline })

const router = useRouter()
const selectedMethod = ref<'wallet' | 'googlepay' | 'card'>('wallet')
const pagando = ref(false)
const errorMsg = ref('')
const cardEl = ref<HTMLDivElement | null>(null)

const methods = [
  { id: 'wallet',    name: 'Pago rápido (demo)',          icon: 'wallet-outline' },
  { id: 'googlepay', name: 'Google Pay (demo)',           icon: 'logo-google'    },
  { id: 'card',      name: 'Tarjeta de crédito (Stripe)', icon: 'card-outline'   },
] as const

let stripe: Stripe | null = null
let elements: StripeElements | null = null
let cardElement: StripeCardElement | null = null

async function ensureStripe() {
  if (stripe) return
  stripe = await loadStripe(import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY)
}

async function montarCard() {
  await ensureStripe()
  if (!stripe || !cardEl.value || cardElement) return
  elements = stripe.elements()
  cardElement = elements.create('card', {
    style: {
      base: { color: '#fff', fontFamily: 'DM Sans, sans-serif', fontSize: '16px', '::placeholder': { color: 'rgba(255,255,255,0.45)' } },
      invalid: { color: '#ff6b6b' }
    }
  })
  cardElement.mount(cardEl.value)
}

function onSelectMethod(id: typeof selectedMethod.value) {
  selectedMethod.value = id
  errorMsg.value = ''
}

watch(selectedMethod, async (val) => {
  if (val === 'card') { await new Promise(r => setTimeout(r, 0)); montarCard() }
})

onMounted(() => { if (selectedMethod.value === 'card') montarCard() })
onBeforeUnmount(() => { cardElement?.destroy(); cardElement = null })

function formatoPrecio(p: number) {
  return `${(p ?? 10).toFixed(2).replace('.', ',')} €`
}

async function pagar() {
  if (pagando.value) return
  errorMsg.value = ''
  pagando.value = true

  try {
    if (selectedMethod.value !== 'card') {
      // Wallet / Google Pay — simulación
      if (bookingStore.reservaId) {
        try {
          await reservaApi.updateEstado(bookingStore.reservaId, 'PAGADA')
        } catch (e: any) {
          errorMsg.value = e?.message || 'Error al actualizar la reserva'
          return
        }
      }
      bookingStore.pagado = true
      router.push('/pago-exitoso')
      return
    }

    if (!stripe || !cardElement) {
      errorMsg.value = 'Stripe no está listo. Revisa la clave pública en .env'
      return
    }

    const { clientSecret, paymentIntentId } = await pagoApi.createPaymentIntent({
      reservaId: bookingStore.reservaId,
      amount: bookingStore.servicioPrecio || 10
    })

    const { error, paymentIntent } = await stripe.confirmCardPayment(clientSecret, {
      payment_method: { card: cardElement }
    })

    if (error) { errorMsg.value = error.message || 'Error en el pago'; return }

    if (paymentIntent?.status === 'succeeded') {
      if (bookingStore.reservaId) {
        await pagoApi.confirmar({ reservaId: bookingStore.reservaId, paymentIntentId }).catch(() => {})
      }
      bookingStore.pagado = true
      router.push('/pago-exitoso')
    } else {
      errorMsg.value = `Estado inesperado: ${paymentIntent?.status}`
    }
  } catch (e: any) {
    errorMsg.value = e?.message || 'Error en el pago'
  } finally {
    pagando.value = false
  }
}
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
.card-number { color: white; font-size: 15px; font-weight: 600; margin-bottom: 8px; }
.card-expiry { color: rgba(255,255,255,0.6); font-size: 13px; }
.stripe-card-mount { background: rgba(255,255,255,0.06); border: 1px solid rgba(255,255,255,0.1); border-radius: 12px; padding: 16px; }
.error-msg { color: #ff6b6b; font-size: 13px; margin: 10px 4px 0; }
.bottom-bar { position: fixed; bottom: 0; left: 0; right: 0; background: rgba(10,20,30,0.95); backdrop-filter: blur(10px); border-top: 1px solid rgba(255,255,255,0.08); padding: 16px 24px 32px; display: flex; align-items: center; justify-content: space-between; }
.bottom-info { display: flex; flex-direction: column; gap: 2px; }
.bottom-time { color: rgba(255,255,255,0.6); font-size: 13px; }
.bottom-price { color: white; font-size: 20px; font-weight: 700; }
.reserve-btn { background: #4a8fe8; border: none; border-radius: 12px; color: white; padding: 14px 24px; font-family: 'DM Sans', sans-serif; font-size: 15px; font-weight: 600; cursor: pointer; display: flex; align-items: center; }
.reserve-btn:disabled { opacity: 0.5; cursor: not-allowed; }
</style>

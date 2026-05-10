<template>
  <ion-app>
    <ion-router-outlet />
  </ion-app>
</template>

<script setup lang="ts">
import { IonApp, IonRouterOutlet } from '@ionic/vue';
</script>

<style>
.barber-tooltip {
  background: rgba(15, 25, 40, 0.92) !important;
  border: 1px solid rgba(74, 143, 232, 0.4) !important;
  border-radius: 8px !important;
  color: white !important;
  font-family: 'DM Sans', sans-serif !important;
  font-size: 12px !important;
  font-weight: 600 !important;
  padding: 4px 10px !important;
  box-shadow: 0 2px 8px rgba(0,0,0,0.4) !important;
  white-space: nowrap !important;
}
.barber-tooltip::before { display: none !important; }
</style>

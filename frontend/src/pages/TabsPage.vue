<template>
  <ion-page>
    <ion-tabs>
      <ion-router-outlet></ion-router-outlet>
      <ion-tab-bar slot="bottom">
        <ion-tab-button tab="home" href="/tabs/home">
          <ion-icon name="home-outline"></ion-icon>
        </ion-tab-button>
        <ion-tab-button tab="citas" href="/tabs/citas">
          <ion-icon name="calendar-outline"></ion-icon>
        </ion-tab-button>
        <ion-tab-button tab="camara" href="/tabs/camara">
          <ion-icon name="camera-outline"></ion-icon>
        </ion-tab-button>
        <ion-tab-button tab="historial" href="/tabs/historial">
          <ion-icon name="cut-outline"></ion-icon>
        </ion-tab-button>
        <ion-tab-button tab="perfil" href="/tabs/perfil">
          <ion-icon name="person-outline"></ion-icon>
        </ion-tab-button>
      </ion-tab-bar>
    </ion-tabs>
  </ion-page>
</template>

<script setup lang="ts">
import { IonPage, IonTabs, IonRouterOutlet, IonTabBar, IonTabButton, IonIcon } from '@ionic/vue'
import { homeOutline, calendarOutline, cameraOutline, cutOutline, personOutline } from 'ionicons/icons'
import { addIcons } from 'ionicons'

addIcons({
  'home-outline': homeOutline,
  'calendar-outline': calendarOutline,
  'camera-outline': cameraOutline,
  'cut-outline': cutOutline,
  'person-outline': personOutline,
})
</script>
import { createRouter, createWebHistory } from '@ionic/vue-router'
import { RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/splash'
  },
  {
    path: '/splash',
    component: () => import('@/pages/SplashPage.vue')
  },
  {
    path: '/login',
    component: () => import('@/pages/LoginPage.vue')
  },
  {
    path: '/register',
    component: () => import('@/pages/RegisterPage.vue')
  },
  {
    path: '/tabs',
    component: () => import('@/pages/TabsPage.vue'),
    children: [
      {
        path: '',
        redirect: '/tabs/home'
      },
      {
        path: 'home',
        component: () => import('@/pages/HomePage.vue')
      },
      {
        path: 'citas',
        component: () => import('@/pages/CitasPage.vue')
      },
      {
        path: 'camara',
        component: () => import('@/pages/CamaraPage.vue')
      },
      {
        path: 'historial',
        component: () => import('@/pages/HistorialPage.vue')
      },
      {
        path: 'perfil',
        component: () => import('@/pages/PerfilPage.vue')
      }
    ]
  },
  {
    path: '/buscar',
    component: () => import('@/pages/BuscarPage.vue')
  },
  {
    path: '/barbero/:id',
    component: () => import('@/pages/PerfilBarberoPage.vue')
  },
  {
    path: '/reservar',
    component: () => import('@/pages/ReservarPage.vue')
  },
  {
    path: '/horarios',
    component: () => import('@/pages/HorariosPage.vue')
  },
  {
    path: '/elegir-barbero',
    component: () => import('@/pages/ElegirBarberoPage.vue')
  },
  {
    path: '/metodo-pago',
    component: () => import('@/pages/MetodoPagoPage.vue')
  },
  {
    path: '/pago-exitoso',
    component: () => import('@/pages/PagoExitosoPage.vue')
  },
  {
    path: '/editar-perfil',
    component: () => import('@/pages/EditarPerfilPage.vue')
  },
  {
    path: '/historial',
    component: () => import('@/pages/HistorialPage.vue')
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
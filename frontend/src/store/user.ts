import { reactive } from 'vue'

export const userStore = reactive({
  photo: '',
  name: 'Invitado',
  email: '',
  id: null as number | null,
  rol: '',
  logueado: false,
})

export const bookingStore = reactive({
  barberoId: 1,
  barberoNombre: 'Charles S.',
  barberoImg: '/assets/Rectangle.png',
  barberoColor: '#8B4513',
  barberoInitials: 'CS',
  servicioId: 1,
  servicioNombre: 'Corte Clásico',
  servicioPrecio: 10,
  tieneCita: false,
  pagado: false,
  fecha: '19 Dic',
  hora: '18:30 - 19:00',
  desdeIA: false
})

export function setUsuario(data: any) {
  userStore.id = data.id
  userStore.name = data.nombre
  userStore.email = data.email
  userStore.rol = data.rol
  userStore.logueado = true
  sessionStorage.setItem('2look_user', JSON.stringify(data))
}

export function cerrarSesion() {
  userStore.id = null
  userStore.name = 'Invitado'
  userStore.email = ''
  userStore.rol = ''
  userStore.logueado = false
  userStore.photo = ''
  sessionStorage.removeItem('2look_user')
}

export function restaurarSesion() {
  const saved = sessionStorage.getItem('2look_user')
  if (saved) {
    try {
      setUsuario(JSON.parse(saved))
    } catch {}
  }
}
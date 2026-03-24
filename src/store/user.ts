import { reactive } from 'vue'

export const userStore = reactive({
  photo: '',
  name: 'Ivan Martin'
})

export const bookingStore = reactive({
  barberoId: 1,
  barberoNombre: 'Charles S.',
  barberoImg: '/assets/Rectangle.png',
  barberoColor: '#8B4513',
  barberoInitials: 'CS',
  tieneCita: false,
  pagado: false,
  fecha: '19 Dic',
  hora: '18:30 - 19:00'
})

export const BASE_URL = 'http://localhost:8080'

async function request<T>(method: string, path: string, body?: any): Promise<T> {
  const res = await fetch(`${BASE_URL}${path}`, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: body ? JSON.stringify(body) : undefined
  })
  if (!res.ok) {
    const err = await res.json().catch(() => ({}))
    throw new Error(err?.message || `Error ${res.status}`)
  }
  return res.json()
}

export const personaApi = {
  getAll: () => request<any[]>('GET', '/api/personas'),
  getById: (id: number) => request<any>('GET', `/api/personas/${id}`),
  create: (data: any) => request<any>('POST', '/api/personas', data),
  update: (id: number, data: any) => request<any>('PUT', `/api/personas/${id}`, data),
  delete: (id: number) => request<void>('DELETE', `/api/personas/${id}`)
}

export const barberoApi = {
  getAll: () => request<any[]>('GET', '/api/barberos'),
  getById: (id: number) => request<any>('GET', `/api/barberos/${id}`),
  create: (data: any) => request<any>('POST', '/api/barberos', data),
  update: (id: number, data: any) => request<any>('PUT', `/api/barberos/${id}`, data),
  delete: (id: number) => request<void>('DELETE', `/api/barberos/${id}`)
}

export const servicioApi = {
  getAll: () => request<any[]>('GET', '/api/servicios'),
  getById: (id: number) => request<any>('GET', `/api/servicios/${id}`),
  create: (data: any) => request<any>('POST', '/api/servicios', data)
}

export const reservaApi = {
  getAll: () => request<any[]>('GET', '/api/reservas'),
  getById: (id: number) => request<any>('GET', `/api/reservas/${id}`),
  getByCliente: (clienteId: number) => request<any[]>('GET', `/api/reservas/cliente/${clienteId}`),
  getByBarbero: (barberoId: number) => request<any[]>('GET', `/api/reservas/barbero/${barberoId}`),
  create: (data: any) => request<any>('POST', '/api/reservas', data),
  updateEstado: (id: number, estado: string) => request<any>('PATCH', `/api/reservas/${id}/estado?estado=${estado}`),
  delete: (id: number) => request<void>('DELETE', `/api/reservas/${id}`)
}

export const favoritoApi = {
  getAll: () => request<any[]>('GET', '/api/favoritos'),
  create: (data: any) => request<any>('POST', '/api/favoritos', data),
  delete: (id: number) => request<void>('DELETE', `/api/favoritos/${id}`)
}

export const opinionApi = {
  getAll: () => request<any[]>('GET', '/api/opiniones'),
  getByBarbero: (barberoId: number) => request<any[]>('GET', `/api/opiniones/barbero/${barberoId}`),
  create: (data: any) => request<any>('POST', '/api/opiniones', data)
}

export const historialApi = {
  getAll: () => request<any[]>('GET', '/api/historial'),
  getByPersona: (personaId: number) => request<any[]>('GET', `/api/historial/persona/${personaId}`),
  create: (data: any) => request<any>('POST', '/api/historial', data)
}
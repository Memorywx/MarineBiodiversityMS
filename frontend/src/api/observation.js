import request from './request.js'

export const getObservationList = (params) => {
  const p = { ...params }
  if (p.locationName) { p.keyword = p.locationName; delete p.locationName }
  if (p.page) { p.current = p.page; delete p.page }
  return request.get('/observations', { params: p })
}
export const getObservationDetail = (id) => request.get(`/observations/${id}`)
export const createObservation = (data) => request.post('/observations', data)
export const updateObservation = (id, data) => request.put(`/observations/${id}`, data)
export const deleteObservation = (id) => request.delete(`/observations/${id}`)

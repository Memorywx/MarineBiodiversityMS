import request from './request.js'

export const getEcosystemList = (params) => {
  const p = { ...params }
  if (p.name) { p.keyword = p.name; delete p.name }
  if (p.page) { p.current = p.page; delete p.page }
  return request.get('/ecosystems', { params: p })
}
export const getEcosystemDetail = (id) => request.get(`/ecosystems/${id}`)
export const getAllEcosystems = () => request.get('/ecosystems/all')
export const createEcosystem = (data) => request.post('/ecosystems', data)
export const updateEcosystem = (id, data) => request.put(`/ecosystems/${id}`, data)
export const deleteEcosystem = (id) => request.delete(`/ecosystems/${id}`)

import request from './request.js'

export const getSpeciesList = (params) => {
  const p = { ...params }
  if (p.page) { p.current = p.page; delete p.page }
  return request.get('/species', { params: p })
}
export const getSpeciesDetail = (id) => request.get(`/species/${id}`)
export const createSpecies = (data) => request.post('/species', data)
export const updateSpecies = (id, data) => request.put(`/species/${id}`, data)
export const deleteSpecies = (id) => request.delete(`/species/${id}`)
export const getTaxonomy = () => request.get('/species/taxonomy')

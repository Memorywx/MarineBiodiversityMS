import request from './request.js'

export const getStats = () => request.get('/dashboard/stats')
export const getSpeciesStats = (type) => request.get('/dashboard/species-stats', { params: { type } })
export const getObservationStats = (type) => request.get('/dashboard/observation-stats', { params: { type } })
export const getSpeciesDistribution = () => request.get('/dashboard/species-distribution')
export const getObservationPoints = () => request.get('/dashboard/observation-points')

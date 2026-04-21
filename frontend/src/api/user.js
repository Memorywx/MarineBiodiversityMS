import request from './request.js'

export const getUserList = (params) => {
  const p = { ...params }
  if (p.page) { p.current = p.page; delete p.page }
  return request.get('/users', { params: p })
}
export const getUserDetail = (id) => request.get(`/users/${id}`)
export const createUser = (data) => request.post('/users', data)
export const updateUser = (id, data) => request.put(`/users/${id}`, data)
export const deleteUser = (id) => request.delete(`/users/${id}`)
export const approveUser = (id) => request.put(`/users/${id}/approve`)
export const updateUserRoleStatus = (id, data) => request.put(`/users/${id}/role-status`, data)

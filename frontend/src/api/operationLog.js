import request from './request.js'

export const getOperationLogList = (params) => {
  const p = { ...params }
  if (p.page) { p.current = p.page; delete p.page }
  return request.get('/operation-logs', { params: p })
}

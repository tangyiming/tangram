import http from '../utils/http'

export const cmpAdd = p => http.post('/component/add', p)
export const cmpUpdate = p => http.post('/component/update', p)
export const cmpUpdateStatus = p => http.post('/component/updateStatus', p)
export const cmpDel = p => http.post('/component/delete', p)
export const cmpQuery = p => http.post('/component/query', p)
export const cmpExec = p => http.post('/component/execute', p)

import http from '../utils/http'

export const bizAdd = p => http.post('/biz/add', p)
export const bizList = () => http.post('/biz/list')
export const bizUpdate = p => http.post('/biz/update', p)
export const bizDelete = p => http.post('/biz/delete', p)

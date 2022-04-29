import http from '../utils/http'

export const sceneAdd = p => http.post('/scene/add', p)
export const sceneUpdate = p => http.post('/scene/update', p)
export const sceneUpdateStatus = p => http.post('/scene/updateStatus', p)
export const sceneQuery = p => http.post('/scene/query', p)
export const sceneExec = p => http.post('/scene/exec', p)
export const sceneDel = p => http.post('/scene/delete', p)

import http from '../http.js'

const MODULE_PREFIX = '/s'

export function saveJob(data) {
  return http.post(MODULE_PREFIX + '/job/save', data)
}

export function listJob() {
  return http.get(MODULE_PREFIX + '/job/list')
}

export function deleteJob(data) {
  return http.post(MODULE_PREFIX + '/job/delete', data)
}

export function pauseJob(data) {
  return http.post(MODULE_PREFIX + '/job/pause', data)
}

export function resumeJob(data) {
  return http.post(MODULE_PREFIX + '/job/resume', data)
}

export function rescheduleJob(data) {
  return http.post(MODULE_PREFIX + '/job/reschedule', data)
}

export function runJob(data) {
  return http.post(MODULE_PREFIX + '/job/run', data)
}

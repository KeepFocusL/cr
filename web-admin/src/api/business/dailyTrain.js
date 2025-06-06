import http from '../http.js'

const MODULE_PREFIX = '/b'

/**
 * 获取每日车次列表
 * @param {Object} params 查询参数
 * @param {number} params.page 当前页码
 * @param {number} params.size 每页显示条数
 * @returns {Promise}
 */
export function listDailyTrain(params = {}) {
  const { page = 1, size = 20, keyword } = params
  return http.get(MODULE_PREFIX + '/admin/daily-train/list', {
    params: {
      page,
      size,
      keyword
    }
  })
}

export function saveDailyTrain(data) {
  return http.post(MODULE_PREFIX + '/admin/daily-train/save', data)
}

export function deleteDailyTrain(ids) {
  return http.delete(MODULE_PREFIX + '/admin/daily-train/delete', { data: ids })
}

export function genDailyData(date){
  return http.get(MODULE_PREFIX + '/admin/daily-train/gen-daily/' + date)
}

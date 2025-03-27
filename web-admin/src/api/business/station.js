import http from '../http.js'

const MODULE_PREFIX = '/b'

/**
 * 获取车站列表
 * @param {Object} params 查询参数
 * @param {number} params.page 当前页码
 * @param {number} params.size 每页显示条数
 * @returns {Promise}
 */
export function listStation(params = {}) {
  const { page = 1, size = 20, keyword } = params
  return http.get(MODULE_PREFIX + '/admin/station/list', {
    params: {
      page,
      size,
      keyword
    }
  })
}

export function saveStation(data) {
  return http.post(MODULE_PREFIX + '/admin/station/save', data)
}

export function deleteStation(ids) {
  return http.delete(MODULE_PREFIX + '/admin/station/delete', { data: ids })
}

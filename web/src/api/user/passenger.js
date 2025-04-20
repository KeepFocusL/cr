import http from '../http.js'

const MODULE_PREFIX = '/u'

/**
 * 获取乘车人列表
 * @param {Object} params 查询参数
 * @param {number} params.page 当前页码
 * @param {number} params.size 每页显示条数
 * @returns {Promise}
 */
export function listPassenger(params = {}) {
  const { page = 1, size = 20, keyword } = params
  return http.get(MODULE_PREFIX + '/passenger/list', {
    params: {
      page,
      size,
      keyword
    }
  })
}

export function savePassenger(data) {
  return http.post(MODULE_PREFIX + '/passenger/save', data)
}

export function deletePassenger(ids) {
  return http.delete(MODULE_PREFIX + '/passenger/delete', { data: ids })
}

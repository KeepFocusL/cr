import http from '../http.js'

const MODULE_PREFIX = '/u'

/**
 * 获取会员车票列表
 * @param {Object} params 查询参数
 * @param {number} params.page 当前页码
 * @param {number} params.size 每页显示条数
 * @returns {Promise}
 */
export function listTicket(params = {}) {
  const { page = 1, size = 20, keyword } = params
  return http.get(MODULE_PREFIX + '/admin/ticket/list', {
    params: {
      page,
      size,
      keyword
    }
  })
}

export function saveTicket(data) {
  return http.post(MODULE_PREFIX + '/admin/ticket/save', data)
}

export function deleteTicket(ids) {
  return http.delete(MODULE_PREFIX + '/admin/ticket/delete', { data: ids })
}
